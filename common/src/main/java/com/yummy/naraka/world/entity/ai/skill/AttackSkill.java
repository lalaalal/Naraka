package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 * Provides an interface for skill tick with target entity, attacking methods.
 *
 * @param <T> Type of mob
 * @see AttackSkill#hurtHitEntity(ServerLevel, LivingEntity)
 * @see AttackSkill#hurtHitEntities(ServerLevel, Predicate, double)
 */
public abstract class AttackSkill<T extends SkillUsingMob> extends TargetSkill<T> {
    protected AttackSkill(ResourceLocation location, int duration, int cooldown, T mob, @Nullable Skill<?> linkedSkill) {
        super(location, duration, cooldown, mob, linkedSkill);
    }

    protected AttackSkill(String name, int duration, int cooldown, T mob) {
        super(name, duration, cooldown, mob);
    }

    protected AttackSkill(ResourceLocation location, int duration, int cooldown, T mob) {
        super(location, duration, cooldown, mob);
    }

    /**
     * Hurt entities satisfying given predicate and collide with mob.
     * Applying {@link #hurtHitEntity(ServerLevel, LivingEntity)} for selected targets.
     *
     * @param level     Server level
     * @param predicate Predicate to select target
     * @param size      Collision size
     * @see AttackSkill#hurtHitEntity(ServerLevel, LivingEntity)
     */
    protected final void hurtHitEntities(ServerLevel level, Predicate<LivingEntity> predicate, double size) {
        level.getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), mob, mob.getBoundingBox().inflate(size))
                .stream()
                .filter(predicate)
                .forEach(target -> hurtHitEntity(level, target));
    }

    /**
     * Hurt a single entity given with damage from {@link #calculateDamage(LivingEntity)}.
     *
     * @param level  Server level
     * @param target Target entity to hurt
     */
    protected void hurtHitEntity(ServerLevel level, LivingEntity target) {
        DamageSource damageSource = mob.getDefaultDamageSource();
        target.hurtServer(level, damageSource, calculateDamage(target));
    }

    protected abstract float calculateDamage(LivingEntity target);
}
