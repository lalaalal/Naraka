package com.yummy.naraka.client.animation;

import com.yummy.naraka.client.animation.herobrine.HerobrineAnimation;
import com.yummy.naraka.client.animation.herobrine.HerobrineComboAttackAnimation;
import com.yummy.naraka.client.animation.herobrine.HerobrineSkillAnimation;
import com.yummy.naraka.world.entity.animation.AnimationLocations;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class AnimationMapper {
    private static final Map<ResourceLocation, AnimationDefinition> MAPPER = new HashMap<>();

    public static void register(ResourceLocation key, AnimationDefinition animationDefinition) {
        MAPPER.putIfAbsent(key, animationDefinition);
    }

    public static AnimationDefinition get(ResourceLocation key) {
        return MAPPER.get(key);
    }

    public static void initialize() {
        HerobrineAnimation.initialize();
        HerobrineSkillAnimation.initialize();
        HerobrineComboAttackAnimation.initialize();

        AnimationLocations.checkMappings(MAPPER.keySet());
    }
}
