package com.yummy.naraka.client.renderer.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class LightTailEntityRenderState extends EntityRenderState {
    public List<Vector3f> tailPositions = List.of();
    public List<Integer> tailAlphas = new ArrayList<>();
    public float tailWidth;
    public int tailColor;
    public float xRot;
    public float yRot;
    public float zRot;
}
