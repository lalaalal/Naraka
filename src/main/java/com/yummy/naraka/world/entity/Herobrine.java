package com.yummy.naraka.world.entity;

import com.yummy.naraka.attachment.DeathCountHelper;
import com.yummy.naraka.util.NarakaNbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Herobrine extends Monster implements DeathCountingEntity {
    private final Set<UUID> deathCountedEntities = new HashSet<>();

    public static AttributeSupplier getAttributeSupplier() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .build();
    }

    public Herobrine(EntityType<? extends Herobrine> entityType, Level level) {
        super(entityType, level);
        registerGoals();
        DeathCountHelper.addDeathCountingEntity(this);
    }

    public Herobrine(Level level, Vec3 pos) {
        this(NarakaEntityTypes.HEROBRINE.get(), level);
        setPos(pos);
    }

    @Override
    protected boolean canRide(Entity vehicle) {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source);
    }

    @Override
    public boolean canDrownInFluidType(FluidType type) {
        return false;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public boolean canStandOnFluid(FluidState pFluidState) {
        return true;
    }

    public void addDeathCountedEntity(LivingEntity entity) {
        UUID uuid = entity.getUUID();
        deathCountedEntities.add(uuid);
    }

    public void removeDeathCountedEntity(LivingEntity entity) {
        UUID uuid = entity.getUUID();
        deathCountedEntities.remove(uuid);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        NarakaNbtUtils.writeUUIDs(compoundTag, "DeathCountingEntities", deathCountedEntities);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        List<UUID> list = NarakaNbtUtils.readUUIDs(compoundTag, "DeathCountingEntities");
        if (list != null) {
            deathCountedEntities.clear();
            deathCountedEntities.addAll(list);
            DeathCountHelper.updateDeathCountingEntity(this);
        }
    }

    @Override
    public Set<UUID> getDeathCountedEntities() {
        return deathCountedEntities;
    }

    @Override
    public void onDeathCountZero(LivingEntity livingEntity) {
        removeDeathCountedEntity(livingEntity);
        if (livingEntity instanceof ServerPlayer player)
            DeathCountHelper.hideDeathCount(player);
        kill();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof LivingEntity livingEntity) {
            addDeathCountedEntity(livingEntity);
            if (livingEntity instanceof ServerPlayer serverPlayer)
                DeathCountHelper.showDeathCount(serverPlayer);
        }

        return super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource damageSource) {
        DeathCountHelper.removeDeathCountingEntity(this);
        deathCountedEntities.clear();
        super.die(damageSource);
    }
}
