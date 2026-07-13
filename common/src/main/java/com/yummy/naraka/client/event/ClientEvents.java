package com.yummy.naraka.client.event;

import com.yummy.naraka.event.Event;
import com.yummy.naraka.event.PlatformInvokeRequired;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;

import java.util.function.Consumer;

public class ClientEvents {
    @PlatformInvokeRequired
    public static final Event<ClientState<Minecraft>> TICK_PRE = client();
    @PlatformInvokeRequired
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

    public static final Event<ItemTooltip> ITEM_TOOLTIP_TOP = itemTooltip();
    public static final Event<ItemTooltip> ITEM_TOOLTIP_BOTTOM = itemTooltip();

    private static Event<ItemTooltip> itemTooltip() {
        return Event.create(listeners -> (item, context, player, flag, builder) -> {
            for (ItemTooltip listener : listeners)
                listener.addToTooltip(item, context, player, flag, builder);
        });
    }

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

    @FunctionalInterface
    public interface ItemTooltip {
        void addToTooltip(DataComponentHolder item, Item.TooltipContext context, Player player, TooltipFlag tooltipFlag, Consumer<Component> builder);
    }
}
