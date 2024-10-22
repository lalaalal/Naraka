package com.yummy.naraka.neoforge.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaModClient;
import com.yummy.naraka.client.renderer.CustomItemRenderManager;
import com.yummy.naraka.client.renderer.NarakaCustomRenderer;
import com.yummy.naraka.init.NarakaClientInitializer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
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
import net.neoforged.neoforge.common.NeoForge;

import java.io.IOException;
import java.util.function.Consumer;

@Mod(value = NarakaMod.MOD_ID, dist = Dist.CLIENT)
public class NarakaModNeoForgeClient implements NarakaClientInitializer, IClientItemExtensions {
    public NarakaModNeoForgeClient(FMLModContainer container, IEventBus modBus, Dist dist) {

    }

    public void clientSetup(FMLClientSetupEvent event) {
        NarakaModClient.initializeClient(this);
    }

    @Override
    public void registerCustomItemRenderer(ItemLike item, CustomItemRenderManager.CustomItemRenderer renderer) {
        NeoForge.EVENT_BUS.register((Consumer<RegisterClientExtensionsEvent>) event -> event.registerItem(this, item.asItem()));
    }

    @Override
    public void registerBlockRenderLayer(RenderType renderType, Block... blocks) {

    }

    @Override
    public void registerShader(ResourceLocation id, VertexFormat format, Consumer<ShaderInstance> consumer) {
        NeoForge.EVENT_BUS.register((Consumer<RegisterShadersEvent>) event -> {
            try {
                event.registerShader(new ShaderInstance(event.getResourceProvider(), id, format), consumer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return new BlockEntityWithoutLevelRenderer(null, null) {
            @Override
            public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                NarakaCustomRenderer.INSTANCE.render(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
            }
        };
    }
}