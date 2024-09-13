package com.yummy.naraka.world.item;

import com.yummy.naraka.world.block.NarakaBlocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum SoulType {
    REDSTONE(() -> NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK, () -> NarakaItems.SOUL_INFUSED_REDSTONE),
    COPPER(() -> NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK, () -> NarakaItems.SOUL_INFUSED_COPPER),
    GOLD(() -> NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK, () -> NarakaItems.SOUL_INFUSED_GOLD),
    EMERALD(() -> NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK, () -> NarakaItems.SOUL_INFUSED_EMERALD),
    DIAMOND(() -> NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK, () -> NarakaItems.SOUL_INFUSED_DIAMOND),
    LAPIS(() -> NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK, () -> NarakaItems.SOUL_INFUSED_LAPIS),
    AMETHYST(() -> NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK, () -> NarakaItems.SOUL_INFUSED_AMETHYST),
    NECTARIUM(() -> NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK, () -> NarakaItems.SOUL_INFUSED_NECTARIUM);

    private final Supplier<Block> soulBlock;
    private final Supplier<Item> soulItem;
    private final String translationKey;

    @Nullable
    public static SoulType fromItem(ItemStack itemStack) {
        for (SoulType soulType : values()) {
            if (soulType.test(itemStack))
                return soulType;
        }
        return null;
    }

    SoulType(Supplier<Block> soulBlock, Supplier<Item> soulItem) {
        this.soulBlock = soulBlock;
        this.soulItem = soulItem;
        this.translationKey = "soul_type.naraka." + name().toLowerCase();
    }

    public Block block() {
        return soulBlock.get();
    }

    public Item item() {
        return soulItem.get();
    }

    public boolean test(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return this.item() == item || this.block().asItem() == item;
    }

    public String translationKey() {
        return translationKey;
    }
}
