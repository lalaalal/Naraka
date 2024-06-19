package com.yummy.naraka.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.block.NarakaBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
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

    public static final ResourceLocation TEMPLATE_FIRE_FLOOR = NarakaMod.mcLocaion("template_fire_floor");
    public static final ResourceLocation TEMPLATE_FIRE_SIDE = NarakaMod.mcLocaion("template_fire_side");
    public static final ResourceLocation TEMPLATE_FIRE_SIDE_ALT = NarakaMod.mcLocaion("template_fire_side_alt");

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
        simpleBlockWithItem(NarakaBlocks.NECTARIUM_BLOCK);
        simpleBlockWithItem(NarakaBlocks.NECTARIUM_ORE);
        simpleBlockWithItem(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE);
        horizontalBlockWithStateOnOff(NarakaBlocks.SOUL_CRAFTING_BLOCK);
        simpleBlockItem(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.get(), existingModel("purified_soul_fire_side0"));
        simpleBlockItem(NarakaBlocks.SOUL_CRAFTING_BLOCK);
    }

    public ModelFile existingModel(String path) {
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
        MultiPartBlockStateBuilder builder = getMultipartBuilder(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.get());
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
