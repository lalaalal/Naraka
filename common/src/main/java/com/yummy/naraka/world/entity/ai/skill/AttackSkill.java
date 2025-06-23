package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

/**
 * Provides an interface for skill tick with target entity, attacking methods.
 *
 * @param <T> Type of mob
 * @see AttackSkill#hurtEntity(ServerLevel, LivingEntity)
 * @see AttackSkill#hurtEntities(ServerLevel, Predicate, double)
 */
public abstract class AttackSkill<T extends SkillUsingMob> extends TargetSkill<T> {
    protected int shieldCooldown = 0;
    protected int shieldDamage = 0;

    protected AttackSkill(ResourceLocation location, int duration, int cooldown, T mob, @Nullable Skill<?> linkedSkill) {
        super(location, duration, cooldown, mob, linkedSkill);
    }

    protected AttackSkill(String name, int duration, int cooldown, T mob) {
        super(name, duration, cooldown, mob);
    }

    protected AttackSkill(ResourceLocation location, int duration, int cooldown, T mob) {
        super(location, duration, cooldown, mob);
    }

    protected AttackSkill(ResourceLocation location, int duration, int cooldown, int shieldCooldown, int shieldDamage, T mob) {
        super(location, duration, cooldown, mob);
        this.shieldCooldown = shieldCooldown;
        this.shieldDamage = shieldDamage;
    }

    /**
     * Hurt entities satisfying given predicate and collide with mob.
     * Applying {@link #hurtEntity(ServerLevel, LivingEntity)} for selected targets.
     *
     * @param level     Server level
     * @param predicate Predicate to select target
     * @param size      Collision size
     * @see AttackSkill#hurtEntity(ServerLevel, LivingEntity)
     * @see AttackSkill#onHurtEntity(ServerLevel, LivingEntity)
     */
    protected final void hurtEntities(ServerLevel level, Predicate<LivingEntity> predicate, double size) {
        level.getNearbyEntities(LivingEntity.class, TargetingConditions.forCombat(), mob, mob.getBoundingBox().inflate(size))
                .stream()
                .filter(predicate)
                .forEach(target -> hurtEntity(level, target));
    }

    protected boolean canDisableShield() {
        return shieldCooldown > 0 && shieldDamage > 0;
    }

    protected boolean tryDisableShield(LivingEntity target) {
        return canDisableShield() && NarakaEntityUtils.disableAndHurtShield(target, shieldCooldown, shieldDamage);
    }

    /**
     * Hurt a single entity given with damage from {@link #calculateDamage(LivingEntity)}.
     *
     * @param level  Server level
     * @param target Target entity to hurt
     * @see AttackSkill#onHurtEntity(ServerLevel, LivingEntity)
     */
    protected boolean hurtEntity(ServerLevel level, LivingEntity target) {
        if (tryDisableShield(target))
            return false;

        onHurtEntity(level, target);
        return target.hurtServer(level, mob.getDefaultDamageSource(), calculateDamage(target));
    }

    protected void onHurtEntity(ServerLevel level, LivingEntity target) {

    }

    protected abstract float calculateDamage(LivingEntity target);
}
