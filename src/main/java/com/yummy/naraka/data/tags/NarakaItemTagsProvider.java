package com.yummy.naraka.data.tags;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.tags.NarakaItemTags;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class NarakaItemTagsProvider extends ItemTagsProvider {
    public NarakaItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper existingFileHelper) {
        super(packOutput, provider, blockTags, NarakaMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(NarakaItemTags.SPEAR)
                .add(NarakaItems.SPEAR_ITEM.get())
                .add(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get())
                .add(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get());

        tag(NarakaItemTags.ALWAYS_RENDER_ITEM_ENTITY)
                .add(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get());

        tag(NarakaItemTags.SPEAR_ENCHANTABLE)
                .add(NarakaItems.SPEAR_ITEM.get())
                .add(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get())
                .addTag(ItemTags.TRIDENT_ENCHANTABLE);

        tag(NarakaItemTags.EBONY_LOGS)
                .add(NarakaBlocks.EBONY_LOG.asItem())
                .add(NarakaBlocks.STRIPPED_EBONY_LOG.asItem());

        tag(ItemTags.LOGS)
                .addTag(NarakaItemTags.EBONY_LOGS);
        tag(ItemTags.PLANKS)
                .add(NarakaBlocks.EBONY_PLANKS.asItem());
        tag(ItemTags.SIGNS)
                .add(NarakaItems.EBONY_SIGN.get());
        tag(ItemTags.HANGING_SIGNS)
                .add(NarakaItems.EBONY_HANGING_SIGN.get());

        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .add(NarakaItems.SPEAR_ITEM.get())
                .add(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get());
        tag(ItemTags.VANISHING_ENCHANTABLE)
                .add(NarakaItems.SPEAR_ITEM.get())
                .add(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get());
    }
}
