package com.yummy.naraka.block;

import com.yummy.naraka.NarakaMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class NarakaBlocks {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(NarakaMod.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NarakaMod.MOD_ID);

    public static final DeferredBlock<Block> TRANSPARENT_BLOCK = registerBlockWithItem(
            "transparent_block", TransparentBlock::new, BlockBehaviour.Properties.of()
                    .noCollission()
                    .forceSolidOn()
    );

    private static <B extends Block> DeferredBlock<Block> registerBlockWithItem(String name, Function<BlockBehaviour.Properties, ? extends B> function, BlockBehaviour.Properties properties) {
        DeferredBlock<Block> blockHolder = BLOCKS.registerBlock(name, function, properties);
        ITEMS.registerSimpleBlockItem(blockHolder);
        return blockHolder;
    }

    private static DeferredBlock<Block> registerSimpleBlockWithItem(String name, BlockBehaviour.Properties properties) {
        return registerBlockWithItem(name, Block::new, properties);
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }
}
