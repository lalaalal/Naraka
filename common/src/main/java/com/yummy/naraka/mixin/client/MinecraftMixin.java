package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.event.ClientEvents;
import com.yummy.naraka.core.posteffects.DynamicPostEffectType;
import com.yummy.naraka.core.posteffects.NarakaDynamicPostEffectTypes;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.world.entity.data.StunHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.ShaderManager;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    @Nullable
    public LocalPlayer player;

    @Shadow
    @Final
    private ShaderManager shaderManager;

    /**
     * Preload dynamic post effects due to prevent lag during boss play.
     * {@link LevelTargetBundle#MAIN_TARGETS} is modified by {@link LevelTargetBundleMixin}
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void preloadDynamicPostEffects(GameConfig gameConfig, CallbackInfo ci) {
        NarakaRegistries.DYNAMIC_POST_EFFECT_TYPE.stream()
                        .map(DynamicPostEffectType::id)
                .forEach(id -> shaderManager.getPostChain(id, LevelTargetBundle.MAIN_TARGETS));
    }

    @Inject(method = "close", at = @At("HEAD"))
    public void onMinecraftStopping(CallbackInfo ci) {
        ClientEvents.CLIENT_STOPPING.invoker().run(Minecraft.getInstance());
    }

    @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
    public void preventItemUseOnStun(CallbackInfo ci) {
        if (player != null && StunHelper.isStun(player))
            ci.cancel();
    }

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    public void preventAttackOnStun(CallbackInfoReturnable<Boolean> cir) {
        if (player != null && StunHelper.isStun(player))
            cir.cancel();
    }
}
