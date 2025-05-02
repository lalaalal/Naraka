package com.yummy.naraka.world.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

public class PurifiedSoulArmorItem extends Item {
    public PurifiedSoulArmorItem(ArmorMaterial material, ArmorType type, Properties properties) {
        super(NarakaArmorMaterials.humanoidPropertiesWithoutEnchantable(properties, material, type));
    }
}
