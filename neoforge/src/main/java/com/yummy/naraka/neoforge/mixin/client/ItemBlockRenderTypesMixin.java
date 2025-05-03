package com.yummy.naraka.neoforge.mixin.client;

import com.yummy.naraka.client.init.BlockRenderTypeRegistry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
@Mixin(ItemBlockRenderTypes.class)
public abstract class ItemBlockRenderTypesMixin {
    @Shadow @Final
    private static Map<Block, ChunkRenderTypeSet> BLOCK_RENDER_TYPES;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void afterInitializeClass(CallbackInfo ci) {
        BlockRenderTypeRegistry.getCustomBlockRenderTypes().forEach((block, renderType) -> {
            BLOCK_RENDER_TYPES.put(block, ChunkRenderTypeSet.of(renderType));
        });
    }
}
