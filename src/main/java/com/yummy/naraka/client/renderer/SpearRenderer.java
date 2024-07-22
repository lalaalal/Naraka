package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.SpearModel;
import com.yummy.naraka.client.model.SpearOfLonginusModel;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.entity.Spear;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class SpearRenderer extends EntityRenderer<Spear> {
    private static final Map<EntityType<? extends Spear>, ResourceLocation> TEXTURE_MAP = Map.of(
            NarakaEntityTypes.THROWN_SPEAR, NarakaTextures.SPEAR,
            NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR, NarakaTextures.MIGHTY_HOLY_SPEAR,
            NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS, NarakaTextures.LONGINUS
    );

    protected final EntityModel<? extends Spear> model;
    private final int yOffset;

    public static SpearRenderer longinus(EntityRendererProvider.Context context) {
        SpearOfLonginusModel model = new SpearOfLonginusModel(context.bakeLayer(NarakaModelLayers.SPEAR_OF_LONGINUS));
        return new SpearRenderer(context, model, 3);
    }

    public SpearRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new SpearModel(context.bakeLayer(NarakaModelLayers.SPEAR));
        this.yOffset = 0;
    }

    public SpearRenderer(EntityRendererProvider.Context context, EntityModel<? extends Spear> model, int yOffset) {
        super(context);
        this.model = model;
        this.yOffset = yOffset;
    }

    @Override
    public boolean shouldRender(Spear pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    @Override
    public ResourceLocation getTextureLocation(Spear spear) {
        if (TEXTURE_MAP.containsKey(spear.getType()))
            return TEXTURE_MAP.get(spear.getType());
        return NarakaTextures.SPEAR;
    }

    @Override
    public void render(Spear spear, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, spear.yRotO, spear.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, spear.xRotO, spear.getXRot()) + 90.0F));
        poseStack.translate(0, yOffset, 0);
        if (spear.getType() == NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS)
            packedLight = LightTexture.FULL_BRIGHT;
        RenderType renderType = model.renderType(getTextureLocation(spear));
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer, renderType, false, spear.hasFoil());
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
