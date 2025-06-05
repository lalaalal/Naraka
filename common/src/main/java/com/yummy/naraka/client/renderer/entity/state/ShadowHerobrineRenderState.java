package com.yummy.naraka.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ShadowHerobrineRenderState extends AbstractHerobrineRenderState {
    @Override
    public WavingScarfRenderState.ModelType getModelType() {
        return WavingScarfRenderState.ModelType.SMALL;
    }
}
