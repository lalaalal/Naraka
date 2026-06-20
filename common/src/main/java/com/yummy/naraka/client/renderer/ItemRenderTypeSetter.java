package com.yummy.naraka.client.renderer;

import net.minecraft.client.renderer.rendertype.RenderType;

public interface ItemRenderTypeSetter {
    void naraka$setRenderType(RenderType renderType);

    void naraka$setRenderType(RenderType renderType, int layer);
}
