package com.yummy.naraka.data;

import com.yummy.naraka.world.block.EbonyLogBlock;
import com.yummy.naraka.world.block.HerobrineTotem;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.List;

public class NarakaModelProvider extends FabricModelProvider {
    public NarakaModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        generator.skipAutoItemBlock(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK);
        createPurifiedSoulFire(generator);
        generator.createTrivialCube(NarakaBlocks.TRANSPARENT_BLOCK);
        generator.createTrivialCube(NarakaBlocks.NECTARIUM_ORE);
        generator.createTrivialCube(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE);
        generator.createTrivialCube(NarakaBlocks.COMPRESSED_IRON_BLOCK);
        generator.createTrivialCube(NarakaBlocks.NECTARIUM_BLOCK);
        generator.createTrivialCube(NarakaBlocks.FAKE_GOLD_BLOCK);
        generator.createTrivialCube(NarakaBlocks.PURIFIED_SOUL_BLOCK);
        generator.createTrivialCube(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK);
        generator.createTrivialBlock(NarakaBlocks.EBONY_LEAVES, TexturedModel.LEAVES);
        generator.createTrivialCube(NarakaBlocks.AMETHYST_SHARD_BLOCK);
        createEbonyLog(generator, NarakaBlocks.EBONY_LOG);
        generator.woodProvider(NarakaBlocks.EBONY_LOG)
                .wood(NarakaBlocks.EBONY_WOOD);
        generator.woodProvider(NarakaBlocks.STRIPPED_EBONY_LOG)
                .log(NarakaBlocks.STRIPPED_EBONY_LOG)
                .wood(NarakaBlocks.STRIPPED_EBONY_WOOD);
        generator.createTrivialCube(NarakaBlocks.HARD_EBONY_PLANKS);
        createHerobrineTotem(generator);
        generator.createFurnace(NarakaBlocks.SOUL_CRAFTING_BLOCK, TexturedModel.ORIENTABLE_ONLY_TOP);
        generator.createNonTemplateModelBlock(NarakaBlocks.EBONY_ROOTS);
        generator.createPlant(NarakaBlocks.EBONY_SAPLING, NarakaBlocks.POTTED_EBONY_SAPLING, BlockModelGenerators.TintState.NOT_TINTED);
        NarakaBlocks.forEachSoulInfusedBlock(generator::createTrivialCube);
        generator.createTrivialCube(NarakaBlocks.EBONY_METAL_BLOCK);
    }

    private static void createEbonyLog(BlockModelGenerators generator, Block block) {
        ResourceLocation model = ModelTemplates.CUBE_COLUMN.create(block, TextureMapping.logColumn(block), generator.modelOutput);
        generator.blockStateOutput.accept(MultiVariantGenerator
                .multiVariant(block, Variant.variant()
                        .with(VariantProperties.MODEL, model))
                .with(createEbonyLog(createEbonyBranchModel(generator, block)))
        );
    }

    private static ResourceLocation createEbonyBranchModel(BlockModelGenerators generator, Block block) {
        return TexturedModel.CUBE.createWithSuffix(block, "_branch", generator.modelOutput);
    }

    private static PropertyDispatch createEbonyLog(ResourceLocation branchModel) {
        return PropertyDispatch.properties(BlockStateProperties.AXIS, EbonyLogBlock.BRANCH)
                .generate((axis, branch) -> {
                    if (branch) {
                        return Variant.variant().with(VariantProperties.MODEL, branchModel);
                    } else {
                        return switch (axis) {
                            case Y -> Variant.variant();
                            case Z -> Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90);
                            case X -> Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90)
                                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90);
                        };
                    }
                });
    }

    private static PropertyDispatch createIntegerModelDispatch(IntegerProperty property, ResourceLocation[] models) {
        return PropertyDispatch.property(property)
                .generate(crack -> Variant.variant()
                        .with(VariantProperties.MODEL, models[crack])
                );
    }

    private static ResourceLocation[] createTotemModels(BlockModelGenerators generator) {
        ResourceLocation[] models = new ResourceLocation[HerobrineTotem.MAX_CRACK + 1];
        models[0] = TexturedModel.CUBE_TOP.create(NarakaBlocks.HEROBRINE_TOTEM, generator.modelOutput);
        for (int crack = 1; crack <= HerobrineTotem.MAX_CRACK; crack++) {
            ResourceLocation texture = TextureMapping.getBlockTexture(NarakaBlocks.HEROBRINE_TOTEM, "_" + crack);
            models[crack] = TexturedModel.CUBE_TOP
                    .updateTexture(mapping -> mapping.put(TextureSlot.SIDE, texture))
                    .createWithSuffix(NarakaBlocks.HEROBRINE_TOTEM, "_" + crack, generator.modelOutput);
        }
        return models;
    }

    private void createHerobrineTotem(BlockModelGenerators generator) {
        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(NarakaBlocks.HEROBRINE_TOTEM)
                .with(createIntegerModelDispatch(HerobrineTotem.CRACK, createTotemModels(generator)))
        );
    }

    private void createPurifiedSoulFire(BlockModelGenerators generator) {
        List<ResourceLocation> floorModels = generator.createFloorFireModels(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK);
        List<ResourceLocation> sideModels = generator.createSideFireModels(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK);
        generator.blockStateOutput.accept(MultiPartGenerator.multiPart(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK)
                .with(BlockModelGenerators.wrapModels(floorModels, (variant) -> variant))
                .with(BlockModelGenerators.wrapModels(sideModels, (variant) -> variant))
                .with(BlockModelGenerators.wrapModels(sideModels, (variant) -> variant.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)))
                .with(BlockModelGenerators.wrapModels(sideModels, (variant) -> variant.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)))
                .with(BlockModelGenerators.wrapModels(sideModels, (variant) -> variant.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)))
        );
        generator.delegateItemModel(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK, sideModels.getFirst());
    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        generator.generateFlatItem(NarakaItems.SPEAR_ITEM, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.SPEAR_OF_LONGINUS_ITEM, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.GOD_BLOOD, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.NECTARIUM, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.EBONY_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        generator.generateFlatItem(NarakaItems.COMPRESSED_IRON_INGOT, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.PURIFIED_SOUL_METAL, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.PURIFIED_SOUL_SHARD, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.PURIFIED_SOUL_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        generator.generateFlatItem(NarakaItems.EBONY_METAL_INGOT, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.EBONY_ROOTS_SCRAP, ModelTemplates.FLAT_ITEM);
        NarakaItems.forEachSoulInfusedItem(item -> generator.generateFlatItem(item, ModelTemplates.FLAT_ITEM));
        NarakaItems.forEachSoulInfusedSword(item -> generator.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM));
    }
}
