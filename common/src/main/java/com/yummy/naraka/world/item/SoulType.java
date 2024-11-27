package com.yummy.naraka.world.item;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;

public enum SoulType implements StringRepresentable {
    REDSTONE(0, 0xeb4747),
    COPPER(1, 0xff8000),
    GOLD(2, 0xffd24d),
    EMERALD(3, 0x0ec70e),
    DIAMOND(4, 0x33cccc),
    LAPIS(5, 0x3939c6),
    AMETHYST(6, 0x9957db),
    NECTARIUM(7, 0xd65cd6),
    GOD_BLOOD(8, "god_blood", "god_blood", 0x625859);

    private static final IntFunction<SoulType> BY_ID = ByIdMap.continuous(SoulType::getId, SoulType.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<SoulType> CODEC = StringRepresentable.fromEnum(SoulType::values);
    public static final StreamCodec<ByteBuf, SoulType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, SoulType::getId);

    public final int id;
    private final ResourceLocation itemName;
    private final ResourceLocation blockItemName;
    @Nullable
    private Item item;
    @Nullable
    private Item blockItem;
    public final int color;
    public final String translationKey;

    @Nullable
    public static SoulType fromItem(ItemStack itemStack) {
        for (SoulType soulType : values()) {
            if (soulType.test(itemStack))
                return soulType;
        }
        return null;
    }

    SoulType(int id, String itemName, String blockItemName, int color) {
        this.id = id;
        this.itemName = NarakaMod.location(itemName);
        this.blockItemName = NarakaMod.location(blockItemName);
        BuiltInRegistries.ITEM.getOptional(this.itemName)
                .ifPresent(item -> this.item = item);
        BuiltInRegistries.ITEM.getOptional(this.blockItemName)
                .ifPresent(item -> this.blockItem = item);
        this.color = color;
        this.translationKey = "soul_type.naraka." + name().toLowerCase();
    }

    SoulType(int id, int color) {
        this.id = id;
        this.itemName = getDefaultItemName();
        this.blockItemName = getDefaultBlockItemName();
        this.color = color;
        this.translationKey = "soul_type.naraka." + name().toLowerCase();
    }

    private ResourceLocation getDefaultItemName() {
        return NarakaMod.location("soul_infused_" + name().toLowerCase());
    }

    private ResourceLocation getDefaultBlockItemName() {
        return NarakaMod.location("soul_infused_" + name().toLowerCase() + "_block");
    }

    public int getId() {
        return id;
    }

    public int getColor() {
        return color;
    }

    public Item getBlockItem() {
        if (blockItem != null)
            return blockItem;
        return blockItem = BuiltInRegistries.ITEM.getOptional(blockItemName).orElseThrow();
    }

    public Item getItem() {
        if (item != null)
            return item;
        return item = BuiltInRegistries.ITEM.getOptional(itemName).orElseThrow();
    }

    public boolean test(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return !itemStack.isEmpty()
                && (this.getItem() == item || this.getBlockItem() == item);
    }

    public String translationKey() {
        return translationKey;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
