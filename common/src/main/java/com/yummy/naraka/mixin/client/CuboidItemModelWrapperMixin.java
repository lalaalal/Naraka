package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.yummy.naraka.client.renderer.ItemRenderRegistry;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.ItemQuads;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;

@Mixin(CuboidItemModelWrapper.class)
public abstract class CuboidItemModelWrapperMixin {
    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState$LayerRenderState;setQuads(Lnet/minecraft/client/resources/model/geometry/ItemQuads;)V"))
    private ItemQuads updateRenderType(ItemQuads quads, @Local(argsOnly = true, name = "output") ItemStackRenderState output, @Local(argsOnly = true, name = "item") ItemStack item) {
        if (ItemRenderRegistry.hasRenderTypeOverride(item)) {
            RenderType renderType = ItemRenderRegistry.getRenderType(item);
            output.setAnimated();
            return ItemQuads.split(quads.all().stream()
                    .map(quad -> naraka$modifyRenderType(quad, renderType))
                    .toList());
        }
        return quads;
    }

    @Unique
    private BakedQuad naraka$modifyRenderType(BakedQuad quad, RenderType renderType) {
        BakedQuad.MaterialInfo materialInfo = quad.materialInfo();
        ChunkSectionLayer layer = naraka$selectLayer(renderType, materialInfo.layer());

        BakedQuad.MaterialInfo modified = new BakedQuad.MaterialInfo(materialInfo.sprite(), layer, renderType, materialInfo.itemGlintRenderType(), materialInfo.itemGlintSpecialRenderType(), materialInfo.tintIndex(), materialInfo.shadeDirectionOverride(), materialInfo.lightEmission());
        return new BakedQuad(
                quad.position0(), quad.position1(), quad.position2(), quad.position3(),
                quad.packedUV0(), quad.packedUV1(), quad.packedUV2(), quad.packedUV3(),
                quad.direction(), modified
        );
    }

    @Unique
    private ChunkSectionLayer naraka$selectLayer(RenderType renderType, ChunkSectionLayer defaultLayer) {
        List<@Nullable ColorTargetState> colorTargetStates = renderType.pipeline().getColorTargetStates();
        if (colorTargetStates.isEmpty())
            return defaultLayer;
        for (ColorTargetState colorTargetState : colorTargetStates) {
            if (colorTargetState != null && colorTargetState.blendFunction()
                    .filter(function -> function.equals(BlendFunction.TRANSLUCENT))
                    .isPresent())
                return ChunkSectionLayer.TRANSLUCENT;
        }
        return defaultLayer;
    }
}
