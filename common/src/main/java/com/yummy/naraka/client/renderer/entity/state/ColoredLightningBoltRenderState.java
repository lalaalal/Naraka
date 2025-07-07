package com.yummy.naraka.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LightningBoltRenderState;

@Environment(EnvType.CLIENT)
public class ColoredLightningBoltRenderState extends LightningBoltRenderState {
    public int color;
}
