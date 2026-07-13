package com.yummy.naraka.world.item.equipmentset;

import com.yummy.naraka.core.component.NarakaDataComponentTypes;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class NarakaEquipmentSets {
    public static final HolderProxy<EquipmentSet, EquipmentSet> SOUL_ARMOR_AND_SWORD = register(
            "soul_armor_and_sword",
            new SoulEquipmentSet()
    );

    public static final HolderProxy<EquipmentSet, EquipmentSet> BLESSED_SOUL_ARMOR = register(
            "blessed_soul_armor",
            new EquipmentSet(NarakaEquipmentSets::testBlessedSoulArmor, new MobEffectEquipmentSetEffect(
                    Map.of(
                            MobEffects.SPEED, 1,
                            MobEffects.STRENGTH, 1
                    )
            ))
    );

    public static void updateAllSetEffects(LivingEntity livingEntity) {
        NarakaRegistries.EQUIPMENT_SET.forEach(equipmentSet -> equipmentSet.updateEffect(livingEntity));
    }

    private static boolean testBlessedSoulArmor(LivingEntity livingEntity) {
        for (EquipmentSlot slot : EquipmentSlotGroup.ARMOR.slots()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR)
                continue;
            ItemStack armorItemStack = livingEntity.getItemBySlot(slot);
            if (!armorItemStack.getOrDefault(NarakaDataComponentTypes.BLESSED.get(), false))
                return false;
        }

        return true;
    }

    private static HolderProxy<EquipmentSet, EquipmentSet> register(String name, EquipmentSet equipmentSet) {
        return RegistryProxy.register(NarakaRegistries.Keys.EQUIPMENT_SET, name, () -> equipmentSet);
    }

    public static void initialize() {

    }
}
