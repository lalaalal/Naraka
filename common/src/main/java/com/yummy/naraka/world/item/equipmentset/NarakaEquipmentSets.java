package com.yummy.naraka.world.item.equipmentset;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.NarakaRegistries;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.entity.LivingEntity;

public class NarakaEquipmentSets {
    private static final DeferredRegister<EquipmentSet> EQUIPMENT_SETS = DeferredRegister.create(NarakaMod.MOD_ID, NarakaRegistries.Keys.EQUIPMENT_SET);

    public static final RegistrySupplier<EquipmentSet> SOUL_ARMOR_AND_SWORD = register(
            "soul_armor_and_sword",
            new SoulEquipmentSet()
    );

    public static void updateAllSetEffects(LivingEntity livingEntity) {
        livingEntity.registryAccess()
                .registryOrThrow(NarakaRegistries.Keys.EQUIPMENT_SET)
                .forEach(equipmentSet -> equipmentSet.updateEffect(livingEntity));
    }

    private static RegistrySupplier<EquipmentSet> register(String name, EquipmentSet equipmentSet) {
        return EQUIPMENT_SETS.register(name, () -> equipmentSet);
    }

    public static void initialize() {
        EQUIPMENT_SETS.register();
    }
}
