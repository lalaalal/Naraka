package com.yummy.naraka.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

@Environment(EnvType.CLIENT)
public class AreaEffectRenderState extends EntityRenderState {
    public float xWidth;
    public float zWidth;
    public int lifetime;
    public int color;
    public int index;
}
