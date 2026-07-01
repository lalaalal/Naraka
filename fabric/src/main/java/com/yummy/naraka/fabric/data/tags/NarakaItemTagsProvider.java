package com.yummy.naraka.fabric.data.tags;

import com.yummy.naraka.tags.NarakaItemTags;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;

import java.util.concurrent.CompletableFuture;

public class NarakaItemTagsProvider extends FabricTagProvider.ItemTagProvider {
    public NarakaItemTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(ItemTags.PICKAXES)
                .add(NarakaItems.NARAKA_PICKAXE.get());
        getOrCreateTagBuilder(ItemTags.AXES)
                .add(NarakaItems.NARAKA_PICKAXE.get());

        getOrCreateTagBuilder(ItemTags.CLUSTER_MAX_HARVESTABLES)
                .add(NarakaItems.NARAKA_PICKAXE.get());

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
                .addTag(NarakaItemTags.SPEAR_ENCHANTABLE);

        getOrCreateTagBuilder(ItemTags.COMPASSES)
                .add(NarakaItems.SANCTUARY_COMPASS.get());

        getOrCreateTagBuilder(NarakaItemTags.ENTER_NARAKA_DIMENSION)
                .add(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get())
                .add(NarakaItems.PURIFIED_SOUL_SWORD.get())
                .addTag(NarakaItemTags.SOUL_SWORDS);

        getOrCreateTagBuilder(NarakaItemTags.PURIFIED_SOUL_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_HELMET.get())
                .add(NarakaItems.PURIFIED_SOUL_CHESTPLATE.get())
                .add(NarakaItems.PURIFIED_SOUL_LEGGINGS.get())
                .add(NarakaItems.PURIFIED_SOUL_BOOTS.get());

        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
                .addTag(NarakaItemTags.PURIFIED_SOUL_ARMOR);
        FabricTagBuilder soulReinforceable = getOrCreateTagBuilder(NarakaItemTags.SOUL_REINFORCEABLE)
                .addTag(NarakaItemTags.PURIFIED_SOUL_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_SWORD.get());
        NarakaItems.forEachSoulInfusedSword(soulReinforceable::add);

        FabricTagBuilder soulMaterials = getOrCreateTagBuilder(NarakaItemTags.SOUL_MATERIALS);
        NarakaItems.forEachSoulInfusedItem(soulMaterials::add);

        FabricTagBuilder soulSwords = getOrCreateTagBuilder(NarakaItemTags.SOUL_SWORDS);
        NarakaItems.forEachSoulInfusedSword(soulSwords::add);

        getOrCreateTagBuilder(ItemTags.SWORDS)
                .add(NarakaItems.NARAKA_PICKAXE.get())
                .addTag(NarakaItemTags.SOUL_SWORDS);
    }
}
