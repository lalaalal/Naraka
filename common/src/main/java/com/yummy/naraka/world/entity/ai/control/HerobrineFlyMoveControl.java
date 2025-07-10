package com.yummy.naraka.world.entity.ai.control;

import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.Herobrine;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class HerobrineFlyMoveControl extends MoveControl {
    private final double hoverHeight;

    public HerobrineFlyMoveControl(Herobrine mob, double hoverHeight) {
        super(mob);
        this.hoverHeight = hoverHeight;
    }

    @Override
    public void setWantedPosition(double x, double y, double z, double speed) {
        BlockPos floor = NarakaUtils.findFloor(mob.level(), BlockPos.containing(x, y, z));
        double newY = floor.getY() + hoverHeight + 1;
        super.setWantedPosition(x, Math.max(newY, y), z, speed);
    }

    @Override
    public void tick() {
        if (this.operation == Operation.MOVE_TO) {
            this.operation = Operation.WAIT;
            float speed = (float) (mob.getAttributeValue(Attributes.FLYING_SPEED) * speedModifier);

            mob.setSpeed(speed);
            mob.setNoGravity(true);
            Vec3 wanted = new Vec3(wantedX, wantedY, wantedZ);
            Vec3 delta = wanted.subtract(mob.position());
            double distanceSquare = delta.x * delta.x + delta.z * delta.z;

            float yRot = (float) (Math.toDegrees(Mth.atan2(delta.z, delta.x))) - 90.0f;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), yRot, 90.0f));

            float proj = (float) Math.sqrt(distanceSquare);
            float xRot = (float) Math.toDegrees(Math.atan2(delta.y, proj));
            this.mob.setXRot(xRot);
            if (Math.abs(wantedY - mob.getY()) > 0.1) {
                this.mob.setYya(xRot * 0.015f);
            } else {
                this.mob.setYya(0);
                this.mob.setDeltaMovement(mob.getDeltaMovement().multiply(1, 0, 1));
            }

        } else {
            this.mob.setYya(0.0f);
            this.mob.setZza(0.0f);
        }
    }
}
