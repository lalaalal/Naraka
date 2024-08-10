package com.yummy.naraka.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class NarakaNetworks {
    public static void initialize() {
        PayloadTypeRegistry.playS2C().register(SyncEntityDataPayload.TYPE, SyncEntityDataPayload.CODEC);
    }

    public static void initializeServer() {

    }

    @Environment(EnvType.CLIENT)
    public static void initializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(SyncEntityDataPayload.TYPE, SyncEntityDataPayload::handleClient);
    }
}
