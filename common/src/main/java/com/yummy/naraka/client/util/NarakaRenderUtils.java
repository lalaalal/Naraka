package com.yummy.naraka.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.util.NarakaUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class NarakaRenderUtils {
    public static final float SIN_45 = (float) Math.sin(Math.PI / 4);

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

    private static final Map<Direction, List<Vector3f>> VERTEX_MAPPINGS = Map.of(
            Direction.UP, VERTICAL,
            Direction.DOWN, VERTICAL,
            Direction.EAST, HORIZONTAL_X,
            Direction.WEST, HORIZONTAL_X,
            Direction.NORTH, HORIZONTAL_Z,
            Direction.SOUTH, HORIZONTAL_Z
    );

    private static final Map<Direction, List<Vector3f>> OPPOSITE_VERTEX_MAPPINGS = Map.of(
            Direction.UP, VERTICAL.reversed(),
            Direction.DOWN, VERTICAL.reversed(),
            Direction.EAST, HORIZONTAL_X.reversed(),
            Direction.WEST, HORIZONTAL_X.reversed(),
            Direction.NORTH, HORIZONTAL_Z.reversed(),
            Direction.SOUTH, HORIZONTAL_Z.reversed()
    );

    public static final List<Vector2f> DEFAULT_UVS = createUVList(0, 0, 1, 1);
    public static final List<Vector2f> OPPOSITE_UVS = DEFAULT_UVS.reversed();

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

    public static void renderFlatImage(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color, Direction face) {
        vertices(vertexConsumer, poseStack.last(), VERTEX_MAPPINGS.get(face), DEFAULT_UVS, packedLight, packedOverlay, color, Direction.UP, false);
        vertices(vertexConsumer, poseStack.last(), OPPOSITE_VERTEX_MAPPINGS.get(face), OPPOSITE_UVS, packedLight, packedOverlay, color, Direction.UP, false);
    }

    public static void renderFlatImage(PoseStack poseStack, VertexConsumer vertexConsumer, List<Vector3f> vertices, float u, float v, float width, float height, int packedLight, int packedOverlay, int color) {
        List<Vector2f> uvs = createUVList(u, v, width, height);
        vertices(vertexConsumer, poseStack.last(), vertices, uvs, packedLight, packedOverlay, color, Direction.UP, false);
        vertices(vertexConsumer, poseStack.last(), vertices, uvs, packedLight, packedOverlay, color, Direction.UP, true);
    }

    public static Vector3f vector3f(Vec3 vec3) {
        return new Vector3f((float) vec3.x, (float) vec3.y, (float) vec3.z);
    }
}
