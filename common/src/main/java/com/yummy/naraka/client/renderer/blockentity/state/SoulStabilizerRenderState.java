package com.yummy.naraka.client.renderer.blockentity.state;

import com.yummy.naraka.world.item.SoulType;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class SoulStabilizerRenderState extends BlockEntityRenderState {
    public SoulType soulType = SoulType.NONE;
    public int souls;
}
