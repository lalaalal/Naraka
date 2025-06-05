package com.yummy.naraka.world.entity;

import com.yummy.naraka.config.NarakaConfig;
import net.minecraft.util.Mth;

import java.util.LinkedList;
import java.util.List;

public class ScarfWavingData {
    private final List<Float> verticalPositions;
    private final List<Float> verticalShifts;
    private final List<Float> horizontalPositions;

    private final int partitionNumber;
    private float verticalDegree;
    private float ySpeed;

    public ScarfWavingData() {
        partitionNumber = NarakaConfig.CLIENT.herobrineScarfPartitionNumber.getValue();
        verticalPositions = new LinkedList<>();
        verticalShifts = new LinkedList<>();
        horizontalPositions = new LinkedList<>();
        for (int i = 0; i < partitionNumber * 2; i++) {
            verticalPositions.add(0f);
            verticalShifts.add(0f);
            horizontalPositions.add(0f);
        }
    }

    public void update(float xzSpeed, float ySpeed, float rotationSpeed) {
        float multiplier = 1 + xzSpeed;
        this.ySpeed = Mth.lerp(0.5f, this.ySpeed, Mth.clamp(ySpeed * 2f, -1, 1) * 0.1f);
        verticalDegree = Mth.wrapDegrees(verticalDegree + 9 * multiplier);
        float verticalAngle = (float) Math.toRadians(verticalDegree);

        float verticalY = Mth.sin(verticalAngle) * 0.02f;
        verticalPositions.removeLast();
        verticalPositions.addFirst(verticalY);
        float value = verticalPositions.get(2);
        verticalPositions.set(2, value + this.ySpeed);

        float horizontalY = Mth.sin(verticalAngle) * 0.005f;
        horizontalPositions.removeFirst();
        horizontalPositions.addLast(horizontalY);

        float prevShift = verticalShifts.getFirst();
        rotationSpeed = Mth.clamp(rotationSpeed, -45, 45);
        float shift = Mth.lerp(0.3f, prevShift, (rotationSpeed / 180) * 0.2f);
        verticalShifts.removeLast();
        verticalShifts.addFirst(shift);
    }

    public int getVerticalSize() {
        return partitionNumber;
    }

    public int getHorizontalSize() {
        return partitionNumber;
    }

    public float getVerticalPosition(int index, float partialTick) {
        index = index + 1;
        if (index < 0 || verticalPositions.size() - 1 <= index)
            return 0;

        return Mth.lerp(partialTick, verticalPositions.get(index + 1), verticalPositions.get(index));
    }

    public float getHorizontalPosition(int index, float partialTick) {
        index = index + 1;
        if (index < 0 || horizontalPositions.size() - 1 <= index)
            return 0;

        return Mth.lerp(partialTick, horizontalPositions.get(index), horizontalPositions.get(index + 1));
    }

    public float getVerticalShift(int index, float partialTick) {
        index = index + 1;
        if (index < 0 || verticalShifts.size() - 1 <= index)
            return 0;
        return Mth.lerp(partialTick, verticalShifts.get(index + 1), verticalShifts.get(index));
    }
}
