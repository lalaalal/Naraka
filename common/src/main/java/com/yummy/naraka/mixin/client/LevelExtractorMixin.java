package com.yummy.naraka.mixin.client;

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
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelExtractor.class)
public abstract class LevelExtractorMixin {
    @Unique
    private static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();

    @Shadow
    @Final
    private LevelRenderState levelRenderState;

    @Shadow
    private @Nullable ClientLevel level;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Unique
    @Nullable
    private BlockModelResolver naraka$blockModelResolver;

    @Inject(method = "onResourceManagerReload", at = @At("RETURN"))
    private void prepareDimensionSkyRenderers(ResourceManager resourceManager, CallbackInfo ci) {
        DimensionSkyRendererRegistry.setup();
        naraka$blockModelResolver = new BlockModelResolver(minecraft.getModelManager());
    }

    @Inject(method = "extract", at = @At("RETURN"))
    private void extractDimensionType(DeltaTracker deltaTracker, Camera camera, float deltaPartialTick, CallbackInfo ci) {
        if (level != null && levelRenderState.skyRenderState instanceof DimensionTypeProvider dimensionTypeProvider) {
            dimensionTypeProvider.naraka$setDimensionType(level.dimension());
        }
    }

    @Inject(method = "extract", at = @At("RETURN"))
    private void extractHiddenOres(DeltaTracker deltaTracker, Camera camera, float deltaPartialTick, CallbackInfo ci) {
        if (!(levelRenderState instanceof HiddenOreRenderStateProvider hiddenOreRenderStateProvider))
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
                hiddenOreRenderStateProvider.naraka$getHiddenOreRenderStates()
                        .add(renderState);
            }
        });
    }
}
