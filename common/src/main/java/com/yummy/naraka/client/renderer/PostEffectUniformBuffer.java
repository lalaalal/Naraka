package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.RenderPass;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link PostEffectUniformBuffer#update(float, Identifier)} currently active post effects have {@link PostEffectUniform}
 * and bind uniforms by {@link PostEffectUniformBuffer#setUniform(Identifier, RenderPass)}
 *
 * @see com.yummy.naraka.mixin.client.PostChainMixin
 * @see com.yummy.naraka.mixin.client.PostPassMixin
 */
public class PostEffectUniformBuffer implements AutoCloseable {
    private final Map<Identifier, PostEffectUniform> uniforms = new HashMap<>();

    @Nullable
    private static PostEffectUniformBuffer instance = null;

    /**
     * To easily access from Mixin
     */
    public static PostEffectUniformBuffer getInstance() {
        if (instance == null)
            throw new IllegalStateException("PostEffectUniformBuffer is not initialized");
        return instance;
    }

    public PostEffectUniformBuffer() {
        add(new ChromaticAberrationUniform());

        instance = this;
    }

    private void add(PostEffectUniform uniform) {
        this.uniforms.put(uniform.target(), uniform);
    }

    public void reset(Identifier postEffectId, long gameTime) {
        if (uniforms.containsKey(postEffectId)) {
            uniforms.get(postEffectId).reset(gameTime);
        }
    }

    public void update(float gameTime, Identifier postEffectId) {
        if (uniforms.containsKey(postEffectId)) {
            PostEffectUniform uniform = uniforms.get(postEffectId);
            uniform.update(gameTime);
        }
    }

    /**
     * @param passId Identifier of the RenderPipeline contains an index of the PostPass
     */
    public void defineUniforms(Identifier passId, RenderPipeline.Builder pipelineBuilder) {
        Identifier postEffectId = passId.withPath(path -> path.split("/")[0]);
        if (uniforms.containsKey(postEffectId)) {
            pipelineBuilder.withUniform(uniforms.get(postEffectId).name(), UniformType.UNIFORM_BUFFER);
        }
    }

    public void setUniform(Identifier postEffectId, RenderPass renderPass) {
        if (uniforms.containsKey(postEffectId)) {
            PostEffectUniform uniform = uniforms.get(postEffectId);
            GpuBuffer buffer = uniform.getBuffer();
            renderPass.setUniform(uniform.name(), buffer);
        }
    }

    @Override
    public void close() {
        for (PostEffectUniform uniform : uniforms.values())
            uniform.close();
    }
}
