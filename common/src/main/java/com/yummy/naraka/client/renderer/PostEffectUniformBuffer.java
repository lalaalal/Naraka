package com.yummy.naraka.client.renderer;

import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.UniformType;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PostEffectUniformBuffer implements AutoCloseable {
    private final Map<Identifier, PostEffectUniform> uniforms = new HashMap<>();

    @Nullable
    private static PostEffectUniformBuffer instance = null;

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

    public void update(float gameTime, Collection<Identifier> postEffects) {
        for (Identifier postEffectId : postEffects) {
            if (uniforms.containsKey(postEffectId)) {
                PostEffectUniform uniform = uniforms.get(postEffectId);
                uniform.update(gameTime);
            }
        }
    }

    public void defineUniforms(Identifier passId, BindGroupLayout.Builder bindGroupLayoutBuilder) {
        Identifier postEffectId = passId.withPath(path -> path.split("/")[0]);
        if (uniforms.containsKey(postEffectId)) {
            bindGroupLayoutBuilder.withUniform(uniforms.get(postEffectId).name(), UniformType.UNIFORM_BUFFER);
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
