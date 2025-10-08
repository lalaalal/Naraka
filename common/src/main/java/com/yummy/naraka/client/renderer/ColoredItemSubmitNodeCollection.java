package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public interface ColoredItemSubmitNodeCollection {
    void naraka$submitColoredItem(
            PoseStack poseStack,
            ItemDisplayContext displayContext,
            int lightCoords,
            int overlayCoords,
            int color,
            int outlineColor,
            int[] tintLayers,
            List<BakedQuad> quads,
            RenderType renderType,
            ItemStackRenderState.FoilType foilType
    );

    Map<SubmitNodeStorage.ItemSubmit, Integer> naraka$getColoredItemSubmits();
}
