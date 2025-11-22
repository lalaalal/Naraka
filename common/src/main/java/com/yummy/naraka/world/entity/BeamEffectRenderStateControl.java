package com.yummy.naraka.world.entity;

import com.yummy.naraka.client.renderer.entity.state.BeamEffectRenderState;

import java.util.List;
import java.util.function.Consumer;

public interface BeamEffectRenderStateControl {
    void addBeamEffectRenderState(BeamEffectRenderState beamEffectRenderState);

    void forEachBeamEffectRenderStates(Consumer<BeamEffectRenderState> consumer);

    default void updateBeamEffects(List<BeamEffect> beamEffects, float ageInTicks) {
        for (BeamEffect beamEffect : beamEffects) {
            BeamEffectRenderState beamEffectRenderState = new BeamEffectRenderState(beamEffect);
            addBeamEffectRenderState(beamEffectRenderState);
            beamEffectRenderState.update(ageInTicks);
        }
    }
}
