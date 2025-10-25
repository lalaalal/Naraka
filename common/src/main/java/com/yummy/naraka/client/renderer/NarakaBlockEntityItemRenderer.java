package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.entity.SoulSmithingBlockEntity;
import com.yummy.naraka.world.block.entity.SoulStabilizerBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class NarakaBlockEntityItemRenderer implements CustomRenderManager.CustomItemRenderer, ResourceManagerReloadListener {
    public static final NarakaBlockEntityItemRenderer INSTANCE = new NarakaBlockEntityItemRenderer();

    private final Minecraft minecraft;

    private Map<Item, BlockEntity> entityByItem = Map.of();
    private Map<Item, CompoundTag> defaultDataByItem = Map.of();
    private Map<Item, Float> scales = Map.of();
    private Map<Item, Vec3> translations = Map.of();

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
        Item item = stack.getItem();
        if (entityByItem.containsKey(item)) {
            poseStack.pushPose();

            BlockEntity blockEntity = entityByItem.get(item);
            CompoundTag defaultData = defaultDataByItem.getOrDefault(item, new CompoundTag());
            Vec3 translation = translations.getOrDefault(item, Vec3.ZERO);
            float scale = scales.getOrDefault(item, 1f);
            poseStack.translate(translation.x, translation.y, translation.z);
            poseStack.scale(scale, scale, scale);
            NarakaItemUtils.loadBlockEntity(stack, blockEntity, defaultData, RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY));
            blockEntityRenderDispatcher.renderItem(blockEntity, poseStack, bufferSource, light, overlay);
            poseStack.popPose();
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        blockEntityRenderDispatcher = minecraft.getBlockEntityRenderDispatcher();
        SoulStabilizerBlockEntity soulStabilizerBlockEntity = new SoulStabilizerBlockEntity(BlockPos.ZERO, NarakaBlocks.SOUL_STABILIZER.get().defaultBlockState());
        SoulSmithingBlockEntity soulSmithingBlockEntity = new SoulSmithingBlockEntity(BlockPos.ZERO, NarakaBlocks.SOUL_SMITHING_BLOCK.get().defaultBlockState());

        entityByItem = Map.of(
                NarakaBlocks.SOUL_STABILIZER.get().asItem(), soulStabilizerBlockEntity,
                NarakaBlocks.SOUL_SMITHING_BLOCK.get().asItem(), soulSmithingBlockEntity
        );
        RegistryAccess registries = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
        defaultDataByItem = Map.of(
                NarakaBlocks.SOUL_STABILIZER.get().asItem(), soulStabilizerBlockEntity.getUpdateTag(registries)
        );
        scales = Map.of(
                NarakaBlocks.SOUL_STABILIZER.get().asItem(), 2.75f
        );
        translations = Map.of(
                NarakaBlocks.SOUL_STABILIZER.get().asItem(), new Vec3(-0.88, 0, -0.88)
        );
    }
}
