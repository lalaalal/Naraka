package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.yummy.naraka.client.renderer.ItemRenderRegistry;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Collection;

@Mixin(CuboidItemModelWrapper.class)
public abstract class CuboidItemModelWrapperMixin {
    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Ljava/util/List;addAll(Ljava/util/Collection;)Z"))
    private Collection<BakedQuad> updateRenderType(Collection<BakedQuad> original, @Local(argsOnly = true, name = "output") ItemStackRenderState output, @Local(argsOnly = true, name = "item") ItemStack item) {
        if (ItemRenderRegistry.hasRenderTypeOverride(item)) {
            RenderType renderType = ItemRenderRegistry.getRenderType(item);
            output.setAnimated();
            return original.stream()
                    .map(quad -> naraka$modifyRenderType(quad, renderType))
                    .toList();
        }
        return original;
    }

    @Unique
    private BakedQuad naraka$modifyRenderType(BakedQuad quad, RenderType renderType) {
        BakedQuad.MaterialInfo materialInfo = quad.materialInfo();
        ChunkSectionLayer layer = naraka$selectLayer(renderType, materialInfo.layer());

        BakedQuad.MaterialInfo modified = new BakedQuad.MaterialInfo(materialInfo.sprite(), layer, renderType, materialInfo.tintIndex(), materialInfo.shade(), materialInfo.lightEmission());
        return new BakedQuad(
                quad.position0(), quad.position1(), quad.position2(), quad.position3(),
                quad.packedUV0(), quad.packedUV1(), quad.packedUV2(), quad.packedUV3(),
                quad.direction(), modified
        );
    }

    @Unique
    private ChunkSectionLayer naraka$selectLayer(RenderType renderType, ChunkSectionLayer defaultLayer) {
        ColorTargetState colorTargetState = renderType.pipeline().getColorTargetState();
        return colorTargetState.blendFunction()
                .map(function -> function.equals(BlendFunction.TRANSLUCENT) ? ChunkSectionLayer.TRANSLUCENT : defaultLayer)
                .orElse(defaultLayer);
    }
}
