package com.yummy.naraka.client.service;

import com.yummy.naraka.client.event.ClientEventInitializer;
import com.yummy.naraka.client.init.*;

import java.util.ServiceLoader;

public final class NarakaClientServices {
    public static final ClientEventInitializer CLIENT_EVENT_INITIALIZER = load(ClientEventInitializer.class);

    public static final BlockEntityRendererRegistry.Registrar BLOCK_ENTITY_RENDERER_REGISTRY = load(BlockEntityRendererRegistry.Registrar.class);
    public static final BuiltinResourcePackRegistry.Registrar BUILTIN_RESOURCE_PACK_REGISTRY = load(BuiltinResourcePackRegistry.Registrar.class);
    public static final DimensionSpecialEffectsRegistry.Registrar DIMENSION_SPECIAL_EFFECTS_REGISTRY = load(DimensionSpecialEffectsRegistry.Registrar.class);
    public static final EntityRendererRegistry.Registrar ENTITY_RENDERER_REGISTRY = load(EntityRendererRegistry.Registrar.class);
    public static final HudRendererRegistry.Registrar HUD_RENDERER_REGISTRY = load(HudRendererRegistry.Registrar.class);
    public static final ItemPropertyRegistry.Registrar ITEM_PROPERTY_REGISTRY = load(ItemPropertyRegistry.Registrar.class);
    public static final KeyMappingRegistry.Registrar KEY_MAPPING_REGISTRY = load(KeyMappingRegistry.Registrar.class);
    public static final ModelLayerRegistry.Registrar MODEL_LAYER_REGISTRY = load(ModelLayerRegistry.Registrar.class);
    public static final ParticleProviderRegistry.Registrar PARTICLE_PROVIDER_REGISTRY = load(ParticleProviderRegistry.Registrar.class);
    public static final ScreenFactoryRegistry.Registrar SCREEN_FACTORY_REGISTRY = load(ScreenFactoryRegistry.Registrar.class);
    public static final ShaderRegistry.Registrar SHADER_REGISTRY = load(ShaderRegistry.Registrar.class);

    private NarakaClientServices() {
    }

    public static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz, NarakaClientServices.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load client service for " + clazz.getName()));
    }

    public static void initialize() {

    }
}
