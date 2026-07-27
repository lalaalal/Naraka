package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.init.DimensionSkyRendererRegistry;
import com.yummy.naraka.client.renderer.DimensionTypeProvider;
import com.yummy.naraka.client.renderer.HiddenOreRenderState;
import com.yummy.naraka.client.renderer.HiddenOreRenderStateProvider;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.tags.ConventionalTags;
import com.yummy.naraka.util.Color;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.util.NarakaUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LevelRenderer.class)
public abstract class HiddenOreRendererMixin {
    @Shadow
    @Final
    private SubmitNodeStorage submitNodeStorage;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private @Nullable ClientLevel level;

    @Shadow
    @Final
    private LevelRenderState levelRenderState;

    @Unique
    private static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();

    @Unique
    @Nullable
    private BlockModelResolver naraka$blockModelResolver;


    @Inject(method = "onResourceManagerReload", at = @At("RETURN"))
    private void prepareDimensionSkyRenderers(ResourceManager resourceManager, CallbackInfo ci) {
        DimensionSkyRendererRegistry.setup();
        naraka$blockModelResolver = new BlockModelResolver(minecraft.getModelManager());
    }

    @Inject(method = "extractLevel", at = @At("RETURN"))
    private void extractDimensionType(DeltaTracker deltaTracker, Camera camera, float deltaPartialTick, CallbackInfo ci) {
        if (level != null && levelRenderState.skyRenderState instanceof DimensionTypeProvider dimensionTypeProvider) {
            dimensionTypeProvider.naraka$setDimensionType(level.dimension());
        }
    }

    @Inject(method = "extractLevel", at = @At("RETURN"))
    private void extractHiddenOres(DeltaTracker deltaTracker, Camera camera, float deltaPartialTick, CallbackInfo ci) {
        if (!(levelRenderState instanceof HiddenOreRenderStateProvider dimensionTypeProvider))
            return;

        if (!(camera.entity() instanceof LivingEntity livingEntity) || !NarakaItemUtils.canApplyOreSeeThrough(livingEntity)
                || !NarakaConfig.CLIENT.enableOreSeeThrough.getValue())
            return;

        BlockPos cameraBlockPos = camera.blockPosition();
        int range = NarakaConfig.CLIENT.oreSeeThroughRange.getValue();
        Vec3i cornerOffset = new Vec3i(range, range, range);
        BoundingBox box = BoundingBox.fromCorners(cameraBlockPos.offset(cornerOffset), cameraBlockPos.offset(cornerOffset.multiply(-1)));

        NarakaUtils.sphere(box, 1, pos -> {
            if (level == null || naraka$blockModelResolver == null)
                return;
            BlockState state = level.getBlockState(pos);
            if (state.is(ConventionalTags.Blocks.ORES)) {
                Color color = NarakaConfig.ORE_COLORS.getColor(state);
                if (color.alpha() == 0)
                    return;
                HiddenOreRenderState renderState = new HiddenOreRenderState();

                naraka$blockModelResolver.update(renderState.blockModel, state, BLOCK_DISPLAY_CONTEXT);
                renderState.color = color.pack();
                renderState.pos = pos;
                dimensionTypeProvider.naraka$getHiddenOreRenderStates()
                        .add(renderState);
                levelRenderState.haveGlowingEntities = true;
            }
        });
    }

    @Inject(method = "submitBlockEntities", at = @At("HEAD"))
    private void submitHiddenOreOutlines(PoseStack poseStack, LevelRenderState levelRenderState, SubmitNodeStorage submitNodeStorage, CallbackInfo ci) {
        if (levelRenderState instanceof HiddenOreRenderStateProvider hiddenOreRenderStateProvider) {
            for (HiddenOreRenderState renderState : hiddenOreRenderStateProvider.naraka$getHiddenOreRenderStates()) {
                Vec3 position = new Vec3(renderState.pos)
                        .subtract(levelRenderState.cameraRenderState.pos);

                poseStack.pushPose();
                poseStack.scale(0.98f, 0.98f, 0.98f);
                poseStack.translate(position);
                renderState.blockModel.submit(poseStack, this.submitNodeStorage, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, renderState.color);
                poseStack.popPose();
            }
            hiddenOreRenderStateProvider.naraka$clearHiddenOreRenderStates();
        }
    }
}
