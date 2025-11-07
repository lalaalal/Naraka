package com.yummy.naraka.fabric.data.tags;

import com.yummy.naraka.tags.NarakaItemTags;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class NarakaItemTagsProvider extends FabricTagProvider.ItemTagProvider {
    public NarakaItemTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        valueLookupBuilder(NarakaItemTags.SPEAR)
                .add(NarakaItems.SPEAR_ITEM.get())
                .add(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get())
                .add(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get());

        valueLookupBuilder(NarakaItemTags.ALWAYS_RENDER_ITEM_ENTITY)
                .add(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get());

        valueLookupBuilder(NarakaItemTags.SPEAR_ENCHANTABLE)
                .add(NarakaItems.SPEAR_ITEM.get())
                .add(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get());
        valueLookupBuilder(NarakaItemTags.LOYALTY_ENCHANTABLE)
                .addTag(NarakaItemTags.SPEAR_ENCHANTABLE)
                .forceAddTag(ItemTags.TRIDENT_ENCHANTABLE);

        valueLookupBuilder(ItemTags.DURABILITY_ENCHANTABLE)
                .addTag(NarakaItemTags.SPEAR_ENCHANTABLE);
        valueLookupBuilder(ItemTags.VANISHING_ENCHANTABLE)
                .addTag(NarakaItemTags.SPEAR_ENCHANTABLE);
        valueLookupBuilder(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                .addTag(NarakaItemTags.SPEAR_ENCHANTABLE);

        valueLookupBuilder(ItemTags.COMPASSES)
                .add(NarakaItems.SANCTUARY_COMPASS.get());

        valueLookupBuilder(ItemTags.HEAD_ARMOR_ENCHANTABLE)
                .add(NarakaItems.PURIFIED_SOUL_HELMET.get());
        valueLookupBuilder(ItemTags.CHEST_ARMOR_ENCHANTABLE)
                .add(NarakaItems.PURIFIED_SOUL_CHESTPLATE.get());
        valueLookupBuilder(ItemTags.LEG_ARMOR_ENCHANTABLE)
                .add(NarakaItems.PURIFIED_SOUL_LEGGINGS.get());
        valueLookupBuilder(ItemTags.FOOT_ARMOR_ENCHANTABLE)
                .add(NarakaItems.PURIFIED_SOUL_BOOTS.get());

        valueLookupBuilder(NarakaItemTags.ENTER_NARAKA_DIMENSION)
                .add(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get())
                .add(NarakaItems.PURIFIED_SOUL_SWORD.get())
                .addTag(NarakaItemTags.SOUL_SWORDS);

        valueLookupBuilder(NarakaItemTags.PURIFIED_SOUL_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_HELMET.get())
                .add(NarakaItems.PURIFIED_SOUL_CHESTPLATE.get())
                .add(NarakaItems.PURIFIED_SOUL_LEGGINGS.get())
                .add(NarakaItems.PURIFIED_SOUL_BOOTS.get());

        TagAppender<Item, Item> soulReinforceable = valueLookupBuilder(NarakaItemTags.SOUL_REINFORCEABLE)
                .addTag(NarakaItemTags.PURIFIED_SOUL_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_SWORD.get());
        NarakaItems.forEachSoulInfusedSword(soulReinforceable::add);

        TagAppender<Item, Item> soulMaterials = valueLookupBuilder(NarakaItemTags.SOUL_MATERIALS);
        NarakaItems.forEachSoulInfusedItem(soulMaterials::add);

        TagAppender<Item, Item> soulSwords = valueLookupBuilder(NarakaItemTags.SOUL_SWORDS);
        NarakaItems.forEachSoulInfusedSword(soulSwords::add);

        valueLookupBuilder(ItemTags.SWORDS)
                .addTag(NarakaItemTags.SOUL_SWORDS);

        valueLookupBuilder(ItemTags.HEAD_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_HELMET.get());
        valueLookupBuilder(ItemTags.CHEST_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_CHESTPLATE.get());
        valueLookupBuilder(ItemTags.LEG_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_LEGGINGS.get());
        valueLookupBuilder(ItemTags.FOOT_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_BOOTS.get());
    }
}
