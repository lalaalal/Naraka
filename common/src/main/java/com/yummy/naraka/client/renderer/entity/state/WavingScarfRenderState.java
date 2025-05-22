package com.yummy.naraka.client.renderer.entity.state;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ScarfWavingData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class WavingScarfRenderState {
    private static final Vec3 PHASE_3_TRANSLATION = new Vec3(-0.12, -0.05, 0.035);
    private static final Vec3 PHASE_2_TRANSLATION = new Vec3(-0.08, -0.12, 0.08);

    public float rotationDegree;
    public float partialTick;
    public float scale = 3;
    public Vec3 translation = Vec3.ZERO;
    public WavingScarfTexture textureInfo = WavingScarfTexture.PHASE_2_FRONT;
    public ScarfWavingData waveData = new ScarfWavingData();

    public void extract(Herobrine herobrine, float partialTick) {
        this.rotationDegree = herobrine.getScarfRotationDegree(partialTick) - NarakaConfig.CLIENT.herobrineScarfDefaultRotation.getValue();
        if (herobrine.getPhase() < 3) {
            this.textureInfo = WavingScarfTexture.PHASE_2_FRONT;
            this.translation = PHASE_2_TRANSLATION;
            this.scale = 5;
        } else {
            this.textureInfo = WavingScarfTexture.PHASE_3;
            this.translation = PHASE_3_TRANSLATION;
            this.scale = 7;
        }
        this.waveData = herobrine.getScarfWavingData();
        this.partialTick = partialTick;
    }
}
