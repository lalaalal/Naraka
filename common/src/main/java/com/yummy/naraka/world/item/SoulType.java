package com.yummy.naraka.world.item;

import com.mojang.serialization.Codec;
import com.yummy.naraka.world.block.NarakaBlocks;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;
import java.util.function.Supplier;

public enum SoulType implements StringRepresentable {
    REDSTONE(0, () -> NarakaItems.SOUL_INFUSED_REDSTONE.get(), () -> NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK.get().asItem(), 0xeb4747),
    COPPER(1, () -> NarakaItems.SOUL_INFUSED_COPPER.get(), () -> NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK.get().asItem(), 0xff8000),
    GOLD(2, () -> NarakaItems.SOUL_INFUSED_GOLD.get(), () -> NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK.get().asItem(), 0xffd24d),
    EMERALD(3, () -> NarakaItems.SOUL_INFUSED_EMERALD.get(), () -> NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK.get().asItem(), 0x0ec70e),
    DIAMOND(4, () -> NarakaItems.SOUL_INFUSED_DIAMOND.get(), () -> NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK.get().asItem(), 0x33cccc),
    LAPIS(5, () -> NarakaItems.SOUL_INFUSED_LAPIS.get(), () -> NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK.get().asItem(), 0x3939c6),
    AMETHYST(6, () -> NarakaItems.SOUL_INFUSED_AMETHYST.get(), () -> NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK.get().asItem(), 0x9957db),
    NECTARIUM(7, () -> NarakaItems.SOUL_INFUSED_NECTARIUM.get(), () -> NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK.get().asItem(), 0xd65cd6),
    GOD_BLOOD(8, () -> NarakaItems.GOD_BLOOD.get(), () -> NarakaItems.GOD_BLOOD.get(), 0x625859);

    private static final IntFunction<SoulType> BY_ID = ByIdMap.continuous(SoulType::getId, SoulType.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<SoulType> CODEC = StringRepresentable.fromEnum(SoulType::values);
    public static final StreamCodec<ByteBuf, SoulType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, SoulType::getId);

    public final int id;
    private final Supplier<Item> soulItem;
    private final Supplier<Item> soulBlock;
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

    SoulType(int id, Supplier<Item> soulItem, Supplier<Item> soulBlock, int color) {
        this.id = id;
        this.soulBlock = soulBlock;
        this.soulItem = soulItem;
        this.color = color;
        this.translationKey = "soul_type.naraka." + name().toLowerCase();
    }

    public int getId() {
        return id;
    }

    public int getColor() {
        return color;
    }

    public Item getBlockItem() {
        return soulBlock.get();
    }

    public Item getItem() {
        return soulItem.get();
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
