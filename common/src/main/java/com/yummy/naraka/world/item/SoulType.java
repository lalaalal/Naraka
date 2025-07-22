package com.yummy.naraka.world.item;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.item.equipment.trim.NarakaTrimMaterials;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import org.jetbrains.annotations.Nullable;

public enum SoulType implements StringRepresentable {
    NONE(0xffffff),
    REDSTONE(0xeb4747),
    COPPER(0xff8000),
    GOLD(0xffd24d),
    EMERALD(0x0ec70e),
    DIAMOND(0x33cccc),
    LAPIS(0x3939c6),
    AMETHYST(0x9957db),
    NECTARIUM(0xd65cd6),
    GOD_BLOOD(NarakaTrimMaterials.GOD_BLOOD, "god_blood", "god_blood", 0x625859);

    public static final Codec<SoulType> CODEC = StringRepresentable.fromEnum(SoulType::values);
    public static final StreamCodec<ByteBuf, SoulType> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public final int id;
    private final ResourceLocation itemName;
    private final ResourceLocation blockItemName;
    @Nullable
    private Item item;
    @Nullable
    private Item blockItem;
    public final int color;
    public final String translationKey;
    public final ResourceKey<TrimMaterial> material;

    public static SoulType fromItem(ItemStack itemStack) {
        for (SoulType soulType : values()) {
            if (soulType.test(itemStack))
                return soulType;
        }
        return NONE;
    }

    SoulType(ResourceKey<TrimMaterial> material, String itemName, String blockItemName, int color) {
        this.id = ordinal();
        this.itemName = NarakaMod.location(itemName);
        this.blockItemName = NarakaMod.location(blockItemName);
        BuiltInRegistries.ITEM.getOptional(this.itemName)
                .ifPresent(item -> this.item = item);
        BuiltInRegistries.ITEM.getOptional(this.blockItemName)
                .ifPresent(item -> this.blockItem = item);
        this.color = color;
        this.translationKey = "soul_type.naraka." + name().toLowerCase();
        this.material = material;
    }

    SoulType(int color) {
        this.id = ordinal();
        this.itemName = getDefaultItemName();
        this.blockItemName = getDefaultBlockItemName();
        this.color = color;
        this.translationKey = "soul_type.naraka." + name().toLowerCase();
        this.material = NarakaTrimMaterials.create("soul_infused_" + name().toLowerCase());
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
        if (this == NONE)
            return false;
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
