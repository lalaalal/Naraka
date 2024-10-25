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
import java.util.function.Consumer;
import java.util.function.Supplier;

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
    public void registerCustomItemRenderer(Supplier<? extends Block> block, CustomItemRenderManager.CustomItemRenderer renderer) {
        bus.addListener((Consumer<RegisterClientExtensionsEvent>) event -> event.registerItem(this, block.get().asItem()));
        RegisterClientExtensionsEvent event;
    }

    @Override
    public void registerBlockRenderLayer(RenderType renderType, Block... blocks) {

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
        return new BlockEntityWithoutLevelRenderer(null, null) {
            @Override
            public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
                NarakaCustomRenderer.INSTANCE.render(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
            }
        };
    }
}