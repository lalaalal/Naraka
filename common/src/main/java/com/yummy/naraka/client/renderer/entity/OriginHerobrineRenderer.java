package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.HerobrineEyeLayer;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.util.ComponentStyles;
import com.yummy.naraka.world.entity.OriginHerobrine;
import com.yummy.naraka.world.item.SoulType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class OriginHerobrineRenderer extends LivingEntityRenderer<OriginHerobrine, HerobrineModel<OriginHerobrine>> {
    private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0) / 2.0);

    public OriginHerobrineRenderer(EntityRendererProvider.Context context) {
        super(context, new HerobrineModel<>(context.bakeLayer(NarakaModelLayers.HEROBRINE), "origin_herobrine", true), 0);
        addLayer(new HerobrineEyeLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(OriginHerobrine originHerobrine) {
        return NarakaTextures.HEROBRINE;
    }

    @Override
    public void render(OriginHerobrine entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0, 2.5, -1);

        int alpha = entity.getAlpha();
        float yRot = entity.getViewYRot(partialTicks);
        float shinyTick = 0x80;
        if (alpha < 0xff)
            shinyTick += (0xff - alpha) / 2f;
        ShinyEffectRenderer.renderShiny(shinyTick, 0xff, 0.25f, yRot + 180, false, 0xffffff, poseStack, buffer);
        ShinyEffectRenderer.renderShiny(shinyTick, 0xff, 0.25f, yRot, false, 0xffffff, poseStack, buffer);
        renderColors(entity, poseStack, buffer, partialTicks);

        poseStack.popPose();

        if (entity.deathTime > 0.0F) {
            float deathProgress = (entity.deathTime + partialTicks) / 200.0F;
            poseStack.pushPose();
            poseStack.translate(0, 1, 0);
            renderRays(poseStack, deathProgress, buffer.getBuffer(RenderType.dragonRays()));
            renderRays(poseStack, deathProgress, buffer.getBuffer(RenderType.dragonRaysDepth()));
            poseStack.popPose();
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, 0);
    }

    @Override
    protected float getFlipDegrees(OriginHerobrine livingEntity) {
        return 0;
    }

    private static void renderRays(PoseStack poseStack, float dragonDeathCompletion, VertexConsumer buffer) {
        poseStack.pushPose();
        float alpha = Math.min(dragonDeathCompletion > 0.8F ? (dragonDeathCompletion - 0.8F) / 0.2F : 0.0F, 1.0F);
        int white = FastColor.ARGB32.colorFromFloat(1.0F - alpha, 1.0F, 1.0F, 1.0F);
        int color = ComponentStyles.RAINBOW_COLOR.getCurrentColor().withAlpha(2).pack();
        RandomSource randomSource = RandomSource.create(432L);
        Vector3f vector3f = new Vector3f();
        Vector3f vector3f2 = new Vector3f();
        Vector3f vector3f3 = new Vector3f();
        Vector3f vector3f4 = new Vector3f();
        Quaternionf quaternionf = new Quaternionf();
        int k = Mth.floor((dragonDeathCompletion + dragonDeathCompletion * dragonDeathCompletion) / 2.0F * 60.0F);

        for (int l = 0; l < k; ++l) {
            quaternionf.rotationXYZ(randomSource.nextFloat() * ((float) Math.PI * 2F), randomSource.nextFloat() * ((float) Math.PI * 2F), randomSource.nextFloat() * ((float) Math.PI * 2F)).rotateXYZ(randomSource.nextFloat() * ((float) Math.PI * 2F), randomSource.nextFloat() * ((float) Math.PI * 2F), randomSource.nextFloat() * ((float) Math.PI * 2F) + dragonDeathCompletion * ((float) Math.PI / 2F));
            poseStack.mulPose(quaternionf);
            float g = randomSource.nextFloat() * 20.0F + 5.0F + alpha * 10.0F;
            float h = randomSource.nextFloat() * 2.0F + 1.0F + alpha * 2.0F;
            vector3f2.set(-HALF_SQRT_3 * h, g, -0.5F * h);
            vector3f3.set(HALF_SQRT_3 * h, g, -0.5F * h);
            vector3f4.set(0.0F, g, h);
            PoseStack.Pose pose = poseStack.last();
            buffer.addVertex(pose, vector3f).setColor(white);
            buffer.addVertex(pose, vector3f2).setColor(color);
            buffer.addVertex(pose, vector3f3).setColor(color);
            buffer.addVertex(pose, vector3f).setColor(white);
            buffer.addVertex(pose, vector3f3).setColor(color);
            buffer.addVertex(pose, vector3f4).setColor(color);
            buffer.addVertex(pose, vector3f).setColor(white);
            buffer.addVertex(pose, vector3f4).setColor(color);
            buffer.addVertex(pose, vector3f2).setColor(color);
        }

        poseStack.popPose();
    }

    private void renderColors(OriginHerobrine originHerobrine, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick) {
        int index = 0;
        float yRot = originHerobrine.getViewYRot(partialTick);
        for (SoulType soulType : originHerobrine.getSoulTypeAlpha().keySet()) {
            float alpha = originHerobrine.getSoulTypeAlpha().getOrDefault(soulType, 0f);
            int halfIndex = index / 2;
            double xOffset = 0.6 * (halfIndex + 1);
            if (index % 2 == 0)
                xOffset *= -1;
            float scale = 0.066f - Mth.log2(halfIndex) * 0.03f;

            poseStack.pushPose();
            poseStack.translate(xOffset, 0, 0);
            poseStack.scale(4 + halfIndex, 1, 1);
            ShinyEffectRenderer.renderShiny(alpha * 50, 100, scale, yRot + 180, true, soulType.color, poseStack, bufferSource);
            ShinyEffectRenderer.renderShiny(alpha * 50, 100, scale, yRot, true, soulType.color, poseStack, bufferSource);

            poseStack.popPose();

            index++;
        }
    }

    @Override
    protected @Nullable RenderType getRenderType(OriginHerobrine livingEntity, boolean bodyVisible, boolean translucent, boolean glowing) {
        return RenderType.entityTranslucent(getTextureLocation(livingEntity));
    }

    @Override
    protected boolean shouldShowName(OriginHerobrine entity) {
        return false;
    }
}
