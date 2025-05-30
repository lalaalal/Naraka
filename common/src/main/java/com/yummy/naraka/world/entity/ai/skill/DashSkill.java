package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.util.NarakaEntityUtils;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Afterimage;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class DashSkill<T extends AbstractHerobrine> extends TargetSkill<T> {
    public static final ResourceLocation LOCATION = createLocation("dash");

    private Vec3 deltaMovement = Vec3.ZERO;
    private float scale = 1;

    public static void setupDashBack(DashSkill<?> dashSkill, Skill<?> linkSkill) {
        dashSkill.setScale(-0.5f);
        dashSkill.setLinkedSkill(linkSkill);
    }

    public DashSkill(T mob) {
        super(LOCATION, 10, 40, mob);
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        LivingEntity target = mob.getTarget();
        return target != null;
    }

    @Override
    protected void onFirstTick(ServerLevel level) {
        mob.getNavigation().stop();
    }

    @Override
    protected void tickWithTarget(ServerLevel level, LivingEntity target) {
        lookTarget(target);
        rotateTowardTarget(target);

        runAt(0, () -> updateDeltaMovement(target));
        run(between(0, 10) && mob.distanceToSqr(target) > 3, () -> move(level));
        run(at(9) || (mob.distanceToSqr(target) < 3 && scale > 0), () -> mob.setDeltaMovement(Vec3.ZERO));
    }

    @Override
    protected void onLastTick(ServerLevel level) {
        scale = 1;
    }

    private void updateDeltaMovement(LivingEntity target) {
        this.deltaMovement = NarakaEntityUtils.getDirectionNormalVector(mob, target)
                .multiply(1, 0, 1)
                .scale(scale);
        if (hasLinkedSkill())
            this.deltaMovement = deltaMovement.scale(1.5);
    }

    private void move(ServerLevel level) {
        Vec3 xzMovement = deltaMovement.multiply(1, 0, 1).normalize();
        Vec3 targetPosition = mob.position().add(xzMovement);
        BlockPos blockPos = BlockPos.containing(targetPosition);
        while (isWall(level, blockPos) || isWall(level, mob.blockPosition())) {
            mob.setPos(targetPosition);
            targetPosition = mob.position().add(xzMovement);
            blockPos = BlockPos.containing(targetPosition).above();
        }
        mob.setDeltaMovement(deltaMovement);
        if (!deltaMovement.equals(Vec3.ZERO))
            mob.addAfterimage(Afterimage.of(mob, 10), 1, tickCount < 9);
    }

    private boolean isWall(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        BlockState aboveState = level.getBlockState(pos.above());
        return state.canOcclude() || aboveState.canOcclude();
    }
}
