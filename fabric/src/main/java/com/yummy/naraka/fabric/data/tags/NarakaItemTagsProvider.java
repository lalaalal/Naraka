package com.yummy.naraka.fabric.data.tags;

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
                .add(NarakaItems.SPEAR_ITEM.get())
                .add(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get())
                .add(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get());

        getOrCreateTagBuilder(NarakaItemTags.ALWAYS_RENDER_ITEM_ENTITY)
                .add(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get());

        getOrCreateTagBuilder(NarakaItemTags.SPEAR_ENCHANTABLE)
                .add(NarakaItems.SPEAR_ITEM.get())
                .add(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get());
        getOrCreateTagBuilder(NarakaItemTags.LOYALTY_ENCHANTABLE)
                .addTag(NarakaItemTags.SPEAR_ENCHANTABLE)
                .forceAddTag(ItemTags.TRIDENT_ENCHANTABLE);

        getOrCreateTagBuilder(NarakaItemTags.EBONY_LOGS)
                .add(NarakaBlocks.EBONY_LOG.get().asItem())
                .add(NarakaBlocks.EBONY_WOOD.get().asItem())
                .add(NarakaBlocks.STRIPPED_EBONY_LOG.get().asItem())
                .add(NarakaBlocks.STRIPPED_EBONY_WOOD.get().asItem());

        getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
                .addTag(NarakaItemTags.EBONY_LOGS);
        getOrCreateTagBuilder(ItemTags.PLANKS)
                .add(NarakaBlocks.HARD_EBONY_PLANKS.get().asItem());

        getOrCreateTagBuilder(ItemTags.DURABILITY_ENCHANTABLE)
                .addTag(NarakaItemTags.SPEAR_ENCHANTABLE);
        getOrCreateTagBuilder(ItemTags.VANISHING_ENCHANTABLE)
                .addTag(NarakaItemTags.SPEAR_ENCHANTABLE);
        getOrCreateTagBuilder(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                .addTag(NarakaItemTags.SPEAR_ENCHANTABLE);

        getOrCreateTagBuilder(ItemTags.COMPASSES)
                .add(NarakaItems.SANCTUARY_COMPASS.get());
        getOrCreateTagBuilder(ItemTags.TRIM_TEMPLATES)
                .add(NarakaItems.PURIFIED_SOUL_SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE.get());
        FabricTagBuilder trimMaterials = getOrCreateTagBuilder(ItemTags.TRIM_MATERIALS)
                .add(NarakaItems.GOD_BLOOD.get());
        NarakaItems.forEachSoulInfusedItem(trimMaterials::add);

        getOrCreateTagBuilder(NarakaItemTags.PURIFIED_SOUL_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_HELMET.get())
                .add(NarakaItems.PURIFIED_SOUL_CHESTPLATE.get())
                .add(NarakaItems.PURIFIED_SOUL_LEGGINGS.get())
                .add(NarakaItems.PURIFIED_SOUL_BOOTS.get());

        FabricTagBuilder soulReinforceable = getOrCreateTagBuilder(NarakaItemTags.SOUL_REINFORCEABLE)
                .addTag(NarakaItemTags.PURIFIED_SOUL_ARMOR);
        NarakaItems.forEachSoulInfusedSword(soulReinforceable::add);

        getOrCreateTagBuilder(ItemTags.HEAD_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_HELMET.get())
                .add(NarakaItems.EBONY_METAL_HELMET.get());
        getOrCreateTagBuilder(ItemTags.CHEST_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_CHESTPLATE.get())
                .add(NarakaItems.EBONY_METAL_CHESTPLATE.get());
        getOrCreateTagBuilder(ItemTags.LEG_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_LEGGINGS.get())
                .add(NarakaItems.EBONY_METAL_LEGGINGS.get());
        getOrCreateTagBuilder(ItemTags.FOOT_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_BOOTS.get())
                .add(NarakaItems.EBONY_METAL_BOOTS.get());

        getOrCreateTagBuilder(ItemTags.TRIM_TEMPLATES)
                .add(NarakaItems.PURIFIED_SOUL_SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE.get());
    }
}
