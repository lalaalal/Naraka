package com.yummy.naraka.client.renderer.entity.state;

import net.minecraft.client.renderer.entity.state.LightningBoltRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class ColoredLightningBoltRenderState extends LightningBoltRenderState {
    public int color;
    public RenderType renderType = RenderTypes.lightning();
}
