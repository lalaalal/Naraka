package com.yummy.naraka.client.event;

import com.yummy.naraka.event.Event;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;

public class ClientEvents {
    public static final Event<ClientState<Minecraft>> TICK_PRE = create();
    public static final Event<ClientState<Minecraft>> TICK_POST = create();
    public static final Event<ClientState<Minecraft>> CLIENT_STOPPING = create();
    public static final Event<CameraSetup> CAMERA_SETUP = Event.create(listeners -> (context, level, entity, detached, thirdPersonReverse, partialTick) -> {
        for (CameraSetup listener : listeners)
            listener.setup(context, level, entity, detached, thirdPersonReverse, partialTick);
    });

    /**
     * @see com.yummy.naraka.mixin.client.ClientHandshakePacketListenerImplMixin
     */
    public static final Event<Runnable> LOGIN = Event.simple();

    public static final Event<ClientState<ClientLevel>> CHANGE_LEVEL = Event.create(listeners -> level -> {
        for (ClientState<ClientLevel> listener : listeners) {
            listener.run(level);
        }
    });

    static <T> Event<ClientState<T>> create() {
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
        void setup(Context context, BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick);

        interface Context {
            Camera getCamera();

            void move(float zoom, float dy, float dx);

            void setRotation(float yRot, float xRot);
        }
    }
}
