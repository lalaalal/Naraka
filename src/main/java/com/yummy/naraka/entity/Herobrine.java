package com.yummy.naraka.entity;

import com.yummy.naraka.NarakaUtil;
import com.yummy.naraka.attachment.DeathCountHelper;
import com.yummy.naraka.client.animation.AnimationInstance;
import com.yummy.naraka.client.animation.NarakaAnimations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Herobrine extends Monster implements DeathCountingEntity, Animatable {
    private final Set<UUID> deathCountedEntities = new HashSet<>();
    private AnimationInstance animationInstance = null;

    public static AttributeSupplier getAttributeSupplier() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .build();
    }

    public Herobrine(EntityType<? extends Herobrine> entityType, Level level) {
        super(entityType, level);
        registerGoals();

        DeathCountHelper.addDeathCountingEntity(this);
        if (level.isClientSide)
            animationInstance = NarakaAnimations.instance("herobrine.idle", true);
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
        NarakaUtil.writeUUIDs(compoundTag, "DeathCountingEntities", deathCountedEntities);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        List<UUID> list = NarakaUtil.readUUIDs(compoundTag, "DeathCountingEntities");
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

    @Override
    public AnimationInstance getAnimation() {
        return animationInstance;
    }
}
