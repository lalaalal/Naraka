package com.yummy.naraka.network;

import com.yummy.naraka.client.renderer.ProgressOverlayExtensionRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

import java.util.Map;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class SyncProgressOverlayExtensionHandler {
    private static final Map<SyncProgressOverlayExtensionPacket.Action, Consumer<SyncProgressOverlayExtensionPacket>> HANDLERS = Map.of(
            SyncProgressOverlayExtensionPacket.Action.REGISTER, SyncProgressOverlayExtensionHandler::handleRegister,
            SyncProgressOverlayExtensionPacket.Action.REMOVE, SyncProgressOverlayExtensionHandler::handleRemove,
            SyncProgressOverlayExtensionPacket.Action.UPDATE, SyncProgressOverlayExtensionHandler::handleUpdate
    );

    public static void handle(SyncProgressOverlayExtensionPacket packet, NetworkManager.Context context) {
        Minecraft.getInstance().execute(() -> {
            HANDLERS.get(packet.action()).accept(packet);
        });
    }

    private static void handleRegister(SyncProgressOverlayExtensionPacket packet) {
        packet.data().ifPresent(data -> {
            ProgressOverlayExtensionRenderer.INSTANCE.register(packet.uuid(), data.type());
            ProgressOverlayExtensionRenderer.INSTANCE.update(packet.uuid(), data);
        });
    }

    private static void handleRemove(SyncProgressOverlayExtensionPacket packet) {
        ProgressOverlayExtensionRenderer.INSTANCE.remove(packet.uuid());
    }

    private static void handleUpdate(SyncProgressOverlayExtensionPacket packet) {
        packet.data().ifPresent(data -> {
            ProgressOverlayExtensionRenderer.INSTANCE.update(packet.uuid(), data);
        });
    }
}
