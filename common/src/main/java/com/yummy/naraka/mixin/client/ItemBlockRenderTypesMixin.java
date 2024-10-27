package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.renderer.CustomRenderManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(ItemBlockRenderTypes.class)
public abstract class ItemBlockRenderTypesMixin {
    @Shadow
    @Final
    private static Map<Block, RenderType> TYPE_BY_BLOCK;

    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void afterInitializeClass(CallbackInfo ci) {
        TYPE_BY_BLOCK.putAll(CustomRenderManager.getCustomBlockRenderTypes());
    }
}
