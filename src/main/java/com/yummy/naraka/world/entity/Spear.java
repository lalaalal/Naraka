package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Position;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Supplier;

public class Spear extends AbstractArrow {
    private static final EntityDataAccessor<Integer> ID_LOYALTY = SynchedEntityData.defineId(Spear.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(Spear.class, EntityDataSerializers.BOOLEAN);
    private final float baseDamage;
    protected boolean dealtDamage = false;
    private int clientSideReturnTickCount = 0;

    protected Spear(EntityType<? extends Spear> entityType, Level level) {
        super(entityType, level);
        baseDamage = 1;
    }

    public Spear(Supplier<? extends EntityType<? extends Spear>> type, Level level, Position position, ItemStack pickupItem, float baseDamage) {
        super(type.get(), position.x(), position.y(), position.z(), level, pickupItem, null);
        entityData.set(ID_LOYALTY, getLoyaltyFromItem(pickupItem));
        entityData.set(ID_FOIL, pickupItem.hasFoil());
        this.baseDamage = baseDamage;
    }

    public Spear(Supplier<? extends EntityType<? extends Spear>> type, Level level, LivingEntity owner, ItemStack pickupItem, float baseDamage) {
        super(type.get(), owner, level, pickupItem, null);
        entityData.set(ID_LOYALTY, getLoyaltyFromItem(pickupItem));
        entityData.set(ID_FOIL, pickupItem.hasFoil());
        this.baseDamage = baseDamage;
    }

    @Override
    protected void defineSynchedData(Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_LOYALTY, 0);
        builder.define(ID_FOIL, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("DealtDamage", dealtDamage);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        dealtDamage = compound.getBoolean("DealtDamage");
        entityData.set(ID_LOYALTY, getLoyaltyFromItem(getPickupItem()));
        entityData.set(ID_FOIL, getPickupItem().hasFoil());
    }

    protected int getLoyaltyFromItem(ItemStack stack) {
        if (level() instanceof ServerLevel serverLevel)
            return EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverLevel, stack, this);
        return 0;
    }

    public int getLoyalty() {
        return entityData.get(ID_LOYALTY);
    }

    public boolean hasFoil() {
        return entityData.get(ID_FOIL);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.AIR);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity owner = this.getOwner();
        int loyaltyLevel = getLoyalty();
        if (loyaltyLevel > 0 && (this.dealtDamage || this.isNoPhysics()) && owner != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!level().isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1f);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = owner.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015 * loyaltyLevel, this.getZ());
                if (level().isClientSide) {
                    this.yOld = this.getY();
                }

                double scale = 0.05 * loyaltyLevel;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vec3.normalize().scale(scale)));
                if (this.clientSideReturnTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0f, 1.0f);
                }

                ++this.clientSideReturnTickCount;
            }
        }

        super.tick();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        Entity entity = result.getEntity();

        if (canHurtEntity(entity))
            hurtHitEntity(entity);

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        this.playSound(soundevent, 1.0F, 1.0F);
    }

    protected boolean canHurtEntity(Entity entity) {
        return entity.getType() != EntityType.ENDERMAN;
    }

    protected void hurtHitEntity(Entity entity) {
        ItemStack stack = getPickupItem();
        Optional<HolderLookup.RegistryLookup<Enchantment>> enchantmentLookup = level().registryAccess().lookup(Registries.ENCHANTMENT);
        double damage = stack.getAttributeModifiers().compute(baseDamage, EquipmentSlot.MAINHAND);
        if (enchantmentLookup.isPresent()) {
            ItemEnchantments enchantments = stack.getAllEnchantments(enchantmentLookup.get());
            for (Holder<Enchantment> enchantment : enchantments.keySet()) {
                if (enchantment.is(Enchantments.IMPALING))
                    damage += 1;
            }
        }

        DamageSource damageSource = NarakaDamageSources.spear(this);
        entity.hurt(damageSource, (float) damage);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }
}