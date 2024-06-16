package com.yummy.naraka.data;

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

        simpleItem(NarakaItems.NECTARIUM);

        spearItem(NarakaItems.SPEAR_ITEM);
        spearItem(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM);
        spearItem(NarakaItems.SPEAR_OF_LONGINUS_ITEM);
    }

    public ItemModelBuilder spearItem(DeferredItem<? extends SpearItem> spearItem) {
        ResourceLocation empty = NarakaMod.location("item/empty");
        String spearName = spearItem.getId().getPath();
        String inventory = spearName + "_inventory";
        String inHand = spearName + "_in_hand";

        withExistingParent(inventory, "item/generated")
                .texture("layer0", NarakaMod.location("item", spearName));
        spearAsEntity(spearName, inHand);
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

    private ItemModelBuilder spearAsEntity(String spearName, String modelName) {
        return getBuilder(modelName)
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .texture("particle", NarakaMod.location("item", spearName))
                .transforms()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .rotation(0, -90, 205)
                .translation(-4, -8, -6)
                .end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                .rotation(0, 90, 155)
                .translation(13, -8, -7)
                .end()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                .rotation(0, 60, 180)
                .translation(4, 5, 12)
                .end()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                .rotation(0, 60, 180)
                .translation(11, 5, -1)
                .end()
                .transform(ItemDisplayContext.GROUND)
                .rotation(180, 0, 0)
                .translation(8.25f, 0, -8.25f)
                .end()
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

    public ItemModelBuilder withExistingParent(DeferredItem<? extends Item> item, ResourceLocation parent) {
        return withExistingParent(item.getId().getPath(), parent);
    }
}
