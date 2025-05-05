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
    public static final ResourceLocation THROW_NARAKA_FIREBALL = create("herobrine", "throw_naraka_fireball");
    public static final ResourceLocation STIGMATIZE_ENTITIES = create("herobrine", "stigmatize_entities");
    public static final ResourceLocation BLOCKING = create("herobrine", "blocking");
    public static final ResourceLocation STAGGERING = create("herobrine", "staggering");
    public static final ResourceLocation IDLE = create("herobrine", "idle");

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
