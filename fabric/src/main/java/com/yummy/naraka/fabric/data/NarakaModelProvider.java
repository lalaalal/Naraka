package com.yummy.naraka.fabric.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.renderer.special.SoulSmithingBlockSpecialRenderer;
import com.yummy.naraka.client.renderer.special.SoulStabilizerSpecialRenderer;
import com.yummy.naraka.client.renderer.special.SpearOfLonginusSpecialRenderer;
import com.yummy.naraka.client.renderer.special.SpearSpecialRenderer;
import com.yummy.naraka.core.component.NarakaDataComponentTypes;
import com.yummy.naraka.world.block.HerobrineTotem;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.SpearItem;
import com.yummy.naraka.world.item.equipment.NarakaEquipmentAssets;
import com.yummy.naraka.world.item.equipment.trim.NarakaTrimMaterials;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.color.item.Dye;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.renderer.item.properties.conditional.HasComponent;
import net.minecraft.client.renderer.item.properties.numeric.CompassAngle;
import net.minecraft.client.renderer.item.properties.numeric.CompassAngleState;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.client.renderer.item.properties.select.TrimMaterialProperty;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NarakaModelProvider extends FabricModelProvider {
    private static final ModelTemplate BLOCK_ENTITY = new ModelTemplate(
            Optional.of(NarakaMod.identifier("item", "template_block_entity")),
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
        generator.createTrivialCube(NarakaBlocks.NECTARIUM_BLOCK.get());
        generator.createTrivialCube(NarakaBlocks.IMITATION_GOLD_BLOCK.get());
        generator.createTrivialCube(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.get());
        generator.createTrivialCube(NarakaBlocks.AMETHYST_SHARD_BLOCK.get());
        createHerobrineTotem(generator);
        NarakaBlocks.forEachSoulInfusedBlock(generator::createTrivialCube);
        createBlockEntity(generator, Blocks.ANVIL, NarakaBlocks.SOUL_SMITHING_BLOCK.get(), new SoulSmithingBlockSpecialRenderer.Unbaked());
        createBlockEntity(generator, Blocks.GLASS, NarakaBlocks.SOUL_STABILIZER.get(), new SoulStabilizerSpecialRenderer.Unbaked());
        createNectariumCrystal(generator);
        generator.createTrivialCube(NarakaBlocks.NECTARIUM_CORE_BLOCK.get());
        generator.createTrivialCube(NarakaBlocks.AMETHYST_ORE.get());
        generator.createTrivialCube(NarakaBlocks.DEEPSLATE_AMETHYST_ORE.get());
        generator.createTrivialCube(NarakaBlocks.PURIFIED_SOUL_LAMP.get());
        generator.createTrivialCube(NarakaBlocks.PURIFIED_SOUL_LANTERN.get());
        generator.createAirLikeBlock(NarakaBlocks.DIAMOND_GOLEM_SPAWNER.get(), Items.STRUCTURE_VOID);
        generator.createAirLikeBlock(NarakaBlocks.NARAKA_PORTAL.get(), Items.STRUCTURE_VOID);
    }

    private static void createBlockEntity(BlockModelGenerators generator, Block particle, Block block, SpecialModelRenderer.Unbaked unbaked) {
        generator.createParticleOnlyBlock(block, particle);
        Item item = block.asItem();
        Identifier blockModel = BLOCK_ENTITY.create(item, TextureMapping.particle(particle), generator.modelOutput);
        ItemModel.Unbaked unbakedItemModel = ItemModelUtils.specialModel(blockModel, unbaked);
        generator.itemModelOutput.accept(item, unbakedItemModel);
    }

    private static void createNectariumCrystal(BlockModelGenerators generator) {
        PropertyDispatch.C2<MultiVariant, Direction, DripstoneThickness> properties = PropertyDispatch.initial(
                BlockStateProperties.VERTICAL_DIRECTION, BlockStateProperties.DRIPSTONE_THICKNESS
        );

        for (DripstoneThickness dripstoneThickness : DripstoneThickness.values())
            properties.select(Direction.UP, dripstoneThickness, createNectariumCrystalVariant(generator, Direction.UP, dripstoneThickness));

        for (DripstoneThickness dripstoneThickness : DripstoneThickness.values())
            properties.select(Direction.DOWN, dripstoneThickness, createNectariumCrystalVariant(generator, Direction.DOWN, dripstoneThickness));

        generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get()).with(properties));
    }

    private static MultiVariant createNectariumCrystalVariant(BlockModelGenerators generator, Direction direction, DripstoneThickness dripstoneThickness) {
        String model_name = "_" + direction.getSerializedName() + "_" + dripstoneThickness.getSerializedName();
        TextureMapping textureMapping = TextureMapping.cross(TextureMapping.getBlockTexture(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get(), model_name));
        Identifier model = ModelTemplates.POINTED_DRIPSTONE.createWithSuffix(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get(), model_name, textureMapping, generator.modelOutput);
        return BlockModelGenerators.plainVariant(model);
    }

    private static PropertyDispatch<MultiVariant> createIntegerModelDispatch(IntegerProperty property, Identifier[] models) {
        return PropertyDispatch.initial(property)
                .generate(crack -> BlockModelGenerators.plainVariant(models[crack]));
    }

    private static Identifier[] createTotemModels(BlockModelGenerators generator) {
        Identifier[] models = new Identifier[HerobrineTotem.MAX_CRACK + 1];
        models[0] = TexturedModel.COLUMN.create(NarakaBlocks.HEROBRINE_TOTEM.get(), generator.modelOutput);
        for (int crack = 1; crack <= HerobrineTotem.MAX_CRACK; crack++) {
            Identifier texture = TextureMapping.getBlockTexture(NarakaBlocks.HEROBRINE_TOTEM.get(), "_" + crack);
            models[crack] = TexturedModel.COLUMN
                    .updateTexture(mapping -> mapping.put(TextureSlot.SIDE, texture))
                    .createWithSuffix(NarakaBlocks.HEROBRINE_TOTEM.get(), "_" + crack, generator.modelOutput);
        }
        return models;
    }

    private void createHerobrineTotem(BlockModelGenerators generator) {
        generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(NarakaBlocks.HEROBRINE_TOTEM.get())
                .with(createIntegerModelDispatch(HerobrineTotem.CRACK, createTotemModels(generator)))
        );
    }

    private void createPurifiedSoulFire(BlockModelGenerators generator) {
        MultiVariant floorModels = generator.createFloorFireModels(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.get());
        MultiVariant sideModels = generator.createSideFireModels(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.get());
        generator.blockStateOutput.accept(MultiPartGenerator.multiPart(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.get())
                .with(floorModels)
                .with(sideModels)
                .with(sideModels.with(BlockModelGenerators.Y_ROT_90))
                .with(sideModels.with(BlockModelGenerators.Y_ROT_180))
                .with(sideModels.with(BlockModelGenerators.Y_ROT_270))
        );
    }

    private static final List<ItemModelGenerators.TrimMaterialData> TRIM_MATERIAL_MODELS = List.of(
            new ItemModelGenerators.TrimMaterialData(MaterialAssetGroup.QUARTZ, TrimMaterials.QUARTZ),
            new ItemModelGenerators.TrimMaterialData(MaterialAssetGroup.IRON, TrimMaterials.IRON),
            new ItemModelGenerators.TrimMaterialData(MaterialAssetGroup.NETHERITE, TrimMaterials.NETHERITE),
            new ItemModelGenerators.TrimMaterialData(MaterialAssetGroup.REDSTONE, TrimMaterials.REDSTONE),
            new ItemModelGenerators.TrimMaterialData(MaterialAssetGroup.COPPER, TrimMaterials.COPPER),
            new ItemModelGenerators.TrimMaterialData(MaterialAssetGroup.GOLD, TrimMaterials.GOLD),
            new ItemModelGenerators.TrimMaterialData(MaterialAssetGroup.EMERALD, TrimMaterials.EMERALD),
            new ItemModelGenerators.TrimMaterialData(MaterialAssetGroup.DIAMOND, TrimMaterials.DIAMOND),
            new ItemModelGenerators.TrimMaterialData(MaterialAssetGroup.LAPIS, TrimMaterials.LAPIS),
            new ItemModelGenerators.TrimMaterialData(MaterialAssetGroup.AMETHYST, TrimMaterials.AMETHYST),
            new ItemModelGenerators.TrimMaterialData(MaterialAssetGroup.create("soul_infused_redstone"), NarakaTrimMaterials.SOUL_INFUSED_REDSTONE),
            new ItemModelGenerators.TrimMaterialData(MaterialAssetGroup.create("soul_infused_copper"), NarakaTrimMaterials.SOUL_INFUSED_COPPER),
            new ItemModelGenerators.TrimMaterialData(MaterialAssetGroup.create("soul_infused_gold"), NarakaTrimMaterials.SOUL_INFUSED_GOLD),
            new ItemModelGenerators.TrimMaterialData(MaterialAssetGroup.create("soul_infused_emerald"), NarakaTrimMaterials.SOUL_INFUSED_EMERALD),
            new ItemModelGenerators.TrimMaterialData(MaterialAssetGroup.create("soul_infused_diamond"), NarakaTrimMaterials.SOUL_INFUSED_DIAMOND),
            new ItemModelGenerators.TrimMaterialData(MaterialAssetGroup.create("soul_infused_lapis"), NarakaTrimMaterials.SOUL_INFUSED_LAPIS),
            new ItemModelGenerators.TrimMaterialData(MaterialAssetGroup.create("soul_infused_amethyst"), NarakaTrimMaterials.SOUL_INFUSED_AMETHYST),
            new ItemModelGenerators.TrimMaterialData(MaterialAssetGroup.create("soul_infused_nectarium"), NarakaTrimMaterials.SOUL_INFUSED_NECTARIUM),
            new ItemModelGenerators.TrimMaterialData(MaterialAssetGroup.create("god_blood"), NarakaTrimMaterials.GOD_BLOOD)
    );

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        generator.generateFlatItem(NarakaItems.STARDUST_STAFF.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        generator.generateFlatItem(NarakaItems.NARAKA_FIREBALL_STAFF.get(), ModelTemplates.FLAT_ITEM);

        generateSpear(generator, NarakaItems.SPEAR_ITEM.get(), NarakaModelLayers.SPEAR, NarakaTextures.SPEAR);
        generateSpear(generator, NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get(), NarakaModelLayers.SPEAR, NarakaTextures.MIGHTY_HOLY_SPEAR);
        generateSpearOfLonginus(generator, NarakaItems.SPEAR_OF_LONGINUS_ITEM.get());

        generator.generateFlatItem(NarakaItems.SKILL_CONTROLLER.get(), Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.ANIMATION_CONTROLLER.get(), Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.GOD_BLOOD.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.NECTARIUM.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.PURIFIED_SOUL_METAL.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.PURIFIED_SOUL_SHARD.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.PURIFIED_SOUL_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        generator.generateFlatItem(NarakaItems.RAINBOW_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        generator.generateFlatItem(NarakaItems.HEROBRINE_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);

        generateTrimmableItem(generator, Items.LEATHER_HELMET, EquipmentAssets.LEATHER, "helmet", true);
        generateTrimmableItem(generator, Items.LEATHER_CHESTPLATE, EquipmentAssets.LEATHER, "chestplate", true);
        generateTrimmableItem(generator, Items.LEATHER_LEGGINGS, EquipmentAssets.LEATHER, "leggings", true);
        generateTrimmableItem(generator, Items.LEATHER_BOOTS, EquipmentAssets.LEATHER, "boots", true);
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

        generateTrimmableScarfItem(generator, NarakaItems.PURIFIED_SOUL_CHESTPLATE.get(), NarakaEquipmentAssets.PURIFIED_SOUL);
        generateTrimmableItem(generator, NarakaItems.PURIFIED_SOUL_HELMET.get(), NarakaEquipmentAssets.PURIFIED_SOUL, "helmet");
        generateTrimmableItem(generator, NarakaItems.PURIFIED_SOUL_LEGGINGS.get(), NarakaEquipmentAssets.PURIFIED_SOUL, "leggings");
        generateTrimmableItem(generator, NarakaItems.PURIFIED_SOUL_BOOTS.get(), NarakaEquipmentAssets.PURIFIED_SOUL, "boots");

        generator.generateFlatItem(NarakaItems.STIGMA_ROD.get(), ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
        generator.generateFlatItem(NarakaItems.HEROBRINE_PHASE_1_DISC.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.HEROBRINE_PHASE_2_DISC.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.HEROBRINE_PHASE_3_DISC.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.HEROBRINE_PHASE_4_DISC.get(), ModelTemplates.FLAT_ITEM);

        generator.itemModelOutput.accept(NarakaItems.NARAKA_PICKAXE.get(), ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(NarakaItems.NARAKA_PICKAXE.get())));
        generator.generateFlatItem(NarakaItems.HEROBRINE_SCARF.get(), ModelTemplates.FLAT_ITEM);

        NarakaItems.forEachSoulInfusedItem(item -> generator.generateFlatItem(item, ModelTemplates.FLAT_ITEM));
        NarakaItems.forEachSoulInfusedSword(item -> generator.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM));

        generateSanctuaryCompassItem(generator, NarakaItems.SANCTUARY_COMPASS.get());
        generator.declareCustomModelItem(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get().asItem());
    }

    public static void generateSpear(ItemModelGenerators generator, SpearItem spearItem, ModelLayerLocation modelLayer, Identifier texture) {
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
        generateTrimmableItem(generator, item, key, name, false);
    }

    public static void generateTrimmableScarfItem(ItemModelGenerators generator, Item item, ResourceKey<EquipmentAsset> key) {
        ItemModel.Unbaked defaultModel = generateTrimmableItemModel(generator, item, key, "chestplate", false, false);
        ItemModel.Unbaked scarfModel = generateTrimmableItemModel(generator, item, key, "chestplate", false, true);
        ItemModel.Unbaked model = ItemModelUtils.conditional(
                new HasComponent(NarakaDataComponentTypes.HEROBRINE_SCARF.get(), false),
                scarfModel,
                defaultModel
        );
        generator.itemModelOutput.accept(NarakaItems.PURIFIED_SOUL_CHESTPLATE.get(), model);
    }

    public static void generateTrimmableItem(ItemModelGenerators generator, Item item, ResourceKey<EquipmentAsset> key, String name, boolean withDye) {
        generator.itemModelOutput.accept(item, generateTrimmableItemModel(generator, item, key, name, withDye, false));
    }

    public static ItemModel.Unbaked generateTrimmableItemModel(ItemModelGenerators generator, Item item, ResourceKey<EquipmentAsset> key, String name, boolean withDye, boolean withScarf) {
        Identifier modelLocation = ModelLocationUtils.getModelLocation(item);
        Identifier texture = TextureMapping.getItemTexture(item);
        Identifier overlayTexture = TextureMapping.getItemTexture(item, "_overlay");

        List<SelectItemModel.SwitchCase<ResourceKey<TrimMaterial>>> list = new ArrayList<>(TRIM_MATERIAL_MODELS.size());
        for (ItemModelGenerators.TrimMaterialData data : TRIM_MATERIAL_MODELS) {
            Identifier trimmedArmorModelLocation = modelLocation.withSuffix("_" + data.assets().base().suffix() + "_trim");
            Identifier trimTexture = NarakaMod.mcLocation("trims/items/" + name + "_trim_" + data.assets().assetId(key).suffix());
            ItemModel.Unbaked unbaked = generateLayer(generator, trimmedArmorModelLocation, texture, overlayTexture, trimTexture, withDye, withScarf);

            list.add(ItemModelUtils.when(data.materialKey(), unbaked));
        }

        ItemModel.Unbaked unbaked = generateModel(generator, modelLocation, texture, overlayTexture, withDye, withScarf);
        return ItemModelUtils.select(new TrimMaterialProperty(), unbaked, list);
    }

    private static ItemModel.Unbaked generateLayer(ItemModelGenerators generator, Identifier trimmedArmorModelLocation, Identifier texture, Identifier overlayTexture, Identifier trimTexture, boolean secondLayer, boolean withScarf) {
        if (secondLayer) {
            generator.generateLayeredItem(trimmedArmorModelLocation, texture, overlayTexture, trimTexture);
            return ItemModelUtils.tintedModel(trimmedArmorModelLocation, new Dye(-6265536));
        }
        if (withScarf) {
            trimmedArmorModelLocation = trimmedArmorModelLocation.withSuffix("_scarf");
            Identifier scarfTexture = TextureMapping.getItemTexture(NarakaItems.HEROBRINE_SCARF.get());
            generator.generateLayeredItem(trimmedArmorModelLocation, scarfTexture, texture, trimTexture);
            return ItemModelUtils.plainModel(trimmedArmorModelLocation);
        }
        generator.generateLayeredItem(trimmedArmorModelLocation, texture, trimTexture);
        return ItemModelUtils.plainModel(trimmedArmorModelLocation);
    }

    private static ItemModel.Unbaked generateModel(ItemModelGenerators generator, Identifier modelLocation, Identifier texture, Identifier overlayTexture, boolean secondLayer, boolean withScarf) {
        if (secondLayer) {
            ModelTemplates.TWO_LAYERED_ITEM.create(modelLocation, TextureMapping.layered(texture, overlayTexture), generator.modelOutput);
            return ItemModelUtils.tintedModel(modelLocation, new Dye(-6265536));
        }
        if (withScarf) {
            modelLocation = modelLocation.withSuffix("_scarf");
            Identifier scarfTexture = TextureMapping.getItemTexture(NarakaItems.HEROBRINE_SCARF.get());
            ModelTemplates.TWO_LAYERED_ITEM.create(modelLocation, TextureMapping.layered(scarfTexture, texture), generator.modelOutput);
            return ItemModelUtils.plainModel(modelLocation);
        }
        ModelTemplates.FLAT_ITEM.create(modelLocation, TextureMapping.layer0(texture), generator.modelOutput);
        return ItemModelUtils.plainModel(modelLocation);
    }

    public static void generateSanctuaryCompassItem(ItemModelGenerators generator, Item item) {
        List<RangeSelectItemModel.Entry> list = generator.createCompassModels(item);
        generator.itemModelOutput
                .accept(
                        item,
                        ItemModelUtils.rangeSelect(new CompassAngle(true, CompassAngleState.CompassTarget.NONE), 32.0F, list)
                );
    }
}
