package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.renderer.FlameFeatureSubmitExtension;
import com.yummy.naraka.client.renderer.PurifiedSoulFireSubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import net.minecraft.client.renderer.feature.phase.SimpleFeatureRenderPhase;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SubmitNodeCollection.class)
public abstract class SubmitNodeCollectionMixin implements PurifiedSoulFireSubmitNodeCollection {
    @Shadow
    @Final
    public SimpleFeatureRenderPhase solid;

    @Override
    public void naraka$submitPurifiedSoulFlame(PoseStack poseStack, EntityRenderState entityRenderState, Quaternionf quaternionf) {
        FlameFeatureRenderer.Submit submit = new FlameFeatureRenderer.Submit(poseStack.last().copy(), entityRenderState, quaternionf);
        ((FlameFeatureSubmitExtension) (Object) submit).naraka$setPurifiedSoulFire();
        this.solid.submit(submit);
    }
}
