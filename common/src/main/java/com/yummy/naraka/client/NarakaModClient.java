package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.animation.AnimationMapper;
import com.yummy.naraka.client.event.ClientEventHandler;
import com.yummy.naraka.client.gui.hud.DeathCountHud;
import com.yummy.naraka.client.gui.hud.LockedHealthHud;
import com.yummy.naraka.client.gui.hud.StigmaHud;
import com.yummy.naraka.client.gui.hud.WhiteHud;
import com.yummy.naraka.client.init.*;
import com.yummy.naraka.client.particle.*;
import com.yummy.naraka.client.renderer.*;
import com.yummy.naraka.client.renderer.blockentity.NarakaPortalBlockEntityRenderer;
import com.yummy.naraka.client.renderer.blockentity.SoulSmithingBlockEntityRenderer;
import com.yummy.naraka.client.renderer.blockentity.SoulStabilizerBlockEntityRenderer;
import com.yummy.naraka.client.renderer.entity.*;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.core.component.NarakaDataComponentTypes;
import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.data.worldgen.NarakaDimensionTypes;
import com.yummy.naraka.network.NarakaNetworks;
import com.yummy.naraka.world.NarakaDimensions;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.component.SanctuaryTracker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public final class NarakaModClient {
    public static void initialize(NarakaClientInitializer initializer) {
        ClientEventHandler.prepare();
        NarakaModelLayers.initialize();
        NarakaShaders.initialize();
        NarakaRenderTypes.initialize();
        NarakaNetworks.initializeClient();
        NarakaShaders.initialize();

        registerParticles();

        initializer.registerClientReloadListener("spear_item_renderer", () -> SpearItemRenderer.INSTANCE);
        initializer.registerClientReloadListener("custom_renderer", () -> NarakaBlockEntityItemRenderer.INSTANCE);

        registerEntityRenderers();
        registerBlockEntityRenderers();
        registerHudRenders();
        registerMenus();
        registerKeyMappings();
        DimensionSpecialEffectsRegistry.register(NarakaDimensionTypes.NARAKA_EFFECT, NarakaDimensionSpecialEffects.NARAKA);
        DimensionSkyRendererRegistry.register(NarakaDimensions.NARAKA, NarakaSkyRenderer::new);

        AnimationMapper.initialize();

        initializer.runAfterRegistryLoaded(NarakaModClient::onRegistryLoaded);
        initializer.runAfterRegistryLoaded(NarakaConfig::checkIris);
    }

    private static void onRegistryLoaded() {
        initializeItems();
        initializeBlocks();

        NarakaClientEvents.initialize();

        NarakaMod.isModLoaded = true;
    }

    private static void initializeItems() {
        CustomRenderManager.register(NarakaItems.SPEAR_ITEM.get(), SpearItemRenderer.INSTANCE);
        CustomRenderManager.register(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get(), SpearItemRenderer.INSTANCE);
        CustomRenderManager.register(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get(), SpearItemRenderer.INSTANCE);

        CustomRenderManager.registerRainbow(NarakaItems.RAINBOW_SWORD.get());
        CustomRenderManager.registerCustomRenderType(NarakaItems.HEROBRINE_SCARF.get(), defaultType -> {
            if (NarakaClientContext.SHADER_ENABLED.getValue())
                return defaultType;
            return NarakaRenderTypes.longinusCutout(NarakaTextures.LOCATION_BLOCKS);
        });

        ItemPropertyRegistry.register(NarakaItems.SANCTUARY_COMPASS.get(), NarakaMod.location("angle"), new CompassItemPropertyFunction((clientLevel, itemStack, entity) -> {
            SanctuaryTracker tracker = itemStack.get(NarakaDataComponentTypes.SANCTUARY_TRACKER.get());
            if (tracker == null)
                return null;
            return tracker.sanctuaryPos().orElse(null);
        }));
    }

    private static void initializeBlocks() {
        CustomRenderManager.register(NarakaBlocks.SOUL_STABILIZER.get(), NarakaBlockEntityItemRenderer.INSTANCE);
        CustomRenderManager.register(NarakaBlocks.SOUL_SMITHING_BLOCK.get(), NarakaBlockEntityItemRenderer.INSTANCE);

        BlockRenderTypeRegistry.register(RenderType.cutout(),
                NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.get(),
                NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get()
        );
    }

    private static void registerBlockEntityRenderers() {
        BlockEntityRendererRegistry.register(NarakaBlockEntityTypes.SOUL_STABILIZER, SoulStabilizerBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(NarakaBlockEntityTypes.SOUL_SMITHING, SoulSmithingBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(NarakaBlockEntityTypes.NARAKA_PORTAL, NarakaPortalBlockEntityRenderer::new);
    }

    private static void registerEntityRenderers() {
        EntityRendererRegistry.register(NarakaEntityTypes.HEROBRINE, HerobrineRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.ABSOLUTE_HEROBRINE, AbsoluteHerobrineRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.SHADOW_HEROBRINE, ShadowHerobrineRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.DIAMOND_GOLEM, DiamondGolemRenderer::new);

        EntityRendererRegistry.register(NarakaEntityTypes.MAGIC_CIRCLE, MagicCircleRenderer::new);

        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR, SpearRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_SPEAR, SpearRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS, SpearRenderer::longinus);

        EntityRendererRegistry.register(NarakaEntityTypes.STARDUST, StardustRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.CORRUPTED_STAR, CorruptedStarRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.NARAKA_FIREBALL, NarakaFireballRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.PICKAXE_SLASH, PickaxeSlashRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.COLORED_LIGHTNING_BOLT, ColoredLightningBoltRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.MASSIVE_LIGHTNING, MassiveLightningRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.LIGHTNING_CIRCLE, LightningCircleRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.NARAKA_PICKAXE, NarakaPickaxeRenderer::new);

        EntityRendererRegistry.register(NarakaEntityTypes.SHINY_EFFECT, ShinyEffectRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.AREA_EFFECT, AreaEffectRenderer::new);
    }

    private static void registerHudRenders() {
        HudRendererRegistry.registerPostLayer(NarakaMod.location("hud", "death_count"), DeathCountHud::new);
        HudRendererRegistry.registerPostLayer(NarakaMod.location("hud", "stigma"), StigmaHud::new);
        HudRendererRegistry.registerPostLayer(NarakaMod.location("hud", "locked_health"), LockedHealthHud::new);
        HudRendererRegistry.registerPreLayer(NarakaMod.location("hud", "white"), WhiteHud::new);
    }

    private static void registerMenus() {

    }

    private static void registerParticles() {
        ParticleProviderRegistry.register(NarakaParticleTypes.DRIPPING_NECTARIUM, NectariumParticle::createNectariumHangParticle);
        ParticleProviderRegistry.register(NarakaParticleTypes.FALLING_NECTARIUM, NectariumParticle::createNectariumFallParticle);
        ParticleProviderRegistry.register(NarakaParticleTypes.LANDING_NECTARIUM, NectariumParticle::createNectariumLandParticle);
        ParticleProviderRegistry.register(NarakaParticleTypes.SOUL, SoulParticle::create);
        ParticleProviderRegistry.register(NarakaParticleTypes.HEROBRINE_SPAWN, TurningParticle::herobrineSpawn);
        ParticleProviderRegistry.register(NarakaParticleTypes.PARRYING, TurningParticle::parrying);
        ParticleProviderRegistry.register(NarakaParticleTypes.NARAKA_FLAME, NarakaFlame.Provider::new);
        ParticleProviderRegistry.register(NarakaParticleTypes.FLICKER, BlinkParticle.Provider::new);
        ParticleProviderRegistry.register(NarakaParticleTypes.STARDUST, BlinkParticle.Provider::withGlowing);
        ParticleProviderRegistry.register(NarakaParticleTypes.TELEPORT, BlinkParticle.Provider::withGlowing);
    }

    private static void registerKeyMappings() {
        KeyMappingRegistry.register(NarakaKeyMappings.TOGGLE_ORE_SEE_THROUGH, (minecraft, keyMapping) -> {
            if (keyMapping.consumeClick()) {
                boolean disabled = !NarakaConfig.CLIENT.disableOreSeeThrough.getValue();
                NarakaConfig.CLIENT.disableOreSeeThrough.set(disabled);
                NarakaConfig.CLIENT.saveValues();
                if (minecraft.player != null)
                    minecraft.player.displayClientMessage(Component.translatable(LanguageKey.toggleOreSeeThroughMessage(disabled)), false);
            }
        });
    }
}
