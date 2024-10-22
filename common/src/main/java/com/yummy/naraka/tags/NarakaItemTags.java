package com.yummy.naraka.tags;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface NarakaItemTags {
    TagKey<Item> SPEAR = create("spear");
    TagKey<Item> ALWAYS_RENDER_ITEM_ENTITY = create("always_render_item_entity");

    TagKey<Item> SPEAR_ENCHANTABLE = create("enchantable/spear");
    TagKey<Item> LOYALTY_ENCHANTABLE = create("enchantable/loyalty");

    TagKey<Item> EBONY_LOGS = create("ebony_logs");
    TagKey<Item> PURIFIED_SOUL_ARMOR = create("purified_soul_armor");

    TagKey<Item> SOUL_REINFORCEABLE = create("soul_reinforceable");

    private static TagKey<Item> create(String name) {
        return TagKey.create(Registries.ITEM, NarakaMod.location(name));
    }
}
