package com.yummy.naraka.world.item;

import com.yummy.naraka.util.NarakaItemUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

public class PurifiedSoulArmorItem extends ArmorItem {
    public PurifiedSoulArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack itemStack = super.getDefaultInstance();
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putBoolean(NarakaItemUtils.TAG_UNBREAKABLE, true);

        return itemStack;
    }
}
