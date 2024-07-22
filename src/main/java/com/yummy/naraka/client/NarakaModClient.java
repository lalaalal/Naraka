package com.yummy.naraka.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.gui.screen.SoulCraftingScreen;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.model.SpearModel;
import com.yummy.naraka.client.model.SpearOfLonginusModel;
import com.yummy.naraka.client.renderer.HerobrineRenderer;
import com.yummy.naraka.client.renderer.NarakaCustomRenderer;
import com.yummy.naraka.client.renderer.SpearRenderer;
import com.yummy.naraka.util.ComponentStyles;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.inventory.NarakaMenuTypes;
import com.yummy.naraka.world.item.NarakaItems;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.impl.client.rendering.BuiltinItemRendererRegistryImpl;
import net.fabricmc.fabric.mixin.client.indigo.renderer.ItemRendererMixin;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.FoliageColor;

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
        BuiltinItemRendererRegistry.INSTANCE.register(NarakaItems.SPEAR_ITEM, NarakaCustomRenderer.INSTANCE);
        BuiltinItemRendererRegistry.INSTANCE.register(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, NarakaCustomRenderer.INSTANCE);
        BuiltinItemRendererRegistry.INSTANCE.register(NarakaItems.SPEAR_OF_LONGINUS_ITEM, NarakaCustomRenderer.INSTANCE);

        ClientTickEvents.START_CLIENT_TICK.register(client -> ComponentStyles.tick());
        MenuScreens.register(NarakaMenuTypes.SOUL_CRAFTING, SoulCraftingScreen::new);
        ColorProviderRegistry.BLOCK.register((blockState, blockAndTintGetter, blockPos, i) -> FoliageColor.getDefaultColor(), NarakaBlocks.EBONY_LEAVES);
        ColorProviderRegistry.ITEM.register((itemStack, i) -> FoliageColor.getDefaultColor(), NarakaBlocks.EBONY_LEAVES);
    }
}
