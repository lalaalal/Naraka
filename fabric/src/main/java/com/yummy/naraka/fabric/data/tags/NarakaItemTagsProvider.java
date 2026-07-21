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
                .add(NarakaItems.NARAKA_PICKAXE.getConcreteValue());
        getOrCreateTagBuilder(ItemTags.AXES)
                .add(NarakaItems.NARAKA_PICKAXE.getConcreteValue());

        getOrCreateTagBuilder(ItemTags.CLUSTER_MAX_HARVESTABLES)
                .add(NarakaItems.NARAKA_PICKAXE.getConcreteValue());

        getOrCreateTagBuilder(NarakaItemTags.SPEAR)
                .add(NarakaItems.SPEAR_ITEM.getConcreteValue())
                .add(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.getConcreteValue())
                .add(NarakaItems.SPEAR_OF_LONGINUS_ITEM.getConcreteValue());

        getOrCreateTagBuilder(NarakaItemTags.ALWAYS_RENDER_ITEM_ENTITY)
                .add(NarakaItems.SPEAR_OF_LONGINUS_ITEM.getConcreteValue());

        getOrCreateTagBuilder(NarakaItemTags.SPEAR_ENCHANTABLE)
                .add(NarakaItems.SPEAR_ITEM.getConcreteValue())
                .add(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.getConcreteValue());
        getOrCreateTagBuilder(NarakaItemTags.LOYALTY_ENCHANTABLE)
                .addTag(NarakaItemTags.SPEAR_ENCHANTABLE);

        getOrCreateTagBuilder(ItemTags.COMPASSES)
                .add(NarakaItems.SANCTUARY_COMPASS.getConcreteValue());

        getOrCreateTagBuilder(NarakaItemTags.ENTER_NARAKA_DIMENSION)
                .add(NarakaItems.SPEAR_OF_LONGINUS_ITEM.getConcreteValue())
                .add(NarakaItems.PURIFIED_SOUL_SWORD.getConcreteValue())
                .add(NarakaItems.NARAKA_PICKAXE.getConcreteValue())
                .addTag(NarakaItemTags.SOUL_SWORDS);

        getOrCreateTagBuilder(NarakaItemTags.PURIFIED_SOUL_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_HELMET.getConcreteValue())
                .add(NarakaItems.PURIFIED_SOUL_CHESTPLATE.getConcreteValue())
                .add(NarakaItems.PURIFIED_SOUL_LEGGINGS.getConcreteValue())
                .add(NarakaItems.PURIFIED_SOUL_BOOTS.getConcreteValue());

        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
                .addTag(NarakaItemTags.PURIFIED_SOUL_ARMOR);
        FabricTagBuilder soulReinforceable = getOrCreateTagBuilder(NarakaItemTags.SOUL_REINFORCEABLE)
                .addTag(NarakaItemTags.PURIFIED_SOUL_ARMOR)
                .add(NarakaItems.PURIFIED_SOUL_SWORD.getConcreteValue());
        NarakaItems.forEachSoulInfusedSword(soulReinforceable::add);

        FabricTagBuilder soulMaterials = getOrCreateTagBuilder(NarakaItemTags.SOUL_MATERIALS);
        NarakaItems.forEachSoulInfusedItem(soulMaterials::add);

        FabricTagBuilder soulSwords = getOrCreateTagBuilder(NarakaItemTags.SOUL_SWORDS);
        NarakaItems.forEachSoulInfusedSword(soulSwords::add);

        getOrCreateTagBuilder(ItemTags.SWORDS)
                .add(NarakaItems.NARAKA_PICKAXE.getConcreteValue())
                .addTag(NarakaItemTags.SOUL_SWORDS);
    }
}
