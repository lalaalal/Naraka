package com.yummy.naraka.world.item.equipmentset;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.NarakaRegistries;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.LivingEntity;

public class NarakaEquipmentSets {
    public static final EquipmentSet SOUL_ARMOR_AND_SWORD = register(
            "soul_armor_and_sword",
            new SoulEquipmentSet()
    );

    public static void updateAllSetEffects(LivingEntity livingEntity) {
        NarakaRegistries.EQUIPMENT_SET.forEach(equipmentSet -> equipmentSet.updateEffect(livingEntity));
    }

    private static EquipmentSet register(String name, EquipmentSet equipmentSet) {
        return Registry.register(NarakaRegistries.EQUIPMENT_SET, NarakaMod.location(name), equipmentSet);
    }

    public static void initialize() {

    }
}
