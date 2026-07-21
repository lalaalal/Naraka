package com.yummy.naraka.world.entity;

import com.google.common.collect.Multimap;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.core.Position;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.function.Supplier;

public class Spear extends AbstractArrow {
    private static final Map<EntityType<?>, Supplier<ItemStack>> ITEM_BY_TYPE = Map.of(
            NarakaEntityTypes.THROWN_SPEAR.getConcreteValue(), () -> new ItemStack(NarakaItems.SPEAR_ITEM.getConcreteValue()),
            NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR.getConcreteValue(), () -> new ItemStack(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.getConcreteValue()),
            NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS.getConcreteValue(), () -> new ItemStack(NarakaItems.SPEAR_OF_LONGINUS_ITEM.getConcreteValue())
    );

    private static final EntityDataAccessor<Integer> ID_LOYALTY = SynchedEntityData.defineId(Spear.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(Spear.class, EntityDataSerializers.BOOLEAN);
    protected boolean dealtDamage = false;
    private int clientSideReturnTickCount = 0;
    private final ItemStack pickupItem;

    protected Spear(EntityType<? extends Spear> entityType, Level level) {
        super(entityType, level);
        pickupItem = ItemStack.EMPTY;
    }

    public Spear(EntityType<? extends Spear> type, Level level, Position position, ItemStack pickupItem) {
        super(type, position.x(), position.y(), position.z(), level);
        this.pickup = Pickup.ALLOWED;
        this.pickupItem = pickupItem.copy();

        entityData.set(ID_LOYALTY, getLoyaltyFromItem(pickupItem));
        entityData.set(ID_FOIL, pickupItem.hasFoil());
    }

    public Spear(EntityType<? extends Spear> type, Level level, LivingEntity owner, ItemStack pickupItem) {
        super(type, owner, level);
        this.pickup = Pickup.ALLOWED;
        this.pickupItem = pickupItem.copy();

        entityData.set(ID_LOYALTY, getLoyaltyFromItem(pickupItem));
        entityData.set(ID_FOIL, pickupItem.hasFoil());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ID_LOYALTY, 0);
        entityData.define(ID_FOIL, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("DealtDamage", dealtDamage);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag input) {
        super.readAdditionalSaveData(input);
        dealtDamage = input.getBoolean("DealtDamage");
        entityData.set(ID_LOYALTY, getLoyaltyFromItem(getPickupItem()));
        entityData.set(ID_FOIL, getPickupItem().hasFoil());
    }

    @Override
    public ItemStack getPickupItem() {
        if (pickupItem.isEmpty())
            return ITEM_BY_TYPE.getOrDefault(getType(), () -> ItemStack.EMPTY).get();
        return pickupItem;
    }

    protected int getLoyaltyFromItem(ItemStack stack) {
        return EnchantmentHelper.getLoyalty(stack);
    }

    public int getLoyalty() {
        return entityData.get(ID_LOYALTY);
    }

    public boolean hasFoil() {
        return entityData.get(ID_FOIL);
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
                if (this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1f);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = owner.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015 * loyaltyLevel, this.getZ());
                if (level().isClientSide()) {
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

        if (canHurtEntity(entity) && level() instanceof ServerLevel serverLevel)
            hurtHitEntity(serverLevel, entity);

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        this.playSound(soundevent, 1.0F, 1.0F);
    }

    protected boolean canHurtEntity(Entity entity) {
        return entity.getType() != EntityType.ENDERMAN;
    }

    protected void hurtHitEntity(ServerLevel serverLevel, Entity entity) {
        DamageSource damageSource = NarakaDamageSources.spear(this);
        entity.hurt(damageSource, getAttackDamage());
    }

    protected float getAttackDamage() {
        ItemStack spearItem = getPickupItem();
        double baseDamage = 1;
        Multimap<Attribute, AttributeModifier> attributeModifiers = spearItem.getAttributeModifiers(EquipmentSlot.MAINHAND);
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(spearItem);

        int sharpness = enchantments.getOrDefault(Enchantments.SHARPNESS, 0);
        for (AttributeModifier modifier : attributeModifiers.get(Attributes.ATTACK_DAMAGE))
            baseDamage += modifier.getAmount();
        return (float) baseDamage + sharpness;
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

    @Override
    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) {
            super.playerTouch(player);
        }
    }

    @Override
    protected boolean tryPickup(final Player player) {
        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }
}