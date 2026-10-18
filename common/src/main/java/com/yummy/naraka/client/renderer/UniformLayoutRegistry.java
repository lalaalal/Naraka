package com.yummy.naraka.client.renderer;

import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.yummy.naraka.core.posteffects.PostEffectConfig;
import com.yummy.naraka.core.posteffects.PostEffectType;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link UniformLayoutRegistry#extract(float, Collection)} currently active post effects have {@link PostEffectType}
 * and bind uniforms by {@link UniformLayoutRegistry#setUniform(Identifier, RenderPass)}
 *
 * @see com.yummy.naraka.mixin.client.PostChainMixin
 * @see com.yummy.naraka.mixin.client.PostPassMixin
 */
public class UniformLayoutRegistry implements AutoCloseable {
    private final Map<PostEffectType<?>, UniformLayout<?>> uniformLayouts = new HashMap<>();

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

    public void extract(float gameTime, Collection<Identifier> postEffects) {
        for (Identifier postEffectId : postEffects) {
            PostEffectType.fromId(postEffectId)
                    .ifPresent(type -> this.extract(type, gameTime));
        }
    }

    private void extract(PostEffectType<?> type, float gameTime) {
        if (uniformLayouts.containsKey(type)) {
            UniformLayout<?> uniformLayout = uniformLayouts.get(type);
            uniformLayout.extract(gameTime);
        }
    }

    /**
     * @param passId Identifier of the RenderPipeline contains an index of the PostPass
     */
    public void defineUniform(Identifier passId, BindGroupLayout.Builder bindGroupLayoutBuilder) {
        Identifier postEffectId = passId.withPath(path -> path.split("/")[0]);
        PostEffectType.fromId(postEffectId)
                .ifPresent(type -> this.defineUniform(type, bindGroupLayoutBuilder));
    }

    public void defineUniform(PostEffectType<?> type, BindGroupLayout.Builder bindGroupLayoutBuilder) {
        if (uniformLayouts.containsKey(type)) {
            bindGroupLayoutBuilder.withUniform(uniformLayouts.get(type).uniformName(), UniformType.UNIFORM_BUFFER);
        }
    }

    public void setUniform(Identifier postEffectId, RenderPass renderPass) {
        PostEffectType.fromId(postEffectId)
                .ifPresent(type -> this.setUniform(type, renderPass));
    }

    public void setUniform(PostEffectType<?> type, RenderPass renderPass) {
        if (uniformLayouts.containsKey(type)) {
            UniformLayout<?> uniformLayout = uniformLayouts.get(type);
            uniformLayout.setUniform(renderPass);
        }
    }

    @Override
    public void close() {
        for (UniformLayout<?> uniform : uniformLayouts.values())
            uniform.close();
    }
}
