package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Vec3i;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public interface VertexConsumerExtension extends VertexConsumer {
    default void naraka$putBulkData(PoseStack.Pose poseEntry, BakedQuad quad, float red, float green, float blue, float alpha, int combinedLight, int combinedOverlay) {
        this.naraka$putBulkData(
                poseEntry,
                quad,
                new float[]{1.0F, 1.0F, 1.0F, 1.0F},
                red,
                green,
                blue,
                alpha,
                new int[]{combinedLight, combinedLight, combinedLight, combinedLight},
                combinedOverlay,
                false
        );
    }

    default void naraka$putBulkData(
            PoseStack.Pose poseEntry, BakedQuad quad, float[] colorMuls, float red, float green, float blue, float alpha, int[] combinedLights, int combinedOverlay, boolean mulColor
    ) {
        float[] fs = new float[]{colorMuls[0], colorMuls[1], colorMuls[2], colorMuls[3]};
        int[] is = new int[]{combinedLights[0], combinedLights[1], combinedLights[2], combinedLights[3]};
        int[] js = quad.getVertices();
        Vec3i vec3i = quad.getDirection().getNormal();
        Matrix4f matrix4f = poseEntry.pose();
        Vector3f vector3f = poseEntry.normal().transform(new Vector3f(vec3i.getX(), vec3i.getY(), vec3i.getZ()));
        int i = 8;
        int j = js.length / 8;

        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            ByteBuffer byteBuffer = memoryStack.malloc(DefaultVertexFormat.BLOCK.getVertexSize());
            IntBuffer intBuffer = byteBuffer.asIntBuffer();

            for (int k = 0; k < j; k++) {
                intBuffer.clear();
                intBuffer.put(js, k * 8, 8);
                float f = byteBuffer.getFloat(0);
                float g = byteBuffer.getFloat(4);
                float h = byteBuffer.getFloat(8);
                float o;
                float p;
                float q;
                if (mulColor) {
                    float l = (byteBuffer.get(12) & 255) / 255.0F;
                    float m = (byteBuffer.get(13) & 255) / 255.0F;
                    float n = (byteBuffer.get(14) & 255) / 255.0F;
                    o = l * fs[k] * red;
                    p = m * fs[k] * green;
                    q = n * fs[k] * blue;
                } else {
                    o = fs[k] * red;
                    p = fs[k] * green;
                    q = fs[k] * blue;
                }

                int r = is[k];
                float m = byteBuffer.getFloat(16);
                float n = byteBuffer.getFloat(20);
                Vector4f vector4f = matrix4f.transform(new Vector4f(f, g, h, 1.0F));
                this.vertex(vector4f.x(), vector4f.y(), vector4f.z(), o, p, q, alpha, m, n, combinedOverlay, r, vector3f.x(), vector3f.y(), vector3f.z());
            }
        }
    }
}
