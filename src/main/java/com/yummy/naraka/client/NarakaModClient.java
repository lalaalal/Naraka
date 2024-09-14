package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.gui.hud.DeathCountHud;
import com.yummy.naraka.client.gui.hud.StigmaHud;
import com.yummy.naraka.client.gui.screen.SoulCraftingScreen;
import com.yummy.naraka.client.particle.EbonyParticle;
import com.yummy.naraka.client.particle.NarakaDripParticles;
import com.yummy.naraka.client.renderer.*;
import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.network.NarakaNetworks;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.inventory.NarakaMenuTypes;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import com.yummy.naraka.world.item.component.SanctuaryTracker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.packs.PackType;

@Environment(EnvType.CLIENT)
public class NarakaModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NarakaModelLayers.initialize();
        initializeItems();
        initializeBlocks();

        registerRenderers();
        registerParticles();
        registerHudRenders();
        registerMenus();

        NarakaNetworks.initializeClient();
        NarakaClientEvents.initialize();

        ResourceManagerHelper resourceManagerHelper = ResourceManagerHelper.get(PackType.CLIENT_RESOURCES);
        resourceManagerHelper.registerReloadListener(SpearItemRenderer.INSTANCE);
        resourceManagerHelper.registerReloadListener(NarakaCustomRenderer.INSTANCE);
        resourceManagerHelper.registerReloadListener(BlockTransparentRenderer.INSTANCE);
    }

    private void initializeItems() {
        BuiltinItemRendererRegistry.INSTANCE.register(NarakaBlocks.FORGING_BLOCK, NarakaCustomRenderer.INSTANCE);
        CustomItemRenderManager.register(NarakaItems.SPEAR_ITEM, SpearItemRenderer.INSTANCE);
        CustomItemRenderManager.register(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, SpearItemRenderer.INSTANCE);
        CustomItemRenderManager.register(NarakaItems.SPEAR_OF_LONGINUS_ITEM, SpearItemRenderer.INSTANCE);

        ItemProperties.register(NarakaItems.SANCTUARY_COMPASS, NarakaMod.location("angle"), new CompassItemPropertyFunction((clientLevel, itemStack, entity) -> {
            SanctuaryTracker tracker = itemStack.get(NarakaDataComponentTypes.SANCTUARY_TRACKER);
            if (tracker == null)
                return null;
            return tracker.sanctuaryPos().orElse(null);
        }));
    }

    private void initializeBlocks() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                NarakaBlocks.EBONY_SAPLING,
                NarakaBlocks.POTTED_EBONY_SAPLING,
                NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK,
                NarakaBlocks.EBONY_ROOTS,
                NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK
        );
    }

    private void registerRenderers() {
        EntityRendererRegistry.register(NarakaEntityTypes.HEROBRINE, HerobrineRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR, SpearRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_SPEAR, SpearRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS, SpearRenderer::longinus);

        BlockEntityRenderers.register(NarakaBlockEntityTypes.FORGING_BLOCK_ENTITY, ForgingBlockEntityRenderer::new);
    }

    private void registerHudRenders() {
        HudRenderCallback.EVENT.register(new DeathCountHud());
        HudRenderCallback.EVENT.register(new StigmaHud());
    }

    private void registerMenus() {
        MenuScreens.register(NarakaMenuTypes.SOUL_CRAFTING, SoulCraftingScreen::new);
    }

    private void registerParticles() {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        registry.register(NarakaParticleTypes.EBONY_LEAVES, EbonyParticle.Provider::new);
        registry.register(
                NarakaParticleTypes.DRIPPING_NECTARIUM_CORE_HONEY,
                withSprite(NarakaDripParticles::createNectariumCoreHoneyHangParticle)
        );
        registry.register(
                NarakaParticleTypes.FALLING_NECTARIUM_CORE_HONEY,
                withSprite(NarakaDripParticles::createNectariumCoreHoneyFallParticle)
        );
        registry.register(
                NarakaParticleTypes.LANDING_NECTARIUM_CORE_HONEY,
                withSprite(NarakaDripParticles::createNectariumCoreHoneyLandParticle)
        );
    }

    private <T extends ParticleOptions> ParticleFactoryRegistry.PendingParticleFactory<T> withSprite(ParticleProvider.Sprite<T> factory) {
        return sprite -> (type, level, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            TextureSheetParticle particle = factory.createParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed);
            if (particle != null)
                particle.pickSprite(sprite);
            return particle;
        };
    }
}
