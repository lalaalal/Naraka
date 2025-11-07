package com.yummy.naraka.client.renderer.blockentity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

@Environment(EnvType.CLIENT)
public class NarakaPortalRenderState extends BlockEntityRenderState {
    public int usage;
}
