package com.yummy.naraka.client;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.gui.hud.DeathCountHud;
import com.yummy.naraka.client.gui.hud.StigmaHud;
import com.yummy.naraka.client.gui.screen.SoulCraftingScreen;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.model.SpearModel;
import com.yummy.naraka.client.model.SpearOfLonginusModel;
import com.yummy.naraka.client.particle.EbonyParticle;
import com.yummy.naraka.client.renderer.*;
import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.event.NarakaClientEvents;
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
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.server.packs.PackType;

@Environment(EnvType.CLIENT)
public class NarakaModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        initializeEntities();
        initializeItems();
        initializeBlocks();
        initializeParticles();

        NarakaNetworks.initializeClient();

        NarakaClientEvents.initialize();

        ResourceManagerHelper clientResourceManagerHelper = ResourceManagerHelper.get(PackType.CLIENT_RESOURCES);
        clientResourceManagerHelper.registerReloadListener(SpearItemRenderer.INSTANCE);
        clientResourceManagerHelper.registerReloadListener(NarakaCustomRenderer.INSTANCE);

        MenuScreens.register(NarakaMenuTypes.SOUL_CRAFTING, SoulCraftingScreen::new);
        HudRenderCallback.EVENT.register(new DeathCountHud());
        HudRenderCallback.EVENT.register(new StigmaHud());
    }

    private void initializeEntities() {
        EntityModelLayerRegistry.registerModelLayer(NarakaModelLayers.HEROBRINE, HerobrineModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(NarakaModelLayers.SPEAR, SpearModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(NarakaModelLayers.SPEAR_OF_LONGINUS, SpearOfLonginusModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(NarakaModelLayers.FORGING_BLOCK, ForgingBlockEntityRenderer::createBodyLayer);

        EntityRendererRegistry.register(NarakaEntityTypes.HEROBRINE, HerobrineRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR, SpearRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_SPEAR, SpearRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS, SpearRenderer::longinus);

        BlockEntityRenderers.register(NarakaBlockEntityTypes.FORGING_BLOCK_ENTITY, ForgingBlockEntityRenderer::new);
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

    private void initializeParticles() {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        registry.register(NarakaParticleTypes.EBONY_LEAVES, EbonyParticle.Provider::new);
    }
}
