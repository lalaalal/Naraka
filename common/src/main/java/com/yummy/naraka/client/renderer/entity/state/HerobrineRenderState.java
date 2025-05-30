package com.yummy.naraka.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class HerobrineRenderState extends AbstractHerobrineRenderState {
    public int phase;

    @Override
    public WavingScarfRenderState.ModelType getModelType() {
        if (phase == 3)
            return WavingScarfRenderState.ModelType.BIG;
        return WavingScarfRenderState.ModelType.SMALL;
    }
}
