package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.renderer.ProgressOverlayExtensionRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.world.BossEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BossHealthOverlay.class)
public abstract class BossHealthOverlayMixin {
    @Inject(method = "drawBar(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/world/BossEvent;)V", at = @At("RETURN"))
    private void renderExtension(GuiGraphics guiGraphics, int x, int y, BossEvent bossEvent, CallbackInfo ci) {
        ProgressOverlayExtensionRenderer.INSTANCE.render(bossEvent.getId(), x, y, guiGraphics);
    }
}
