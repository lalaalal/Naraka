package com.yummy.naraka.fabric.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.renderer.special.SoulSmithingBlockSpecialRenderer;
import com.yummy.naraka.client.renderer.special.SoulStabilizerSpecialRenderer;
import com.yummy.naraka.client.renderer.special.SpearOfLonginusSpecialRenderer;
import com.yummy.naraka.client.renderer.special.SpearSpecialRenderer;
import com.yummy.naraka.fabric.mixin.client.CompassAngleStateMixin;
import com.yummy.naraka.world.block.EbonyLogBlock;
import com.yummy.naraka.world.block.HerobrineTotem;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.SpearItem;
import com.yummy.naraka.world.item.equipment.NarakaEquipmentAssets;
import com.yummy.naraka.world.item.equipment.trim.NarakaTrimMaterials;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.numeric.CompassAngle;
import net.minecraft.client.renderer.item.properties.numeric.CompassAngleState;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.client.renderer.item.properties.select.TrimMaterialProperty;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NarakaModelProvider extends FabricModelProvider {
    private static final ModelTemplate BLOCK_ENTITY = new ModelTemplate(
            Optional.of(NarakaMod.location("item", "template_block_entity")),
            Optional.empty(),
            TextureSlot.PARTICLE
    );

    public NarakaModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        createPurifiedSoulFire(generator);
        generator.createTrivialCube(NarakaBlocks.TRANSPARENT_BLOCK.get());
        generator.createTrivialCube(NarakaBlocks.NECTARIUM_ORE.get());
        generator.createTrivialCube(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.get());
        generator.createTrivialCube(NarakaBlocks.COMPRESSED_IRON_BLOCK.get());
        generator.createTrivialCube(NarakaBlocks.NECTARIUM_BLOCK.get());
        generator.createTrivialCube(NarakaBlocks.IMITATION_GOLD_BLOCK.get());
        generator.createTrivialCube(NarakaBlocks.PURIFIED_SOUL_BLOCK.get());
        generator.createTrivialCube(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.get());
        generator.createTrivialBlock(NarakaBlocks.EBONY_LEAVES.get(), TexturedModel.LEAVES);
        generator.createTrivialCube(NarakaBlocks.AMETHYST_SHARD_BLOCK.get());
        createEbonyLog(generator, NarakaBlocks.EBONY_LOG.get());
        generator.woodProvider(NarakaBlocks.EBONY_LOG.get())
                .wood(NarakaBlocks.EBONY_WOOD.get());
        generator.woodProvider(NarakaBlocks.STRIPPED_EBONY_LOG.get())
                .log(NarakaBlocks.STRIPPED_EBONY_LOG.get())
                .wood(NarakaBlocks.STRIPPED_EBONY_WOOD.get());
        generator.createTrivialCube(NarakaBlocks.HARD_EBONY_PLANKS.get());
        createHerobrineTotem(generator);
        generator.createNonTemplateModelBlock(NarakaBlocks.EBONY_ROOTS.get());
        generator.createPlant(NarakaBlocks.EBONY_SAPLING.get(), NarakaBlocks.POTTED_EBONY_SAPLING.get(), BlockModelGenerators.PlantType.NOT_TINTED);
        NarakaBlocks.forEachSoulInfusedBlock(generator::createTrivialCube);
        generator.createTrivialCube(NarakaBlocks.EBONY_METAL_BLOCK.get());
        createBlockEntity(generator, Blocks.ANVIL, NarakaBlocks.SOUL_SMITHING_BLOCK.get(), new SoulSmithingBlockSpecialRenderer.Unbaked());
        createBlockEntity(generator, Blocks.GLASS, NarakaBlocks.SOUL_STABILIZER.get(), new SoulStabilizerSpecialRenderer.Unbaked());
        createNectariumCrystal(generator);
        generator.createTrivialCube(NarakaBlocks.NECTARIUM_CORE_BLOCK.get());
        generator.createTrivialCube(NarakaBlocks.AMETHYST_ORE.get());
        generator.createTrivialCube(NarakaBlocks.DEEPSLATE_AMETHYST_ORE.get());
    }

    private static void createBlockEntity(BlockModelGenerators generator, Block particle, Block block, SpecialModelRenderer.Unbaked unbaked) {
        generator.createParticleOnlyBlock(block, particle);
        Item item = block.asItem();
        ResourceLocation blockModel = BLOCK_ENTITY.create(item, TextureMapping.particle(particle), generator.modelOutput);
        ItemModel.Unbaked unbakedItemModel = ItemModelUtils.specialModel(blockModel, unbaked);
        generator.itemModelOutput.accept(item, unbakedItemModel);
    }

    private static void createNectariumCrystal(BlockModelGenerators generator) {
        PropertyDispatch.C2<Direction, DripstoneThickness> properties = PropertyDispatch.properties(
                BlockStateProperties.VERTICAL_DIRECTION, BlockStateProperties.DRIPSTONE_THICKNESS
        );

        for (DripstoneThickness dripstoneThickness : DripstoneThickness.values())
            properties.select(Direction.UP, dripstoneThickness, createNectariumCrystalVariant(generator, Direction.UP, dripstoneThickness));

        for (DripstoneThickness dripstoneThickness : DripstoneThickness.values())
            properties.select(Direction.DOWN, dripstoneThickness, createNectariumCrystalVariant(generator, Direction.DOWN, dripstoneThickness));

        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get()).with(properties));
    }

    private static Variant createNectariumCrystalVariant(BlockModelGenerators generator, Direction direction, DripstoneThickness dripstoneThickness) {
        String model_name = "_" + direction.getSerializedName() + "_" + dripstoneThickness.getSerializedName();
        TextureMapping textureMapping = TextureMapping.cross(TextureMapping.getBlockTexture(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get(), model_name));
        return Variant.variant()
                .with(VariantProperties.MODEL, ModelTemplates.POINTED_DRIPSTONE.createWithSuffix(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get(), model_name, textureMapping, generator.modelOutput));
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
        models[0] = TexturedModel.CUBE_TOP.create(NarakaBlocks.HEROBRINE_TOTEM.get(), generator.modelOutput);
        for (int crack = 1; crack <= HerobrineTotem.MAX_CRACK; crack++) {
            ResourceLocation texture = TextureMapping.getBlockTexture(NarakaBlocks.HEROBRINE_TOTEM.get(), "_" + crack);
            models[crack] = TexturedModel.CUBE_TOP
                    .updateTexture(mapping -> mapping.put(TextureSlot.SIDE, texture))
                    .createWithSuffix(NarakaBlocks.HEROBRINE_TOTEM.get(), "_" + crack, generator.modelOutput);
        }
        return models;
    }

    private void createHerobrineTotem(BlockModelGenerators generator) {
        generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(NarakaBlocks.HEROBRINE_TOTEM.get())
                .with(createIntegerModelDispatch(HerobrineTotem.CRACK, createTotemModels(generator)))
        );
    }

    private void createPurifiedSoulFire(BlockModelGenerators generator) {
        List<ResourceLocation> floorModels = generator.createFloorFireModels(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.get());
        List<ResourceLocation> sideModels = generator.createSideFireModels(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.get());
        generator.blockStateOutput.accept(MultiPartGenerator.multiPart(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.get())
                .with(BlockModelGenerators.wrapModels(floorModels, (variant) -> variant))
                .with(BlockModelGenerators.wrapModels(sideModels, (variant) -> variant))
                .with(BlockModelGenerators.wrapModels(sideModels, (variant) -> variant.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90)))
                .with(BlockModelGenerators.wrapModels(sideModels, (variant) -> variant.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180)))
                .with(BlockModelGenerators.wrapModels(sideModels, (variant) -> variant.with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)))
        );
    }

    private static final List<ItemModelGenerators.TrimMaterialData> TRIM_MATERIAL_MODELS = List.of(
            new ItemModelGenerators.TrimMaterialData("quartz", TrimMaterials.QUARTZ, Map.of()),
            new ItemModelGenerators.TrimMaterialData("iron", TrimMaterials.IRON, Map.of(EquipmentAssets.IRON, "iron_darker")),
            new ItemModelGenerators.TrimMaterialData("netherite", TrimMaterials.NETHERITE, Map.of(EquipmentAssets.NETHERITE, "netherite_darker")),
            new ItemModelGenerators.TrimMaterialData("redstone", TrimMaterials.REDSTONE, Map.of()),
            new ItemModelGenerators.TrimMaterialData("copper", TrimMaterials.COPPER, Map.of()),
            new ItemModelGenerators.TrimMaterialData("gold", TrimMaterials.GOLD, Map.of(EquipmentAssets.GOLD, "gold_darker")),
            new ItemModelGenerators.TrimMaterialData("emerald", TrimMaterials.EMERALD, Map.of()),
            new ItemModelGenerators.TrimMaterialData("diamond", TrimMaterials.DIAMOND, Map.of(EquipmentAssets.DIAMOND, "diamond_darker")),
            new ItemModelGenerators.TrimMaterialData("lapis", TrimMaterials.LAPIS, Map.of()),
            new ItemModelGenerators.TrimMaterialData("amethyst", TrimMaterials.AMETHYST, Map.of()),
            new ItemModelGenerators.TrimMaterialData("soul_infused_redstone", NarakaTrimMaterials.SOUL_INFUSED_REDSTONE, Map.of()),
            new ItemModelGenerators.TrimMaterialData("soul_infused_copper", NarakaTrimMaterials.SOUL_INFUSED_COPPER, Map.of()),
            new ItemModelGenerators.TrimMaterialData("soul_infused_gold", NarakaTrimMaterials.SOUL_INFUSED_GOLD, Map.of()),
            new ItemModelGenerators.TrimMaterialData("soul_infused_emerald", NarakaTrimMaterials.SOUL_INFUSED_EMERALD, Map.of()),
            new ItemModelGenerators.TrimMaterialData("soul_infused_diamond", NarakaTrimMaterials.SOUL_INFUSED_DIAMOND, Map.of()),
            new ItemModelGenerators.TrimMaterialData("soul_infused_lapis", NarakaTrimMaterials.SOUL_INFUSED_LAPIS, Map.of()),
            new ItemModelGenerators.TrimMaterialData("soul_infused_amethyst", NarakaTrimMaterials.SOUL_INFUSED_AMETHYST, Map.of()),
            new ItemModelGenerators.TrimMaterialData("soul_infused_nectarium", NarakaTrimMaterials.SOUL_INFUSED_NECTARIUM, Map.of()),
            new ItemModelGenerators.TrimMaterialData("god_blood", NarakaTrimMaterials.GOD_BLOOD, Map.of())
    );

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        generator.generateFlatItem(NarakaItems.STARDUST_STAFF.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        generator.generateFlatItem(NarakaItems.NARAKA_FIREBALL.get(), ModelTemplates.FLAT_ITEM);

        generateSpear(generator, NarakaItems.SPEAR_ITEM.get(), NarakaModelLayers.SPEAR, NarakaTextures.SPEAR);
        generateSpear(generator, NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get(), NarakaModelLayers.SPEAR, NarakaTextures.MIGHTY_HOLY_SPEAR);
        generateSpearOfLonginus(generator, NarakaItems.SPEAR_OF_LONGINUS_ITEM.get());

        generator.generateFlatItem(NarakaItems.SKILL_CONTROLLER.get(), NarakaItems.PURIFIED_SOUL_SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.ANIMATION_CONTROLLER.get(), Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.GOD_BLOOD.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.NECTARIUM.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.EBONY_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        generator.generateFlatItem(NarakaItems.COMPRESSED_IRON_INGOT.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.PURIFIED_SOUL_METAL.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.PURIFIED_SOUL_SHARD.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.PURIFIED_SOUL_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        generator.generateFlatItem(NarakaItems.EBONY_METAL_INGOT.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.EBONY_ROOTS_SCRAP.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.PURIFIED_SOUL_SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.RAINBOW_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        generateTrimmableItem(generator, Items.LEATHER_HELMET, EquipmentAssets.LEATHER, "helmet");
        generateTrimmableItem(generator, Items.LEATHER_CHESTPLATE, EquipmentAssets.LEATHER, "chestplate");
        generateTrimmableItem(generator, Items.LEATHER_LEGGINGS, EquipmentAssets.LEATHER, "leggings");
        generateTrimmableItem(generator, Items.LEATHER_BOOTS, EquipmentAssets.LEATHER, "boots");
        generateTrimmableItem(generator, Items.CHAINMAIL_HELMET, EquipmentAssets.CHAINMAIL, "helmet");
        generateTrimmableItem(generator, Items.CHAINMAIL_CHESTPLATE, EquipmentAssets.CHAINMAIL, "chestplate");
        generateTrimmableItem(generator, Items.CHAINMAIL_LEGGINGS, EquipmentAssets.CHAINMAIL, "leggings");
        generateTrimmableItem(generator, Items.CHAINMAIL_BOOTS, EquipmentAssets.CHAINMAIL, "boots");
        generateTrimmableItem(generator, Items.IRON_HELMET, EquipmentAssets.IRON, "helmet");
        generateTrimmableItem(generator, Items.IRON_CHESTPLATE, EquipmentAssets.IRON, "chestplate");
        generateTrimmableItem(generator, Items.IRON_LEGGINGS, EquipmentAssets.IRON, "leggings");
        generateTrimmableItem(generator, Items.IRON_BOOTS, EquipmentAssets.IRON, "boots");
        generateTrimmableItem(generator, Items.DIAMOND_HELMET, EquipmentAssets.DIAMOND, "helmet");
        generateTrimmableItem(generator, Items.DIAMOND_CHESTPLATE, EquipmentAssets.DIAMOND, "chestplate");
        generateTrimmableItem(generator, Items.DIAMOND_LEGGINGS, EquipmentAssets.DIAMOND, "leggings");
        generateTrimmableItem(generator, Items.DIAMOND_BOOTS, EquipmentAssets.DIAMOND, "boots");
        generateTrimmableItem(generator, Items.GOLDEN_HELMET, EquipmentAssets.GOLD, "helmet");
        generateTrimmableItem(generator, Items.GOLDEN_CHESTPLATE, EquipmentAssets.GOLD, "chestplate");
        generateTrimmableItem(generator, Items.GOLDEN_LEGGINGS, EquipmentAssets.GOLD, "leggings");
        generateTrimmableItem(generator, Items.GOLDEN_BOOTS, EquipmentAssets.GOLD, "boots");
        generateTrimmableItem(generator, Items.NETHERITE_HELMET, EquipmentAssets.NETHERITE, "helmet");
        generateTrimmableItem(generator, Items.NETHERITE_CHESTPLATE, EquipmentAssets.NETHERITE, "chestplate");
        generateTrimmableItem(generator, Items.NETHERITE_LEGGINGS, EquipmentAssets.NETHERITE, "leggings");
        generateTrimmableItem(generator, Items.NETHERITE_BOOTS, EquipmentAssets.NETHERITE, "boots");

        generateTrimmableItem(generator, NarakaItems.PURIFIED_SOUL_CHESTPLATE.get(), NarakaEquipmentAssets.PURIFIED_SOUL, "chestplate");
        generateTrimmableItem(generator, NarakaItems.PURIFIED_SOUL_HELMET.get(), NarakaEquipmentAssets.PURIFIED_SOUL, "helmet");
        generateTrimmableItem(generator, NarakaItems.PURIFIED_SOUL_LEGGINGS.get(), NarakaEquipmentAssets.PURIFIED_SOUL, "leggings");
        generateTrimmableItem(generator, NarakaItems.PURIFIED_SOUL_BOOTS.get(), NarakaEquipmentAssets.PURIFIED_SOUL, "boots");

        generator.generateFlatItem(NarakaItems.STIGMA_ROD.get(), ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
        generator.generateFlatItem(NarakaItems.HEROBRINE_PHASE_1_DISC.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.HEROBRINE_PHASE_2_DISC.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.HEROBRINE_PHASE_3_DISC.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.HEROBRINE_PHASE_4_DISC.get(), ModelTemplates.FLAT_ITEM);

        NarakaItems.forEachSoulInfusedItem(item -> generator.generateFlatItem(item, ModelTemplates.FLAT_ITEM));
        NarakaItems.forEachSoulInfusedSword(item -> generator.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM));

        generateSanctuaryCompassItem(generator, NarakaItems.SANCTUARY_COMPASS.get());
        generator.declareCustomModelItem(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get().asItem());
    }

    public static void generateSpear(ItemModelGenerators generator, SpearItem spearItem, ModelLayerLocation modelLayer, ResourceLocation texture) {
        ItemModel.Unbaked plainModel = ItemModelUtils.plainModel(generator.createFlatItemModel(spearItem, ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked inHandModel = ItemModelUtils.specialModel(
                ModelLocationUtils.getModelLocation(Items.TRIDENT, "_in_hand"), new SpearSpecialRenderer.Unbaked(modelLayer, texture)
        );
        ItemModel.Unbaked throwingModel = ItemModelUtils.specialModel(
                ModelLocationUtils.getModelLocation(Items.TRIDENT, "_throwing"), new SpearSpecialRenderer.Unbaked(modelLayer, texture)
        );
        ItemModel.Unbaked entityModel = ItemModelUtils.conditional(ItemModelUtils.isUsingItem(), throwingModel, inHandModel);
        generator.itemModelOutput.accept(spearItem, ItemModelGenerators.createFlatModelDispatch(plainModel, entityModel));
    }

    public static void generateSpearOfLonginus(ItemModelGenerators generator, SpearItem spearItem) {
        ItemModel.Unbaked plainModel = ItemModelUtils.plainModel(generator.createFlatItemModel(spearItem, ModelTemplates.FLAT_ITEM));
        ItemModel.Unbaked inHandModel = ItemModelUtils.specialModel(
                ModelLocationUtils.getModelLocation(Items.TRIDENT, "_in_hand"), new SpearOfLonginusSpecialRenderer.Unbaked()
        );
        ItemModel.Unbaked throwingModel = ItemModelUtils.specialModel(
                ModelLocationUtils.getModelLocation(Items.TRIDENT, "_throwing"), new SpearOfLonginusSpecialRenderer.Unbaked()
        );
        ItemModel.Unbaked entityModel = ItemModelUtils.conditional(ItemModelUtils.isUsingItem(), throwingModel, inHandModel);
        generator.itemModelOutput.accept(spearItem, createSpearOfLonginusModelDispatch(plainModel, entityModel));
    }

    public static ItemModel.Unbaked createSpearOfLonginusModelDispatch(ItemModel.Unbaked itemModel, ItemModel.Unbaked holdingModel) {
        return ItemModelUtils.select(
                new DisplayContext(), holdingModel, ItemModelUtils.when(List.of(ItemDisplayContext.GUI, ItemDisplayContext.FIXED), itemModel)
        );
    }

    public static void generateTrimmableItem(ItemModelGenerators generator, Item item, ResourceKey<EquipmentAsset> key, String name) {
        ResourceLocation modelLocation = ModelLocationUtils.getModelLocation(item);
        ResourceLocation texture = TextureMapping.getItemTexture(item);

        List<SelectItemModel.SwitchCase<ResourceKey<TrimMaterial>>> list = new ArrayList<>(TRIM_MATERIAL_MODELS.size());
        for (ItemModelGenerators.TrimMaterialData data : TRIM_MATERIAL_MODELS) {
            ResourceLocation trimmedArmorModelLocation = modelLocation.withSuffix("_" + data.name() + "_trim");
            ResourceLocation trimTexture = NarakaMod.mcLocation("trims/items/" + name + "_trim_" + data.textureName(key));
            generator.generateLayeredItem(trimmedArmorModelLocation, texture, trimTexture);
            ItemModel.Unbaked unbaked = ItemModelUtils.plainModel(trimmedArmorModelLocation);

            list.add(ItemModelUtils.when(data.materialKey(), unbaked));
        }

        ModelTemplates.FLAT_ITEM.create(modelLocation, TextureMapping.layer0(texture), generator.modelOutput);
        ItemModel.Unbaked unbaked = ItemModelUtils.plainModel(modelLocation);
        generator.itemModelOutput.accept(item, ItemModelUtils.select(new TrimMaterialProperty(), unbaked, list));
    }

    /**
     * @see CompassAngleStateMixin
     */
    public static void generateSanctuaryCompassItem(ItemModelGenerators generator, Item item) {
        List<RangeSelectItemModel.Entry> list = generator.createCompassModels(item);
        generator.itemModelOutput
                .accept(
                        item,
                        ItemModelUtils.rangeSelect(new CompassAngle(true, CompassAngleState.CompassTarget.NONE), 32.0F, list)
                );
    }
}
