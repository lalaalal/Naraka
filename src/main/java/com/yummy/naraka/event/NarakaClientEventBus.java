package com.yummy.naraka.event;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.model.SpearModel;
import com.yummy.naraka.client.model.SpearOfLonginusModel;
import com.yummy.naraka.client.renderer.HerobrineRenderer;
import com.yummy.naraka.client.renderer.NarakaCustomRenderer;
import com.yummy.naraka.client.renderer.SpearOfLonginusRenderer;
import com.yummy.naraka.client.renderer.SpearRenderer;
import com.yummy.naraka.entity.NarakaEntities;
import com.yummy.naraka.entity.SpearOfLonginus;
import com.yummy.naraka.gui.layer.DeathCountLayer;
import com.yummy.naraka.gui.layer.NarakaGuiLayers;
import com.yummy.naraka.gui.layer.StigmaLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = NarakaMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NarakaClientEventBus {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {

        });
    }

    @SubscribeEvent
    public static void registerClientReloadListener(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(NarakaCustomRenderer.getInstance());
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(NarakaModelLayers.HEROBRINE, HerobrineModel::createBodyLayer);
        event.registerLayerDefinition(NarakaModelLayers.SPEAR, SpearModel::createBodyLayer);
        event.registerLayerDefinition(NarakaModelLayers.SPEAR_OF_LONGINUS, SpearOfLonginusModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(NarakaEntities.HEROBRINE.get(), HerobrineRenderer::new);
        event.registerEntityRenderer(NarakaEntities.THROWN_SPEAR.get(), SpearRenderer::new);
        event.registerEntityRenderer(NarakaEntities.THROWN_SPEAR_OF_LONGINUS.get(), SpearOfLonginusRenderer::new);
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL, NarakaGuiLayers.PLAYER_STIGMA, new StigmaLayer());
        event.registerAboveAll(NarakaGuiLayers.PLAYER_DEATH_COUNT, new DeathCountLayer());
    }
}
