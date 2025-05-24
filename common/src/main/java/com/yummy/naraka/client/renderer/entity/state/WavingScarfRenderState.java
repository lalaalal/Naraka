package com.yummy.naraka.client.renderer.entity.state;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ScarfWavingData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class WavingScarfRenderState {
    private static final List<ModelData> PHASE_2_MODEL_DATA = List.of(
            ModelData.of(WavingScarfTexture.PHASE_2_BACK, WavingScarfPose.PHASE_2_BACK),
            ModelData.of(WavingScarfTexture.PHASE_2_FRONT, WavingScarfPose.PHASE_2_FRONT)
    );

    private static final List<ModelData> PHASE_3_MODEL_DATA = List.of(
            ModelData.of(WavingScarfTexture.PHASE_3, WavingScarfPose.PHASE_3)
    );

    private static final Map<Integer, List<ModelData>> PHASE_SCARF_MODEL_DATA = Map.of(
            2, PHASE_2_MODEL_DATA,
            3, PHASE_3_MODEL_DATA
    );

    public float rotationDegree;
    public float partialTick;
    public List<ModelData> modelDataList = PHASE_2_MODEL_DATA;
    public ScarfWavingData waveData = new ScarfWavingData();

    public void extract(Herobrine herobrine, float partialTick) {
        this.rotationDegree = herobrine.getScarfRotationDegree(partialTick) - NarakaConfig.CLIENT.herobrineScarfDefaultRotation.getValue();
        this.modelDataList = PHASE_SCARF_MODEL_DATA.getOrDefault(herobrine.getPhase(), PHASE_2_MODEL_DATA);
        this.waveData = herobrine.getScarfWavingData();
        this.partialTick = partialTick;
    }

    public record ModelData(WavingScarfTexture textureInfo, WavingScarfPose pose) {
        public static ModelData of(WavingScarfTexture textureInfo, WavingScarfPose pose) {
            return new ModelData(textureInfo, pose);
        }
    }
}
