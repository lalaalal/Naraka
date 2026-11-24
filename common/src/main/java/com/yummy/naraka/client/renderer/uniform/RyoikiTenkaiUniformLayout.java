package com.yummy.naraka.client.renderer.uniform;

import com.yummy.naraka.core.posteffects.DynamicPostEffectType;
import com.yummy.naraka.core.posteffects.NarakaDynamicPostEffectTypes;
import com.yummy.naraka.core.posteffects.RyoikiTenkaiConfig;

public class RyoikiTenkaiUniformLayout extends UniformLayout<RyoikiTenkaiConfig> {
    private RyoikiTenkaiConfig config;
    private final UniformBuffer<Float> delta = register(UniformBuffer.createFloat("delta"));

    public static RyoikiTenkaiUniformLayout green() {
        return new RyoikiTenkaiUniformLayout(NarakaDynamicPostEffectTypes.RYOIKI_GREEN.get(), "RyoikiGreenConfig", RyoikiTenkaiConfig.green(Float.POSITIVE_INFINITY, 0));
    }

    public static RyoikiTenkaiUniformLayout gray() {
        return new RyoikiTenkaiUniformLayout(NarakaDynamicPostEffectTypes.RYOIKI_GRAY.get(), "RyoikiGrayConfig", RyoikiTenkaiConfig.gray(Float.POSITIVE_INFINITY, 0));
    }

    private RyoikiTenkaiUniformLayout(DynamicPostEffectType<RyoikiTenkaiConfig> type, String uniformName, RyoikiTenkaiConfig defaultConfig) {
        super(type, uniformName);
        this.config = defaultConfig;
    }

    @Override
    protected void readConfig(RyoikiTenkaiConfig config) {
        this.config = config;
        this.delta.storeValue(1f);
    }

    @Override
    protected void update(float tick) {
        if (tick > config.restoreStartTime())
            this.delta.storeValue(1 - Math.clamp((tick - config.restoreStartTime()) / config.restoreDuration(), 0, 1));
    }
}
