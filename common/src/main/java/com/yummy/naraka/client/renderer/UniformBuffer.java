package com.yummy.naraka.client.renderer;

import net.minecraft.resources.Identifier;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;

public interface UniformBuffer {
    Identifier target();

    ByteBuffer create(MemoryStack stack);
}
