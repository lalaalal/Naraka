package com.yummy.naraka.world.entity.data;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class DynamicPostEffectHelper {
    public static void addDynamicPostEffects(Player player, List<Identifier> dynamicPostEffects) {
        List<Identifier> currentEffects = EntityDataHelper.getRawEntityData(player, NarakaEntityDataTypes.DYNAMIC_POST_EFFECTS.get());
        List<Identifier> newEffects = new ArrayList<>(currentEffects);
        newEffects.addAll(dynamicPostEffects);

        EntityDataHelper.setEntityData(player, NarakaEntityDataTypes.DYNAMIC_POST_EFFECTS.get(), newEffects);
    }

    public static void addDynamicPostEffects(Player player, Identifier... dynamicPostEffects) {
        addDynamicPostEffects(player, List.of(dynamicPostEffects));
    }

    public static void addDynamicPostEffects(List<? extends Player> players, List<Identifier> dynamicPostEffects) {
        for (Player player : players) {
            addDynamicPostEffects(player, dynamicPostEffects);
        }
    }

    public static void addDynamicPostEffects(List<? extends Player> players, Identifier... dynamicPostEffects) {
        addDynamicPostEffects(players, List.of(dynamicPostEffects));
    }

    public static void removeDynamicPostEffects(Player player, List<Identifier> dynamicPostEffects) {
        List<Identifier> currentEffects = EntityDataHelper.getRawEntityData(player, NarakaEntityDataTypes.DYNAMIC_POST_EFFECTS.get());
        List<Identifier> newEffects = new ArrayList<>(currentEffects);
        newEffects.removeAll(dynamicPostEffects);

        EntityDataHelper.setEntityData(player, NarakaEntityDataTypes.DYNAMIC_POST_EFFECTS.get(), newEffects);
    }

    public static void removeDynamicPostEffects(Player player, Identifier... dynamicPostEffects) {
        removeDynamicPostEffects(player, List.of(dynamicPostEffects));
    }

    public static void removeDynamicPostEffects(List<? extends Player> players, List<Identifier> dynamicPostEffects) {
        for (Player player : players) {
            removeDynamicPostEffects(player, dynamicPostEffects);
        }
    }

    public static void removeDynamicPostEffects(List<? extends Player> players, Identifier... dynamicPostEffects) {
        removeDynamicPostEffects(players, List.of(dynamicPostEffects));
    }

    public static List<Identifier> get(Player player) {
        return EntityDataHelper.getRawEntityData(player, NarakaEntityDataTypes.DYNAMIC_POST_EFFECTS.get());
    }
}
