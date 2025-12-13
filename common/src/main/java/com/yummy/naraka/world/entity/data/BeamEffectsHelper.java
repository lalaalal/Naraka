package com.yummy.naraka.world.entity.data;

import com.yummy.naraka.network.AddBeamEffectPacket;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.world.entity.BeamEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class BeamEffectsHelper {
    public static List<BeamEffect> get(Entity entity) {
        return EntityDataHelper.getRawEntityData(entity, NarakaEntityDataTypes.BEAM_EFFECTS.get());
    }

    public static void set(Entity entity, List<BeamEffect> beamEffects) {
        EntityDataHelper.setEntityData(entity, NarakaEntityDataTypes.BEAM_EFFECTS.get(), beamEffects);
    }

    public static void add(Entity entity, List<BeamEffect> beamEffects) {
        List<BeamEffect> newBeamEffects = new ArrayList<>(get(entity));
        newBeamEffects.addAll(beamEffects);
        set(entity, newBeamEffects);
    }

    public static void add(Entity entity, BeamEffect... beamEffects) {
        add(entity, List.of(beamEffects));
    }

    public static void remove(Entity entity, List<BeamEffect> beamEffects) {
        List<BeamEffect> newBeamEffects = new ArrayList<>(get(entity));
        newBeamEffects.removeAll(beamEffects);
        set(entity, newBeamEffects);
    }

    public static void send(List<ServerPlayer> players, AddBeamEffectPacket.BeamEffectType type, Entity entity, int color) {
        AddBeamEffectPacket packet = new AddBeamEffectPacket(type, entity, color);
        NetworkManager.clientbound().send(players, packet);
    }

    public static void addSimpleSet(Entity entity, int color) {
        BeamEffectsHelper.add(entity,
                BeamEffect.spin(entity.tickCount, BeamEffect.Speed.FAST, 0.25, 0, 0, 0.6, color),
                BeamEffect.spin(entity.tickCount, BeamEffect.Speed.FAST, 0.25, 0, Math.PI, 0.6, color),
                BeamEffect.spin(entity.tickCount, BeamEffect.Speed.NORMAL, 0.6, 0, 0, 0.4, color),
                BeamEffect.spin(entity.tickCount, BeamEffect.Speed.NORMAL, 0.6, Math.PI, 0, 0.4, color),
                BeamEffect.spin(entity.tickCount, BeamEffect.Speed.NORMAL, 0.6, 0, Math.PI, 0.4, color),
                BeamEffect.spin(entity.tickCount, BeamEffect.Speed.SLOW, 0.9, 0, 0, 0.4, color)
        );
    }

    public static void addPullSet(Entity entity, int color) {
        List<BeamEffect> beamEffects = new ArrayList<>();
        for (double yRot = 0; yRot < Math.TAU; yRot += Math.PI / 16)
            beamEffects.add(BeamEffect.pull(entity.tickCount, BeamEffect.Speed.FAST, 2, yRot, color));
        BeamEffectsHelper.add(entity, beamEffects);
    }

    public static void addPushSet(Entity entity, int color) {
        List<BeamEffect> beamEffects = new ArrayList<>();
        for (double yRot = 0; yRot < Math.TAU; yRot += Math.PI / 12)
            beamEffects.add(BeamEffect.push(entity.tickCount, BeamEffect.Speed.NORMAL, 1, yRot, color));
        BeamEffectsHelper.add(entity, beamEffects);
    }
}
