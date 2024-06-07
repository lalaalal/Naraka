package com.yummy.naraka.client.model;

import com.yummy.naraka.client.animation.AnimationInstance;
import com.yummy.naraka.entity.Animatable;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.Map;
import java.util.function.Function;

/**
 * @param <T>
 * @author lalaalal
 */
@OnlyIn(Dist.CLIENT)
public abstract class AnimatedEntityModel<T extends Entity & Animatable> extends EntityModel<T> {
    public AnimatedEntityModel() {

    }

    public AnimatedEntityModel(Function<ResourceLocation, RenderType> renderType) {
        super(renderType);
    }

    protected abstract Map<String, ModelPart> getAnimatingModelParts();

    @Override
    public void setupAnim(T entity, float pLimbSwing, float pLimbSwingAmount, float ageInTicks, float pNetHeadYaw, float pHeadPitch) {
        AnimationInstance animationInstance = entity.getAnimation();
        animationInstance.setupAnimation(getAnimatingModelParts(), ageInTicks);
    }
}
