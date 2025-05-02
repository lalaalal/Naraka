package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.client.init.SpecialModelRendererRegistry;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderers;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(SpecialModelRenderers.class)
public abstract class SpecialModelRenderersMixin {
    @Inject(method = "createBlockRenderers", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/ChestRenderer;xmasTextures()Z"))
    private static void createBlockRenderers(EntityModelSet modelSet, CallbackInfoReturnable<Map<Block, SpecialModelRenderer<?>>> cir, @Local Map<Block, SpecialModelRenderer.Unbaked> map) {
        SpecialModelRendererRegistry.forEachUnbaked(map::put);
    }
}
