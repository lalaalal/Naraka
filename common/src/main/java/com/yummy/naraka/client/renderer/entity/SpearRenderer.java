package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.SpearModel;
import com.yummy.naraka.client.model.SpearOfLonginusModel;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.entity.Spear;
import com.yummy.naraka.world.entity.SpearOfLonginus;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import org.joml.Quaternionf;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class SpearRenderer<T extends Spear> extends EntityRenderer<T> {
    private static final Map<EntityType<? extends Spear>, ResourceLocation> TEXTURE_MAP = Map.of(
            NarakaEntityTypes.THROWN_SPEAR.get(), NarakaTextures.SPEAR,
            NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR.get(), NarakaTextures.MIGHTY_HOLY_SPEAR,
            NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS.get(), NarakaTextures.LONGINUS
    );

    protected final EntityModel<? extends Spear> model;
    private final int yOffset;

    public static SpearRenderer<SpearOfLonginus> longinus(EntityRendererProvider.Context context) {
        SpearOfLonginusModel model = new SpearOfLonginusModel(context.bakeLayer(NarakaModelLayers.SPEAR_OF_LONGINUS));
        return new SpearRenderer<>(context, model, 3);
    }

    public SpearRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new SpearModel(context.bakeLayer(NarakaModelLayers.SPEAR));
        this.yOffset = 0;
    }

    public SpearRenderer(EntityRendererProvider.Context context, EntityModel<? extends T> model, int yOffset) {
        super(context);
        this.model = model;
        this.yOffset = yOffset;
    }

    @Override
    public boolean shouldRender(Spear spear, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE_MAP.getOrDefault(entity.getType(), NarakaTextures.SPEAR);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Quaternionf yRotation = Axis.YP.rotationDegrees(Mth.lerp(partialTick, entity.yRotO, entity.getViewYRot(partialTick)) - 90.0F);
        Quaternionf xRotation = Axis.ZP.rotationDegrees(Mth.lerp(partialTick, entity.xRotO, entity.getXRot()) + 90.0F);

        poseStack.pushPose();
        poseStack.mulPose(yRotation);
        poseStack.mulPose(xRotation);
        poseStack.translate(0, yOffset, 0);

        float ageInTicks = entity.tickCount + partialTick;
        boolean isLonginus = entity.getType() == NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS.get();
        if (isLonginus && NarakaClientContext.SHADER_ENABLED.getValue()) {
            renderNonShaderLonginus(model, ageInTicks, poseStack, bufferSource);
        } else {
            if (isLonginus)
                packedLight = LightTexture.FULL_BRIGHT;
            RenderType renderType = model.renderType(getTextureLocation(entity));
            NarakaRenderUtils.submitModelWithFoilRenderTypes(model, poseStack, renderType, bufferSource, packedLight, entity.hasFoil());
        }

        poseStack.popPose();
    }

    public static void renderNonShaderLonginus(EntityModel<? extends Spear> model, float ageInTicks, PoseStack poseStack, MultiBufferSource bufferSource) {
        poseStack.pushPose();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(NarakaTextures.LONGINUS));
        model.renderToBuffer(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xff000000);
        poseStack.popPose();

        renderLonginus(model, ageInTicks, 0.001f, 0.01f, poseStack, bufferSource);
        renderLonginus(model, ageInTicks, 0.002f, 0.005f, poseStack, bufferSource);
        renderLonginus(model, ageInTicks, 0.0015f, 0.0025f, poseStack, bufferSource);
    }

    private static void renderLonginus(EntityModel<? extends Spear> model, float ageInTicks, float uMultiplier, float vMultiplier, PoseStack poseStack, MultiBufferSource bufferSource) {
        poseStack.pushPose();
        RenderType renderType = RenderType.energySwirl(NarakaTextures.LONGINUS, (ageInTicks * uMultiplier) % 1, (ageInTicks * vMultiplier) % 1);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        model.renderToBuffer(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }
}
