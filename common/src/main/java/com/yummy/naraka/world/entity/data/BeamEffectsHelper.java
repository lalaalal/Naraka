package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.world.entity.BeamEffect;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class BeamEffectsHelper {
    public static List<BeamEffect> get(LivingEntity livingEntity) {
        return EntityDataHelper.getRawEntityData(livingEntity, NarakaEntityDataTypes.BEAM_EFFECTS.get());
    }

    public static void set(LivingEntity livingEntity, List<BeamEffect> beamEffects) {
        EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.BEAM_EFFECTS.get(), beamEffects);
    }

    public static void add(LivingEntity livingEntity, BeamEffect... beamEffects) {
        List<BeamEffect> newBeamEffects = new ArrayList<>(get(livingEntity));
        newBeamEffects.addAll(List.of(beamEffects));
        set(livingEntity, newBeamEffects);
    }

    public static void remove(LivingEntity livingEntity, List<BeamEffect> beamEffects) {
        List<BeamEffect> newBeamEffects = new ArrayList<>(get(livingEntity));
        newBeamEffects.removeAll(beamEffects);
        set(livingEntity, newBeamEffects);
    }

    public static void addSimpleSet(LivingEntity livingEntity, int tickAfter, int color) {
        BeamEffectsHelper.add(livingEntity,
                BeamEffect.spin(livingEntity.tickCount + tickAfter, BeamEffect.Speed.FAST, 0.25, 0, 0, 0.6, color),
                BeamEffect.spin(livingEntity.tickCount + tickAfter, BeamEffect.Speed.FAST, 0.25, 0, Math.PI, 0.6, color),
                BeamEffect.spin(livingEntity.tickCount + tickAfter, BeamEffect.Speed.NORMAL, 0.6, 0, 0, 0.4, color),
                BeamEffect.spin(livingEntity.tickCount + tickAfter, BeamEffect.Speed.NORMAL, 0.6, Math.PI, 0, 0.4, color),
                BeamEffect.spin(livingEntity.tickCount + tickAfter, BeamEffect.Speed.NORMAL, 0.6, 0, Math.PI, 0.4, color),
                BeamEffect.spin(livingEntity.tickCount + tickAfter, BeamEffect.Speed.SLOW, 0.9, 0, 0, 0.4, color)
        );
    }
}
