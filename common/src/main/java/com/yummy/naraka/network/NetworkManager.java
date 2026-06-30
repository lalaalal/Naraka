package com.yummy.naraka.network;

import com.yummy.naraka.service.NarakaServices;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class NetworkManager {
    public static ServerboundNetworkManager serverbound() {
        return NarakaServices.SERVERBOUND_NETWORK_MANAGER;
    }

    public static ClientboundNetworkManager clientbound() {
        return NarakaServices.CLIENTBOUND_NETWORK_MANAGER;
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
