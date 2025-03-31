package com.yummy.naraka.world.item.equipmentset;

import com.yummy.naraka.core.registries.LazyHolder;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryInitializer;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.world.entity.LivingEntity;

public class NarakaEquipmentSets {
    public static final LazyHolder<EquipmentSet, EquipmentSet> SOUL_ARMOR_AND_SWORD = register(
            "soul_armor_and_sword",
            new SoulEquipmentSet()
    );

    public static void updateAllSetEffects(LivingEntity livingEntity) {
        NarakaRegistries.EQUIPMENT_SET.forEach(equipmentSet -> equipmentSet.updateEffect(livingEntity));
    }

    private static LazyHolder<EquipmentSet, EquipmentSet> register(String name, EquipmentSet equipmentSet) {
        return RegistryProxy.register(NarakaRegistries.Keys.EQUIPMENT_SET, name, () -> equipmentSet);
    }

    public static void initialize() {
        RegistryInitializer.get(NarakaRegistries.Keys.EQUIPMENT_SET)
                .onRegistrationFinished();
    }
}
