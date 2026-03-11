package com.yummy.naraka.client.renderer.entity.state;

import com.yummy.naraka.world.entity.SwordEffectData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;

import java.util.List;

@Environment(EnvType.CLIENT)
public class NarakaSwordRenderState extends EntityRenderState {
    public List<SwordEffectData> swordEffectData = List.of();
    public int color;
    public float maxAlpha;
    public float scale;
    public Quaternionfc rotation = new Quaternionf();
}
