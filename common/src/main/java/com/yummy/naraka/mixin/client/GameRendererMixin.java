package com.yummy.naraka.mixin.client;

import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    // TODO : Add post effect uniforms
}
