package com.yummy.naraka.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.block.NarakaBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class NarakaBlockStateProvider extends BlockStateProvider {
    public NarakaBlockStateProvider(PackOutput packOutput, ExistingFileHelper fileHelper) {
        super(packOutput, NarakaMod.MOD_ID, fileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(NarakaBlocks.TRANSPARENT_BLOCK);
        simpleBlockWithItem(NarakaBlocks.NECTARIUM_BLOCK);
        simpleBlockWithItem(NarakaBlocks.NECTARIUM_ORE);
        simpleBlockWithItem(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE);
    }

    public void simpleBlockWithItem(Supplier<? extends Block> blockSupplier) {
        simpleBlockWithItem(blockSupplier.get(), cubeAll(blockSupplier.get()));
    }
}
