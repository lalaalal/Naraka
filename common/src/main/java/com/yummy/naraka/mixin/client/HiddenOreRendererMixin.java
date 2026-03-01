package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.tags.ConventionalTags;
import com.yummy.naraka.util.Color;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.util.NarakaUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(value = LevelRenderer.class)
public abstract class HiddenOreRendererMixin {
    @Shadow
    @Final
    private RenderBuffers renderBuffers;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    protected abstract void checkPoseStack(PoseStack poseStack);

    @Shadow
    @Final
    private LevelRenderState levelRenderState;

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;extractVisibleEntities(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/culling/Frustum;Lnet/minecraft/client/DeltaTracker;Lnet/minecraft/client/renderer/state/LevelRenderState;)V"))
    private void checkOreSeeThrough(GraphicsResourceAllocator graphicsResourceAllocator, DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, Matrix4f frustumMatrix, Matrix4f projectionMatrix, Matrix4f cullingProjectionMatrix, GpuBufferSlice shaderFog, Vector4f fogColor, boolean renderSky, CallbackInfo ci) {
        boolean haveOreSeeThrough = minecraft.player != null && NarakaItemUtils.canApplyOreSeeThrough(minecraft.player) && !NarakaConfig.CLIENT.disableOreSeeThrough.getValue();
        this.levelRenderState.haveGlowingEntities |= haveOreSeeThrough;
    }

    @Inject(method = "method_62214", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher;renderAllFeatures()V"))
    protected void addHiddenOres(GpuBufferSlice gpuBufferSlice, LevelRenderState levelRenderState, ProfilerFiller profilerFiller, Matrix4f matrix4f, ResourceHandle<RenderTarget> resourceHandle, ResourceHandle<RenderTarget> resourceHandle2, boolean bl, ResourceHandle<RenderTarget> resourceHandle3, ResourceHandle<RenderTarget> resourceHandle4, CallbackInfo ci) {
        if (minecraft.player == null || !NarakaItemUtils.canApplyOreSeeThrough(minecraft.player) || NarakaConfig.CLIENT.disableOreSeeThrough.getValue())
            return;

        PoseStack poseStack = new PoseStack();
        naraka$renderHiddenOres(poseStack, minecraft.gameRenderer.getMainCamera());
        this.checkPoseStack(poseStack);
    }

    @Unique
    private void naraka$renderHiddenOres(PoseStack poseStack, Camera camera) {
        ClientLevel level = minecraft.level;
        if (level == null)
            return;
        Vec3 cameraPosition = camera.position();
        BlockPos cameraBlockPos = NarakaUtils.pos(cameraPosition).offset(0, 0, -1);
        OutlineBufferSource outlineBufferSource = this.renderBuffers.outlineBufferSource();

        int range = NarakaConfig.CLIENT.oreSeeThroughRange.getValue();
        Vec3i cornerOffset = new Vec3i(range, range, range);
        BoundingBox box = BoundingBox.fromCorners(cameraBlockPos.offset(cornerOffset), cameraBlockPos.offset(cornerOffset.multiply(-1)));

        NarakaUtils.sphere(box, 1, pos -> {
            BlockState state = level.getBlockState(pos);
            if (state.is(ConventionalTags.Blocks.ORES)) {
                Color color = NarakaConfig.ORE_COLORS.getColor(state);
                if (color.alpha() == 0)
                    return;
                outlineBufferSource.setColor(color.pack());

                poseStack.pushPose();
                poseStack.translate(pos.getX() - cameraPosition.x, pos.getY() - cameraPosition.y, pos.getZ() - cameraPosition.z);

                RenderType renderType = RenderTypes.outline(NarakaTextures.LOCATION_BLOCKS);
                VertexConsumer vertexConsumer = outlineBufferSource.getBuffer(renderType);

                List<BlockModelPart> blockModelParts = new ArrayList<>();
                BlockRenderDispatcher blockRenderer = minecraft.getBlockRenderer();
                blockRenderer.getBlockModel(state).collectParts(level.random, blockModelParts);
                blockRenderer.renderBatched(state, pos, level, poseStack, vertexConsumer, false, blockModelParts);

                poseStack.popPose();
            }
        });
    }
}
