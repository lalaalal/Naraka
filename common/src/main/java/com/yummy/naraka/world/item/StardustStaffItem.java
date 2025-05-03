package com.yummy.naraka.world.item;

import com.yummy.naraka.util.TickSchedule;
import com.yummy.naraka.world.entity.Stardust;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class StardustStaffItem extends Item {
    public StardustStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        RandomSource random = player.getRandom();
        if (!level.isClientSide) {
            for (int i = 0; i < 16; i++) {
                int tickAfter = random.nextIntBetweenInclusive(1, 10);
                float yRot = 360f / 16 * i;
                float xRot = random.nextFloat() * 30 + 15;
                Vec3 shootingVector = player.calculateViewVector(-xRot, yRot);
                int power = random.nextIntBetweenInclusive(3, 5);
                int waitingTick = random.nextIntBetweenInclusive(2, 3) * 5;
                TickSchedule.executeAfter(level.getGameTime(), tickAfter, () -> {
                    Stardust stardust = new Stardust(level, player, shootingVector, power, waitingTick);
                    level.addFreshEntity(stardust);
                });
            }
        }
        return super.use(level, player, usedHand);
    }
}
