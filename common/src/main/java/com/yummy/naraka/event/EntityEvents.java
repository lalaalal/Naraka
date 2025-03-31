package com.yummy.naraka.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public final class EntityEvents {
    public static final Event<LivingDeath> LIVING_DEATH = Event.create(listeners -> (entity, source) -> {
        boolean result = false;
        for (LivingDeath listener : listeners)
            result |= listener.die(entity, source);
        return result;
    });

    public static final Event<PlayerJoin> PLAYER_JOIN = Event.create(listeners -> player -> {
        for (PlayerJoin listener : listeners)
            listener.join(player);
    });

    @FunctionalInterface
    public interface LivingDeath {
        /**
         * Handle on entity die
         *
         * @param entity Entity going to die
         * @param source Damage source
         * @return True if accepting death
         */
        boolean die(LivingEntity entity, DamageSource source);
    }

    @FunctionalInterface
    public interface PlayerJoin {
        void join(ServerPlayer player);
    }
}
