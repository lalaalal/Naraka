package com.yummy.naraka.network;

import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class NetworkManager {
    private static final ClientboundNetworkManager CLIENTBOUND = MethodInvoker.of(NetworkManager.class, "clientbound")
            .invoke().result(ClientboundNetworkManager.class);

    public static ClientboundNetworkManager clientbound() {
        return CLIENTBOUND;
    }

    public static void initialize() {

    }

    @FunctionalInterface
    public interface PacketHandler<T extends CustomPacketPayload<T>> {
        void handle(T value, Context context);
    }

    @FunctionalInterface
    public interface Context {
        Player player();

        default RegistryAccess registryAccess() {
            return level().registryAccess();
        }

        default Level level() {
            return player().level();
        }
    }
}
