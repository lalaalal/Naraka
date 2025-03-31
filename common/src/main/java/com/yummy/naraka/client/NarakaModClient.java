package com.yummy.naraka.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.event.ClientEventHandler;
import com.yummy.naraka.client.gui.hud.DeathCountHud;
import com.yummy.naraka.client.gui.hud.LockedHealthHud;
import com.yummy.naraka.client.gui.hud.StigmaHud;
import com.yummy.naraka.client.gui.screen.SoulCraftingScreen;
import com.yummy.naraka.client.init.*;
import com.yummy.naraka.client.particle.EbonyParticle;
import com.yummy.naraka.client.particle.NectariumParticle;
import com.yummy.naraka.client.particle.SoulParticle;
import com.yummy.naraka.client.renderer.BlockTransparentRenderer;
import com.yummy.naraka.client.renderer.CustomRenderManager;
import com.yummy.naraka.client.renderer.SpearItemRenderer;
import com.yummy.naraka.client.renderer.blockentity.*;
import com.yummy.naraka.client.renderer.entity.HerobrineRenderer;
import com.yummy.naraka.client.renderer.entity.NarakaFireballRenderer;
import com.yummy.naraka.client.renderer.entity.SpearRenderer;
import com.yummy.naraka.client.renderer.entity.StardustRenderer;
import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.network.NarakaNetworks;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.inventory.NarakaMenuTypes;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import com.yummy.naraka.world.item.component.SanctuaryTracker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;

@Environment(EnvType.CLIENT)
public final class NarakaModClient {
    public static void initialize(NarakaClientInitializer initializer) {
        ClientEventHandler.prepare();
        NarakaModelLayers.initialize();
        registerParticles();
        registerShaders();

        initializer.registerResourceReloadListener("spear_item_renderer", () -> SpearItemRenderer.INSTANCE);
        initializer.registerResourceReloadListener("custom_renderer", () -> NarakaBlockEntityItemRenderer.INSTANCE);
        initializer.registerResourceReloadListener("block_transparent_renderer", () -> BlockTransparentRenderer.INSTANCE);

        registerEntityRenderers();
        registerBlockEntityRenderers();
        registerHudRenders();
        registerMenus();

        initializer.runAfterRegistryLoaded(NarakaModClient::onRegistryLoaded);
    }

    private static void onRegistryLoaded() {
        initializeItems();
        initializeBlocks();

        NarakaNetworks.initializeClient();
        NarakaClientEvents.initialize();

        NarakaMod.isModLoaded = true;
    }

    private static void initializeItems() {
        CustomRenderManager.register(NarakaItems.SPEAR_ITEM.get(), SpearItemRenderer.INSTANCE);
        CustomRenderManager.register(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get(), SpearItemRenderer.INSTANCE);
        CustomRenderManager.register(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get(), SpearItemRenderer.INSTANCE);

        CustomRenderManager.renderRainbow(NarakaItems.RAINBOW_SWORD.get());

        ItemPropertyRegistry.register(NarakaItems.SANCTUARY_COMPASS.get(), NarakaMod.location("angle"), new CompassItemPropertyFunction((clientLevel, itemStack, entity) -> {
            SanctuaryTracker tracker = itemStack.get(NarakaDataComponentTypes.SANCTUARY_TRACKER.get());
            if (tracker == null)
                return null;
            return tracker.sanctuaryPos().orElse(null);
        }));
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

    private static void registerShaders() {
        ShaderRegistry.register(
                NarakaMod.location("longinus"),
                DefaultVertexFormat.POSITION,
                shaderInstance -> NarakaShaders.longinus = shaderInstance
        );
    }

    private static void registerBlockEntityRenderers() {
        BlockEntityRendererRegistry.register(NarakaBlockEntityTypes.FORGING, ForgingBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(NarakaBlockEntityTypes.SOUL_STABILIZER, SoulStabilizerBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(NarakaBlockEntityTypes.SOUL_SMITHING, SoulSmithingBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(NarakaBlockEntityTypes.UNSTABLE_BLOCK, UnstableBlockEntityRenderer::new);
    }

    private static void registerEntityRenderers() {
        EntityRendererRegistry.register(NarakaEntityTypes.HEROBRINE, HerobrineRenderer::herobrine);
        EntityRendererRegistry.register(NarakaEntityTypes.SHADOW_HEROBRINE, HerobrineRenderer::shadow);
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
        ScreenFactoryRegistry.register(NarakaMenuTypes.SOUL_CRAFTING, SoulCraftingScreen::new);
    }

    private static void registerParticles() {
        ParticleProviderRegistry.register(NarakaParticleTypes.EBONY_LEAVES, EbonyParticle.Provider::new);
        ParticleProviderRegistry.register(NarakaParticleTypes.DRIPPING_NECTARIUM, NectariumParticle::createNectariumHangParticle);
        ParticleProviderRegistry.register(NarakaParticleTypes.FALLING_NECTARIUM, NectariumParticle::createNectariumFallParticle);
        ParticleProviderRegistry.register(NarakaParticleTypes.LANDING_NECTARIUM, NectariumParticle::createNectariumLandParticle);
        ParticleProviderRegistry.register(NarakaParticleTypes.SOUL, SoulParticle::create);
    }
}
