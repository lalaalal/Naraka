package com.yummy.naraka.client.renderer.entity.state;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ScarfWavingData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class WavingScarfRenderState {
    public float rotationDegree;
    public float partialTick;
    public WavingScarfTexture textureInfo = WavingScarfTexture.PHASE_2_FRONT;
    public ScarfWavingData waveData = new ScarfWavingData();

    public void extract(Herobrine herobrine, float partialTick) {
        this.rotationDegree = herobrine.getScarfRotationDegree(partialTick) - NarakaConfig.CLIENT.herobrineScarfDefaultRotation.getValue();
        this.textureInfo = WavingScarfTexture.PHASE_2_FRONT;
        this.waveData = herobrine.getScarfWavingData();
        this.partialTick = partialTick;
    }
}
