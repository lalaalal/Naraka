package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.NarakaFireball;
import com.yummy.naraka.world.entity.SkillUsingMob;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

public class ThrowFireballSkill extends Skill {
    public static final String NAME = "throw_fireball";

    public ThrowFireballSkill(SkillUsingMob mob) {
        super(NAME, 30, 160, mob);
    }

    private boolean canMove() {
        return !NarakaAttributeModifiers.hasAttributeModifier(mob, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.PREVENT_MOVING);
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        return target != null && (!canMove() || mob.distanceToSqr(target) > 5 * 5);
    }

    @Override
    public boolean readyToUse() {
        if (canMove())
            return super.readyToUse();
        return !disabled;
    }

    @Override
    protected void skillTick() {
        if (tickCount == 18) {
            NarakaFireball fireball = new NarakaFireball(mob, Vec3.ZERO, level());
            fireball.setPos(mob.getEyePosition());
            level().addFreshEntity(fireball);
            Vec3 view = mob.getViewVector(0);
            fireball.shoot(view.x, view.y, view.z, 1, 0);
        }
    }
}
