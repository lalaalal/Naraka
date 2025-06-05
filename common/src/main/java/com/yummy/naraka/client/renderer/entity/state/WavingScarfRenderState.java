package com.yummy.naraka.client.renderer.entity.state;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.ScarfWavingData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.List;

@Environment(EnvType.CLIENT)
public class WavingScarfRenderState {
    private static final List<ModelData> PHASE_2_MODEL_DATA = List.of(
            ModelData.of(WavingScarfTexture.PHASE_2_BACK, WavingScarfPose.PHASE_2_BACK),
            ModelData.of(WavingScarfTexture.PHASE_2_FRONT, WavingScarfPose.PHASE_2_FRONT)
    );

    private static final List<ModelData> PHASE_3_MODEL_DATA = List.of(
            ModelData.of(WavingScarfTexture.PHASE_3, WavingScarfPose.PHASE_3)
    );

    public float rotationDegree;
    public float partialTick;
    public List<ModelData> modelDataList = PHASE_2_MODEL_DATA;
    public ScarfWavingData waveData = new ScarfWavingData();

    public void extract(AbstractHerobrine herobrine, ModelType type, float partialTick) {
        this.rotationDegree = herobrine.getScarfRotationDegree(partialTick) - NarakaConfig.CLIENT.herobrineScarfDefaultRotation.getValue();
        this.modelDataList = type.modelData;
        this.waveData = herobrine.getScarfWavingData();
        this.partialTick = partialTick;
    }

    public record ModelData(WavingScarfTexture textureInfo, WavingScarfPose pose) {
        public static ModelData of(WavingScarfTexture textureInfo, WavingScarfPose pose) {
            return new ModelData(textureInfo, pose);
        }
    }

    public enum ModelType {
        SMALL(PHASE_2_MODEL_DATA),
        BIG(PHASE_3_MODEL_DATA);

        public final List<ModelData> modelData;

        ModelType(List<ModelData> modelData) {
            this.modelData = modelData;
        }
    }
}
