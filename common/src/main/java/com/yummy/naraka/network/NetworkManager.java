package com.yummy.naraka.network;

import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Collection;

public abstract class NetworkManager {
    private static final ServerboundNetworkManager SERVERBOUND = MethodInvoker.of(NetworkManager.class, "serverbound")
            .invoke().result(ServerboundNetworkManager.class);

    private static final ClientboundNetworkManager CLIENTBOUND = MethodInvoker.of(NetworkManager.class, "clientbound")
            .invoke().result(ClientboundNetworkManager.class);

    public static ServerboundNetworkManager serverbound() {
        return SERVERBOUND;
    }

    public static ClientboundNetworkManager clientbound() {
        return CLIENTBOUND;
    }

    public static void sendToClient(ServerPlayer player, CustomPacketPayload packet) {
        clientbound().send(player, packet);
    }

    public static void sendToClient(Collection<ServerPlayer> players, CustomPacketPayload payload) {
        clientbound().send(players, payload);
    }

    public static void sendToServer(CustomPacketPayload payload) {
        serverbound().send(payload);
    }

    @FunctionalInterface
    public interface PacketHandler<T extends CustomPacketPayload> {
        void handle(T value, Context context);
    }

    @FunctionalInterface
    public interface Context {
        Player player();

        default RegistryAccess registryAccess() {
            return player().registryAccess();
        }

        default Level level() {
            return player().level();
        }
    }
}
