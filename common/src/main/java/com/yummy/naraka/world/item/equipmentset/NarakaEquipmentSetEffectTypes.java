package com.yummy.naraka.world.item.equipmentset;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryProxy;

public class NarakaEquipmentSetEffectTypes {
    public static final HolderProxy<EquipmentSetEffect.Type<?>, EquipmentSetEffect.Type<MobEffectEquipmentSetEffect>> MOB_EFFECT_EQUIPMENT_SET_EFFECT = register(
            "mob_effect_equipment_set_effect",
            EquipmentSetEffect.type(MobEffectEquipmentSetEffect.CODEC, MobEffectEquipmentSetEffect.STREAM_CODEC)
    );

    private static <T extends EquipmentSetEffect> HolderProxy<EquipmentSetEffect.Type<?>, EquipmentSetEffect.Type<T>> register(String name, EquipmentSetEffect.Type<T> equipmentSet) {
        return RegistryProxy.register(NarakaRegistries.Keys.EQUIPMENT_SET_EFFECT_TYPE, name, () -> equipmentSet);
    }

    public static void initialize() {

    }
}
