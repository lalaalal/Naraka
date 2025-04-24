package com.yummy.naraka.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ShaderProgram;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class NarakaShaders {
    @Nullable
    public static ShaderProgram longinus;

    public static ShaderProgram longinus() {
        if (longinus == null)
            throw new IllegalStateException("Shader longinus not initialized");
        return longinus;
    }
}
