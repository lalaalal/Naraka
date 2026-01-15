package com.yummy.naraka.client.renderer.entity.state;

import com.yummy.naraka.world.entity.SkillUsingMob;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class SkillUsingMobRenderState extends LivingEntityRenderState {
    public boolean isIdle = false;
    public boolean doWalkAnimation = true;
    public Map<Identifier, AnimationState> animationStates = new HashMap<>();

    public void setupAnimationStates(SkillUsingMob mob) {
        mob.getAnimations().forEach(animationIdentifier -> {
            AnimationState animationState = new AnimationState();
            animationState.copyFrom(mob.getAnimationState(animationIdentifier));
            animationStates.put(animationIdentifier, animationState);
        });
    }
}
