package com.yummy.naraka.world.entity.animation;

import com.yummy.naraka.NarakaMod;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class AnimationLocations {
    private static final Set<ResourceLocation> REGISTERED = new HashSet<>();

    public static final ResourceLocation WALKING = create("herobrine", "walking");
    public static final ResourceLocation COMBO_ATTACK_1 = create("herobrine", "combo_attack_1");
    public static final ResourceLocation COMBO_ATTACK_2 = create("herobrine", "combo_attack_2");
    public static final ResourceLocation COMBO_ATTACK_3 = create("herobrine", "combo_attack_3");
    public static final ResourceLocation COMBO_ATTACK_4 = create("herobrine", "combo_attack_4");
    public static final ResourceLocation COMBO_ATTACK_5 = create("herobrine", "combo_attack_5");
    public static final ResourceLocation RUSH = create("herobrine", "rush");
    public static final ResourceLocation RUSH_SUCCEED = create("herobrine", "rush.succeed");
    public static final ResourceLocation RUSH_FAILED = create("herobrine", "rush.failed");
    public static final ResourceLocation THROW_NARAKA_FIREBALL = create("herobrine", "throw_naraka_fireball");
    public static final ResourceLocation STIGMATIZE_ENTITIES_START = create("herobrine", "stigmatize_entities.start");
    public static final ResourceLocation STIGMATIZE_ENTITIES = create("herobrine", "stigmatize_entities");
    public static final ResourceLocation STIGMATIZE_ENTITIES_END = create("herobrine", "stigmatize_entities.end");
    public static final ResourceLocation BLOCKING = create("herobrine", "blocking");
    public static final ResourceLocation STAGGERING = create("herobrine", "staggering");
    public static final ResourceLocation STAGGERING_PHASE_2 = create("herobrine", "staggering.enter_phase_2");
    public static final ResourceLocation ENTER_PHASE_2 = create("herobrine", "enter_phase_2");
    public static final ResourceLocation IDLE = create("herobrine", "idle");
    public static final ResourceLocation SHADOW_SUMMONED = create("shadow_herobrine", "summoned");

    public static final ResourceLocation ENTER_PHASE_3 = create("final_herobrine", "enter_phase_3");
    public static final ResourceLocation PHASE_3_IDLE = create("final_herobrine", "idle");
    public static final ResourceLocation DYING = create("final_herobrine", "dying");
    public static final ResourceLocation CHZZK = create("final_herobrine", "chzzk");
    public static final ResourceLocation HIDDEN_CHZZK = create("final_herobrine", "hidden_chzzk");

    public static final ResourceLocation STORM = create("final_herobrine", "storm");
    public static final ResourceLocation CARPET_BOMBING = create("final_herobrine", "carpet_bombing");
    public static final ResourceLocation EXPLOSION = create("final_herobrine", "explosion");
    public static final ResourceLocation EARTH_SHOCK = create("final_herobrine", "earth_shock");
    public static final ResourceLocation PARRYING = create("final_herobrine", "parrying");
    public static final ResourceLocation PARRYING_SUCCEED = create("final_herobrine", "parrying.succeed");
    public static final ResourceLocation PARRYING_FAILED = create("final_herobrine", "parrying.failed");

    public static final ResourceLocation FINAL_COMBO_ATTACK_1 = create("final_herobrine", "combo_attack_1");
    public static final ResourceLocation FINAL_COMBO_ATTACK_2 = create("final_herobrine", "combo_attack_2");
    public static final ResourceLocation FINAL_COMBO_ATTACK_3 = create("final_herobrine", "combo_attack_3");
    public static final ResourceLocation FINAL_COMBO_ATTACK_1_RETURN = create("final_herobrine", "combo_attack_1.return");
    public static final ResourceLocation FINAL_COMBO_ATTACK_2_RETURN = create("final_herobrine", "combo_attack_2.return");
    public static final ResourceLocation PICKAXE_SLASH_SINGLE = create("final_herobrine", "pickaxe_slash.single");
    public static final ResourceLocation PICKAXE_SLASH_TRIPLE = create("final_herobrine", "pickaxe_slash.triple");

    private static ResourceLocation create(String entity, String name) {
        ResourceLocation location = NarakaMod.location("animation/" + entity, name);
        REGISTERED.add(location);
        return location;
    }

    public static void checkMappings(Set<ResourceLocation> locations) {
        REGISTERED.stream()
                .filter(location -> !locations.contains(location))
                .forEach(AnimationLocations::warn);
    }

    private static void warn(ResourceLocation location) {
        NarakaMod.LOGGER.warn("{} doesn't have any animation mapping", location);
    }
}
