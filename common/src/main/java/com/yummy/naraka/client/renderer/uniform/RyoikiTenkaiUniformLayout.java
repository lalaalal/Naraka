package com.yummy.naraka.client.renderer.uniform;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.yummy.naraka.core.posteffects.NarakaPostEffectTypes;
import com.yummy.naraka.core.posteffects.PostEffectType;
import com.yummy.naraka.core.posteffects.RyoikiTenkaiConfig;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

public class RyoikiTenkaiUniformLayout extends UniformLayout<RyoikiTenkaiConfig> {
    private static final int UBO = new Std140SizeCalculator().putFloat().get();

    private RyoikiTenkaiConfig config;
    private float delta;

    public static RyoikiTenkaiUniformLayout green() {
        return new RyoikiTenkaiUniformLayout(NarakaPostEffectTypes.RYOIKI_GREEN.get(), "RyoikiGreenConfig", RyoikiTenkaiConfig.green(Float.POSITIVE_INFINITY, 0));
    }

    public static RyoikiTenkaiUniformLayout gray() {
        return new RyoikiTenkaiUniformLayout(NarakaPostEffectTypes.RYOIKI_GRAY.get(), "RyoikiGrayConfig", RyoikiTenkaiConfig.gray(Float.POSITIVE_INFINITY, 0));
    }

    private RyoikiTenkaiUniformLayout(PostEffectType<RyoikiTenkaiConfig> type, String uniformName, RyoikiTenkaiConfig defaultConfig) {
        super(type, uniformName, UBO);
        this.config = defaultConfig;
    }

    @Override
    protected void readConfig(RyoikiTenkaiConfig config) {
        this.config = config;
        this.delta = 1;
    }

    @Override
    protected void update(float tick) {
        if (tick > config.restoreStartTime())
            this.delta = 1 - Math.clamp((tick - config.restoreStartTime()) / config.restoreDuration(), 0, 1);
    }

    @Override
    protected ByteBuffer getBuffer(MemoryStack stack) {
        return Std140Builder.onStack(stack, UBO).putFloat(delta).get();
    }
}
