package com.yummy.naraka.world.item;

import com.mojang.serialization.Codec;
import com.yummy.naraka.util.NarakaItemUtils;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

public class PurifiedSoulArmorItem extends ArmorItem {
    public PurifiedSoulArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public ItemStack getDefaultInstance() {
        return NarakaItemUtils.makeUnbreakable(super.getDefaultInstance());
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return NarakaItemUtils.readNbtDataOrDefault(stack, NarakaItemUtils.TAG_BLESSED, Codec.BOOL, false);
    }
}
