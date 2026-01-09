package com.yummy.naraka.world.item;

import com.yummy.naraka.util.QuadraticBezier;
import com.yummy.naraka.util.TickSchedule;
import com.yummy.naraka.world.entity.CorruptedStar;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CorruptedStarStaffItem extends Item {
    public CorruptedStarStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        float yRot = (float) Math.toRadians(90 - player.getYRot());
        level.addFreshEntity(new CorruptedStar(level, player, player.getEyePosition(), QuadraticBezier.fromZero(
                new Vec3(3, 1.5f, -3),
                new Vec3(0, 3, 3)
        ).rotated(yRot)));
        TickSchedule.executeAfter(level.getGameTime(), 2, () -> {
            level.addFreshEntity(new CorruptedStar(level, player, player.getEyePosition(), QuadraticBezier.fromZero(
                    new Vec3(3, 1.5f, -3),
                    new Vec3(0, 4, 1.5)
            ).rotated(yRot)));
        });
        TickSchedule.executeAfter(level.getGameTime(), 4, () -> {
            level.addFreshEntity(new CorruptedStar(level, player, player.getEyePosition(), QuadraticBezier.fromZero(
                    new Vec3(3, 1.5f, -3),
                    new Vec3(0, 4.5, 0)
            ).rotated(yRot)));
        });
        TickSchedule.executeAfter(level.getGameTime(), 6, () -> {
            level.addFreshEntity(new CorruptedStar(level, player, player.getEyePosition(), QuadraticBezier.fromZero(
                    new Vec3(3, 1.5f, -3),
                    new Vec3(0, 4, -1.5)
            ).rotated(yRot)));
        });
        TickSchedule.executeAfter(level.getGameTime(), 8, () -> {
            level.addFreshEntity(new CorruptedStar(level, player, player.getEyePosition(), QuadraticBezier.fromZero(
                    new Vec3(3, 1.5f, -3),
                    new Vec3(0, 3, -3)
            ).rotated(yRot)));
        });

        return super.use(level, player, hand);
    }
}
