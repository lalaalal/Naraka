package com.yummy.naraka.mixin.client;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaClientContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Unique
    private static final Identifier MONOCHROME_POST_EFFECT = NarakaMod.location("monochrome");

    @Unique @Nullable
    private Identifier naraka$previousPostEffect;
    @Unique
    private boolean naraka$previousEffectActive;

    @Shadow @Nullable private Identifier postEffectId;

    @Shadow private boolean effectActive;

    @Shadow
    protected abstract void setPostEffect(Identifier postEffectId);

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = At.Shift.AFTER))
    private void checkMonochromePostEffect(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        naraka$previousPostEffect = postEffectId;
        naraka$previousEffectActive = effectActive;
        if (NarakaClientContext.MONOCHROME_EFFECT_TICK.getValue() > 0) {
            setPostEffect(MONOCHROME_POST_EFFECT);
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/fog/FogRenderer;endFrame()V"))
    private void restorePostEffect(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo ci) {
        postEffectId = naraka$previousPostEffect;
        effectActive = naraka$previousEffectActive;
    }
}
