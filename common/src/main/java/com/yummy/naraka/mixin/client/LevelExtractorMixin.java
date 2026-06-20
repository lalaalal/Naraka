package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.init.DimensionSkyRendererRegistry;
import com.yummy.naraka.client.renderer.DimensionTypeProvider;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelExtractor.class)
public abstract class LevelExtractorMixin {
    @Shadow
    @Final
    private LevelRenderState levelRenderState;

    @Shadow
    private @Nullable ClientLevel level;

    @Inject(method = "onResourceManagerReload", at = @At("RETURN"))
    private void prepareDimensionSkyRenderers(ResourceManager resourceManager, CallbackInfo ci) {
        DimensionSkyRendererRegistry.setup();
    }

    @Inject(method = "extract", at = @At("RETURN"))
    private void extractDimensionType(DeltaTracker deltaTracker, Camera camera, float deltaPartialTick, CallbackInfo ci) {
        if (level != null && levelRenderState.skyRenderState instanceof DimensionTypeProvider dimensionTypeProvider) {
            dimensionTypeProvider.naraka$setDimensionType(level.dimension());
        }
    }
}
