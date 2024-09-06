package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.renderer.BlockTransparentRenderer;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.util.NarakaUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    @Final
    private RenderBuffers renderBuffers;

    @Shadow
    @Final
    private Camera mainCamera;

    @Shadow
    @Final
    Minecraft minecraft;

    @Unique
    @Nullable PoseStack poseStack;

    @ModifyExpressionValue(method = "renderLevel", at = @At(value = "NEW", target = "()Lcom/mojang/blaze3d/vertex/PoseStack;"))
    private PoseStack getPoseStack(PoseStack poseStack) {
        return this.poseStack = poseStack;
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;renderItemInHand(Lnet/minecraft/client/Camera;FLorg/joml/Matrix4f;)V"))
    private void renderHiddenOres(DeltaTracker deltaTracker, CallbackInfo ci) {
        LocalPlayer player = minecraft.player;
        ClientLevel level = minecraft.level;
        if (player == null || level == null || poseStack == null
                || !NarakaItemUtils.canApplyOreSeeThrough(player))
            return;
        Vec3 cameraPosition = mainCamera.getPosition();
        BlockPos cameraBlockPos = NarakaUtils.pos(cameraPosition).offset(0, 0, -1);
        Direction xAxisDirection = Direction.getFacingAxis(mainCamera.getEntity(), Direction.Axis.X);
        Direction yAxisDirection = Direction.getFacingAxis(mainCamera.getEntity(), Direction.Axis.Y);
        Direction zAxisDirection = Direction.getFacingAxis(mainCamera.getEntity(), Direction.Axis.Z);
        MultiBufferSource.BufferSource bufferSource = this.renderBuffers.bufferSource();

        Vec3i cornerOffset = new Vec3i(15, 15, 15);
        BoundingBox box = BoundingBox.fromCorners(cameraBlockPos.offset(cornerOffset), cameraBlockPos.offset(cornerOffset.multiply(-1)));

        NarakaUtils.sphere(box, 1, pos -> {
            BlockState state = level.getBlockState(pos);
            if (state.is(ConventionalBlockTags.ORES)
                    && (cameraPosition.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 24
                    || isSideOccluded(level, state, pos, xAxisDirection.getOpposite())
                    && isSideOccluded(level, state, pos, yAxisDirection.getOpposite())
                    && isSideOccluded(level, state, pos, zAxisDirection.getOpposite()))
            ) {
                poseStack.pushPose();
                poseStack.translate(pos.getX() - cameraPosition.x, pos.getY() - cameraPosition.y, pos.getZ() - cameraPosition.z);
                BlockTransparentRenderer.INSTANCE.renderTransparentBlock(state, poseStack, bufferSource, 0.3f, LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY);
                poseStack.popPose();
            }
        });
    }

    @Unique
    private static boolean isSideOccluded(Level level, BlockState state, BlockPos pos, Direction direction) {
        BlockPos checkingPos = pos.relative(direction);
        BlockState checkingState = level.getBlockState(checkingPos);
        return state.canOcclude() && checkingState.isFaceSturdy(level, checkingPos, direction.getOpposite());
    }
}
