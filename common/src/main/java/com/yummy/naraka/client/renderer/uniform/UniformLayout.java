package com.yummy.naraka.client.renderer.uniform;

import com.mojang.blaze3d.shaders.Shader;
import com.mojang.blaze3d.shaders.Uniform;
import com.yummy.naraka.core.posteffects.DynamicPostEffectType;
import com.yummy.naraka.core.posteffects.PostEffectConfig;
import net.minecraft.client.renderer.EffectInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class UniformLayout<C extends PostEffectConfig> {
    private final DynamicPostEffectType<C> type;
    private final String uniformName;
    private final List<UniformBuffer<?>> uniforms = new ArrayList<>();

    private long startGameTime;

    protected UniformLayout(DynamicPostEffectType<C> type, String uniformName) {
        this.type = type;
        this.uniformName = uniformName;
    }

    protected <T> UniformBuffer<T> register(UniformBuffer<T> uniformBuffer) {
        uniforms.add(uniformBuffer);
        return uniformBuffer;
    }

    public DynamicPostEffectType<C> type() {
        return type;
    }

    public String uniformName() {
        return uniformName;
    }

    protected abstract void readConfig(C config);

    public final void setConfig(long startGameTime, PostEffectConfig config) {
        if (config.type() == this.type())
            readConfig(type.cast(config));
        this.startGameTime = startGameTime;
    }

    protected abstract void update(float tick);

    public void define(Shader parent, Consumer<Uniform> registrar) {
        for (UniformBuffer<?> uniform : uniforms) {
            registrar.accept(uniform.createUniform(parent));
        }
    }

    public final void extract(float gameTime) {
        float tick = gameTime - startGameTime;
        this.update(tick);
    }

    public void setUniform(EffectInstance effectInstance) {
        for (UniformBuffer<?> uniform : uniforms) {
            uniform.setUniform(effectInstance);
        }
    }
}
