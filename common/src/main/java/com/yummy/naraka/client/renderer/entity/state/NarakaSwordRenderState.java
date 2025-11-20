package com.yummy.naraka.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class NarakaSwordRenderState extends LightTailEntityRenderState implements BeamEffectRenderStateControl {
    public List<BeamEffectRenderState> beamEffectRenderStates = new ArrayList<>();

    @Override
    public void add(BeamEffectRenderState beamEffectRenderState) {
        beamEffectRenderStates.add(beamEffectRenderState);
    }

    @Override
    public void forEach(Consumer<BeamEffectRenderState> consumer) {
        beamEffectRenderStates.forEach(consumer);
    }
}
