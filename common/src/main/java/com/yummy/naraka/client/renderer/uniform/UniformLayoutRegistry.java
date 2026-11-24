package com.yummy.naraka.client.renderer.uniform;

import com.mojang.blaze3d.shaders.Shader;
import com.mojang.blaze3d.shaders.Uniform;
import com.yummy.naraka.core.posteffects.DynamicPostEffectType;
import com.yummy.naraka.core.posteffects.PostEffectConfig;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class UniformLayoutRegistry {
    private final Map<DynamicPostEffectType<?>, UniformLayout<?>> uniformLayouts = new HashMap<>();

    @Nullable
    private static UniformLayoutRegistry instance = null;

    /**
     * To easily access from Mixin
     */
    public static UniformLayoutRegistry getInstance() {
        if (instance == null)
            throw new IllegalStateException("UniformLayoutRegistry is not initialized");
        return instance;
    }

    public UniformLayoutRegistry() {
        add(new ChromaticAberrationUniformLayout());
        add(RyoikiTenkaiUniformLayout.green());
        add(RyoikiTenkaiUniformLayout.gray());

        instance = this;
    }

    private void add(UniformLayout<?> uniformLayout) {
        this.uniformLayouts.put(uniformLayout.type(), uniformLayout);
    }

    public void setConfig(long gameTime, PostEffectConfig config) {
        if (uniformLayouts.containsKey(config.type())) {
            uniformLayouts.get(config.type()).setConfig(gameTime, config);
        }
    }

    public void extract(float gameTime, Collection<ResourceLocation> postEffects) {
        for (ResourceLocation postEffectId : postEffects) {
            DynamicPostEffectType.fromId(postEffectId)
                    .ifPresent(type -> this.extract(type, gameTime));
        }
    }

    private void extract(DynamicPostEffectType<?> type, float gameTime) {
        if (uniformLayouts.containsKey(type)) {
            UniformLayout<?> uniformLayout = uniformLayouts.get(type);
            uniformLayout.extract(gameTime);
        }
    }

    public void defineUniform(ResourceLocation postEffectId, Shader parent, Consumer<Uniform> registrar) {
        DynamicPostEffectType.fromId(postEffectId)
                .ifPresent(type -> this.defineUniform(type, parent, registrar));
    }

    public void defineUniform(DynamicPostEffectType<?> type, Shader parent, Consumer<Uniform> registrar) {
        if (uniformLayouts.containsKey(type)) {
            UniformLayout<?> layout = uniformLayouts.get(type);
            layout.define(parent, registrar);
        }
    }

    public void setUniform(ResourceLocation postEffectId, EffectInstance effectInstance) {
        DynamicPostEffectType.fromId(postEffectId)
                .ifPresent(type -> this.setUniform(type, effectInstance));
    }

    public void setUniform(DynamicPostEffectType<?> type, EffectInstance effectInstance) {
        if (uniformLayouts.containsKey(type)) {
            UniformLayout<?> uniformLayout = uniformLayouts.get(type);
            uniformLayout.setUniform(effectInstance);
        }
    }
}
