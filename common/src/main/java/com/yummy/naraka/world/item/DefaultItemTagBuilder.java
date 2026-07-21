package com.yummy.naraka.world.item;

import com.mojang.serialization.Codec;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.util.NarakaNbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;

public interface DefaultItemTagBuilder extends DefaultItemTagProvider {
    void naraka$setDefaultTag(CompoundTag tag);

    Item.Properties naraka$asItemProperties();

    default CompoundTag naraka$createEmpty() {
        CompoundTag tag = new CompoundTag();
        naraka$setDefaultTag(tag);
        return tag;
    }

    default <T> DefaultItemTagBuilder naraka$set(String key, Codec<T> codec, T value) {
        CompoundTag tag = naraka$getDefaultTag().orElseGet(this::naraka$createEmpty);
        NarakaNbtUtils.store(tag, key, codec, value);
        return this;
    }

    default <T> DefaultItemTagBuilder naraka$tooltip(NarakaItemTooltip tooltip) {
        return naraka$set(NarakaItemUtils.TAG_ITEM_DETAIL, ItemDetail.CODEC, tooltip.itemDetail());
    }

    default DefaultItemTagBuilder naraka$unbreakable() {
        CompoundTag tag = naraka$getDefaultTag().orElseGet(this::naraka$createEmpty);
        tag.putBoolean(NarakaItemUtils.TAG_UNBREAKABLE, true);
        return this;
    }
}
