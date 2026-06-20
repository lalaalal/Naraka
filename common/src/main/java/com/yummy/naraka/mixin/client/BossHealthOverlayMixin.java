package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.renderer.ProgressOverlayExtensionRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.world.BossEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossHealthOverlay.class)
public abstract class BossHealthOverlayMixin {
    @Inject(method = "extractBar(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IILnet/minecraft/world/BossEvent;)V", at = @At("RETURN"))
    private void renderExtension(GuiGraphicsExtractor graphics, int x, int y, BossEvent event, CallbackInfo ci) {
        ProgressOverlayExtensionRenderer.INSTANCE.render(event.getId(), x, y, graphics);
    }
}
