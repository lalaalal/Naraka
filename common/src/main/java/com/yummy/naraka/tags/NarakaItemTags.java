package com.yummy.naraka.tags;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class NarakaItemTags {
    public static final TagKey<Item> SPEAR = create("spear");
    public static final TagKey<Item> ALWAYS_RENDER_ITEM_ENTITY = create("always_render_item_entity");

    public static final TagKey<Item> SPEAR_ENCHANTABLE = create("enchantable/spear");
    public static final TagKey<Item> LOYALTY_ENCHANTABLE = create("enchantable/loyalty");

    public static final TagKey<Item> EBONY_LOGS = create("ebony_logs");
    public static final TagKey<Item> PURIFIED_SOUL_ARMOR = create("purified_soul_armor");

    public static final TagKey<Item> SOUL_REINFORCEABLE = create("soul_reinforceable");
    public static final TagKey<Item> SOUL_MATERIALS = create("soul_materials");
    public static final TagKey<Item> SOUL_SWORDS = create("soul_swords");

    public static TagKey<Item> create(String name) {
        return TagKey.create(Registries.ITEM, NarakaMod.location(name));
    }
}
