package com.yummy.naraka.fabric.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.block.HerobrineTotem;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NarakaModelProvider extends FabricModelProvider {
    private static final ModelTemplate BLOCK_ENTITY_ITEM = new ModelTemplate(
            Optional.of(NarakaMod.location("item", "template_block_entity")),
            Optional.empty(),
            TextureSlot.PARTICLE
    );

    public NarakaModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        generator.skipAutoItemBlock(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.get());
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
        createBlockEntityModels(generator, Blocks.GLASS, NarakaBlocks.SOUL_STABILIZER.get());
        createBlockEntityModels(generator, Blocks.STONE, NarakaBlocks.SOUL_SMITHING_BLOCK.get());

        createNectariumCrystal(generator);
        generator.createTrivialCube(NarakaBlocks.NECTARIUM_CORE_BLOCK.get());
        generator.createTrivialCube(NarakaBlocks.AMETHYST_ORE.get());
        generator.createTrivialCube(NarakaBlocks.DEEPSLATE_AMETHYST_ORE.get());
        generator.createTrivialCube(NarakaBlocks.PURIFIED_SOUL_LAMP.get());
        generator.createTrivialCube(NarakaBlocks.PURIFIED_SOUL_LANTERN.get());
        generator.createAirLikeBlock(NarakaBlocks.DIAMOND_GOLEM_SPAWNER.get(), Items.DIAMOND);
        generator.skipAutoItemBlock(NarakaBlocks.NARAKA_PORTAL.get());
        createBlockEntityModels(generator, Blocks.OBSIDIAN, NarakaBlocks.NARAKA_PORTAL.get());
    }

    private static void createBlockEntityModels(BlockModelGenerators generator, Block particle, Block... blocks) {
        for (Block block : blocks) {
            BLOCK_ENTITY_ITEM.create(ModelLocationUtils.getModelLocation(block.asItem()), TextureMapping.particle(particle), generator.modelOutput);
            generator.blockEntityModels(block, particle).create(block);
        }
    }

    private static void createNectariumCrystal(BlockModelGenerators generator) {
        generator.skipAutoItemBlock(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get());
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

    private static final List<ItemModelGenerators.TrimModelData> TRIM_MODELS = List.of(
            new ItemModelGenerators.TrimModelData("quartz", 0.052f, Map.of()),
            new ItemModelGenerators.TrimModelData("iron", 0.104f, Map.of(ArmorMaterials.IRON, "iron_darker")),
            new ItemModelGenerators.TrimModelData("netherite", 0.156f, Map.of(ArmorMaterials.NETHERITE, "netherite_darker")),
            new ItemModelGenerators.TrimModelData("redstone", 0.208f, Map.of()),
            new ItemModelGenerators.TrimModelData("copper", 0.26f, Map.of()),
            new ItemModelGenerators.TrimModelData("gold", 0.312f, Map.of(ArmorMaterials.GOLD, "gold_darker")),
            new ItemModelGenerators.TrimModelData("emerald", 0.364f, Map.of()),
            new ItemModelGenerators.TrimModelData("diamond", 0.416f, Map.of(ArmorMaterials.DIAMOND, "diamond_darker")),
            new ItemModelGenerators.TrimModelData("lapis", 0.468f, Map.of()),
            new ItemModelGenerators.TrimModelData("amethyst", 0.52f, Map.of()),
            new ItemModelGenerators.TrimModelData("soul_infused_redstone", 0.572f, Map.of()),
            new ItemModelGenerators.TrimModelData("soul_infused_copper", 0.624f, Map.of()),
            new ItemModelGenerators.TrimModelData("soul_infused_gold", 0.676f, Map.of()),
            new ItemModelGenerators.TrimModelData("soul_infused_emerald", 0.728f, Map.of()),
            new ItemModelGenerators.TrimModelData("soul_infused_diamond", 0.780f, Map.of()),
            new ItemModelGenerators.TrimModelData("soul_infused_lapis", 0.832f, Map.of()),
            new ItemModelGenerators.TrimModelData("soul_infused_amethyst", 0.884f, Map.of()),
            new ItemModelGenerators.TrimModelData("soul_infused_nectarium", 0.935f, Map.of()),
            new ItemModelGenerators.TrimModelData("god_blood", 1.0f, Map.of())
    );

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
        List<Item> armorItems = List.of(
                Items.LEATHER_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.IRON_CHESTPLATE, Items.GOLDEN_CHESTPLATE, Items.DIAMOND_CHESTPLATE, Items.NETHERITE_CHESTPLATE,
                Items.LEATHER_HELMET, Items.CHAINMAIL_HELMET, Items.TURTLE_HELMET, Items.IRON_HELMET, Items.GOLDEN_HELMET, Items.DIAMOND_HELMET, Items.NETHERITE_HELMET,
                Items.LEATHER_LEGGINGS, Items.CHAINMAIL_LEGGINGS, Items.IRON_LEGGINGS, Items.GOLDEN_LEGGINGS, Items.DIAMOND_LEGGINGS, Items.NETHERITE_LEGGINGS,
                Items.LEATHER_BOOTS, Items.CHAINMAIL_BOOTS, Items.IRON_BOOTS, Items.GOLDEN_BOOTS, Items.DIAMOND_BOOTS, Items.NETHERITE_BOOTS,
                NarakaItems.PURIFIED_SOUL_HELMET.get(), NarakaItems.PURIFIED_SOUL_CHESTPLATE.get(), NarakaItems.PURIFIED_SOUL_LEGGINGS.get(), NarakaItems.PURIFIED_SOUL_BOOTS.get()
        );

        generator.generateFlatItem(NarakaItems.STARDUST_STAFF.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        generator.generateFlatItem(NarakaItems.SPEAR_ITEM.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.GOD_BLOOD.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.NECTARIUM.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.PURIFIED_SOUL_METAL.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.PURIFIED_SOUL_SHARD.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.PURIFIED_SOUL_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        generator.generateFlatItem(NarakaItems.RAINBOW_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        generator.generateFlatItem(NarakaItems.HEROBRINE_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.HEROBRINE_PICKAXE.get(), ModelTemplates.FLAT_ITEM);

        for (Item item : armorItems) {
            if (item instanceof ArmorItem armorItem)
                generateArmorTrims(generator, armorItem);
        }
        generator.generateFlatItem(NarakaItems.STIGMA_ROD.get(), ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
        generator.generateFlatItem(NarakaItems.HEROBRINE_PHASE_1_DISC.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.HEROBRINE_PHASE_2_DISC.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.HEROBRINE_PHASE_3_DISC.get(), ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.HEROBRINE_PHASE_4_DISC.get(), ModelTemplates.FLAT_ITEM);

        generator.generateFlatItem(NarakaItems.ANIMATION_CONTROLLER.get(), Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.SKILL_CONTROLLER.get(), Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, ModelTemplates.FLAT_ITEM);
        generator.generateFlatItem(NarakaItems.NARAKA_FIREBALL_STAFF.get(), ModelTemplates.FLAT_HANDHELD_ROD_ITEM);

        NarakaItems.forEachSoulInfusedItem(item -> generator.generateFlatItem(item, ModelTemplates.FLAT_ITEM));
        NarakaItems.forEachSoulInfusedSword(item -> generator.generateFlatItem(item, ModelTemplates.FLAT_HANDHELD_ITEM));
        generator.generateCompassItem(NarakaItems.SANCTUARY_COMPASS.get());
    }

    public static void generateArmorTrims(ItemModelGenerators generator, ArmorItem armorItem) {
        ResourceLocation modelLocation = ModelLocationUtils.getModelLocation(armorItem);
        ResourceLocation texture = TextureMapping.getItemTexture(armorItem);
        ResourceLocation textureOverlay = TextureMapping.getItemTexture(armorItem, "_overlay");
        if (armorItem.getMaterial().is(ArmorMaterials.LEATHER.unwrapKey().orElseThrow())) {
            ModelTemplates.TWO_LAYERED_ITEM
                    .create(
                            modelLocation,
                            TextureMapping.layered(texture, textureOverlay),
                            generator.output,
                            (resourceLocation, map) -> generator.generateBaseArmorTrimTemplate(resourceLocation, map, armorItem.getMaterial())
                    );
        } else {
            ModelTemplates.FLAT_ITEM
                    .create(
                            modelLocation,
                            TextureMapping.layer0(texture),
                            generator.output,
                            (resourceLocation, map) -> generateBaseArmorTrimTemplate(generator, resourceLocation, map, armorItem.getMaterial())
                    );
        }

        for (ItemModelGenerators.TrimModelData trimModelData : TRIM_MODELS) {
            String materialName = trimModelData.name(armorItem.getMaterial());
            ResourceLocation trimMaterialModel = generator.getItemModelForTrimMaterial(modelLocation, materialName);
            String armorName = armorItem.getType().getName();
            String trimmedArmorName = armorName + "_trim_" + materialName;
            ResourceLocation trimmedArmorModel = NarakaMod.mcLocation(trimmedArmorName).withPrefix("trims/items/");
            if (armorItem.getMaterial().is(ArmorMaterials.LEATHER.unwrapKey().orElseThrow())) {
                generator.generateLayeredItem(trimMaterialModel, texture, textureOverlay, trimmedArmorModel);
            } else {
                generator.generateLayeredItem(trimMaterialModel, texture, trimmedArmorModel);
            }
        }
    }

    public static JsonObject generateBaseArmorTrimTemplate(ItemModelGenerators generator, ResourceLocation modelLocation, Map<TextureSlot, ResourceLocation> modelGetter, Holder<ArmorMaterial> armorMaterial) {
        JsonObject jsonObject = ModelTemplates.TWO_LAYERED_ITEM.createBaseTemplate(modelLocation, modelGetter);
        JsonArray overrides = new JsonArray();

        for (ItemModelGenerators.TrimModelData trimModelData : TRIM_MODELS) {
            JsonObject predicate = new JsonObject();
            JsonObject model = new JsonObject();
            model.addProperty(ItemModelGenerators.TRIM_TYPE_PREDICATE_ID.getPath(), trimModelData.itemModelIndex());
            predicate.add("predicate", model);
            predicate.addProperty("model", generator.getItemModelForTrimMaterial(modelLocation, trimModelData.name(armorMaterial)).toString());
            overrides.add(predicate);
        }

        jsonObject.add("overrides", overrides);
        return jsonObject;
    }
}
