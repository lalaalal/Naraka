package com.yummy.naraka.event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public final class ServerEvents {
    public static final Event<ServerState<MinecraftServer>> SERVER_STARTING = ServerEvents.create();
    public static final Event<ServerState<MinecraftServer>> SERVER_STARTED = ServerEvents.create();
    public static final Event<ServerState<MinecraftServer>> SERVER_STOPPING = ServerEvents.create();
    public static final Event<ServerState<MinecraftServer>> SERVER_TICK_PRE = ServerEvents.create();
    public static final Event<ServerState<MinecraftServer>> SERVER_TICK_POST = ServerEvents.create();
    public static final Event<ServerState<ServerLevel>> SERVER_LEVEL_LOAD = ServerEvents.create();

    static <T> Event<ServerState<T>> create() {
        return Event.create(listeners -> instance -> {
            for (ServerState<T> listener : listeners)
                listener.run(instance);
        });
    }

    @FunctionalInterface
    public interface ServerState<T> {
        void run(T instance);
    }
}
