package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.Platform;
import com.yummy.naraka.client.animation.AnimationMapper;
import com.yummy.naraka.client.event.ClientEventHandler;
import com.yummy.naraka.client.gui.hud.DeathCountHud;
import com.yummy.naraka.client.gui.hud.LockedHealthHud;
import com.yummy.naraka.client.gui.hud.StigmaHud;
import com.yummy.naraka.client.init.*;
import com.yummy.naraka.client.particle.EbonyProvider;
import com.yummy.naraka.client.particle.NectariumParticle;
import com.yummy.naraka.client.particle.SoulParticle;
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
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
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
        registerSpecialRenderers();

        registerEntityRenderers();
        registerBlockEntityRenderers();
        registerHudRenders();
        registerMenus();
        registerKeyMappings();

        AnimationMapper.initialize();

        initializer.runAfterRegistryLoaded(NarakaModClient::onRegistryLoaded);
        initializer.runAfterRegistryLoaded(NarakaModClient::checkIris);
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

    private static void checkIris() {
        boolean irisLoaded = Platform.getInstance().modExists("iris");
        if (NarakaConfig.CLIENT.enableNonShaderLonginusRendering.getValue())
            NarakaConfig.CLIENT.enableNonShaderLonginusRendering.set(irisLoaded);
    }

    private static void initializeItems() {

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
