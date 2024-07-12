package com.yummy.naraka.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.SpearItem;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Arrays;

public class NarakaItemModelProvider extends ItemModelProvider {
    public NarakaItemModelProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, NarakaMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(NarakaItems.PURIFIED_SOUL_METAL);
        simpleItem(NarakaItems.PURIFIED_SOUL_SHARD);
        simpleItem(NarakaItems.NECTARIUM);
        simpleItem(NarakaItems.GOD_BLOOD);
        simpleItem(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE);
        simpleItem(NarakaItems.PURIFIED_GEMS_UPGRADE_SMITHING_TEMPLATE);
        simpleItem(NarakaItems.COMPRESSED_IRON_INGOT);
        simpleItem(NarakaItems.FAKE_GOLD_INGOT);

        withExistingParent(NarakaItems.TEST_ITEM, "item/stick");
        spearItem(NarakaItems.SPEAR_ITEM, ItemDisplayContext.GUI, ItemDisplayContext.FIXED, ItemDisplayContext.GROUND);
        spearItem(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, ItemDisplayContext.GUI, ItemDisplayContext.FIXED, ItemDisplayContext.GROUND);
        spearItem(NarakaItems.SPEAR_OF_LONGINUS_ITEM, ItemDisplayContext.GUI, ItemDisplayContext.FIXED);

        simpleItem(NarakaItems.SOUL_INFUSED_AMETHYST);
        simpleItem(NarakaItems.SOUL_INFUSED_COPPER);
        simpleItem(NarakaItems.SOUL_INFUSED_DIAMOND);
        simpleItem(NarakaItems.SOUL_INFUSED_EMERALD);
        simpleItem(NarakaItems.SOUL_INFUSED_GOLD);
        simpleItem(NarakaItems.SOUL_INFUSED_LAPIS);
        simpleItem(NarakaItems.SOUL_INFUSED_NECTARIUM);
        simpleItem(NarakaItems.SOUL_INFUSED_REDSTONE);

        simpleItem(NarakaItems.EBONY_SIGN);
        simpleItem(NarakaItems.EBONY_HANGING_SIGN);

        handheld(NarakaItems.SOUL_INFUSED_REDSTONE_SWORD);
        handheld(NarakaItems.SOUL_INFUSED_COPPER_SWORD);
        handheld(NarakaItems.SOUL_INFUSED_GOLD_SWORD);
        handheld(NarakaItems.SOUL_INFUSED_EMERALD_SWORD);
        handheld(NarakaItems.SOUL_INFUSED_DIAMOND_SWORD);
        handheld(NarakaItems.SOUL_INFUSED_LAPIS_SWORD);
        handheld(NarakaItems.SOUL_INFUSED_AMETHYST_SWORD);
        handheld(NarakaItems.SOUL_INFUSED_NECTARIUM_SWORD);
        handheld(NarakaItems.PURIFIED_SOUL_SWORD);
        handheld(NarakaItems.EBONY_SWORD);
    }

    public ItemModelBuilder spearItem(DeferredItem<? extends SpearItem> spearItem, ItemDisplayContext... displayAsItem) {
        ResourceLocation empty = NarakaMod.location("item/empty");
        String spearName = spearItem.getId().getPath();
        String inventory = spearName + "_inventory";
        String inHand = spearName + "_in_hand";

        withExistingParent(inventory, "item/generated")
                .texture("layer0", NarakaMod.location("item", spearName));
        boolean displayAsEntityOnGround = Arrays.stream(displayAsItem).noneMatch(itemDisplayContext -> itemDisplayContext == ItemDisplayContext.GROUND);
        spearAsEntity(spearName, inHand, displayAsEntityOnGround);
        ItemModelBuilder inHandReference = new ItemModelBuilder(empty, existingFileHelper)
                .parent(new ModelFile.ExistingModelFile(NarakaMod.location("item", inHand), existingFileHelper));
        ItemModelBuilder inventoryReference = new ItemModelBuilder(empty, existingFileHelper)
                .parent(new ModelFile.ExistingModelFile(NarakaMod.location("item", inventory), existingFileHelper));
        SeparateTransformsModelBuilder<ItemModelBuilder> builder = withExistingParent(spearItem, "item/generated")
                .customLoader(SeparateTransformsModelBuilder::begin)
                .base(inHandReference);
        for (ItemDisplayContext context : displayAsItem)
            builder.perspective(context, inventoryReference);
        return builder.end();
    }

    private ItemModelBuilder spearAsEntity(String spearName, String modelName, boolean includeGround) {
        ModelBuilder<ItemModelBuilder>.TransformsBuilder builder = getBuilder(modelName)
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
                .end();
        if (includeGround)
            builder.transform(ItemDisplayContext.GROUND)
                    .rotation(180, 0, 0)
                    .translation(8.25f, 0, -8.25f)
                    .end();
        return builder.end();
    }

    public ItemModelBuilder simpleItem(DeferredItem<? extends Item> item) {
        String name = item.getId().getPath();
        return withExistingParent(name, "item/generated")
                .texture("layer0", NarakaMod.location("item", name));
    }

    public ItemModelBuilder handheld(DeferredItem<? extends Item> item) {
        String name = item.getId().getPath();
        return withExistingParent(name, "item/handheld")
                .texture("layer0", NarakaMod.location("item", name));
    }

    public ItemModelBuilder withExistingParent(DeferredItem<? extends Item> item, String parent) {
        return withExistingParent(item.getId().getPath(), parent);
    }

    public ItemModelBuilder withExistingParent(DeferredItem<? extends Item> item, ResourceLocation parent) {
        return withExistingParent(item.getId().getPath(), parent);
    }
}
