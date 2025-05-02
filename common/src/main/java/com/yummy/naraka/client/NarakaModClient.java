package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.animation.AnimationMapper;
import com.yummy.naraka.client.event.ClientEventHandler;
import com.yummy.naraka.client.gui.hud.DeathCountHud;
import com.yummy.naraka.client.gui.hud.LockedHealthHud;
import com.yummy.naraka.client.gui.hud.StigmaHud;
import com.yummy.naraka.client.init.*;
import com.yummy.naraka.client.particle.EbonyProvider;
import com.yummy.naraka.client.particle.NectariumParticle;
import com.yummy.naraka.client.particle.SoulParticle;
import com.yummy.naraka.client.renderer.CustomRenderManager;
import com.yummy.naraka.client.renderer.blockentity.*;
import com.yummy.naraka.client.renderer.entity.*;
import com.yummy.naraka.client.renderer.item.SpearItemRenderer;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public final class NarakaModClient {
    public static void initialize(NarakaClientInitializer initializer) {
        ClientEventHandler.prepare();
        NarakaModelLayers.initialize();
        registerParticles();

        initializer.registerResourceReloadListener("spear_item_renderer", () -> SpearItemRenderer.INSTANCE);
        initializer.registerResourceReloadListener("custom_renderer", () -> NarakaBlockEntityItemRenderer.INSTANCE);

        registerEntityRenderers();
        registerBlockEntityRenderers();
        registerHudRenders();
        registerMenus();
        registerKeyMappings();

        AnimationMapper.initialize();

        initializer.runAfterRegistryLoaded(NarakaModClient::onRegistryLoaded);
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

        CustomRenderManager.renderRainbow(NarakaItems.RAINBOW_SWORD.get());
    }

    private static void initializeBlocks() {
        CustomRenderManager.register(NarakaBlocks.FORGING_BLOCK.get(), NarakaBlockEntityItemRenderer.INSTANCE);
        CustomRenderManager.register(NarakaBlocks.SOUL_STABILIZER.get(), NarakaBlockEntityItemRenderer.INSTANCE);
        CustomRenderManager.register(NarakaBlocks.SOUL_SMITHING_BLOCK.get(), NarakaBlockEntityItemRenderer.INSTANCE);

        CustomRenderManager.register(RenderType.cutout(),
                NarakaBlocks.EBONY_SAPLING.get(),
                NarakaBlocks.POTTED_EBONY_SAPLING.get(),
                NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.get(),
                NarakaBlocks.EBONY_ROOTS.get(),
                NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get()
        );
    }
//
//    private static void registerShaders() {
//        ShaderRegistry.register(
//                NarakaMod.location("longinus"),
//                DefaultVertexFormat.POSITION,
//                shaderInstance -> NarakaShaders.LONGINUS = shaderInstance
//        );
//    }

    private static void registerBlockEntityRenderers() {
        BlockEntityRendererRegistry.register(NarakaBlockEntityTypes.FORGING, ForgingBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(NarakaBlockEntityTypes.SOUL_STABILIZER, SoulStabilizerBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(NarakaBlockEntityTypes.SOUL_SMITHING, SoulSmithingBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(NarakaBlockEntityTypes.UNSTABLE_BLOCK, UnstableBlockEntityRenderer::new);
    }

    private static void registerEntityRenderers() {
        EntityRendererRegistry.register(NarakaEntityTypes.HEROBRINE, HerobrineRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.SHADOW_HEROBRINE, ShadowHerobrineRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR, SpearRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_SPEAR, SpearRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS, SpearRenderer::longinus);
        EntityRendererRegistry.register(NarakaEntityTypes.STARDUST, StardustRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.NARAKA_FIREBALL, NarakaFireballRenderer::new);
    }

    private static void registerHudRenders() {
        HudRendererRegistry.register(NarakaMod.location("hud", "death_count"), DeathCountHud::new);
        HudRendererRegistry.register(NarakaMod.location("hud", "stigma"), StigmaHud::new);
        HudRendererRegistry.register(NarakaMod.location("hud", "locked_health"), LockedHealthHud::new);
    }

    private static void registerMenus() {

    }

    private static void registerParticles() {
        ParticleProviderRegistry.register(NarakaParticleTypes.EBONY_LEAVES, EbonyProvider::new);
        ParticleProviderRegistry.register(NarakaParticleTypes.DRIPPING_NECTARIUM, NectariumParticle::createNectariumHangParticle);
        ParticleProviderRegistry.register(NarakaParticleTypes.FALLING_NECTARIUM, NectariumParticle::createNectariumFallParticle);
        ParticleProviderRegistry.register(NarakaParticleTypes.LANDING_NECTARIUM, NectariumParticle::createNectariumLandParticle);
        ParticleProviderRegistry.register(NarakaParticleTypes.SOUL, SoulParticle::create);
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
