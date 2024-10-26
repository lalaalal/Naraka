package com.yummy.naraka.neoforge.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaModClient;
import com.yummy.naraka.client.renderer.CustomRenderManager;
import com.yummy.naraka.init.NarakaClientInitializer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Mod(value = NarakaMod.MOD_ID, dist = Dist.CLIENT)
public class NarakaModNeoForgeClient implements NarakaClientInitializer, IClientItemExtensions {
    private final IEventBus bus;

    public NarakaModNeoForgeClient(FMLModContainer container, IEventBus modBus, Dist dist) {
        this.bus = modBus;
        NarakaModClient.prepareInitialization();

        modBus.addListener(this::clientSetup);
    }

    public void clientSetup(FMLClientSetupEvent event) {
        NarakaModClient.initializeClient(this);
    }

    @Override
    public void registerCustomItemRenderer(ItemLike item, CustomRenderManager.CustomItemRenderer renderer) {
        bus.addListener((Consumer<RegisterClientExtensionsEvent>) event -> {
                event.registerItem(this, item.asItem());
                NeoForgeCustomItemRenderer.getInstance().register(item, renderer);
            }
        );
    }

    @Override
    public void registerBlockRenderLayer(RenderType renderType, Block... blocks) {
        for (Block block : blocks) {
            CustomRenderManager.register(block.asItem(), renderType);
        }
    }

    @Override
    public void registerShader(ResourceLocation id, VertexFormat format, Consumer<ShaderInstance> consumer) {
        bus.addListener((Consumer<RegisterShadersEvent>) event -> {
            try {
                event.registerShader(new ShaderInstance(event.getResourceProvider(), id, format), consumer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return NeoForgeCustomItemRenderer.getInstance();
    }

    private static class NeoForgeCustomItemRenderer extends BlockEntityWithoutLevelRenderer {
        private static NeoForgeCustomItemRenderer INSTANCE = null;

        public static NeoForgeCustomItemRenderer getInstance() {
            if (INSTANCE == null)
                return INSTANCE = new NeoForgeCustomItemRenderer(Minecraft.getInstance());
            return INSTANCE;
        }

        private final Map<Item, CustomRenderManager.CustomItemRenderer> rendererByItem = new HashMap<>();
        
        public NeoForgeCustomItemRenderer(Minecraft minecraft) {
            super(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
        }

        public void register(Item item, CustomRenderManager.CustomItemRenderer renderer) {
            rendererByItem.put(item, renderer);
        }

        public void register(ItemLike item, CustomRenderManager.CustomItemRenderer renderer) {
            this.register(item.asItem(), renderer);
        }

        @Override
        public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
            CustomRenderManager.CustomItemRenderer renderer = rendererByItem.get(stack.getItem());
            if (renderer != null)
                renderer.render(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
        }
    }
}