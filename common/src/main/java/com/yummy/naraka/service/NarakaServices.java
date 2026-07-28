package com.yummy.naraka.service;

import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.core.registries.RegistryProxyProvider;
import com.yummy.naraka.event.CreativeModeTabEvents;
import com.yummy.naraka.event.EventInitializer;
import com.yummy.naraka.init.BiomeModificationRegistry;
import com.yummy.naraka.init.CommandRegistry;
import com.yummy.naraka.init.EntityAttributeRegistry;
import com.yummy.naraka.init.EntityDataSerializerRegistry;
import com.yummy.naraka.network.ClientboundNetworkManager;
import com.yummy.naraka.network.PacketRegistrar;
import com.yummy.naraka.world.item.SpawnEggItemProvider;

import java.util.ServiceLoader;

public class NarakaServices {
    public static final RegistryFactory REGISTRY_FACTORY = load(RegistryFactory.class);
    public static final RegistryProxyProvider REGISTRY_PROXY_PROVIDER = load(RegistryProxyProvider.class);
    public static final EventInitializer EVENT_INITIALIZER = load(EventInitializer.class);

    public static final PacketRegistrar.Server SERVER_PACKET_REGISTRAR = load(PacketRegistrar.Server.class);
    public static final ClientboundNetworkManager CLIENTBOUND_NETWORK_MANAGER = load(ClientboundNetworkManager.class);

    public static final CreativeModeTabEvents.ModifyEntriesEventFactory MODIFY_ENTRIES_EVENT_FACTORY = load(CreativeModeTabEvents.ModifyEntriesEventFactory.class);
    public static final BiomeModificationRegistry.Registrar BIOME_MODIFIER = load(BiomeModificationRegistry.Registrar.class);
    public static final CommandRegistry.Registrar COMMAND_REGISTRY = load(CommandRegistry.Registrar.class);
    public static final EntityAttributeRegistry.Registrar ENTITY_ATTRIBUTE_REGISTRY = load(EntityAttributeRegistry.Registrar.class);
    public static final EntityDataSerializerRegistry.Registrar ENTITY_DATA_SERIALIZER_REGISTRY = load(EntityDataSerializerRegistry.Registrar.class);
    public static final SpawnEggItemProvider.Factory SPAWN_EGG_ITEM_FACTORY = load(SpawnEggItemProvider.Factory.class);

    public static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz, NarakaServices.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }

    public static void initialize() {

    }
}
