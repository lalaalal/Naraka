package com.yummy.naraka.world.item.equipment;

import com.yummy.naraka.NarakaMod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public interface NarakaEquipmentAssets {
    ResourceKey<EquipmentAsset> PURIFIED_SOUL = create("purified_soul");
    ResourceKey<EquipmentAsset> EBONY = create("ebony");

    static ResourceKey<EquipmentAsset> create(String name) {
        return ResourceKey.create(EquipmentAssets.ROOT_ID, NarakaMod.location(name));
    }
}
