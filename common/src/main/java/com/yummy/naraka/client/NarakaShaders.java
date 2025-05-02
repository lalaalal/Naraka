package com.yummy.naraka.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.yummy.naraka.NarakaMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ShaderDefines;
import net.minecraft.client.renderer.ShaderProgram;

@Environment(EnvType.CLIENT)
public final class NarakaShaders {
    public static final ShaderProgram LONGINUS = new ShaderProgram(NarakaMod.location("longinus"),
            DefaultVertexFormat.POSITION, ShaderDefines.EMPTY);

    public static ShaderProgram longinus() {
        return LONGINUS;
    }
}
