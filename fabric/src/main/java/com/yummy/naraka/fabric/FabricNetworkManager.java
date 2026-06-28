package com.yummy.naraka.fabric;

import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.network.ClientboundNetworkManager;
import com.yummy.naraka.network.CustomPacketPayload;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.ServerboundNetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

@SuppressWarnings("unused")
public class FabricNetworkManager {
    private static final ClientboundNetworkManager CLIENTBOUND = new FabricClientboundNetworkManager();
    private static final ServerboundNetworkManager SERVERBOUND = new FabricServerboundNetworkManager();

    @MethodProxy(NetworkManager.class)
    public static ClientboundNetworkManager clientbound() {
        return CLIENTBOUND;
    }

    @MethodProxy(NetworkManager.class)
    public static ServerboundNetworkManager serverbound() {
        return SERVERBOUND;
    }

    private static class FabricPacketProxy<T extends CustomPacketPayload<T>> implements FabricPacket {
        private final PacketType<FabricPacketProxy<T>> fabricPacketType;
        private final T payload;

        public FabricPacketProxy(T payload) {
            this.payload = payload;
            this.fabricPacketType = createType(payload.type());
        }

        public static <T extends CustomPacketPayload<T>> PacketType<FabricPacketProxy<T>> createType(CustomPacketPayload.Type<T> type) {
            return PacketType.create(type.id(), buf -> new FabricPacketProxy<>(type.decode(buf)));
        }

        public T payload() {
            return payload;
        }

        @Override
        public void write(FriendlyByteBuf buf) {
            payload.type().encode(payload, buf);
        }

        @Override
        public PacketType<?> getType() {
            return fabricPacketType;
        }
    }

    private static class FabricServerboundNetworkManager implements ServerboundNetworkManager {
        @Override
        public <T extends CustomPacketPayload<T>> void register(CustomPacketPayload.Type<T> type, NetworkManager.PacketHandler<T> handler) {
            ServerPlayNetworking.registerGlobalReceiver(FabricPacketProxy.createType(type), (proxy, player, sender) -> {
                handler.handle(proxy.payload(), () -> player);
            });
        }

        @Override
        public <T extends CustomPacketPayload<T>> void send(T payload) {
            ClientPlayNetworking.send(new FabricPacketProxy<>(payload));
        }
    }

    private static class FabricClientboundNetworkManager implements ClientboundNetworkManager {
        @Override
        public <T extends CustomPacketPayload<T>> void define(CustomPacketPayload.Type<T> type) {
            ServerPlayNetworking.registerGlobalReceiver(FabricPacketProxy.createType(type), (proxy, player, sender) -> {
            });
        }

        @Override
        public <T extends CustomPacketPayload<T>> void register(CustomPacketPayload.Type<T> type, NetworkManager.PacketHandler<T> handler) {
            ClientPlayNetworking.registerGlobalReceiver(FabricPacketProxy.createType(type), (proxy, player, sender) -> {
                handler.handle(proxy.payload(), () -> player);
            });
        }

        @Override
        public <T extends CustomPacketPayload<T>> void send(ServerPlayer player, T payload) {
            ServerPlayNetworking.send(player, new FabricPacketProxy<>(payload));
        }
    }
}
