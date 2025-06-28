package com.yummy.naraka.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

@Environment(EnvType.CLIENT)
public class FlatImageRenderState extends EntityRenderState {
    public float xRot;
    public float yRot;
    public float zRot;
    public float scale;
}
