package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class ThrowFireballSkill extends Skill<SkillUsingMob> {
    public static final ResourceLocation LOCATION = createLocation("throw_fireball");
    private final Supplier<Fireball> fireballCreator;

    public ThrowFireballSkill(SkillUsingMob mob, Supplier<Fireball> fireballCreator) {
        super(LOCATION, 30, 160, mob);
        this.fireballCreator = fireballCreator;
    }

    private boolean canMove() {
        return mob.getAttributeValue(Attributes.MOVEMENT_SPEED) > 0;
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
    protected void skillTick(ServerLevel level) {
        if (tickCount == 18) {
            Fireball fireball = fireballCreator.get();
            fireball.setPos(mob.getX(), mob.getEyeY() + 0.5, mob.getZ());
            level.addFreshEntity(fireball);
            Vec3 view = mob.getViewVector(0);
            fireball.shoot(view.x, view.y, view.z, 1, 0);
        }
    }
}
