package com.yummy.naraka.world.entity.ai.control;

import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.Herobrine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class HerobrineFlyMoveControl extends MoveControl {
    private final double hoverHeight;
    private final Herobrine herobrine;

    public HerobrineFlyMoveControl(Herobrine mob, double hoverHeight, double speedModifier) {
        super(mob);
        this.herobrine = mob;
        this.hoverHeight = hoverHeight;
        this.speedModifier = speedModifier;
    }

    @Override
    public void setWantedPosition(double x, double y, double z, double speed) {
        BlockPos floor = NarakaUtils.findFloor(mob.level(), BlockPos.containing(x, y, z));
        super.setWantedPosition(x, floor.getY() + hoverHeight + 1, z, speed);
    }

    @Override
    public void tick() {
        if (!hasWanted() || herobrine.isUsingSkill())
            return;

        if (this.operation == Operation.MOVE_TO) {
            this.operation = Operation.WAIT;
            double speed = mob.getAttributeValue(Attributes.FLYING_SPEED) * 0.3;

            mob.setNoGravity(true);
            Vec3 wanted = new Vec3(wantedX, wantedY, wantedZ);
            Vec3 delta = wanted.subtract(mob.position())
                    .normalize()
                    .scale(speed);

            mob.setDeltaMovement(delta);
        }
    }
}
