package com.yummy.naraka.client.event;

import com.yummy.naraka.event.Event;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class ClientEvents {
    public static final Event<ClientState<Minecraft>> TICK_PRE = create();
    public static final Event<ClientState<Minecraft>> TICK_POST = create();
    public static final Event<ClientState<Minecraft>> CLIENT_STOPPING = create();

    static <T> Event<ClientState<T>> create() {
        return Event.create(listeners -> instance -> {
            for (ClientState<T> listener : listeners)
                listener.run(instance);
        });
    }

    public interface ClientState<T> {
        void run(T instance);
    }
}
