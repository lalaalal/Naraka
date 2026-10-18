package com.yummy.naraka.client.renderer.uniform;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.core.posteffects.ChromaticAberrationConfig;
import com.yummy.naraka.core.posteffects.NarakaPostEffectTypes;
import com.yummy.naraka.util.NarakaUtils;
import net.minecraft.util.Mth;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

public class ChromaticAberrationUniformLayout extends UniformLayout<ChromaticAberrationConfig> {
    private static final int UBO_SIZE = new Std140SizeCalculator()
            .putFloat()
            .putFloat()
            .putFloat()
            .putFloat()
            .get();

    private ChromaticAberrationConfig config = ChromaticAberrationConfig.ZERO;
    private float time;
    private float intensity;
    private float distortion;
    private float zoom;

    public ChromaticAberrationUniformLayout() {
        super(NarakaPostEffectTypes.CHROMATIC_ABERRATION.get(), "ChromaticAberrationConfig", UBO_SIZE);
    }

    @Override
    protected void readConfig(ChromaticAberrationConfig config) {
        this.config = config;
    }

    @Override
    protected void update(float time) {
        float delta = Mth.clamp(time / config.duration(), 0, 1);
        float multiplier = NarakaConfig.CLIENT.chromaticAberrationMultiplier.getValue();
        this.time = time;
        this.intensity = NarakaUtils.interpolate(delta, config.intensity() * multiplier, 0, NarakaUtils::fastStepIn);
        this.distortion = NarakaUtils.interpolate(delta, config.distortion() * multiplier, 0, NarakaUtils::fastStepIn);
        this.zoom = NarakaUtils.interpolate(delta, config.zoom() * multiplier, 0, NarakaUtils::fastStepIn);
    }

    @Override
    protected ByteBuffer getBuffer(MemoryStack stack) {
        return Std140Builder.onStack(stack, UBO_SIZE)
                .putFloat(time)
                .putFloat(intensity)
                .putFloat(distortion)
                .putFloat(zoom)
                .get();
    }
}
