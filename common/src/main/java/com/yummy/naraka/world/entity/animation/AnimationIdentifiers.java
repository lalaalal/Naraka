package com.yummy.naraka.world.entity.animation;

import com.mojang.logging.LogUtils;
import com.yummy.naraka.NarakaMod;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class AnimationIdentifiers {
    private static final Logger LOG = LogUtils.getLogger();

    private static final Set<Identifier> REGISTERED = new HashSet<>();

    public static Identifier create(String entity, String name) {
        Identifier location = NarakaMod.identifier("animation/" + entity, name);
        REGISTERED.add(location);
        return location;
    }

    public static void checkMappings(Set<Identifier> locations) {
        REGISTERED.stream()
                .filter(location -> !locations.contains(location))
                .forEach(AnimationIdentifiers::warn);
    }

    private static void warn(Identifier location) {
        LOG.warn("{} doesn't have any animation mapping", location);
    }
}
