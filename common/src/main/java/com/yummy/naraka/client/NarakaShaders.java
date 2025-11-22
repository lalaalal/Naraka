package com.yummy.naraka.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.init.ShaderRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ShaderInstance;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public final class NarakaShaders {
    @Nullable
    private static ShaderInstance longinus;
    @Nullable
    private static ShaderInstance longinusCutout;
    @Nullable
    private static ShaderInstance space;
    @Nullable
    private static ShaderInstance spaceCutout;

    public static ShaderInstance longinus() {
        if (longinus == null)
            throw new IllegalStateException("longinus shader is not initialized");
        return longinus;
    }

    public static ShaderInstance longinusCutout() {
        if (longinusCutout == null)
            throw new IllegalStateException("longinus_cutout shader is not initialized");
        return longinusCutout;
    }

    public static ShaderInstance space() {
        if (space == null)
            throw new IllegalStateException("space shader is not initialized");
        return space;
    }

    public static ShaderInstance spaceCutout() {
        if (spaceCutout == null)
            throw new IllegalStateException("space_cutout shader is not initialized");
        return spaceCutout;
    }

    public static void initialize() {
        ShaderRegistry.register(NarakaMod.location("longinus"), DefaultVertexFormat.POSITION, shaderInstance -> {
            longinus = shaderInstance;
        });
        ShaderRegistry.register(NarakaMod.location("longinus_cutout"), DefaultVertexFormat.POSITION_TEX, shaderInstance -> {
            longinusCutout = shaderInstance;
        });
        ShaderRegistry.register(NarakaMod.location("space"), DefaultVertexFormat.POSITION, shaderInstance -> {
            space = shaderInstance;
        });
        ShaderRegistry.register(NarakaMod.location("space_cutout"), DefaultVertexFormat.POSITION_TEX, shaderInstance -> {
            spaceCutout = shaderInstance;
        });
    }
}
