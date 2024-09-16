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
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.IntFunction;
import java.util.function.Supplier;

public enum SoulType implements StringRepresentable {
    REDSTONE(0, () -> NarakaItems.SOUL_INFUSED_REDSTONE, () -> NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK, 0xeb4747),
    COPPER(1, () -> NarakaItems.SOUL_INFUSED_COPPER, () -> NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK, 0xff8000),
    GOLD(2, () -> NarakaItems.SOUL_INFUSED_GOLD, () -> NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK, 0xffd24d),
    EMERALD(3, () -> NarakaItems.SOUL_INFUSED_EMERALD, () -> NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK, 0x0ec70e),
    DIAMOND(4, () -> NarakaItems.SOUL_INFUSED_DIAMOND, () -> NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK, 0x33cccc),
    LAPIS(5, () -> NarakaItems.SOUL_INFUSED_LAPIS, () -> NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK, 0x3939c6),
    AMETHYST(6, () -> NarakaItems.SOUL_INFUSED_AMETHYST, () -> NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK, 0x9957db),
    NECTARIUM(7, () -> NarakaItems.SOUL_INFUSED_NECTARIUM, () -> NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK, 0xd65cd6);

    private static final IntFunction<SoulType> BY_ID = ByIdMap.continuous(SoulType::getId, SoulType.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<SoulType> CODEC = StringRepresentable.fromEnum(SoulType::values);
    public static final StreamCodec<ByteBuf, SoulType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, SoulType::getId);

    public final int id;
    private final Supplier<Item> soulItem;
    private final Supplier<Block> soulBlock;
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

    SoulType(int id, Supplier<Item> soulItem, Supplier<Block> soulBlock, int color) {
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

    public Block getBlock() {
        return soulBlock.get();
    }

    public Item getItem() {
        return soulItem.get();
    }

    public boolean test(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return !itemStack.isEmpty()
                && (this.getItem() == item || this.getBlock().asItem() == item);
    }

    public String translationKey() {
        return translationKey;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
