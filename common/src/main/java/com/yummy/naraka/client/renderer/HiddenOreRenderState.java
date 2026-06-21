package com.yummy.naraka.client.renderer;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.core.BlockPos;

public class HiddenOreRenderState {
    public BlockPos pos = BlockPos.ZERO;
    public BlockModelRenderState blockModel = new BlockModelRenderState();
    public int color;
}
