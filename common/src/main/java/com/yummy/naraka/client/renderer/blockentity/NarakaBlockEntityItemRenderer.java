package com.yummy.naraka.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.renderer.CustomRenderManager;
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
public class NarakaBlockEntityItemRenderer implements CustomRenderManager.CustomItemRenderer, ResourceManagerReloadListener {
    public static final NarakaBlockEntityItemRenderer INSTANCE = new NarakaBlockEntityItemRenderer();

    private final Minecraft minecraft;

    private Map<Item, BlockEntity> entityByItem = Map.of();

    private BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public NarakaBlockEntityItemRenderer() {
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
            float partialTicks = minecraft.getDeltaTracker().getGameTimeDeltaTicks();
            blockEntityRenderDispatcher.render(blockEntity, partialTicks, poseStack, bufferSource);
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        blockEntityRenderDispatcher = minecraft.getBlockEntityRenderDispatcher();
        ForgingBlockEntity forgingBlockEntity = new ForgingBlockEntity(BlockPos.ZERO, NarakaBlocks.FORGING_BLOCK.get().defaultBlockState());
        SoulStabilizerBlockEntity soulStabilizerBlockEntity = new SoulStabilizerBlockEntity(BlockPos.ZERO, NarakaBlocks.SOUL_STABILIZER.get().defaultBlockState());
        SoulSmithingBlockEntity soulSmithingBlockEntity = new SoulSmithingBlockEntity(BlockPos.ZERO, NarakaBlocks.SOUL_SMITHING_BLOCK.get().defaultBlockState());

        entityByItem = Map.of(
                NarakaBlocks.FORGING_BLOCK.get().asItem(), forgingBlockEntity,
                NarakaBlocks.SOUL_STABILIZER.get().asItem(), soulStabilizerBlockEntity,
                NarakaBlocks.SOUL_SMITHING_BLOCK.get().asItem(), soulSmithingBlockEntity
        );
    }
}
