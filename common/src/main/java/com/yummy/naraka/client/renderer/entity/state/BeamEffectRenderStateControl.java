package com.yummy.naraka.client.renderer.entity.state;

import com.yummy.naraka.world.entity.BeamEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.List;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public interface BeamEffectRenderStateControl {
    void add(BeamEffectRenderState beamEffectRenderState);

    void forEach(Consumer<BeamEffectRenderState> consumer);

    default void updateBeamEffects(List<BeamEffect> beamEffects, float ageInTicks) {
        for (BeamEffect beamEffect : beamEffects) {
            BeamEffectRenderState beamEffectRenderState = new BeamEffectRenderState(beamEffect);
            add(beamEffectRenderState);
            beamEffectRenderState.update(ageInTicks);
        }
    }
}
