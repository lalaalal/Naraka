package com.yummy.naraka.forge.client;

import com.yummy.naraka.forge.ForgeNetworkManager;
import com.yummy.naraka.network.CustomPacketPayload;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.PacketRegistrar;
import com.yummy.naraka.network.ServerboundNetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ForgeClientNetworkManager {
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

    public static class ForgeServerboundNetworkManager implements ServerboundNetworkManager {
        @Override
        public <T extends CustomPacketPayload<T>> void send(T payload) {
            ForgeNetworkManager.INSTANCE.sendToServer(payload);
        }
    }

    public static class ForgeClientPacketRegistrar implements PacketRegistrar.Client {
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

        }

        @Override
        public <T extends CustomPacketPayload<T>> void register(CustomPacketPayload.Type<T> type, NetworkManager.PacketHandler<T> handler) {
            ForgeNetworkManager.INSTANCE.registerMessage(ForgeNetworkManager.getNextId(), type.classType(), type::encode, type::decode,
                    handleMessage(handler),
                    Optional.of(NetworkDirection.PLAY_TO_CLIENT)
            );
        }
    }
}
