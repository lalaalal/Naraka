package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaRenderTypes;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.world.entity.ColoredLightningBolt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class ColoredLightningBoltRenderer extends EntityRenderer<ColoredLightningBolt> {
    public ColoredLightningBoltRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    private RenderType getRenderType(ColoredLightningBolt entity) {
        if (NarakaClientContext.SHADER_ENABLED.getValue() || !entity.isSpaceRenderType())
            return RenderType.lightning();
        return NarakaRenderTypes.space();
    }

    @Override
    public void render(ColoredLightningBolt entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        float[] fs = new float[8];
        float[] gs = new float[8];
        float f = 0.0F;
        float g = 0.0F;
        RandomSource random = RandomSource.create(entity.seed);

        for (int index = 7; index >= 0; index--) {
            fs[index] = f;
            gs[index] = g;
            f += random.nextInt(11) - 5;
            g += random.nextInt(11) - 5;
        }

        float ff = f;
        float gg = g;

        VertexConsumer vertexConsumer = bufferSource.getBuffer(getRenderType(entity));
        PoseStack.Pose pose = poseStack.last();

        for (int k = 0; k < 4; k++) {
            random = RandomSource.create(entity.seed);
            for (int l = 0; l < 3; l++) {
                int m = 7;
                int n = 0;
                if (l > 0) {
                    m = 7 - l;
                }

                if (l > 0) {
                    n = m - 2;
                }

                float x1 = fs[m] - ff;
                float z1 = gs[m] - gg;

                for (int sectionY = m; sectionY >= n; sectionY--) {
                    float x2 = x1;
                    float z2 = z1;
                    if (l == 0) {
                        x1 += random.nextInt(11) - 5;
                        z1 += random.nextInt(11) - 5;
                    } else {
                        x1 += random.nextInt(31) - 15;
                        z1 += random.nextInt(31) - 15;
                    }

                    float innerThickness = 0.1F + k * 0.2F;
                    if (l == 0) {
                        innerThickness *= sectionY * 0.1F + 1.0F;
                    }

                    float outerThickness = 0.1F + k * 0.2F;
                    if (l == 0) {
                        outerThickness *= (sectionY - 1.0F) * 0.1F + 1.0F;
                    }

                    float alpha = FastColor.ARGB32.alpha(entity.getColor()) / 255f;
                    float red = FastColor.ARGB32.red(entity.getColor()) / 255f;
                    float green = FastColor.ARGB32.green(entity.getColor()) / 255f;
                    float blue = FastColor.ARGB32.blue(entity.getColor()) / 255f;
                    quad(pose, vertexConsumer, x1, z1, sectionY, x2, z2, alpha, red, green, blue, innerThickness, outerThickness, false, false, true, false);
                    quad(pose, vertexConsumer, x1, z1, sectionY, x2, z2, alpha, red, green, blue, innerThickness, outerThickness, true, false, true, true);
                    quad(pose, vertexConsumer, x1, z1, sectionY, x2, z2, alpha, red, green, blue, innerThickness, outerThickness, true, true, false, true);
                    quad(pose, vertexConsumer, x1, z1, sectionY, x2, z2, alpha, red, green, blue, innerThickness, outerThickness, false, true, false, false);
                }
            }
        }
    }

    @Override
    public ResourceLocation getTextureLocation(ColoredLightningBolt entity) {
        return NarakaTextures.LOCATION_BLOCKS;
    }

    private static void quad(
            PoseStack.Pose pose,
            VertexConsumer buffer,
            float x1,
            float z1,
            int sectionY,
            float x2,
            float z2,
            float alpha,
            float red,
            float green,
            float blue,
            float innerThickness,
            float outerThickness,
            boolean addThicknessLeftSideX,
            boolean addThicknessLeftSideZ,
            boolean addThicknessRightSideX,
            boolean addThicknessRightSideZ
    ) {
        buffer.addVertex(
                        pose,
                        x1 + (addThicknessLeftSideX ? outerThickness : -outerThickness),
                        (float) (sectionY * 16),
                        z1 + (addThicknessLeftSideZ ? outerThickness : -outerThickness)
                )
                .setColor(red, green, blue, alpha);
        buffer.addVertex(
                        pose,
                        x2 + (addThicknessLeftSideX ? innerThickness : -innerThickness),
                        (float) ((sectionY + 1) * 16),
                        z2 + (addThicknessLeftSideZ ? innerThickness : -innerThickness)
                )
                .setColor(red, green, blue, alpha);
        buffer.addVertex(
                        pose,
                        x2 + (addThicknessRightSideX ? innerThickness : -innerThickness),
                        (float) ((sectionY + 1) * 16),
                        z2 + (addThicknessRightSideZ ? innerThickness : -innerThickness)
                )
                .setColor(red, green, blue, alpha);
        buffer.addVertex(
                        pose,
                        x1 + (addThicknessRightSideX ? outerThickness : -outerThickness),
                        (float) (sectionY * 16),
                        z1 + (addThicknessRightSideZ ? outerThickness : -outerThickness)
                )
                .setColor(red, green, blue, alpha);
    }
}
