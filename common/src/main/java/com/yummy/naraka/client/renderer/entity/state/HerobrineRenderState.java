package com.yummy.naraka.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.AnimationState;

@Environment(EnvType.CLIENT)
public class HerobrineRenderState extends AbstractHerobrineRenderState {
    public int phase;
    public boolean dead;
    public AnimationState chzzkAnimationState = new AnimationState();
}
