package com.yummy.naraka.data.tags;

import com.yummy.naraka.tags.NarakaItemTags;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class NarakaItemTagsProvider extends FabricTagProvider<Item> {
    public NarakaItemTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ITEM, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(NarakaItemTags.SPEAR)
                .add(NarakaItems.SPEAR_ITEM)
                .add(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM)
                .add(NarakaItems.SPEAR_OF_LONGINUS_ITEM);

        getOrCreateTagBuilder(NarakaItemTags.ALWAYS_RENDER_ITEM_ENTITY)
                .add(NarakaItems.SPEAR_OF_LONGINUS_ITEM);

        getOrCreateTagBuilder(NarakaItemTags.SPEAR_ENCHANTABLE)
                .add(NarakaItems.SPEAR_ITEM)
                .add(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM)
                .forceAddTag(ItemTags.TRIDENT_ENCHANTABLE);

        getOrCreateTagBuilder(NarakaItemTags.EBONY_LOGS)
                .add(NarakaBlocks.EBONY_LOG.asItem())
                .add(NarakaBlocks.EBONY_WOOD.asItem())
                .add(NarakaBlocks.STRIPPED_EBONY_LOG.asItem())
                .add(NarakaBlocks.STRIPPED_EBONY_WOOD.asItem());

        getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
                .addTag(NarakaItemTags.EBONY_LOGS);
        getOrCreateTagBuilder(ItemTags.PLANKS)
                .add(NarakaBlocks.EBONY_PLANKS.asItem());

        getOrCreateTagBuilder(ItemTags.DURABILITY_ENCHANTABLE)
                .add(NarakaItems.SPEAR_ITEM)
                .add(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM);
        getOrCreateTagBuilder(ItemTags.VANISHING_ENCHANTABLE)
                .add(NarakaItems.SPEAR_ITEM)
                .add(NarakaItems.SPEAR_OF_LONGINUS_ITEM);
    }
}
