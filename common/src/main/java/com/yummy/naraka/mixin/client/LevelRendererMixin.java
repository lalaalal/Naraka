package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.init.DimensionSkyRendererRegistry;
import com.yummy.naraka.client.renderer.DimensionTypeProvider;
import com.yummy.naraka.client.renderer.HerobrineSkyRenderHelper;
import com.yummy.naraka.client.renderer.RyoikiMaskExtension;
import com.yummy.naraka.client.renderer.feature.RyoikiMaskExecutor;
import com.yummy.naraka.config.NarakaConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.util.Util;
import net.minecraft.world.level.dimension.DimensionType;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin implements RyoikiMaskExtension<RenderTarget> {
    @Shadow
    private SkyRenderer skyRenderer;

    @Shadow
    @Final
    private LevelRenderState levelRenderState;

    @Shadow
    @Final
    private LevelTargetBundle targets;

    @Shadow
    @Final
    private GameRenderer gameRenderer;

    @Shadow
    @Final
    private static Vector4fc ZERO_CLEAR_COLOR;
    @Unique
    private final RenderTarget naraka$ryoikiRenderTarget = Util.make(() -> {
        Window window = Minecraft.getInstance().getWindow();
        return new TextureTarget("Ryoiki Mask", window.getWidth(), window.getHeight(), GpuFormat.RGBA8_UNORM, GpuFormat.D32_FLOAT);
    });

    @Override
    public RenderTarget naraka$getRyoikiMask() {
        return naraka$ryoikiRenderTarget;
    }

    @Override
    public void naraka$setRyoikiMask(RenderTarget ryoikiMask) {
        throw new UnsupportedOperationException();
    }

    @Inject(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/LevelTargetBundle;entityOutline:Lcom/mojang/blaze3d/resource/ResourceHandle;", opcode = Opcodes.PUTFIELD))
    private void importRyoikiMask(GraphicsResourceAllocator resourceAllocator, boolean renderOutline, CameraRenderState cameraState, GpuBufferSlice terrainFog, Vector4f fogColor, boolean shouldRenderSky, boolean consistentDepthRequired, CallbackInfo ci, @Local(name = "frame") FrameGraphBuilder frame) {
        naraka$getRyoikiMaskExtension().naraka$setRyoikiMask(frame.importExternal("ryoiki_mask", naraka$ryoikiRenderTarget));
    }

    @Inject(method = "lambda$render$0", at = @At("RETURN"))
    private void clearRyoikiMask(Vector4f fogColor, CallbackInfo ci) {
        if (naraka$ryoikiRenderTarget.getColorTexture() == null || naraka$ryoikiRenderTarget.getDepthTexture() == null)
            return;
        RenderSystem.getDevice()
                .createCommandEncoder()
                .clearColorAndDepthTextures(naraka$ryoikiRenderTarget.getColorTexture(), new Vector4f(), naraka$ryoikiRenderTarget.getDepthTexture(), 0);
    }

    @Inject(method = "addMainPass", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/framegraph/FramePass;executes(Ljava/lang/Runnable;)V"))
    private void readRyoikiMask(FrameGraphBuilder frame, FeatureRenderDispatcher.PreparedFrame featureFrame, GpuBufferSlice terrainFog, ChunkSectionsToRender chunkSectionsToRender, boolean consistentDepthRequired, CallbackInfo ci, @Local(name = "pass") FramePass framePass) {
        naraka$getRyoikiMaskExtension().naraka$setRyoikiMask(framePass.readsAndWrites(naraka$getRyoikiMaskExtension().naraka$getRyoikiMask()));
    }

    @Inject(method = "lambda$addMainPass$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;executeOutline(Lnet/minecraft/client/renderer/feature/FeatureRenderDispatcher$PreparedFrame;)V"))
    private void executeRyoikiMask(GpuBufferSlice terrainFog, boolean useImprovedTransparency, ChunkSectionsToRender chunkSectionsToRender, FeatureRenderDispatcher.PreparedFrame featureFrame, boolean hasAlwaysOnTopGizmos, boolean consistentDepthRequired, CallbackInfo ci) {
        RyoikiMaskExecutor ryoikiMaskExecutor = (RyoikiMaskExecutor) featureFrame;
        RenderTarget mainTarget = this.targets.main.get();
        Objects.requireNonNull(naraka$ryoikiRenderTarget.getColorTextureView());
        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> "Ryoiki Mask", naraka$ryoikiRenderTarget.getColorTextureView(), Optional.of(ZERO_CLEAR_COLOR), mainTarget.getDepthTextureView(), OptionalDouble.empty())
        ) {
            RenderSystem.bindDefaultUniforms(renderPass);
            ryoikiMaskExecutor.naraka$executeRyoikiMask(renderPass);
        }
    }

    @Inject(
            method = "addSkyPass",
            at = @At("RETURN")
    )
    private void renderDimensionSky(FrameGraphBuilder frame, CameraRenderState cameraState, GpuBufferSlice skyFog, CallbackInfo ci) {
        if (levelRenderState.skyRenderState instanceof DimensionTypeProvider dimensionTypeProvider) {
            FramePass framePass = frame.addPass("custom dimension sky");
            targets.main = framePass.readsAndWrites(targets.main);
            framePass.executes(() -> {
                DimensionSkyRendererRegistry.get(dimensionTypeProvider.naraka$getDimensionType())
                        .renderSky(levelRenderState, targets, frame, cameraState, skyFog, skyRenderer);
            });
        }
    }

    @ModifyArg(
            method = "addSkyPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/renderer/state/level/CameraRenderState;Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/framegraph/FramePass;executes(Ljava/lang/Runnable;)V")
    )
    public Runnable replaceHerobrineSkyPass(Runnable original,
                                            @Local(argsOnly = true, name = "skyFog") GpuBufferSlice skyFog) {
        if (levelRenderState.skyRenderState.skybox == DimensionType.Skybox.OVERWORLD && naraka$isHerobrineSkyEnabled())
            return () -> HerobrineSkyRenderHelper.renderHerobrineSky(gameRenderer.mainRenderTarget(), skyRenderer, skyFog);
        return original;
    }

    @ModifyArgs(
            method = "prepareTranslucents",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/CloudRenderer;prepare(ILnet/minecraft/client/CloudStatus;FILnet/minecraft/world/phys/Vec3;JF)V")
    )
    public void speedUpClouds(Args args) {
        long gameTime = args.get(5);
        float partialTicks = args.get(6);
        if (naraka$isHerobrineSkyEnabled()) {
            int speed = NarakaConfig.CLIENT.herobrineSkyCloudSpeed.getValue();
            args.set(5, gameTime * speed);
            args.set(6, partialTicks * speed);
        }
    }

    @Inject(method = "resize", at = @At("RETURN"))
    private void resize(int width, int height, CallbackInfo ci) {
        naraka$ryoikiRenderTarget.resize(width, height);
    }

    @Inject(method = "close", at = @At("RETURN"))
    private void onClose(CallbackInfo ci) {
        DimensionSkyRendererRegistry.close();
        naraka$ryoikiRenderTarget.destroyBuffers();
    }

    @Unique
    private static boolean naraka$isHerobrineSkyEnabled() {
        return NarakaClientContext.ENABLE_HEROBRINE_SKY.getValue() && !NarakaClientContext.SHADER_ENABLED.getValue();
    }

    @SuppressWarnings("unchecked")
    @Unique
    private RyoikiMaskExtension<ResourceHandle<RenderTarget>> naraka$getRyoikiMaskExtension() {
        return (RyoikiMaskExtension<ResourceHandle<RenderTarget>>) targets;
    }
}
