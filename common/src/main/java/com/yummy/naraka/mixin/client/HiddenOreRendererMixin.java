package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Environment(EnvType.CLIENT)
@Mixin(value = LevelRenderer.class)
public abstract class HiddenOreRendererMixin {
    @Shadow @Final
    private RenderBuffers renderBuffers;

    @Shadow @Final
    private Minecraft minecraft;

    @Shadow
    protected abstract void checkPoseStack(PoseStack poseStack);

    @Shadow @Nullable
    private PostChain entityEffect;

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;entitiesForRendering()Ljava/lang/Iterable;"))
    protected void addHiddenOres(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
        if (minecraft.player == null || entityEffect == null || !NarakaItemUtils.canApplyOreSeeThrough(minecraft.player) || NarakaConfig.CLIENT.disableOreSeeThrough.getValue())
            return;

        PoseStack poseStack = new PoseStack();
        naraka$renderHiddenOres(poseStack, camera);
        this.checkPoseStack(poseStack);
        this.renderBuffers.outlineBufferSource().endOutlineBatch();
        entityEffect.process(deltaTracker.getGameTimeDeltaTicks());
        minecraft.getMainRenderTarget().bindWrite(false);
    }

    @Unique
    private void naraka$renderHiddenOres(PoseStack poseStack, Camera camera) {
        ClientLevel level = minecraft.level;
        if (level == null)
            return;
        Vec3 cameraPosition = camera.getPosition();
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
                outlineBufferSource.setColor(color.red(), color.green(), color.blue(), color.alpha());

                poseStack.pushPose();
                poseStack.translate(pos.getX() - cameraPosition.x, pos.getY() - cameraPosition.y, pos.getZ() - cameraPosition.z);

                @SuppressWarnings("deprecation")
                RenderType renderType = RenderType.outline(TextureAtlas.LOCATION_BLOCKS);
                VertexConsumer vertexConsumer = outlineBufferSource.getBuffer(renderType);

                BlockRenderDispatcher blockRenderer = minecraft.getBlockRenderer();
                blockRenderer.renderBatched(state, pos, level, poseStack, vertexConsumer, false, camera.getEntity().getRandom());

                poseStack.popPose();
            }
        });
    }
}
