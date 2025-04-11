package com.yummy.naraka.neoforge;

import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.network.ClientboundNetworkManager;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.ServerboundNetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

@SuppressWarnings("unused")
public final class NeoForgeNetworkManager implements NarakaEventBus {
    public static final String VERSION = "1";

    private static final ClientboundNetworkManager CLIENTBOUND = new NeoForgeClientboundNetworkManager();
    private static final ServerboundNetworkManager SERVERBOUND = new NeoForgeServerboundNetworkManager();

    @MethodProxy(NetworkManager.class)
    public static ClientboundNetworkManager clientbound() {
        return CLIENTBOUND;
    }

    @MethodProxy(NetworkManager.class)
    public static ServerboundNetworkManager serverbound() {
        return SERVERBOUND;
    }

    private static <T extends CustomPacketPayload> IPayloadHandler<T> wrap(NetworkManager.PacketHandler<T> handler) {
        return (payload, context) -> {
            handler.handle(payload, context::player);
        };
    }

    private static class NeoForgeServerboundNetworkManager implements ServerboundNetworkManager {
        @Override
        public <T extends CustomPacketPayload> void register(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.PacketHandler<T> handler) {
            NARAKA_BUS.addListener(RegisterPayloadHandlersEvent.class, event -> {
                event.registrar(VERSION).playToServer(type, codec, wrap(handler));
            });
        }

        @Override
        public void send(CustomPacketPayload payload) {
            PacketDistributor.sendToServer(payload);
        }
    }

    private static class NeoForgeClientboundNetworkManager implements ClientboundNetworkManager {
        @Override
        public <T extends CustomPacketPayload> void register(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.PacketHandler<T> handler) {
            NARAKA_BUS.addListener(RegisterPayloadHandlersEvent.class, event -> {
                event.registrar(VERSION).playToClient(type, codec, wrap(handler));
            });
        }

        @Override
        public void send(ServerPlayer player, CustomPacketPayload payload) {
            PacketDistributor.sendToPlayer(player, payload);
        }
    }
}
