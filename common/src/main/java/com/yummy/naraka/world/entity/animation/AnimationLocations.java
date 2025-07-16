package com.yummy.naraka.world.entity.animation;

import com.yummy.naraka.NarakaMod;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class AnimationLocations {
    private static final Set<ResourceLocation> REGISTERED = new HashSet<>();

    public static ResourceLocation create(String entity, String name) {
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
