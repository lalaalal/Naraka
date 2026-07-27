package com.yummy.naraka.fabric.data.tags;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.tags.NarakaItemTags;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class NarakaItemTagsProvider extends FabricTagsProvider.ItemTagsProvider {
    public NarakaItemTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        builder(ItemTags.PICKAXES)
                .add(NarakaItems.NARAKA_PICKAXE.key());
        builder(ItemTags.AXES)
                .add(NarakaItems.NARAKA_PICKAXE.key());

        builder(ItemTags.CLUSTER_MAX_HARVESTABLES)
                .add(NarakaItems.NARAKA_PICKAXE.key());

        builder(NarakaItemTags.SPEAR)
                .add(NarakaItems.SPEAR_ITEM.key())
                .add(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.key())
                .add(NarakaItems.SPEAR_OF_LONGINUS_ITEM.key());

        builder(NarakaItemTags.ALWAYS_RENDER_ITEM_ENTITY)
                .add(NarakaItems.SPEAR_OF_LONGINUS_ITEM.key());

        builder(NarakaItemTags.SPEAR_ENCHANTABLE)
                .add(NarakaItems.SPEAR_ITEM.key())
                .add(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.key());
        builder(NarakaItemTags.LOYALTY_ENCHANTABLE)
                .addTag(NarakaItemTags.SPEAR_ENCHANTABLE)
                .forceAddTag(ItemTags.TRIDENT_ENCHANTABLE);

        builder(ItemTags.DURABILITY_ENCHANTABLE)
                .addTag(NarakaItemTags.SPEAR_ENCHANTABLE);
        builder(ItemTags.VANISHING_ENCHANTABLE)
                .addTag(NarakaItemTags.SPEAR_ENCHANTABLE);
        builder(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                .addTag(NarakaItemTags.SPEAR_ENCHANTABLE);

        builder(ItemTags.COMPASSES)
                .add(NarakaItems.SANCTUARY_COMPASS.key());

        builder(ItemTags.HEAD_ARMOR_ENCHANTABLE)
                .add(NarakaItems.PURIFIED_SOUL_HELMET.key());
        builder(ItemTags.CHEST_ARMOR_ENCHANTABLE)
                .add(NarakaItems.PURIFIED_SOUL_CHESTPLATE.key());
        builder(ItemTags.LEG_ARMOR_ENCHANTABLE)
                .add(NarakaItems.PURIFIED_SOUL_LEGGINGS.key());
        builder(ItemTags.FOOT_ARMOR_ENCHANTABLE)
                .add(NarakaItems.PURIFIED_SOUL_BOOTS.key());

        builder(NarakaItemTags.ENTER_NARAKA_DIMENSION)
                .add(NarakaItems.SPEAR_OF_LONGINUS_ITEM.key())
                .add(NarakaItems.PURIFIED_SOUL_SWORD.key())
                .add(NarakaItems.NARAKA_PICKAXE.key())
                .addTag(NarakaItemTags.SOUL_SWORDS);

        builder(NarakaItemTags.PURIFIED_SOUL_MATERIALS)
                .add(NarakaItems.PURIFIED_SOUL_METAL.key());

        builder(NarakaItemTags.PURIFIED_SOUL_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_HELMET.key())
                .add(NarakaItems.PURIFIED_SOUL_CHESTPLATE.key())
                .add(NarakaItems.PURIFIED_SOUL_LEGGINGS.key())
                .add(NarakaItems.PURIFIED_SOUL_BOOTS.key());

        TagAppender<ResourceKey<Item>, Item> soulReinforceable = builder(NarakaItemTags.SOUL_REINFORCEABLE)
                .addTag(NarakaItemTags.PURIFIED_SOUL_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_SWORD.key());
        NarakaItems.SOUL_INFUSED_SWORDS.stream()
                .map(HolderProxy::key)
                .forEach(soulReinforceable::add);

        TagAppender<ResourceKey<Item>, Item> soulMaterials = builder(NarakaItemTags.SOUL_MATERIALS);
        NarakaItems.SOUL_INFUSED_ITEMS.stream()
                .map(HolderProxy::key)
                .forEach(soulMaterials::add);

        TagAppender<ResourceKey<Item>, Item> soulSwords = builder(NarakaItemTags.SOUL_SWORDS);
        NarakaItems.SOUL_INFUSED_SWORDS.stream()
                .map(HolderProxy::key)
                .forEach(soulSwords::add);

        builder(ItemTags.SWORDS)
                .add(NarakaItems.NARAKA_PICKAXE.key())
                .addTag(NarakaItemTags.SOUL_SWORDS);

        builder(ItemTags.HEAD_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_HELMET.key());
        builder(ItemTags.CHEST_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_CHESTPLATE.key());
        builder(ItemTags.LEG_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_LEGGINGS.key());
        builder(ItemTags.FOOT_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_BOOTS.key());
    }
}
