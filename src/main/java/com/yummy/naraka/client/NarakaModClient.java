package com.yummy.naraka.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.gui.screen.SoulCraftingScreen;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.model.SpearModel;
import com.yummy.naraka.client.model.SpearOfLonginusModel;
import com.yummy.naraka.client.particle.EbonyParticle;
import com.yummy.naraka.client.renderer.CustomItemRenderManager;
import com.yummy.naraka.client.renderer.HerobrineRenderer;
import com.yummy.naraka.client.renderer.NarakaSpearItemRenderer;
import com.yummy.naraka.client.renderer.SpearRenderer;
import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.util.ComponentStyles;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.inventory.NarakaMenuTypes;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.component.NarakaDataComponents;
import com.yummy.naraka.world.item.component.SanctuaryTracker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.Level;

@Environment(EnvType.CLIENT)
public class NarakaModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(NarakaModelLayers.HEROBRINE, HerobrineModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(NarakaModelLayers.SPEAR, SpearModel::createBodyLayer);
        EntityModelLayerRegistry.registerModelLayer(NarakaModelLayers.SPEAR_OF_LONGINUS, SpearOfLonginusModel::createBodyLayer);

        EntityRendererRegistry.register(NarakaEntityTypes.HEROBRINE, HerobrineRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR, SpearRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_SPEAR, SpearRenderer::new);
        EntityRendererRegistry.register(NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS, SpearRenderer::longinus);

        CoreShaderRegistrationCallback.EVENT.register(context -> {
            context.register(NarakaMod.location("longinus"), DefaultVertexFormat.POSITION, shaderInstance -> {
                NarakaShaders.longinus = shaderInstance;
            });
        });
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                NarakaBlocks.EBONY_SAPLING,
                NarakaBlocks.POTTED_EBONY_SAPLING,
                NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK,
                NarakaBlocks.EBONY_ROOTS
        );

        ParticleFactoryRegistry.getInstance().register(NarakaParticleTypes.EBONY_LEAVES, EbonyParticle.Provider::new);

        CustomItemRenderManager.register(NarakaItems.SPEAR_ITEM, NarakaSpearItemRenderer.INSTANCE);
        CustomItemRenderManager.register(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, NarakaSpearItemRenderer.INSTANCE);
        CustomItemRenderManager.register(NarakaItems.SPEAR_OF_LONGINUS_ITEM, NarakaSpearItemRenderer.INSTANCE);

        ItemProperties.register(NarakaItems.SANCTUARY_COMPASS, NarakaMod.location("angle"), new CompassItemPropertyFunction((clientLevel, itemStack, entity) -> {
            SanctuaryTracker tracker = itemStack.get(NarakaDataComponents.SANCTUARY_TRACKER);
            if (tracker == null)
                return GlobalPos.of(Level.OVERWORLD, BlockPos.ZERO);
            return tracker.sanctuaryPos();
        }));

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(NarakaSpearItemRenderer.INSTANCE);

        MenuScreens.register(NarakaMenuTypes.SOUL_CRAFTING, SoulCraftingScreen::new);

        ClientTickEvents.START_CLIENT_TICK.register(client -> ComponentStyles.tick());
    }
}
