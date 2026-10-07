package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.NarakaClientContext;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    @Final
    private List<Identifier> requestedPostEffects;

    @Inject(method = "update", at = @At("RETURN"))
    private void customPostEffects(CallbackInfo ci) {
        if (NarakaClientContext.POST_EFFECT_TICK.getValue() > 0) {
            Identifier newPostEffectId = NarakaClientContext.POST_EFFECT.getValue();
            requestedPostEffects.add(newPostEffectId);
        }
    }
}
