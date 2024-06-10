package com.yummy.naraka.tags;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface NarakaItemTags {
    TagKey<Item> INVULNERABLE_ITEM = create("invulnerable_item");
    TagKey<Item> ALWAYS_RENDER_ITEM_ENTITY = create("always_render_item_entity");

    private static TagKey<Item> create(String name) {
        return TagKey.create(Registries.ITEM, NarakaMod.location(name));
    }
}
