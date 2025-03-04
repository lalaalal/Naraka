package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.SkillUsingMob;
import com.yummy.naraka.world.entity.StunHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class PunchSkill extends Skill<SkillUsingMob> {
    public static final String NAME = "punch";

    public PunchSkill(SkillUsingMob mob) {
        super(NAME, 30, 120, mob);
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

        mob.lookAt(target, 360, 0);
        DamageSource fixedAttack = NarakaDamageSources.fixed(mob);
        float damage = target.getMaxHealth() * 0.03f + 6;
        if (NarakaEntityUtils.disableAndHurtShield(target, 100, 2))
            return;

        StunHelper.stunEntity(target, 100);
        target.hurt(fixedAttack, damage);
        target.knockback(2f, mob.getX() - target.getX(), mob.getZ() - target.getZ());
    }
}
