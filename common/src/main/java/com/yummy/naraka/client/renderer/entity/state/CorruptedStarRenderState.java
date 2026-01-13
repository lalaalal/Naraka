package com.yummy.naraka.client.renderer.entity.state;

import net.minecraft.world.phys.Vec3;

public class CorruptedStarRenderState extends LightTailEntityRenderState {
    public boolean verticalShine;
    public float shineScale;
    public int shineStartTick;
    public int shineLifetime;
    public float shineRotation;
    public Vec3 targetPosition = Vec3.ZERO;
}
