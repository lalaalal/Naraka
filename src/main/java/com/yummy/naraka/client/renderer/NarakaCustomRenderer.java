package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.entity.ForgingBlockEntity;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class NarakaCustomRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer, IdentifiableResourceReloadListener, ResourceManagerReloadListener {
    public static final NarakaCustomRenderer INSTANCE = new NarakaCustomRenderer();

    private final Minecraft minecraft;
    private final ForgingBlockEntity forgingBlockEntity = new ForgingBlockEntity(BlockPos.ZERO, NarakaBlocks.FORGING_BLOCK.defaultBlockState());
    private BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public NarakaCustomRenderer() {
        this.minecraft = Minecraft.getInstance();
        this.blockEntityRenderDispatcher = minecraft.getBlockEntityRenderDispatcher();
    }

    @Override
    public void render(ItemStack stack, ItemDisplayContext mode, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        if (stack.is(NarakaBlocks.FORGING_BLOCK.asItem())) {
            blockEntityRenderDispatcher.renderItem(forgingBlockEntity, poseStack, bufferSource, light, overlay);
        }
    }

    @Override
    public ResourceLocation getFabricId() {
        return NarakaMod.location("custom_renderer");
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        blockEntityRenderDispatcher = minecraft.getBlockEntityRenderDispatcher();
    }
}
