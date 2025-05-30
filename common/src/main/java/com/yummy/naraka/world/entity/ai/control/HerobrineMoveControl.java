package com.yummy.naraka.world.entity.ai.control;

import com.yummy.naraka.util.NarakaUtils;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class HerobrineMoveControl extends MoveControl {
    private final double hoverHeight;

    public HerobrineMoveControl(Mob mob, double hoverHeight, double speedModifier) {
        super(mob);
        this.hoverHeight = hoverHeight;
        this.speedModifier = speedModifier;
    }

    @Override
    public void tick() {
        BlockPos floor = NarakaUtils.findFloor(mob.level(), mob.blockPosition());
        double height = mob.position().y - floor.getY() - 1;
        double diff = height - hoverHeight;
        if (this.operation == Operation.MOVE_TO) {
            this.operation = Operation.WAIT;
            double speed = mob.getAttributeValue(Attributes.FLYING_SPEED) * 0.3;

            mob.setNoGravity(true);
            Vec3 wanted = new Vec3(wantedX, wantedY, wantedZ);
            Vec3 delta = wanted.subtract(mob.position())
                    .normalize()
                    .scale(speed);
            if (-0.2 < diff && diff < 0.2)
                delta.multiply(1, 0, 1);
            mob.setDeltaMovement(delta);
            mob.lookAt(EntityAnchorArgument.Anchor.EYES, wanted);
        } else {
            Vec3 movement = mob.getDeltaMovement();
            if (-0.2 < diff && diff < 0.2) {
                mob.setDeltaMovement(movement.multiply(1, 0, 1));
            } else {
                if (diff < 0) {
                    mob.addDeltaMovement(new Vec3(0, 0.1, 0));
                } else {
                    mob.addDeltaMovement(new Vec3(0, -0.05, 0));
                }
            }
        }
    }
}
