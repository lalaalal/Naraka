package com.yummy.naraka.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.util.NarakaUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class NarakaRenderUtils {
    private static final List<Vector3f> VERTICAL = List.of(
            new Vector3f(-0.5f, 0, 0.5f),
            new Vector3f(-0.5f, 0, -0.5f),
            new Vector3f(0.5f, 0, -0.5f),
            new Vector3f(0.5f, 0, 0.5f)
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

    private static final Map<Direction, List<Vector3f>> VERTICES = Map.of(
            Direction.UP, VERTICAL,
            Direction.DOWN, VERTICAL,
            Direction.EAST, HORIZONTAL_X,
            Direction.WEST, HORIZONTAL_X,
            Direction.NORTH, HORIZONTAL_Z,
            Direction.SOUTH, HORIZONTAL_Z
    );

    /**
     * Add 4 vertices in anti-clockwise from left-top based on a positive direction
     *
     * @param positions Positions, size must be 4
     */
    public static void vertices(VertexConsumer vertexConsumer, PoseStack.Pose pose, List<Vector3f> positions, float u, float v, float width, float height, int packedLight, int packedOverlay, int color, Direction direction) {
        Vec3i normal = direction.getUnitVec3i();
        List<Vector2f> uvs = List.of(
                new Vector2f(u, v + height),
                new Vector2f(u, v),
                new Vector2f(u + width, v),
                new Vector2f(u + width, v + height)
        );
        NarakaUtils.iterate(positions, uvs, (position, uv) -> {
            vertexConsumer.addVertex(pose, position)
                    .setColor(color)
                    .setLight(packedLight)
                    .setOverlay(packedOverlay)
                    .setUv(uv.x, uv.y)
                    .setNormal(pose, normal.getX(), normal.getY(), normal.getZ());
        }, normal.getX() > 0 || normal.getY() > 0 || normal.getZ() > 0);
    }

    public static void vertices(VertexConsumer vertexConsumer, PoseStack.Pose pose, List<Vector3f> positions, int packedLight, int packedOverlay, int color, Direction direction) {
        vertices(vertexConsumer, pose, positions, 0, 0, 1, 1, packedLight, packedOverlay, color, direction);
    }

    public static void renderFlatImage(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color, Direction face) {
        NarakaRenderUtils.vertices(vertexConsumer, poseStack.last(), VERTICES.get(face), packedLight, packedOverlay, color, Direction.UP);
        if (face.getAxis().isVertical())
            NarakaRenderUtils.vertices(vertexConsumer, poseStack.last(), VERTICES.get(face), packedLight, packedOverlay, color, Direction.DOWN);
    }
}
