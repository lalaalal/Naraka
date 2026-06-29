package com.yummy.naraka.forge.mixin.client;

import com.yummy.naraka.client.init.BlockRenderTypeRegistry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.registries.ForgeRegistries;
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
    @Shadow
    @Final
    private static Map<Holder.Reference<Block>, ChunkRenderTypeSet> BLOCK_RENDER_TYPES;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void afterInitializeClass(CallbackInfo ci) {
        BlockRenderTypeRegistry.getCustomBlockRenderTypes().forEach((block, renderType) -> {
            BLOCK_RENDER_TYPES.put(ForgeRegistries.BLOCKS.getDelegateOrThrow(block), ChunkRenderTypeSet.of(renderType));
        });
    }
}
