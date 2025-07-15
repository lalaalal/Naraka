package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yummy.naraka.client.renderer.entity.state.NarakaPickaxeRenderState;
import com.yummy.naraka.world.NarakaPickaxe;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class NarakaPickaxeRenderer extends EntityRenderer<NarakaPickaxe, NarakaPickaxeRenderState> {
    private final ItemModelResolver itemModelResolver;
    private final ItemStack pickaxe = NarakaItems.HEROBRINE_PICKAXE.get().getDefaultInstance();

    public NarakaPickaxeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemModelResolver = context.getItemModelResolver();
    }

    @Override
    public NarakaPickaxeRenderState createRenderState() {
        return new NarakaPickaxeRenderState();
    }

    @Override
    public void extractRenderState(NarakaPickaxe entity, NarakaPickaxeRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.xRot = entity.getXRot(partialTick);
        reusedState.yRot = entity.getYRot(partialTick);
        itemModelResolver.updateForNonLiving(reusedState.pickaxe, pickaxe, ItemDisplayContext.NONE, entity);
    }

    @Override
    public void render(NarakaPickaxeRenderState renderState, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - renderState.yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(90 - renderState.xRot));
        poseStack.mulPose(Axis.ZP.rotationDegrees(225));
        poseStack.translate(-1, -1, 0);
        poseStack.scale(4, 4, 1);
        renderState.pickaxe.render(poseStack, buffer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(renderState, poseStack, buffer, packedLight);
    }

    @Override
    public boolean shouldRender(NarakaPickaxe livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }
}
