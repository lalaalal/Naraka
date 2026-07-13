package com.yummy.naraka.world.item.equipmentset;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class NarakaEquipmentSetEffectTypes {
    public static final HolderProxy<EquipmentSetEffectType<?>, EquipmentSetEffectType<Unit>> EMPTY = register(
            "empty",
            new EquipmentSetEffectType<>(Unit.CODEC) {
                @Override
                public void activate(LivingEntity livingEntity, Unit data) {

                }

                @Override
                public void deactivate(LivingEntity livingEntity, Unit data) {

                }
            }
    );

    public static final HolderProxy<EquipmentSetEffectType<?>, EquipmentSetEffectType<List<MobEffectData>>> MOB_EFFECT_EQUIPMENT_SET_EFFECT = register(
            "mob_effect_equipment_set_effect",
            new MobEffectEquipmentSetEffect()
    );

    private static <T> HolderProxy<EquipmentSetEffectType<?>, EquipmentSetEffectType<T>> register(String name, EquipmentSetEffectType<T> equipmentSet) {
        return RegistryProxy.register(NarakaRegistries.Keys.EQUIPMENT_SET_EFFECT_TYPE, name, () -> equipmentSet);
    }

    public static void initialize() {

    }
}
