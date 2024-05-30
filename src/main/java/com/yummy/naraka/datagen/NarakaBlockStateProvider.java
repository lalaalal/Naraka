package com.yummy.naraka.datagen;

import com.yummy.naraka.NarakaMod;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Function;
import java.util.function.Supplier;

public class NarakaBlockStateProvider extends BlockStateProvider {
    public NarakaBlockStateProvider(PackOutput packOutput, ExistingFileHelper fileHelper) {
        super(packOutput, NarakaMod.MOD_ID, fileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }

    public void simpleBlockWithItem(Supplier<? extends Block> blockSupplier, Function<Block, ModelFile> modelFileProvider) {
        simpleBlockWithItem(blockSupplier.get(), modelFileProvider.apply(blockSupplier.get()));
    }
}
