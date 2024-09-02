package com.yummy.naraka.event;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaShaders;
import com.yummy.naraka.client.renderer.BlockTransparentRenderer;
import com.yummy.naraka.util.ComponentStyles;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class NarakaClientEvents {
    public static void initialize() {
        CoreShaderRegistrationCallback.EVENT.register(NarakaClientEvents::registerShaders);
        ClientTickEvents.START_CLIENT_TICK.register(NarakaClientEvents::onClientTick);
        WorldRenderEvents.LAST.register(NarakaClientEvents::renderHiddenOres);
    }

    private static void registerShaders(CoreShaderRegistrationCallback.RegistrationContext context) throws IOException {
        context.register(NarakaMod.location("longinus"), DefaultVertexFormat.POSITION, shaderInstance -> {
            NarakaShaders.longinus = shaderInstance;
        });
    }

    private static void onClientTick(Minecraft minecraft) {
        ComponentStyles.tick();
    }

    private static void renderHiddenOres(WorldRenderContext context) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null
                || !NarakaItemUtils.canApplyReinforcementEffect(player, EquipmentSlot.HEAD, NarakaReinforcementEffects.ORE_SEE_THROUGH))
            return;

        ClientLevel level = context.world();
        Frustum frustum = context.frustum();
        Camera camera = context.camera();
        Vec3 cameraPosition = camera.getPosition();
        BlockPos cameraBlockPos = NarakaUtils.pos(cameraPosition);
        Direction xAxisDirection = Direction.getFacingAxis(camera.getEntity(), Direction.Axis.X);
        Direction yAxisDirection = Direction.getFacingAxis(camera.getEntity(), Direction.Axis.Y);
        Direction zAxisDirection = Direction.getFacingAxis(camera.getEntity(), Direction.Axis.Z);
        PoseStack poseStack = context.matrixStack();
        MultiBufferSource bufferSource = context.consumers();
        if (poseStack == null || bufferSource == null || frustum == null)
            return;

        Vec3i cornerOffset = new Vec3i(10, 10, 10);
        BoundingBox box = BoundingBox.fromCorners(cameraBlockPos.offset(cornerOffset), cameraBlockPos.offset(cornerOffset.multiply(-1)));
        NarakaUtils.sphere(box, 1, pos -> {
            BlockState state = level.getBlockState(pos);
            if (state.is(ConventionalBlockTags.ORES)
                    && isSideOccluded(level, state, pos, xAxisDirection.getOpposite())
                    && isSideOccluded(level, state, pos, yAxisDirection.getOpposite())
                    && isSideOccluded(level, state, pos, zAxisDirection.getOpposite())) {
                poseStack.pushPose();
                poseStack.translate(pos.getX() - cameraPosition.x, pos.getY() - cameraPosition.y, pos.getZ() - cameraPosition.z);
                BlockTransparentRenderer.INSTANCE.renderTransparentBlock(state, poseStack, bufferSource, 0.3f, LightTexture.FULL_BLOCK, OverlayTexture.NO_OVERLAY);
                poseStack.popPose();
            }
        });
    }

    private static boolean isSideOccluded(Level level, BlockState state, BlockPos pos, Direction direction) {
        BlockPos checkingPos = pos.relative(direction);
        BlockState checkingState = level.getBlockState(checkingPos);
        return state.canOcclude() && checkingState.isFaceSturdy(level, checkingPos, direction.getOpposite());
    }
}
