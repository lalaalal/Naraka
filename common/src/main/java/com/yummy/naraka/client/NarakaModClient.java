package com.yummy.naraka.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.gui.hud.DeathCountHud;
import com.yummy.naraka.client.gui.hud.StigmaHud;
import com.yummy.naraka.client.gui.screen.SoulCraftingScreen;
import com.yummy.naraka.client.particle.EbonyParticle;
import com.yummy.naraka.client.renderer.*;
import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.init.NarakaClientInitializer;
import com.yummy.naraka.network.NarakaNetworks;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.inventory.NarakaMenuTypes;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import com.yummy.naraka.world.item.component.SanctuaryTracker;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.server.packs.PackType;

@Environment(EnvType.CLIENT)
public class NarakaModClient {
    public static void initializeClient(NarakaClientInitializer initializer) {
        NarakaModelLayers.initialize();
        initializeItems(initializer);
        initializeBlocks(initializer);
        registerShaders(initializer);

        registerRenderers();
        registerParticles();
        registerHudRenders();
        registerMenus();

        NarakaNetworks.initializeClient();
        NarakaClientEvents.initialize();

        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, SpearItemRenderer.INSTANCE);
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, NarakaCustomRenderer.INSTANCE);
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, BlockTransparentRenderer.INSTANCE);
    }

    private static void initializeItems(NarakaClientInitializer initializer) {
        initializer.registerCustomItemRenderer(NarakaBlocks.FORGING_BLOCK.get(), NarakaCustomRenderer.INSTANCE);
        initializer.registerCustomItemRenderer(NarakaBlocks.SOUL_STABILIZER.get(), NarakaCustomRenderer.INSTANCE);
        initializer.registerCustomItemRenderer(NarakaBlocks.SOUL_SMITHING_BLOCK.get(), NarakaCustomRenderer.INSTANCE);

        CustomItemRenderManager.register(NarakaItems.SPEAR_ITEM.get(), SpearItemRenderer.INSTANCE);
        CustomItemRenderManager.register(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM.get(), SpearItemRenderer.INSTANCE);
        CustomItemRenderManager.register(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get(), SpearItemRenderer.INSTANCE);

        ItemPropertiesRegistry.register(NarakaItems.SANCTUARY_COMPASS.get(), NarakaMod.location("angle"), new CompassItemPropertyFunction((clientLevel, itemStack, entity) -> {
            SanctuaryTracker tracker = itemStack.get(NarakaDataComponentTypes.SANCTUARY_TRACKER.get());
            if (tracker == null)
                return null;
            return tracker.sanctuaryPos().orElse(null);
        }));
    }

    private static void initializeBlocks(NarakaClientInitializer initializer) {
        initializer.registerBlockRenderLayer(RenderType.cutout(),
                NarakaBlocks.EBONY_SAPLING.get(),
                NarakaBlocks.POTTED_EBONY_SAPLING.get(),
                NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.get(),
                NarakaBlocks.EBONY_ROOTS.get(),
                NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get()
        );
    }

    private static void registerShaders(NarakaClientInitializer initializer) {
        initializer.registerShader(NarakaMod.location("longinus"), DefaultVertexFormat.POSITION, shaderInstance -> {
            NarakaShaders.longinus = shaderInstance;
        });
    }

    private static void registerRenderers() {
        EntityRendererRegistry.register(NarakaEntityTypes.HEROBRINE, HerobrineRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR, SpearRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_SPEAR, SpearRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS, SpearRenderer::longinus);

        BlockEntityRendererRegistry.register(NarakaBlockEntityTypes.FORGING.get(), ForgingBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(NarakaBlockEntityTypes.SOUL_STABILIZER.get(), SoulStabilizerBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(NarakaBlockEntityTypes.SOUL_SMITHING.get(), SoulSmithingBlockEntityRenderer::new);
    }

    private static void registerHudRenders() {
        ClientGuiEvent.RENDER_HUD.register(new DeathCountHud());
        ClientGuiEvent.RENDER_HUD.register(new StigmaHud());
    }

    private static void registerMenus() {
        MenuRegistry.registerScreenFactory(NarakaMenuTypes.SOUL_CRAFTING.get(), SoulCraftingScreen::new);
    }

    private static void registerParticles() {
        ParticleProviderRegistry.register(NarakaParticleTypes.EBONY_LEAVES, EbonyParticle.Provider::new);
    }
}
