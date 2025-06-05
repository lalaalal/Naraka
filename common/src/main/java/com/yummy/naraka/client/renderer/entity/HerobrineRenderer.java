package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.HerobrineScarfLayer;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.client.model.HerobrineFinalModel;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.renderer.entity.state.HerobrineRenderState;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class HerobrineRenderer extends AbstractHerobrineRenderer<Herobrine, HerobrineRenderState, AbstractHerobrineModel<HerobrineRenderState>> {
    private final AbstractHerobrineModel<HerobrineRenderState> herobrineModel;
    private final AbstractHerobrineModel<HerobrineRenderState> herobrineFinalModel;
    private final AbstractHerobrineModel<HerobrineRenderState> afterimageModel;
    private final ItemModelResolver itemModelResolver;
    private final ItemStack pickaxe = NarakaItems.HEROBRINE_PICKAXE.get().getDefaultInstance();

    public HerobrineRenderer(EntityRendererProvider.Context context) {
        super(context, new HerobrineModel<>(context.bakeLayer(NarakaModelLayers.HEROBRINE)), 0.5f);
        this.herobrineModel = model;
        this.afterimageModel = new HerobrineModel<>(context.bakeLayer(NarakaModelLayers.HEROBRINE));
        this.herobrineFinalModel = new HerobrineFinalModel(context.bakeLayer(NarakaModelLayers.HEROBRINE_FINAL));
        this.itemModelResolver = context.getItemModelResolver();

        addLayer(new HerobrineScarfLayer<>(this, context));
    }

    @Override
    public HerobrineRenderState createRenderState() {
        return new HerobrineRenderState();
    }

    @Override
    public void extractRenderState(Herobrine herobrine, HerobrineRenderState renderState, float partialTicks) {
        renderState.phase = herobrine.getPhase();
        renderState.doWalkAnimation = herobrine.getPhase() != 3;
        renderState.displayPickaxe = herobrine.displayPickaxe() && herobrine.isAlive();
        renderState.eyeAlpha = herobrine.getEyeAlpha();

        super.extractRenderState(herobrine, renderState, partialTicks);

        itemModelResolver.updateForLiving(renderState.pickaxe, pickaxe, ItemDisplayContext.NONE, herobrine);

        if (renderState.phase == 3) {
            renderState.eyeTexture = NarakaTextures.HEROBRINE_FINAL_EYE;
            this.model = herobrineFinalModel;
        } else {
            renderState.eyeTexture = NarakaTextures.HEROBRINE_EYE;
            this.model = herobrineModel;
        }
    }

    @Override
    protected float getShadowRadius(HerobrineRenderState renderState) {
        if (renderState.phase == 3)
            return 0.7f * renderState.scale;
        return super.getShadowRadius(renderState);
    }

    private void applyTransformAndRotate(PoseStack poseStack, ModelPart part) {
        poseStack.translate(-part.x / 16, -part.y / 16, part.z / 16);
        if (part.xRot != 0 || part.yRot != 0 || part.zRot != 0) {
            poseStack.mulPose(new Quaternionf().rotationZYX(part.zRot, -part.yRot, -part.xRot));
        }
    }

    @Override
    public void render(HerobrineRenderState renderState, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (renderState.phase == 3 && renderState.displayPickaxe) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - renderState.bodyRot));
            poseStack.translate(0, 1.4, 0);
            applyTransformAndRotate(poseStack, model.root());
            applyTransformAndRotate(poseStack, model.main());
            applyTransformAndRotate(poseStack, model.upperBody());
            applyTransformAndRotate(poseStack, model.rightArm());
            applyTransformAndRotate(poseStack, model.rightHand());
            applyTransformAndRotate(poseStack, model.rightHand().getChild("pickaxe"));
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            poseStack.mulPose(Axis.ZP.rotationDegrees(225));
            poseStack.scale(4, 4, 1);
            renderState.pickaxe.render(poseStack, buffer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }
        super.render(renderState, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(HerobrineRenderState renderState) {
        if (renderState.phase == 3)
            return NarakaTextures.HEROBRINE_FINAL;
        return super.getTextureLocation(renderState);
    }

    @Override
    protected AbstractHerobrineModel<HerobrineRenderState> getAfterimageModel(HerobrineRenderState renderState) {
        if (renderState.phase == 3)
            return herobrineFinalModel;
        return afterimageModel;
    }

    @Override
    protected ResourceLocation getAfterimageTexture(HerobrineRenderState renderState) {
        if (renderState.phase == 3)
            return NarakaTextures.HEROBRINE_FINAL;
        return NarakaTextures.HEROBRINE_AFTERIMAGE;
    }
}
