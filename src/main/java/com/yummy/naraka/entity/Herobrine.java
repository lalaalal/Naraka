package com.yummy.naraka.entity;

import com.yummy.naraka.attachment.DeathCountHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

public class Herobrine extends Monster implements DeathCountingEntity {
    private final Set<LivingEntity> deathCountedEntities = new HashSet<>();

    public Herobrine(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        registerGoals();

        DeathCountHelper.addDeathCountingEntity(this);
    }

    public static AttributeSupplier getAttributeSupplier() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .build();
    }

    @Override
    public Set<LivingEntity> getDeathCountedEntities() {
        return deathCountedEntities;
    }

    @Override
    public void onDeathCountZero(LivingEntity livingEntity) {
        deathCountedEntities.remove(livingEntity);
        if (livingEntity instanceof ServerPlayer player)
            DeathCountHelper.hideDeathCount(player);
        kill();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof LivingEntity livingEntity) {
            deathCountedEntities.add(livingEntity);
            if (livingEntity instanceof ServerPlayer serverPlayer)
                DeathCountHelper.showDeathCount(serverPlayer);
        }

        return super.hurt(source, amount);
    }

    @Override
    public void die(DamageSource damageSource) {
        DeathCountHelper.removeDeathCountingEntity(this);
        super.die(damageSource);
    }
}
