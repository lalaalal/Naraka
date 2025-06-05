package com.yummy.naraka.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.ItemStackRenderState;

@Environment(EnvType.CLIENT)
public class HerobrineRenderState extends AbstractHerobrineRenderState {
    public int phase;
    public boolean displayPickaxe = true;
    public ItemStackRenderState pickaxe = new ItemStackRenderState();

    @Override
    public WavingScarfRenderState.ModelType getModelType() {
        if (phase == 3)
            return WavingScarfRenderState.ModelType.BIG;
        return WavingScarfRenderState.ModelType.SMALL;
    }
}
