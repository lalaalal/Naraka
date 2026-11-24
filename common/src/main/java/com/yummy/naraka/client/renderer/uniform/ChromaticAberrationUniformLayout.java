package com.yummy.naraka.client.renderer.uniform;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.core.posteffects.ChromaticAberrationConfig;
import com.yummy.naraka.core.posteffects.NarakaDynamicPostEffectTypes;
import com.yummy.naraka.util.NarakaUtils;
import net.minecraft.util.Mth;

public class ChromaticAberrationUniformLayout extends UniformLayout<ChromaticAberrationConfig> {
    private ChromaticAberrationConfig config = ChromaticAberrationConfig.ZERO;
    private final UniformBuffer<Float> time = register(UniformBuffer.createFloat("time"));
    private final UniformBuffer<Float> intensity = register(UniformBuffer.createFloat("intensity"));
    private final UniformBuffer<Float> distortion = register(UniformBuffer.createFloat("distortion"));
    private final UniformBuffer<Float> zoom = register(UniformBuffer.createFloat("zoom"));

    public ChromaticAberrationUniformLayout() {
        super(NarakaDynamicPostEffectTypes.CHROMATIC_ABERRATION.get(), "ChromaticAberrationConfig");
    }

    @Override
    protected void readConfig(ChromaticAberrationConfig config) {
        this.config = config;
    }

    @Override
    protected void update(float time) {
        float delta = Mth.clamp(time / config.duration(), 0, 1);
        float multiplier = NarakaConfig.CLIENT.chromaticAberrationMultiplier.getValue();
        this.time.storeValue(time);
        this.intensity.storeValue(NarakaUtils.interpolate(delta, config.intensity() * multiplier, 0, NarakaUtils::fastStepIn));
        this.distortion.storeValue(NarakaUtils.interpolate(delta, config.distortion() * multiplier, 0, NarakaUtils::fastStepIn));
        this.zoom.storeValue(NarakaUtils.interpolate(delta, config.zoom() * multiplier, 0, NarakaUtils::fastStepIn));
    }
}
