package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.renderer.feature.FlameFeatureSubmitExtension;
import com.yummy.naraka.client.renderer.feature.PurifiedSoulFireSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(SubmitNodeCollection.class)
public abstract class SubmitNodeCollectionMixin implements PurifiedSoulFireSubmitNodeCollector {
    @Shadow
    @Final
    private List<SubmitNodeStorage.FlameSubmit> flameSubmits;

    @Override
    public void naraka$submitPurifiedSoulFlame(PoseStack poseStack, EntityRenderState entityRenderState, Quaternionf quaternionf) {
        SubmitNodeStorage.FlameSubmit submit = new SubmitNodeStorage.FlameSubmit(poseStack.last().copy(), entityRenderState, quaternionf);
        ((FlameFeatureSubmitExtension) (Object) submit).naraka$setPurifiedSoulFire();
        this.flameSubmits.add(submit);
    }
}
