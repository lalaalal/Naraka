package com.yummy.naraka.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.block.NarakaBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder.PartBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class NarakaBlockStateProvider extends BlockStateProvider {
    public static final String FIRE_FLOOR_SUFFIX = "_floor";
    public static final String FIRE_SIDE_SUFFIX = "_side";
    public static final String FIRE_SIDE__ALT_SUFFIX = "_side_alt";

    public static final ResourceLocation TEMPLATE_FIRE_FLOOR = NarakaMod.mcLocation("template_fire_floor");
    public static final ResourceLocation TEMPLATE_FIRE_SIDE = NarakaMod.mcLocation("template_fire_side");
    public static final ResourceLocation TEMPLATE_FIRE_SIDE_ALT = NarakaMod.mcLocation("template_fire_side_alt");

    public static ResourceLocation texture(String path) {
        return NarakaMod.location(ModelProvider.BLOCK_FOLDER, path);
    }

    public NarakaBlockStateProvider(PackOutput packOutput, ExistingFileHelper fileHelper) {
        super(packOutput, NarakaMod.MOD_ID, fileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        fireBlock(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK);
        simpleBlockWithItem(NarakaBlocks.TRANSPARENT_BLOCK);
        simpleBlockWithItem(NarakaBlocks.NECTARIUM_ORE);
        simpleBlockWithItem(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE);
        horizontalBlockWithStateOnOff(NarakaBlocks.SOUL_CRAFTING_BLOCK);

        itemModels().getBuilder(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.getId().getPath())
                .parent(existingBlockModel("purified_soul_fire_side0"))
                .transforms()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                .translation(0, 0, 8)
                .end()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                .translation(0, 0, 8);
        simpleBlockItem(NarakaBlocks.SOUL_CRAFTING_BLOCK);
        simpleBlockWithItem(NarakaBlocks.NECTARIUM_BLOCK);
        simpleBlockWithItem(NarakaBlocks.COMPRESSED_IRON_BLOCK);
        simpleBlockWithItem(NarakaBlocks.FAKE_GOLD_BLOCK);

        NarakaBlocks.forEachSoulInfusedBlockHolder(this::simpleBlockWithItem);
        simpleBlockWithItem(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK);
        simpleBlockWithItem(NarakaBlocks.PURIFIED_SOUL_BLOCK);

        logBlockWithItem(NarakaBlocks.EBONY_LOG);
        logBlockWithItem(NarakaBlocks.STRIPPED_EBONY_LOG);
        woodBlockWithItem(NarakaBlocks.EBONY_WOOD, NarakaBlocks.EBONY_LOG);
        woodBlockWithItem(NarakaBlocks.STRIPPED_EBONY_WOOD, NarakaBlocks.STRIPPED_EBONY_LOG);
        parentModelBlockWithItem(NarakaBlocks.EBONY_LEAVES, "leaves", "all");

        signBlock(NarakaBlocks.EBONY_SIGN, texture("ebony_planks"));
        signBlock(NarakaBlocks.EBONY_WALL_SIGN, texture("ebony_planks"));
        signBlock(NarakaBlocks.EBONY_HANGING_SIGN, texture("ebony_planks"));
        signBlock(NarakaBlocks.EBONY_WALL_HANGING_SIGN, texture("ebony_planks"));
        parentCustomRenderTypeModelBlock(NarakaBlocks.EBONY_SAPLING, "cross", "cross", "cutout");
        simpleItem(NarakaBlocks.EBONY_SAPLING, "block");
        simpleBlockWithItem(NarakaBlocks.EBONY_PLANKS);
        simpleBlockWithItem(NarakaBlocks.HARD_EBONY_PLANKS);
        slabBlock(NarakaBlocks.EBONY_SLAB.get(), texture("ebony_planks"), texture("ebony_planks"));
        simpleBlockItem(NarakaBlocks.EBONY_SLAB);
        stairsBlock(NarakaBlocks.EBONY_STAIRS.get(), texture("ebony_planks"));
        simpleBlockItem(NarakaBlocks.EBONY_STAIRS);
        fenceBlock(NarakaBlocks.EBONY_FENCE.get(), texture("ebony_planks"));
        itemWithInventoryModel(NarakaBlocks.EBONY_FENCE, "fence", texture("ebony_planks"));
        fenceGateBlock(NarakaBlocks.EBONY_FENCE_GATE.get(), texture("ebony_planks"));
        simpleBlockItem(NarakaBlocks.EBONY_FENCE_GATE);
        doorBlock(NarakaBlocks.EBONY_DOOR.get(), texture("ebony_door_bottom"), texture("ebony_door_top"));
        simpleItem(NarakaBlocks.EBONY_DOOR, "item");
        trapdoorBlock(NarakaBlocks.EBONY_TRAPDOOR.get(), texture("ebony_trapdoor"), true);
        itemModels().withExistingParent(NarakaBlocks.EBONY_TRAPDOOR.getId().getPath(), NarakaMod.location("block", "ebony_trapdoor_bottom"));
        pressurePlateBlock(NarakaBlocks.EBONY_PRESSURE_PLATE.get(), texture("ebony_planks"));
        simpleBlockItem(NarakaBlocks.EBONY_PRESSURE_PLATE);
        buttonBlock(NarakaBlocks.EBONY_BUTTON.get(), texture("ebony_planks"));
        itemWithInventoryModel(NarakaBlocks.EBONY_BUTTON, "button", texture("ebony_planks"));
    }

    public void itemWithInventoryModel(DeferredBlock<? extends Block> block, String type, ResourceLocation texture) {
        String name = block.getId().getPath();
        String parentName = "block/" + type + "_inventory";
        itemModels().withExistingParent(name, parentName)
                .texture("texture", texture);
    }

    public void simpleItem(DeferredBlock<? extends Block> block, String prefix) {
        String name = block.getId().getPath();
        itemModels().withExistingParent(name, "item/generated")
                .texture("layer0", NarakaMod.location(prefix, name));
    }

    public void parentModelBlockWithItem(DeferredBlock<? extends Block> block, String parent, String textureKey) {
        blockWithItem(
                block,
                modelWithExistingParent(block, parent)
                        .texture(textureKey, blockTexture(block.get()))
        );
    }

    public void parentCustomRenderTypeModelBlock(DeferredBlock<? extends Block> block, String parent, String textureKey, String renderType) {
        simpleBlock(
                block.get(),
                modelWithExistingParent(block, parent)
                        .texture(textureKey, blockTexture(block.get()))
                        .renderType(renderType)
        );
    }

    public void parentCustomRenderTypeModelBlockWithItem(DeferredBlock<? extends Block> block, String parent, String textureKey, String renderType) {
        blockWithItem(
                block,
                modelWithExistingParent(block, parent)
                        .texture(textureKey, blockTexture(block.get()))
                        .renderType(renderType)
        );
    }

    public void blockWithItem(DeferredBlock<? extends Block> block, ModelFile model) {
        simpleBlock(block.get(), model);
        simpleBlockItem(block);
    }

    public BlockModelBuilder modelWithExistingParent(DeferredBlock<? extends Block> block, String parent) {
        return models().withExistingParent(block.getId().getPath(), parent);
    }

    public void signBlock(DeferredBlock<? extends SignBlock> signBlock, ResourceLocation texture) {
        ModelFile sign = models().sign(signBlock.getId().getPath(), texture);
        simpleBlock(signBlock.get(), sign);
    }

    public void logBlockWithItem(DeferredBlock<? extends RotatedPillarBlock> log) {
        logBlock(log.get());
        simpleBlockItem(log);
    }

    public void woodBlockWithItem(DeferredBlock<? extends RotatedPillarBlock> wood, DeferredBlock<? extends RotatedPillarBlock> log) {
        axisBlock(wood.get(), blockTexture(log.get()), blockTexture(log.get()));
        simpleBlockItem(wood);
    }

    public ModelFile existingBlockModel(String path) {
        return models().getExistingFile(NarakaMod.location(path));
    }

    public void simpleBlockItem(DeferredBlock<? extends Block> block) {
        String name = block.getId().getPath();
        ModelFile modelFile = models().getExistingFile(NarakaMod.location(name));
        itemModels().getBuilder(name).parent(modelFile);
    }

    public void simpleBlockWithItem(DeferredBlock<? extends Block> blockSupplier) {
        simpleBlockWithItem(blockSupplier.get(), cubeAll(blockSupplier.get()));
    }

    private ModelFile horizontalModelWithStateOnOff(BlockState state, DeferredBlock<? extends Block> block) {
        String name = block.getId().getPath();
        if (state.getValue(BlockStateProperties.LIT))
            return models().orientable(name, texture(name + "_side"), texture(name + "_front_on"), texture(name + "_top"));
        return models().orientable(name, texture(name + "_side"), texture(name + "_front"), texture(name + "_top"));
    }

    public void horizontalBlockWithStateOnOff(DeferredBlock<? extends Block> block) {
        horizontalBlock(block.get(), blockState -> horizontalModelWithStateOnOff(blockState, block), 180);
    }

    public void fireBlock(DeferredBlock<? extends BaseFireBlock> block) {
        String name = block.getId().getPath();
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block.get());
        builder.part()
                .modelFile(firePartModelFile(name, FIRE_FLOOR_SUFFIX, 0, TEMPLATE_FIRE_FLOOR))
                .nextModel()
                .modelFile(firePartModelFile(name, FIRE_FLOOR_SUFFIX, 1, TEMPLATE_FIRE_FLOOR))
                .addModel()
                .end();
        firePartSide(builder.part(), name, 0);
        firePartSide(builder.part(), name, 90);
        firePartSide(builder.part(), name, 180);
        firePartSide(builder.part(), name, 270);
    }

    private ModelFile firePartModelFile(String name, String suffix, int id, ResourceLocation parent) {
        String modelFileName = name + suffix + id;
        String textureFileName = name + "_" + id;
        return models()
                .withExistingParent(modelFileName, parent)
                .texture("fire", texture(textureFileName))
                .renderType("cutout");
    }

    private void firePartSide(ConfiguredModel.Builder<PartBuilder> builder, String name, int rotationY) {
        builder.rotationY(rotationY)
                .modelFile(firePartModelFile(name, FIRE_SIDE_SUFFIX, 0, TEMPLATE_FIRE_SIDE))
                .nextModel()
                .rotationY(rotationY)
                .modelFile(firePartModelFile(name, FIRE_SIDE_SUFFIX, 1, TEMPLATE_FIRE_SIDE))
                .nextModel()
                .rotationY(rotationY)
                .modelFile(firePartModelFile(name, FIRE_SIDE__ALT_SUFFIX, 0, TEMPLATE_FIRE_SIDE_ALT))
                .nextModel()
                .rotationY(rotationY)
                .modelFile(firePartModelFile(name, FIRE_SIDE__ALT_SUFFIX, 1, TEMPLATE_FIRE_SIDE_ALT))
                .addModel();
    }
}
