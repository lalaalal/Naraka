package com.yummy.naraka.world.entity.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class DynamicPostEffectHelper {
    public static void addDynamicPostEffects(Player player, List<ResourceLocation> dynamicPostEffects) {
        List<ResourceLocation> currentEffects = EntityDataHelper.getRawEntityData(player, NarakaEntityDataTypes.DYNAMIC_POST_EFFECTS.get());
        List<ResourceLocation> newEffects = new ArrayList<>(currentEffects);
        newEffects.addAll(dynamicPostEffects);

        EntityDataHelper.setEntityData(player, NarakaEntityDataTypes.DYNAMIC_POST_EFFECTS.get(), newEffects);
    }

    public static void addDynamicPostEffects(Player player, ResourceLocation... dynamicPostEffects) {
        addDynamicPostEffects(player, List.of(dynamicPostEffects));
    }

    public static void addDynamicPostEffects(List<? extends Player> players, List<ResourceLocation> dynamicPostEffects) {
        for (Player player : players) {
            addDynamicPostEffects(player, dynamicPostEffects);
        }
    }

    public static void addDynamicPostEffects(List<? extends Player> players, ResourceLocation... dynamicPostEffects) {
        addDynamicPostEffects(players, List.of(dynamicPostEffects));
    }

    public static void removeDynamicPostEffects(Player player, List<ResourceLocation> dynamicPostEffects) {
        List<ResourceLocation> currentEffects = EntityDataHelper.getRawEntityData(player, NarakaEntityDataTypes.DYNAMIC_POST_EFFECTS.get());
        List<ResourceLocation> newEffects = new ArrayList<>(currentEffects);
        newEffects.removeAll(dynamicPostEffects);

        EntityDataHelper.setEntityData(player, NarakaEntityDataTypes.DYNAMIC_POST_EFFECTS.get(), newEffects);
    }

    public static void removeDynamicPostEffects(Player player, ResourceLocation... dynamicPostEffects) {
        removeDynamicPostEffects(player, List.of(dynamicPostEffects));
    }

    public static void removeDynamicPostEffects(List<? extends Player> players, List<ResourceLocation> dynamicPostEffects) {
        for (Player player : players) {
            removeDynamicPostEffects(player, dynamicPostEffects);
        }
    }

    public static void removeDynamicPostEffects(List<? extends Player> players, ResourceLocation... dynamicPostEffects) {
        removeDynamicPostEffects(players, List.of(dynamicPostEffects));
    }

    public static List<ResourceLocation> get(Player player) {
        return EntityDataHelper.getRawEntityData(player, NarakaEntityDataTypes.DYNAMIC_POST_EFFECTS.get());
    }
}
