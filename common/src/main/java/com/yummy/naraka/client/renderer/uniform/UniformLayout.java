package com.yummy.naraka.client.renderer.uniform;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.yummy.naraka.core.posteffects.DynamicPostEffectType;
import com.yummy.naraka.core.posteffects.PostEffectConfig;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

public abstract class UniformLayout<T extends PostEffectConfig> implements AutoCloseable {
    private final DynamicPostEffectType<T> type;
    private final String uniformName;
    private final GpuBuffer buffer;

    private long startGameTime;

    private static GpuBuffer createBuffer(String name, long size) {
        GpuDevice device = RenderSystem.getDevice();
        return device.createBuffer(() -> name + " UBO ", 136, size);
    }

    public UniformLayout(DynamicPostEffectType<T> type, String uniformName, long size) {
        this.type = type;
        this.uniformName = uniformName;
        this.buffer = createBuffer(uniformName, size);
    }

    public DynamicPostEffectType<T> type() {
        return type;
    }

    public String uniformName() {
        return uniformName;
    }

    protected abstract void readConfig(T config);

    public final void setConfig(long startGameTime, PostEffectConfig config) {
        if (config.type() == this.type())
            readConfig(type.cast(config));
        this.startGameTime = startGameTime;
    }

    protected abstract void update(float tick);

    protected abstract ByteBuffer getBuffer(MemoryStack stack);

    public final void extract(float gameTime) {
        float tick = gameTime - startGameTime;
        this.update(tick);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(buffer.slice(), getBuffer(stack));
        }
    }

    public void setUniform(RenderPass renderPass) {
        renderPass.setUniform(uniformName, buffer);
    }

    @Override
    public void close() {
        buffer.close();
    }
}
