package com.yummy.naraka.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

@Environment(EnvType.CLIENT)
public class NarakaPickaxeRenderState extends EntityRenderState {
    public ItemStackRenderState pickaxe = new ItemStackRenderState();
    public float xRot;
    public float yRot;
}
