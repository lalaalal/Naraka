package com.yummy.naraka.world.entity;

import com.yummy.naraka.config.NarakaConfig;
import net.minecraft.util.Mth;

import java.util.LinkedList;

public class ScarfWavingData {
    private final LinkedList<Float> verticalPositions;
    private final int verticalPartitionNumber;

    public ScarfWavingData() {
        verticalPartitionNumber = NarakaConfig.CLIENT.herobrineScarfPartitionNumber.getValue();
        verticalPositions = new LinkedList<>();
        for (int i = 0; i < verticalPartitionNumber + 2; i++) {
            verticalPositions.add(0f);
        }
    }

    public void update(int tickCount, float verticalSpeed) {
        float multiplier = 1 + verticalSpeed;
        float degree = Mth.wrapDegrees(tickCount * 6 * multiplier);
        float angle = (float) Math.toRadians(degree);

        float y = Mth.sin(angle) * 0.05f;

        verticalPositions.poll();
        verticalPositions.add(y);
    }

    public int getVerticalSize() {
        return verticalPartitionNumber;
    }

    public int getHorizontalSize() {
        return 1;
    }

    public float getVerticalPosition(int index, float partialTick) {
        index = index + 1;
        if (index < 0 || verticalPositions.size() - 1 <= index)
            return 0;
        index = verticalPositions.size() - 1 - index;

        return Mth.lerp(partialTick, verticalPositions.get(index - 1), verticalPositions.get(index));
    }

    public float getHorizontalPosition(int index) {
        return 0;
    }
}
