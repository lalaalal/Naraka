package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.Identifier;
import org.lwjgl.system.MemoryStack;

public abstract class PostEffectUniform implements AutoCloseable {
    private final String name;
    private final Identifier target;
    private final GpuBuffer buffer;

    protected PostEffectUniform(String name, Identifier target, GpuBuffer buffer) {
        this.name = name;
        this.target = target;
        this.buffer = buffer;
    }

    /**
     * Uniform name
     */
    public String name() {
        return name;
    }

    /**
     * Targeting post effect id
     */
    public Identifier target() {
        return target;
    }

    public abstract void reset(long gameTime);

    /**
     * Actually update its {@link UniformBuffer}
     *
     * @param gameTime Game time with partialTicks
     */
    protected abstract void extract(float gameTime);

    public final void update(float gameTime) {
        if (!uniformBuffer().target().equals(target))
            throw new IllegalStateException("Not a valid target");
        extract(gameTime);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            RenderSystem.getDevice().createCommandEncoder().writeToBuffer(buffer.slice(), uniformBuffer().create(stack));
        }
    }

    protected abstract UniformBuffer uniformBuffer();

    public GpuBuffer getBuffer() {
        return buffer;
    }

    @Override
    public void close() {
        buffer.close();
    }
}
