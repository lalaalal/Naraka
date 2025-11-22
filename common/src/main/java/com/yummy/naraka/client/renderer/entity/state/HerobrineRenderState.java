package com.yummy.naraka.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class HerobrineRenderState extends AbstractHerobrineRenderState implements BeamEffectRenderStateControl {
    public int phase;
    public boolean dead;
    public Vec3 translation = Vec3.ZERO;
    public AnimationState chzzkAnimationState = new AnimationState();
    public final List<BeamEffectRenderState> beamEffectRenderStates = new ArrayList<>();

    @Override
    public void add(BeamEffectRenderState beamEffectRenderState) {
        beamEffectRenderStates.add(beamEffectRenderState);
    }

    @Override
    public void forEach(Consumer<BeamEffectRenderState> consumer) {
        beamEffectRenderStates.forEach(consumer);
    }
}
