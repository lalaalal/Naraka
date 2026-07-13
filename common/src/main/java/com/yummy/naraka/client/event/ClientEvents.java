package com.yummy.naraka.client.event;

import com.yummy.naraka.event.Event;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;

public class ClientEvents {
    public static final Event<ClientState<Minecraft>> TICK_PRE = client();
    public static final Event<ClientState<Minecraft>> TICK_POST = client();

    public static final Event<ClientState<Minecraft>> CLIENT_STOPPING = client();
    public static final Event<CameraSetup> CAMERA_SETUP = Event.create(listeners -> (context, entity, deltaTracker) -> {
        for (CameraSetup listener : listeners)
            listener.setup(context, entity, deltaTracker);
    });

    /**
     * @see com.yummy.naraka.mixin.client.ClientHandshakePacketListenerImplMixin
     */
    public static final Event<Runnable> LOGIN = Event.simple();

    private static <T> Event<ClientState<T>> client() {
        return Event.create(listeners -> instance -> {
            for (ClientState<T> listener : listeners)
                listener.run(instance);
        });
    }

    @FunctionalInterface
    public interface ClientState<T> {
        void run(T instance);
    }

    @FunctionalInterface
    public interface CameraSetup {
        void setup(Context context, Entity entity, DeltaTracker deltaTracker);

        interface Context {
            Camera getCamera();

            void move(float zoom, float dy, float dx);

            void setRotation(float yRot, float xRot);
        }
    }
}
