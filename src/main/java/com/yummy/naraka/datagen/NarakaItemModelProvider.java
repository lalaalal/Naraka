package com.yummy.naraka.datagen;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.item.NarakaItems;
import net.minecraft.data.PackOutput;
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
        ItemModelBuilder spearInventory = withExistingParent("spear_inventory", "item/generated")
                .texture("layer0", NarakaMod.location("item/spear"));
        ItemModelBuilder spearInHand = getBuilder("spear_in_hand")
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .texture("particle", NarakaMod.location("item/spear"));
        withExistingParent(NarakaItems.SPEAR_ITEM, "item/generated")
                .customLoader(SeparateTransformsModelBuilder::begin)
                .base(spearInHand)
                .perspective(ItemDisplayContext.GUI, spearInventory);
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
