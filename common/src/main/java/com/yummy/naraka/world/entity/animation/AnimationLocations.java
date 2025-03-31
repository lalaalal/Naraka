package com.yummy.naraka.world.entity.animation;

import com.yummy.naraka.NarakaMod;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class AnimationLocations {
    private static final Set<ResourceLocation> REGISTERED = new HashSet<>();

    public static final ResourceLocation WALKING = create("herobrine", "walking");
    public static final ResourceLocation PUNCH_1 = create("herobrine", "punch_1");
    public static final ResourceLocation PUNCH_2 = create("herobrine", "punch_2");
    public static final ResourceLocation PUNCH_3 = create("herobrine", "punch_3");
    public static final ResourceLocation RUSH = create("herobrine", "rush");
    public static final ResourceLocation THROW_NARAKA_FIREBALL = create("herobrine", "throw_naraka_fireball");
    public static final ResourceLocation STIGMATIZE_ENTITIES = create("herobrine", "stigmatize_entities");
    public static final ResourceLocation BLOCKING = create("herobrine", "blocking");
    public static final ResourceLocation WEAKNESS = create("herobrine", "weakness");

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
