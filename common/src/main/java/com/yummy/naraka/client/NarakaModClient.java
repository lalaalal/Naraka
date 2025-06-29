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
import com.yummy.naraka.client.renderer.ColoredItemRenderer;
import com.yummy.naraka.client.renderer.blockentity.ForgingBlockEntityRenderer;
import com.yummy.naraka.client.renderer.blockentity.SoulSmithingBlockEntityRenderer;
import com.yummy.naraka.client.renderer.blockentity.SoulStabilizerBlockEntityRenderer;
import com.yummy.naraka.client.renderer.blockentity.UnstableBlockEntityRenderer;
import com.yummy.naraka.client.renderer.entity.*;
import com.yummy.naraka.client.renderer.special.SoulSmithingBlockSpecialRenderer;
import com.yummy.naraka.client.renderer.special.SoulStabilizerSpecialRenderer;
import com.yummy.naraka.client.renderer.special.SpearOfLonginusSpecialRenderer;
import com.yummy.naraka.client.renderer.special.SpearSpecialRenderer;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.network.NarakaNetworks;
import com.yummy.naraka.util.ComponentStyles;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public final class NarakaModClient {
    public static void initialize(NarakaClientInitializer initializer) {
        ClientEventHandler.prepare();
        NarakaModelLayers.initialize();
        NarakaRenderPipelines.initialize();
        NarakaRenderTypes.initialize();
        NarakaNetworks.initializeClient();

        registerParticles();
        registerSpecialRenderers();

        registerEntityRenderers();
        registerBlockEntityRenderers();
        registerHudRenders();
        registerMenus();
        registerKeyMappings();

        AnimationMapper.initialize();

        initializer.runAfterRegistryLoaded(NarakaModClient::onRegistryLoaded);
        initializer.runAfterRegistryLoaded(NarakaConfig::checkIris);
    }

    private static void registerSpecialRenderers() {
        SpecialModelRendererRegistry.registerCodecId(NarakaMod.location("soul_stabilizer"), SoulStabilizerSpecialRenderer.Unbaked.CODEC);
        SpecialModelRendererRegistry.registerCodecId(NarakaMod.location("soul_smithing_block"), SoulSmithingBlockSpecialRenderer.Unbaked.CODEC);
        SpecialModelRendererRegistry.registerCodecId(NarakaMod.location("spear"), SpearSpecialRenderer.Unbaked.CODEC);
        SpecialModelRendererRegistry.registerCodecId(NarakaMod.location("spear_of_longinus"), SpearOfLonginusSpecialRenderer.Unbaked.CODEC);

        SpecialModelRendererRegistry.registerBlock(NarakaBlocks.SOUL_STABILIZER, new SoulStabilizerSpecialRenderer.Unbaked());
        SpecialModelRendererRegistry.registerBlock(NarakaBlocks.SOUL_SMITHING_BLOCK, new SoulSmithingBlockSpecialRenderer.Unbaked());
    }

    private static void onRegistryLoaded() {
        initializeItems();
        initializeBlocks();

        NarakaClientEvents.initialize();

        NarakaMod.isModLoaded = true;
    }

    private static void initializeItems() {
        ColoredItemRenderer.register(NarakaItems.RAINBOW_SWORD, ComponentStyles.LONGINUS_COLOR::getCurrentColor);
    }

    private static void initializeBlocks() {
        BlockRenderTypeRegistry.register(RenderType.cutout(),
                NarakaBlocks.EBONY_SAPLING.get(),
                NarakaBlocks.POTTED_EBONY_SAPLING.get(),
                NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.get(),
                NarakaBlocks.EBONY_ROOTS.get(),
                NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get()
        );
    }

    private static void registerBlockEntityRenderers() {
        BlockEntityRendererRegistry.register(NarakaBlockEntityTypes.FORGING, ForgingBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(NarakaBlockEntityTypes.SOUL_STABILIZER, SoulStabilizerBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(NarakaBlockEntityTypes.SOUL_SMITHING, SoulSmithingBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(NarakaBlockEntityTypes.UNSTABLE_BLOCK, UnstableBlockEntityRenderer::new);
    }

    private static void registerEntityRenderers() {
        EntityRendererRegistry.register(NarakaEntityTypes.HEROBRINE, HerobrineRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.SHADOW_HEROBRINE, ShadowHerobrineRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.DIAMOND_GOLEM, DiamondGolemRenderer::new);

        EntityRendererRegistry.register(NarakaEntityTypes.MAGIC_CIRCLE, MagicCircleRenderer::new);

        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR, SpearRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_SPEAR, SpearRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS, SpearRenderer::longinus);

        EntityRendererRegistry.register(NarakaEntityTypes.STARDUST, StardustRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.NARAKA_FIREBALL, NarakaFireballRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.PICKAXE_SLASH, PickaxeSlashRenderer::new);
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
        ParticleProviderRegistry.register(NarakaParticleTypes.EBONY_LEAVES, EbonyProvider::new);
        ParticleProviderRegistry.register(NarakaParticleTypes.DRIPPING_NECTARIUM, NectariumParticle::createNectariumHangParticle);
        ParticleProviderRegistry.register(NarakaParticleTypes.FALLING_NECTARIUM, NectariumParticle::createNectariumFallParticle);
        ParticleProviderRegistry.register(NarakaParticleTypes.LANDING_NECTARIUM, NectariumParticle::createNectariumLandParticle);
        ParticleProviderRegistry.register(NarakaParticleTypes.SOUL, SoulParticle::create);
        ParticleProviderRegistry.register(NarakaParticleTypes.HEROBRINE_SPAWN, HerobrineSpawnParticle.Provider::new);
        ParticleProviderRegistry.register(NarakaParticleTypes.GOLDEN_FLAME, FlameParticle.Provider::new);
        ParticleProviderRegistry.register(NarakaParticleTypes.CORRUPTED_FIRE_FLAME, FlameParticle.Provider::new);
        ParticleProviderRegistry.register(NarakaParticleTypes.CORRUPTED_SOUL_FIRE_FLAME, FlameParticle.Provider::new);
        ParticleProviderRegistry.register(NarakaParticleTypes.FLICKER, BlinkParticle.Provider::new);
        ParticleProviderRegistry.register(NarakaParticleTypes.STARDUST, BlinkParticle.Provider::withGlowing);
        ParticleProviderRegistry.register(NarakaParticleTypes.STARDUST_FLAME, FlameParticle.Provider::new);
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
