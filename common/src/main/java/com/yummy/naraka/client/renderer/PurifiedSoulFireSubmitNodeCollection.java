package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.joml.Quaternionf;

import java.util.List;

@Environment(EnvType.CLIENT)
public interface PurifiedSoulFireSubmitNodeCollection {
    void naraka$submitFlame(PoseStack poseStack, EntityRenderState entityRenderState, Quaternionf quaternionf);

    List<SubmitNodeStorage.FlameSubmit> naraka$getPurifiedSoulFlameSubmits();
}
