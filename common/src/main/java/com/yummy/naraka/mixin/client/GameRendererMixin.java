package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.yummy.naraka.client.renderer.uniform.UniformLayoutRegistry;
import com.yummy.naraka.world.entity.data.DynamicPostEffectHelper;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    @Final
    private ResourceManager resourceManager;

    @Shadow
    @Final
    Minecraft minecraft;

    @Shadow
    @Final
    static Logger LOGGER;

    @Shadow
    public abstract Camera getMainCamera();

    @Unique
    private final List<PostChain> naraka$dynamicPostChains = new ArrayList<>();

    @Unique
    private final UniformLayoutRegistry uniformLayoutRegistry = new UniformLayoutRegistry();

    @Unique
    private void naraka$loadEffects(List<ResourceLocation> dynamicPostEffects) {
        for (PostChain postChain : naraka$dynamicPostChains)
            postChain.close();
        naraka$dynamicPostChains.clear();

        for (ResourceLocation dynamicPostEffect : dynamicPostEffects) {
            try {
                ResourceLocation location = dynamicPostEffect.withPath(path -> "shaders/post/" + path + ".json");
                PostChain postChain = new PostChain(this.minecraft.getTextureManager(), this.resourceManager, this.minecraft.getMainRenderTarget(), location);
                postChain.resize(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
                naraka$dynamicPostChains.add(postChain);
            } catch (IOException e) {
                LOGGER.warn("Failed to load shader: {}", dynamicPostEffects, e);
            }
        }
    }

    @Inject(method = "resize", at = @At(value = "RETURN"))
    public void resizeDynamicPostEffects(int width, int height, CallbackInfo ci) {
        for (PostChain postChain : naraka$dynamicPostChains) {
            postChain.resize(width, height);
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = At.Shift.AFTER))
    private void applyDynamicPostEffects(float partialTicks, long nanoTime, boolean renderLevel, CallbackInfo ci) {
        if (getMainCamera().getEntity() instanceof Player player) {
            List<ResourceLocation> dynamicPostEffects = DynamicPostEffectHelper.get(player);
            naraka$loadEffects(dynamicPostEffects);
            uniformLayoutRegistry.extract(player.tickCount + partialTicks, dynamicPostEffects);

            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.resetTextureMatrix();
            for (PostChain postChain : naraka$dynamicPostChains) {
                postChain.process(partialTicks);
            }
        }
    }
}
