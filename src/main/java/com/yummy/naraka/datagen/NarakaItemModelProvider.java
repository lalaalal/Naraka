package com.yummy.naraka.datagen;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.item.NarakaItems;
import com.yummy.naraka.item.SpearItem;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class NarakaItemModelProvider extends ItemModelProvider {
    public NarakaItemModelProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, NarakaMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(NarakaItems.TEST_ITEM, "item/stick");
        spearItem(NarakaItems.SPEAR_ITEM);
        spearItem(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM);
    }

    public ItemModelBuilder spearItem(DeferredItem<? extends SpearItem> spearItem) {
        ResourceLocation empty = NarakaMod.location("item/empty");
        String inventory = spearItem.getId().getPath() + "_inventory";
        String inHand = spearItem.getId().getPath() + "_in_hand";

        withExistingParent(inventory, "item/generated")
                .texture("layer0", NarakaMod.location("item/spear"));
        getBuilder(inHand)
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .texture("particle", NarakaMod.location("item/spear"));
        ItemModelBuilder inHandReference = new ItemModelBuilder(empty, existingFileHelper)
                .parent(new ModelFile.ExistingModelFile(NarakaMod.location("item", inHand), existingFileHelper));
        ItemModelBuilder inventoryReference = new ItemModelBuilder(empty, existingFileHelper)
                .parent(new ModelFile.ExistingModelFile(NarakaMod.location("item", inventory), existingFileHelper));
        return withExistingParent(spearItem, "item/generated")
                .customLoader(SeparateTransformsModelBuilder::begin)
                .base(inHandReference)
                .perspective(ItemDisplayContext.GUI, inventoryReference)
                .perspective(ItemDisplayContext.FIXED, inventoryReference)
                .end();
    }

    public ItemModelBuilder simpleItem(DeferredItem<? extends Item> item) {
        String path = item.getId().getPath();
        return withExistingParent(path, "item/generated")
                .texture("layer0", NarakaMod.location("item", path));
    }

    public ItemModelBuilder withExistingParent(DeferredItem<? extends Item> item, String parent) {
        return withExistingParent(item.getId().getPath(), parent);
    }
}
