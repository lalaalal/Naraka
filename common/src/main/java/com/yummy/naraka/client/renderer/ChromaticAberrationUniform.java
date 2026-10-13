package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.yummy.naraka.NarakaPostEffects;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.util.NarakaUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

public class ChromaticAberrationUniform extends PostEffectUniform {
    private static final int UBO_SIZE = new Std140SizeCalculator().putFloat().putFloat().get();

    private AberrationConfig config = AberrationConfig.ZERO;

    private static GpuBuffer createBuffer() {
        GpuDevice device = RenderSystem.getDevice();
        return device.createBuffer(() -> "Chromatic Aberration UBO ", 136, UBO_SIZE);
    }

    protected ChromaticAberrationUniform() {
        super("AberrationConfig", NarakaPostEffects.CHROMATIC_ABERRATION, createBuffer());
    }

    @Override
    protected UniformBuffer uniformBuffer() {
        return config;
    }

    @Override
    public void reset(long gameTime) {
        config = new AberrationConfig(gameTime, gameTime, 1);
    }

    @Override
    public void extract(float gameTime) {
        float delta = Mth.clamp((gameTime - config.startGameTime) / 50f, 0, 1);
        float startIntensity = NarakaConfig.CLIENT.chromaticAberrationIntensity.getValue();
        float intensity = NarakaUtils.interpolate(delta, startIntensity, 0, NarakaUtils::fastStepIn);
        config = new AberrationConfig(config.startGameTime, gameTime, intensity);
    }

    public record AberrationConfig(long startGameTime, float gameTime, float intensity) implements UniformBuffer {
        public static final AberrationConfig ZERO = new AberrationConfig(0, 0, 0);

        @Override
        public Identifier target() {
            return NarakaPostEffects.CHROMATIC_ABERRATION;
        }

        @Override
        public ByteBuffer create(MemoryStack stack) {
            return Std140Builder.onStack(stack, UBO_SIZE).putFloat(gameTime - startGameTime).putFloat(intensity).get();
        }
    }
}
