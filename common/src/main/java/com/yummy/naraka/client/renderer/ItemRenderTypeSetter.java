package com.yummy.naraka.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;

@Environment(EnvType.CLIENT)
public interface ItemRenderTypeSetter {
    void naraka$setRenderType(RenderType renderType);

    void naraka$setRenderType(RenderType renderType, int layer);
}
