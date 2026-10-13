package com.yummy.naraka.client.renderer;

import net.minecraft.resources.Identifier;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

public interface UniformBuffer {
    /**
     * Targeting post effect id
     */
    Identifier target();

    ByteBuffer create(MemoryStack stack);
}
