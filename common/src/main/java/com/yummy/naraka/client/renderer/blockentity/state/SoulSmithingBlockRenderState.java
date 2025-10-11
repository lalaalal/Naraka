package com.yummy.naraka.client.renderer.blockentity.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class SoulSmithingBlockRenderState extends BlockEntityRenderState {
    public ItemStackRenderState forgingItem = new ItemStackRenderState();
    public Direction direction = Direction.UP;
    public ItemStack templateItem = ItemStack.EMPTY;
    public boolean stabilizerAttached = false;
    public SoulStabilizerRenderState stabilizer = new SoulStabilizerRenderState();
}
