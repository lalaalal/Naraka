package com.yummy.naraka.world.entity;

import com.yummy.naraka.config.NarakaConfig;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.LinkedList;
import java.util.List;

public class ScarfWavingData {
    private static final float MAX_SCARF_ROTATION_DEGREE = 70;

    private final List<Float> verticalPositions;
    private final List<Float> verticalShifts;
    private final List<Float> horizontalPositions;

    private final int partitionNumber;
    private float verticalDegree;
    private float ySpeed;

    private float scarfRotationDegree;
    private float prevScarfRotationDegree;

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

    private void updateScarfRotation(Vec3 movement, boolean onGround) {
        Vec3 projection = movement.multiply(1, 0, 1);
        float targetRotation = (float) projection.length() * 100 * 10;
        if (scarfRotationDegree < targetRotation)
            scarfRotationDegree += 1;
        if (scarfRotationDegree > targetRotation)
            scarfRotationDegree -= 1;
        if (!onGround)
            scarfRotationDegree = scarfRotationDegree - (float) movement.y * 30;
        scarfRotationDegree = Mth.clamp(scarfRotationDegree, 0, MAX_SCARF_ROTATION_DEGREE);
    }


    public void update(Vec3 deltaMovement, float rotationSpeed, boolean onGround) {
        prevScarfRotationDegree = scarfRotationDegree;
        updateScarfRotation(deltaMovement, onGround);
        float multiplier = 1 + Mth.lerp(scarfRotationDegree / MAX_SCARF_ROTATION_DEGREE, 0, 1);

        this.ySpeed = Mth.lerp(0.5f, this.ySpeed, Mth.clamp((float) deltaMovement.y * 2f, -1, 1) * 0.1f);
        verticalDegree = Mth.wrapDegrees(verticalDegree + 9 * multiplier);
        float verticalAngle = (float) Math.toRadians(verticalDegree);

        float verticalY = Mth.sin(verticalAngle) * 0.02f;
        verticalPositions.removeLast();
        verticalPositions.addFirst(verticalY + this.ySpeed);

        float horizontalY = Mth.sin(verticalAngle) * 0.005f;
        horizontalPositions.removeFirst();
        horizontalPositions.addLast(horizontalY);

        float prevShift = verticalShifts.getFirst();
        rotationSpeed = Mth.clamp(rotationSpeed, -30, 30);
        float shift = Mth.lerp(0.2f, prevShift, (rotationSpeed / 180) * 0.075f);
        verticalShifts.removeLast();
        verticalShifts.addFirst(shift);
    }

    public float getScarfRotationDegree(float partialTicks) {
        return Mth.lerp(partialTicks, prevScarfRotationDegree, scarfRotationDegree) - MAX_SCARF_ROTATION_DEGREE;
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
        index = (index / 2) + 1;
        if (index < 0 || verticalShifts.size() - 1 <= index)
            return 0;
        return Mth.lerp(partialTick, verticalShifts.get(index + 1), verticalShifts.get(index));
    }
}
