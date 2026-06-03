package com.yummy.naraka.client.renderer.entity.state;

import com.yummy.naraka.world.entity.SwordEffectData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class NarakaSwordRenderState extends EntityRenderState {
    public List<SwordEffectData> swordEffectData = List.of();
    public Vector3fc swordEffectOffset = new Vector3f();
    public List<Float> swordEffectAlpha = new ArrayList<>();
    public int swordEffectUpdateCount;
    public int color;
    public float maxAlpha;
    public float scale;
    public Quaternionfc rotation = new Quaternionf();
}
