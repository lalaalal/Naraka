package com.yummy.naraka.client.renderer.entity.state;

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
