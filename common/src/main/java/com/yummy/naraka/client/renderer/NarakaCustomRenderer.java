package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.entity.ForgingBlockEntity;
import com.yummy.naraka.world.block.entity.SoulSmithingBlockEntity;
import com.yummy.naraka.world.block.entity.SoulStabilizerBlockEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class NarakaCustomRenderer implements CustomRenderManager.CustomItemRenderer, ResourceManagerReloadListener {
    public static final NarakaCustomRenderer INSTANCE = new NarakaCustomRenderer();

    private final Minecraft minecraft;

    private final ForgingBlockEntity FORGING_BLOCK_ENTITY = new ForgingBlockEntity(BlockPos.ZERO, NarakaBlocks.FORGING_BLOCK.get().defaultBlockState());
    private final SoulStabilizerBlockEntity SOUL_STABILIZER_BLOCK_ENTITY = new SoulStabilizerBlockEntity(BlockPos.ZERO, NarakaBlocks.SOUL_STABILIZER.get().defaultBlockState());
    private final SoulSmithingBlockEntity SOUL_SMITHING_BLOCK_ENTITY = new SoulSmithingBlockEntity(BlockPos.ZERO, NarakaBlocks.SOUL_SMITHING_BLOCK.get().defaultBlockState());

    private final Map<Item, BlockEntity> entityByItem = Map.of(
        NarakaBlocks.FORGING_BLOCK.get().asItem(), FORGING_BLOCK_ENTITY,
        NarakaBlocks.SOUL_STABILIZER.get().asItem(), SOUL_STABILIZER_BLOCK_ENTITY,
        NarakaBlocks.SOUL_SMITHING_BLOCK.get().asItem(), SOUL_SMITHING_BLOCK_ENTITY
    );

    private BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public NarakaCustomRenderer() {
        this.minecraft = Minecraft.getInstance();
        this.blockEntityRenderDispatcher = minecraft.getBlockEntityRenderDispatcher();
    }

    @Override
    public boolean shouldRenderCustom(ItemStack stack, ItemDisplayContext context) {
        return true;
    }

    @Override
    public void render(ItemStack stack, ItemDisplayContext mode, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        if (entityByItem.containsKey(stack.getItem())) {
            BlockEntity blockEntity = entityByItem.get(stack.getItem());
            blockEntityRenderDispatcher.renderItem(blockEntity, poseStack, bufferSource, light, overlay);
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        blockEntityRenderDispatcher = minecraft.getBlockEntityRenderDispatcher();
    }
}
