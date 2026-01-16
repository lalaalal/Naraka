package com.yummy.naraka.client.animation;

import com.yummy.naraka.client.animation.herobrine.*;
import com.yummy.naraka.client.animation.naraka_pickaxe.NarakaPickaxeAnimation;
import com.yummy.naraka.world.entity.animation.AnimationIdentifiers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class AnimationMapper {
    private static final Map<Identifier, AnimationDefinition> MAPPER = new HashMap<>();

    public static void register(Identifier key, AnimationDefinition animationDefinition) {
        MAPPER.putIfAbsent(key, animationDefinition);
    }

    public static Set<Identifier> keySet() {
        return MAPPER.keySet();
    }

    public static AnimationDefinition get(Identifier key) {
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

        AnimationIdentifiers.checkMappings(MAPPER.keySet());
    }
}
