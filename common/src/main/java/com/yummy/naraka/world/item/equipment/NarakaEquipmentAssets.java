package com.yummy.naraka.world.item.equipment;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public interface NarakaEquipmentAssets {
    ResourceKey<EquipmentAsset> PURIFIED_SOUL = EquipmentAssets.createId("purified_soul");
    ResourceKey<EquipmentAsset> EBONY = EquipmentAssets.createId("ebony");
}
