package com.yummy.naraka.client.animation;

import com.yummy.naraka.client.animation.herobrine.*;
import com.yummy.naraka.client.animation.naraka_pickaxe.NarakaPickaxeAnimation;
import com.yummy.naraka.world.entity.animation.AnimationLocations;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class AnimationMapper {
    private static final Map<ResourceLocation, AnimationDefinition> MAPPER = new HashMap<>();

    public static void register(ResourceLocation key, AnimationDefinition animationDefinition) {
        MAPPER.putIfAbsent(key, animationDefinition);
    }

    public static Set<ResourceLocation> keySet() {
        return MAPPER.keySet();
    }

    public static AnimationDefinition get(ResourceLocation key) {
        return MAPPER.get(key);
    }

    public static void initialize() {
        HerobrineAnimation.initialize();
        HerobrineSkillAnimation.initialize();
        HerobrineComboAttackAnimation.initialize();

        FinalHerobrineAnimation.initialize();
        FinalHerobrineSkillAnimation.initialize();
        FinalHerobrineComboAnimation.initialize();

        NarakaPickaxeAnimation.initialize();

        DiamondGolemAnimation.initialize();

        AnimationLocations.checkMappings(MAPPER.keySet());
    }
}
