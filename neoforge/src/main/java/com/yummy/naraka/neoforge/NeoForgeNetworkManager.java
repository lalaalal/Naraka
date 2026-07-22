package com.yummy.naraka.neoforge;

import com.yummy.naraka.Platform;
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

public final class NeoForgeNetworkManager implements NarakaEventBus {
    public static final String VERSION = "3";

    private static <T extends CustomPacketPayload> IPayloadHandler<T> wrap(NetworkManager.PacketHandler<T> handler) {
        return (payload, context) -> {
            handler.handle(payload, context::player);
        };
    }

    private static <T extends CustomPacketPayload> IPayloadHandler<T> empty() {
        return (payload, context) -> {
        };
    }

    public static class NeoForgeServerboundNetworkManager implements ServerboundNetworkManager {
        @Override
        public <T extends CustomPacketPayload> void define(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {

        }

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

    public static class NeoForgeClientboundNetworkManager implements ClientboundNetworkManager {
        @Override
        public <T extends CustomPacketPayload> void define(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
            if (Platform.getInstance().getSide() == Platform.Side.SERVER) {
                NARAKA_BUS.addListener(RegisterPayloadHandlersEvent.class, event -> {
                    event.registrar(VERSION).playToClient(type, codec, empty());
                });
            }
        }

        @Override
        public <T extends CustomPacketPayload> void register(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.PacketHandler<T> handler) {
            if (Platform.getInstance().getSide() == Platform.Side.CLIENT) {
                NARAKA_BUS.addListener(RegisterPayloadHandlersEvent.class, event -> {
                    event.registrar(VERSION).playToClient(type, codec, wrap(handler));
                });
            }
        }

        @Override
        public void send(ServerPlayer player, CustomPacketPayload payload) {
            PacketDistributor.sendToPlayer(player, payload);
        }
    }
}
