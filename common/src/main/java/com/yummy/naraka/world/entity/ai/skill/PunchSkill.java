package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.SkillUsingMob;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PunchSkill extends Skill {
    public static final String NAME = "punch";

    public PunchSkill(SkillUsingMob mob) {
        super(NAME, 30, 100, mob);
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        if (target != null)
            return mob.distanceToSqr(target) <= 4;
        return false;
    }

    @Override
    protected void skillTick() {
        LivingEntity target = mob.getTarget();
        if (target == null || tickCount != 8)
            return;

        DamageSource fixedAttack = NarakaDamageSources.fixed(mob);
        float damage = target.getMaxHealth() * 0.03f + 6;
        if (target instanceof Player player && target.isBlocking()) {
            player.disableShield();
            return;
        }

        NarakaAttributeModifiers.stunEntity(target, 100);
        target.hurt(fixedAttack, damage);
        target.knockback(2f, mob.getX() - target.getX(), mob.getZ() - target.getZ());
    }
}
