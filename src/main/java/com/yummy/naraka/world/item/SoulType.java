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
    REDSTONE(0, () -> NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK, () -> NarakaItems.SOUL_INFUSED_REDSTONE),
    COPPER(1, () -> NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK, () -> NarakaItems.SOUL_INFUSED_COPPER),
    GOLD(2, () -> NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK, () -> NarakaItems.SOUL_INFUSED_GOLD),
    EMERALD(3, () -> NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK, () -> NarakaItems.SOUL_INFUSED_EMERALD),
    DIAMOND(4, () -> NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK, () -> NarakaItems.SOUL_INFUSED_DIAMOND),
    LAPIS(5, () -> NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK, () -> NarakaItems.SOUL_INFUSED_LAPIS),
    AMETHYST(6, () -> NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK, () -> NarakaItems.SOUL_INFUSED_AMETHYST),
    NECTARIUM(7, () -> NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK, () -> NarakaItems.SOUL_INFUSED_NECTARIUM);

    private static final IntFunction<SoulType> BY_ID = ByIdMap.continuous(SoulType::getId, SoulType.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<SoulType> CODEC = StringRepresentable.fromEnum(SoulType::values);
    public static final StreamCodec<ByteBuf, SoulType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, SoulType::getId);

    public final int id;
    private final Supplier<Block> soulBlock;
    private final Supplier<Item> soulItem;
    public final String translationKey;

    @Nullable
    public static SoulType fromItem(ItemStack itemStack) {
        for (SoulType soulType : values()) {
            if (soulType.test(itemStack))
                return soulType;
        }
        return null;
    }

    SoulType(int id, Supplier<Block> soulBlock, Supplier<Item> soulItem) {
        this.id = id;
        this.soulBlock = soulBlock;
        this.soulItem = soulItem;
        this.translationKey = "soul_type.naraka." + name().toLowerCase();
    }

    public int getId() {
        return id;
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
