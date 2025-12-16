package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
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
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
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
    @Shadow @Final
    private RenderBuffers renderBuffers;

    @Shadow @Final
    private Minecraft minecraft;

    @Shadow @Final
    private LevelTargetBundle targets;

    @Shadow
    public abstract void doEntityOutline();

    @Shadow
    protected abstract void checkPoseStack(PoseStack poseStack);

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;addLateDebugPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/renderer/state/CameraRenderState;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Matrix4f;)V"))
    protected void addHiddenOres(GraphicsResourceAllocator graphicsResourceAllocator, DeltaTracker deltaTracker, boolean bl, Camera camera, Matrix4f matrix4f, Matrix4f matrix4f2, Matrix4f matrix4f3, GpuBufferSlice gpuBufferSlice, Vector4f vector4f, boolean bl2, CallbackInfo ci, @Local FrameGraphBuilder frameGraphBuilder) {
        if (minecraft.player == null || !NarakaItemUtils.canApplyOreSeeThrough(minecraft.player) || NarakaConfig.CLIENT.disableOreSeeThrough.getValue())
            return;

        int width = this.minecraft.getMainRenderTarget().width;
        int height = this.minecraft.getMainRenderTarget().height;
        naraka$addHiddenOresPass(frameGraphBuilder, camera);
        PostChain postChain = this.minecraft.getShaderManager().getPostChain(LevelTargetBundle.ENTITY_OUTLINE_TARGET_ID, LevelTargetBundle.OUTLINE_TARGETS);
        if (postChain != null) {
            postChain.addToFrame(frameGraphBuilder, width, height, this.targets);
        }
        doEntityOutline();
    }

    @Unique
    private void naraka$addHiddenOresPass(FrameGraphBuilder frameGraphBuilder, Camera camera) {
        FramePass framePass = frameGraphBuilder.addPass("hidden_ores");
        this.targets.main = framePass.readsAndWrites(this.targets.main);
        if (this.targets.entityOutline != null) {
            this.targets.entityOutline = framePass.readsAndWrites(this.targets.entityOutline);
        }
        ResourceHandle<RenderTarget> outlineHandler = this.targets.entityOutline;

        framePass.executes(() -> {
            if (outlineHandler != null) {
                RenderTarget renderTarget = outlineHandler.get();
                GpuTexture colorTexture = renderTarget.getColorTexture();
                GpuTexture depthTexture = renderTarget.getDepthTexture();
                if (colorTexture != null && depthTexture != null) {
                    RenderSystem.getDevice()
                            .createCommandEncoder()
                            .clearColorAndDepthTextures(colorTexture, 0, depthTexture, 1.0);
                }
            }

            PoseStack poseStack = new PoseStack();
            MultiBufferSource.BufferSource bufferSource = this.renderBuffers.bufferSource();
            naraka$renderHiddenOres(poseStack, camera);
            this.checkPoseStack(poseStack);
            bufferSource.endLastBatch();
            this.renderBuffers.outlineBufferSource().endOutlineBatch();
        });
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
