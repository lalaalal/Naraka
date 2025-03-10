package com.yummy.naraka.world.item;

import com.yummy.naraka.tags.NarakaItemTags;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

public class PurifiedSoulArmorItem extends ArmorItem {
    public PurifiedSoulArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        if (stack.is(NarakaItemTags.PURIFIED_SOUL_ARMOR)) {
            Reinforcement reinforcement = Reinforcement.get(stack);
            return reinforcement.value() > 0;
        }
        return super.isEnchantable(stack);
    }
}
