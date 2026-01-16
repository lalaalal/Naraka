package com.yummy.naraka.world.item;

import com.yummy.naraka.util.QuadraticBezier;
import com.yummy.naraka.util.TickSchedule;
import com.yummy.naraka.world.entity.CorruptedStar;
import net.minecraft.util.Mth;
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

        int count = 8;
        for (int i = 0; i < count; i++) {
            float angle = Mth.TWO_PI / count * i + level.getRandom().nextFloat() * Mth.TWO_PI / count;
            addCorruptedStar(player, level, angle, 15, 15, yRot, i);
        }
        for (int i = 0; i < count; i++) {
            float angle = Mth.TWO_PI / count * i + level.getRandom().nextFloat() * Mth.TWO_PI / count;
            addCorruptedStar(player, level, angle, 12, 18, yRot, i + count);
        }

        return super.use(level, player, hand);
    }

    private void addCorruptedStar(Player player, Level level, float angle, float radius, float height, float yRot, int addAfter) {
        float middleAngle = level.getRandom().nextFloat() * Mth.TWO_PI;
        float middleX = Mth.cos(middleAngle) * radius + level.getRandom().nextFloat() - 0.5f;
        float middleY = player.getEyeHeight() + level.getRandom().nextFloat();
        float middleZ = Mth.sin(middleAngle) * radius + level.getRandom().nextFloat() - 0.5f;

        float targetX = Mth.cos(angle) * radius + level.getRandom().nextFloat();
        float targetY = height + level.getRandom().nextFloat() - 0.5f;
        float targetZ = Mth.sin(angle) * radius + level.getRandom().nextFloat();
        TickSchedule.executeAfter(level.getGameTime(), addAfter, () -> {
            level.addFreshEntity(new CorruptedStar(level, player, player.getEyePosition(), QuadraticBezier.fromZero(
                    new Vec3(middleX, middleY, middleZ),
                    new Vec3(targetX, targetY, targetZ)
            ).rotated(yRot)));
        });
    }
}
