package com.yummy.naraka.world.entity.ai.skill.herobrine;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.SkillUsingMob;
import com.yummy.naraka.world.entity.ai.skill.TargetSkill;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ThrowFireballSkill extends TargetSkill<SkillUsingMob> {
    public static final Identifier LOCATION = createLocation("herobrine.throw_fireball");
    private final Function<ServerLevel, Fireball> fireballCreator;
    @Nullable
    private Fireball fireball;

    public ThrowFireballSkill(SkillUsingMob mob, Function<ServerLevel, Fireball> fireballCreator) {
        super(LOCATION, mob, 35, 100);
        this.fireballCreator = fireballCreator;
    }

    private boolean canMove() {
        return mob.getAttributeValue(Attributes.MOVEMENT_SPEED) > 0;
    }

    @Override
    public boolean canUse(ServerLevel level) {
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
    protected void onFirstTick(ServerLevel level) {
        mob.getNavigation().stop();
    }

    @Override
    protected void tickAlways(ServerLevel level, @Nullable LivingEntity target) {
        runAt(5, () -> this.summonFireball(level));
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        lookTarget(target);
        rotateTowardTarget(target);
        runAt(15, () -> this.throwFireball(target));
    }

    private void summonFireball(ServerLevel level) {
        float yRot = (float) Math.toRadians(-mob.getYRot());
        Vec3 relativePosition = new Vec3(0.5, 0.5, -0.7);
        Vec3 fireballPosition = mob.getEyePosition()
                .add(relativePosition.yRot(yRot));

        fireball = fireballCreator.apply(level);
        fireball.setPos(fireballPosition);
        level.addFreshEntity(fireball);
    }

    private void throwFireball(@Nullable LivingEntity target) {
        if (fireball == null)
            return;
        Vec3 vec = mob.getLookAngle();
        if (target != null)
            vec = NarakaEntityUtils.getDirectionNormalVector(fireball.position(), target.getEyePosition());
        fireball.shoot(vec.x, vec.y, vec.z, 1.5f, 0);
    }
}
