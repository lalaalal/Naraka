package com.yummy.naraka.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public record WavingScarfPose(Vec3 translation, float scale, int waveOffset) {
    public static final WavingScarfPose PHASE_2_FRONT = new WavingScarfPose(new Vec3(-0.01, -0.20, 0.12), 3, 0);
    public static final WavingScarfPose PHASE_2_BACK = new WavingScarfPose(new Vec3(-0.04, -0.22, 0.1), 3, 10);
    public static final WavingScarfPose PHASE_3 = new WavingScarfPose(new Vec3(-0.12, -0.05, 0.035), 7, 0);
}
