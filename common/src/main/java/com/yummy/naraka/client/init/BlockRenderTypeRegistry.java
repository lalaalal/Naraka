package com.yummy.naraka.client.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public abstract class BlockRenderTypeRegistry {
    private static final Map<Block, ChunkSectionLayer> CUSTOM_BLOCK_RENDER_TYPES = new HashMap<>();

    public static void register(ChunkSectionLayer chunkSectionLayer, Block... blocks) {
        for (Block block : blocks)
            CUSTOM_BLOCK_RENDER_TYPES.put(block, chunkSectionLayer);
    }

    public static Map<Block, ChunkSectionLayer> getCustomBlockRenderTypes() {
        return Map.copyOf(CUSTOM_BLOCK_RENDER_TYPES);
    }
}
