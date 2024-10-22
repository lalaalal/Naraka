package com.yummy.naraka.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ShaderInstance;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class NarakaShaders {
    public static @Nullable ShaderInstance longinus;

    public static ShaderInstance longinus() {
        if (longinus == null)
            throw new IllegalStateException("Shader longinus not initialized");
        return longinus;
    }
}
