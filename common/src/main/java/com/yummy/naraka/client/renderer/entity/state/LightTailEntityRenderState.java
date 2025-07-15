package com.yummy.naraka.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

@Environment(EnvType.CLIENT)
public class LightTailEntityRenderState extends EntityRenderState {
    public List<Vector3f> tailPositions = List.of();
    public Vec3 partialTranslation = Vec3.ZERO;
    public float tailWidth;
    public int tailColor;
    public float xRot;
    public float yRot;
    public float zRot;
}
