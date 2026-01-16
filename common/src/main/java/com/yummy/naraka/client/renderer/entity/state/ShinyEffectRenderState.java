package com.yummy.naraka.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

@Environment(EnvType.CLIENT)
public class ShinyEffectRenderState extends EntityRenderState {
    public boolean isVertical;
    public float scale;
    public int lifetime;
    public int color;
    public float rotation;
}
