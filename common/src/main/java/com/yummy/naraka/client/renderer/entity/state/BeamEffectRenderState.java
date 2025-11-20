package com.yummy.naraka.client.renderer.entity.state;

import com.yummy.naraka.world.entity.BeamEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class BeamEffectRenderState {
    public final BeamEffect beamEffect;
    public List<BeamEffectPart> parts = new ArrayList<>();

    public BeamEffectRenderState(BeamEffect beamEffect) {
        this.beamEffect = beamEffect;
    }

    public void update(float ageInTicks) {
        parts.clear();
        double current = Math.clamp(ageInTicks - beamEffect.startTick(), 0, beamEffect.tickLength());
        double tail = Math.clamp(ageInTicks - beamEffect.startTick() - beamEffect.beamLength(), 0, beamEffect.tickLength());

        double maxTheta = (current / beamEffect.tickLength()) * Math.TAU * beamEffect.stretch();
        double minTheta = (tail / beamEffect.tickLength()) * Math.TAU * beamEffect.stretch();

        double actualBeamLength = maxTheta - minTheta;
        for (double theta = minTheta; theta <= maxTheta; theta += beamEffect.rotationInterval()) {
            double delta = (theta - minTheta) / actualBeamLength * Math.TAU;
            float alpha = (float) (Math.cos(delta - Math.PI) + 1) / 2;

            Vec3 position = beamEffect.calculatePosition(theta);
            parts.add(BeamEffectPart.of(position, alpha, beamEffect.color()));
        }
    }

    @Environment(EnvType.CLIENT)
    public record BeamEffectPart(Vec3 position, int color) {
        public static BeamEffectPart of(Vec3 position, float alpha, int color) {
            return new BeamEffectPart(position, ARGB.color(alpha, color));
        }
    }
}
