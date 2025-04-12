package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
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
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow @Final
    private RenderBuffers renderBuffers;

    @Shadow @Final
    private Minecraft minecraft;

    @Shadow
    private PostChain entityEffect;

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;entitiesForRendering()Ljava/lang/Iterable;"))
    private void renderHiddenOres(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci, @Local PoseStack poseStack) {
        LocalPlayer player = minecraft.player;
        ClientLevel level = minecraft.level;
        if (player == null || level == null || !NarakaItemUtils.canApplyOreSeeThrough(player) || NarakaConfig.CLIENT.disableOreSeeThrough.getValue())
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


                RenderType renderType = RenderType.outline(InventoryMenu.BLOCK_ATLAS);
                VertexConsumer vertexConsumer = outlineBufferSource.getBuffer(renderType);
                minecraft.getBlockRenderer().renderBatched(state, pos, level, poseStack, vertexConsumer, false, level.random);
                poseStack.popPose();
            }
        });

        renderBuffers.outlineBufferSource().endOutlineBatch();
        this.entityEffect.process(deltaTracker.getGameTimeDeltaTicks());
        this.minecraft.getMainRenderTarget().bindWrite(false);
    }
}
