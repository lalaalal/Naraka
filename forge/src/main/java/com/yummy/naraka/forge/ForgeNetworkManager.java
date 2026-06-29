package com.yummy.naraka.forge;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.network.ClientboundNetworkManager;
import com.yummy.naraka.network.CustomPacketPayload;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.ServerboundNetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class ForgeNetworkManager implements NarakaEventBus {
    private static final String VERSION = "3";
    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            NarakaMod.location("main"),
            () -> VERSION,
            VERSION::equals,
            VERSION::equals
    );
    private static int id = 0;

    private static final ClientboundNetworkManager CLIENTBOUND = new ForgeClientboundNetworkManager();
    private static final ServerboundNetworkManager SERVERBOUND = new ForgeServerboundNetworkManager();

    @MethodProxy(NetworkManager.class)
    public static ClientboundNetworkManager clientbound() {
        return CLIENTBOUND;
    }

    @MethodProxy(NetworkManager.class)
    public static ServerboundNetworkManager serverbound() {
        return SERVERBOUND;
    }

    private static NetworkManager.Context createContext(Supplier<NetworkEvent.Context> contextSupplier) {
        return () -> {
            NetworkEvent.Context context = contextSupplier.get();
            if (context.getSender() != null)
                return context.getSender();
            if (context.getDirection().getReceptionSide().isClient() && Minecraft.getInstance().player != null)
                return Minecraft.getInstance().player;
            throw new IllegalStateException("Cannot find player");
        };
    }

    private static class ForgeServerboundNetworkManager implements ServerboundNetworkManager {
        private <T extends CustomPacketPayload<T>> BiConsumer<T, Supplier<NetworkEvent.Context>> handleMessage(NetworkManager.PacketHandler<T> handler) {
            return (msg, context) -> {
                context.get().enqueueWork(() -> handler.handle(msg, createContext(context)));
                context.get().setPacketHandled(true);
            };
        }

        @Override
        public <T extends CustomPacketPayload<T>> void register(CustomPacketPayload.Type<T> type, NetworkManager.PacketHandler<T> handler) {
            INSTANCE.registerMessage(id++, type.classType(), type::encode, type::decode,
                    handleMessage(handler),
                    Optional.of(NetworkDirection.PLAY_TO_SERVER)
            );
        }

        @Override
        public <T extends CustomPacketPayload<T>> void send(T payload) {
            INSTANCE.sendToServer(payload);
        }
    }

    private static class ForgeClientboundNetworkManager implements ClientboundNetworkManager {
        private <T extends CustomPacketPayload<T>> BiConsumer<T, Supplier<NetworkEvent.Context>> handleMessage(NetworkManager.PacketHandler<T> handler) {
            return (msg, context) -> {
                context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                    handler.handle(msg, createContext(context));
                }));
                context.get().setPacketHandled(true);
            };
        }

        @Override
        public <T extends CustomPacketPayload<T>> void define(CustomPacketPayload.Type<T> type) {
            INSTANCE.registerMessage(id++, type.classType(), type::encode, type::decode,
                    (msg, context) -> {
                        context.get().setPacketHandled(true);
                    },
                    Optional.of(NetworkDirection.PLAY_TO_CLIENT)
            );
        }

        @Override
        public <T extends CustomPacketPayload<T>> void register(CustomPacketPayload.Type<T> type, NetworkManager.PacketHandler<T> handler) {
            INSTANCE.registerMessage(id++, type.classType(), type::encode, type::decode,
                    handleMessage(handler),
                    Optional.of(NetworkDirection.PLAY_TO_CLIENT)
            );
        }

        @Override
        public <T extends CustomPacketPayload<T>> void send(ServerPlayer player, T payload) {
            INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), payload);
        }
    }
}
