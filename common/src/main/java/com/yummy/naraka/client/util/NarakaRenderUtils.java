package com.yummy.naraka.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.util.NarakaUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Environment(EnvType.CLIENT)
public class NarakaRenderUtils {
    public static final int MAX_TAIL_ALPHA = 0xff;
    public static final float SIN_45 = (float) Math.sin(Math.PI / 4);

    private static final List<Vector3f> VERTICAL = List.of(
            new Vector3f(0.5f, 0, 0.5f),
            new Vector3f(0.5f, 0, -0.5f),
            new Vector3f(-0.5f, 0, -0.5f),
            new Vector3f(-0.5f, 0, 0.5f)
    );

    private static final List<Vector3f> HORIZONTAL_X = List.of(
            new Vector3f(0, 0, -0.5f),
            new Vector3f(0, 1, -0.5f),
            new Vector3f(0, 1, 0.5f),
            new Vector3f(0, 0, 0.5f)
    );

    private static final List<Vector3f> HORIZONTAL_Z = List.of(
            new Vector3f(-0.5f, 0, 0),
            new Vector3f(-0.5f, 1, 0),
            new Vector3f(0.5f, 1, 0),
            new Vector3f(0.5f, 0, 0)
    );

    private static final Map<Direction.Axis, List<Vector3f>> VERTEX_MAPPINGS = Map.of(
            Direction.Axis.Y, VERTICAL,
            Direction.Axis.X, HORIZONTAL_X,
            Direction.Axis.Z, HORIZONTAL_Z
    );

    public static final List<Vector2f> DEFAULT_UVS = createUVList(0, 0, 1, 1);

    private static List<Vector2f> createUVList(float u, float v, float width, float height) {
        return List.of(
                new Vector2f(u, v + height),
                new Vector2f(u, v),
                new Vector2f(u + width, v),
                new Vector2f(u + width, v + height)
        );
    }

    public static void vertices(VertexConsumer vertexConsumer, PoseStack.Pose pose, List<Vector3f> vertices, List<Vector2f> uvs, int packedLight, int packedOverlay, int color, Direction direction, boolean reverse) {
        Vec3i normal = direction.getUnitVec3i();
        NarakaUtils.iterate(vertices, uvs, (vertex, uv) -> {
            vertexConsumer.addVertex(pose, vertex)
                    .setColor(color)
                    .setLight(packedLight)
                    .setOverlay(packedOverlay)
                    .setUv(uv.x, uv.y)
                    .setNormal(pose, normal.getX(), normal.getY(), normal.getZ());
        }, reverse);
    }

    private static Direction getNormal(Direction.Axis axis) {
        if (axis.isVertical())
            return Direction.UP;
        if (axis == Direction.Axis.X)
            return Direction.SOUTH;
        return Direction.EAST;
    }

    public static void renderFlatImage(PoseStack.Pose pose, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color, Direction.Axis face) {
        vertices(vertexConsumer, pose, VERTEX_MAPPINGS.get(face), DEFAULT_UVS, packedLight, packedOverlay, color, getNormal(face), false);
    }

    public static void renderFlatImage(PoseStack.Pose pose, VertexConsumer vertexConsumer, List<Vector3f> vertices, float u, float v, float width, float height, int packedLight, int packedOverlay, int color) {
        List<Vector2f> uvs = createUVList(u, v, width, height);
        vertices(vertexConsumer, pose, vertices, uvs, packedLight, packedOverlay, color, Direction.UP, false);
        vertices(vertexConsumer, pose, vertices, uvs, packedLight, packedOverlay, color, Direction.UP, true);
    }

    public static Vector3f vector3f(Vec3 vec3) {
        return new Vector3f((float) vec3.x, (float) vec3.y, (float) vec3.z);
    }

    public static void renderTailPart(PoseStack.Pose pose, VertexConsumer vertexConsumer, Vector3f from, Vector3f to, float tailWidth, float index, float size, int color) {
        NarakaRenderUtils.renderFlatImage(pose, vertexConsumer,
                createVertices(from, to, tailWidth, NarakaRenderUtils::modifyY), index, index, size, size,
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, color
        );
    }

    public static List<Vector3f> createVertices(Vector3f from, Vector3f to, float interval, BiFunction<Vector3f, Float, Vector3f> modifier) {
        return List.of(
                modifier.apply(from, interval),
                modifier.apply(from, -interval),
                modifier.apply(to, -interval),
                modifier.apply(to, interval)
        );
    }

    public static Vector3f modifyX(Vector3f vector, float interval) {
        return vector.add(interval, 0, 0, new Vector3f());
    }

    public static Vector3f modifyY(Vector3f vector, float interval) {
        return vector.add(0, interval, 0, new Vector3f());
    }

    public static Vector3f modifyZ(Vector3f vector, float interval) {
        return vector.add(0, 0, interval, new Vector3f());
    }

    public static boolean isCurrentPlayer(LivingEntity livingEntity) {
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return false;
        return player.getUUID().equals(livingEntity.getUUID());
    }

    public static <S> void submitModelWithFoilRenderTypes(Model<? super S> model, S renderState, PoseStack poseStack, RenderType renderType, SubmitNodeCollector submitNodeCollector, int packedLight, boolean hasFoil) {
        List<RenderType> renderTypes = ItemRenderer.getFoilRenderTypes(renderType, false, hasFoil);
        for (int index = 0; index < renderTypes.size(); index++) {
            submitNodeCollector.order(index).submitModel(
                    model,
                    renderState,
                    poseStack,
                    renderTypes.get(index),
                    packedLight, OverlayTexture.NO_OVERLAY, -1,
                    null,
                    0,
                    null
            );
        }
    }
}
