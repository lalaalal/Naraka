package com.yummy.naraka.client.model;

import com.yummy.naraka.client.animation.AnimationMapper;
import com.yummy.naraka.client.renderer.entity.state.SkillUsingMobRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.Identifier;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public abstract class SkillUsingModModel<T extends SkillUsingMobRenderState> extends EntityModel<T> {
    protected final Map<Identifier, KeyframeAnimation> animations = new HashMap<>();
    @Nullable
    protected final KeyframeAnimation walkingAnimation;

    protected SkillUsingModModel(ModelPart root, String entityName) {
        super(root);
        this.walkingAnimation = null;
        AnimationMapper.keySet().stream()
                .filter(key -> key.getPath().startsWith("animation/" + entityName))
                .forEach(key -> animations.put(key, AnimationMapper.get(key).bake(root)));
    }

    protected SkillUsingModModel(ModelPart root, AnimationDefinition walkingAnimationDefinition, String entityName) {
        super(root);
        this.walkingAnimation = walkingAnimationDefinition.bake(root);
        AnimationMapper.keySet().stream()
                .filter(key -> key.getPath().startsWith("animation/" + entityName))
                .forEach(key -> animations.put(key, AnimationMapper.get(key).bake(root)));
    }

    protected void animateWalk(T renderState, float walkTimeMultiplier, float walkSpeedModifier) {
        if (walkingAnimation != null)
            walkingAnimation.applyWalk(renderState.walkAnimationPos, renderState.walkAnimationSpeed, walkTimeMultiplier, walkSpeedModifier);
    }

    protected void animate(T renderState) {
        renderState.animationStates.forEach((animationLocation, animationState) -> {
            if (animations.containsKey(animationLocation)) {
                KeyframeAnimation animation = animations.get(animationLocation);
                animation.apply(animationState, renderState.ageInTicks);
            }
        });
    }
}
