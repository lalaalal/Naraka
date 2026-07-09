package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.world.item.PickRangeModifiable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Unique
    @Nullable
    private PostChain naraka$previousPostEffect;
    @Unique
    private boolean naraka$previousEffectActive;
    @Unique
    private boolean naraka$monochromeEffectActive = false;

    @Shadow
    private boolean effectActive;

    @Shadow
    @Nullable
    PostChain postEffect;

    @Shadow
    abstract void loadEffect(ResourceLocation resourceLocation);

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V", shift = At.Shift.AFTER))
    private void checkMonochromePostEffect(float partialTicks, long nanoTime, boolean renderLevel, CallbackInfo ci) {
        if (NarakaClientContext.POST_EFFECT_TICK.getValue() > 0 && !naraka$monochromeEffectActive) {
            naraka$previousPostEffect = postEffect;
            naraka$previousEffectActive = effectActive;
            loadEffect(NarakaClientContext.POST_EFFECT.getValue());
            naraka$monochromeEffectActive = true;
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;bindWrite(Z)V"))
    private void restorePostEffect(float partialTicks, long nanoTime, boolean renderLevel, CallbackInfo ci) {
        if (NarakaClientContext.POST_EFFECT_TICK.getValue() == 0 && naraka$monochromeEffectActive) {
            if (postEffect != null)
                postEffect.close();
            postEffect = naraka$previousPostEffect;
            effectActive = naraka$previousEffectActive;
            naraka$monochromeEffectActive = false;
        }
    }

    @ModifyExpressionValue(method = "pick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;hasFarPickRange()Z"))
    private boolean allowFarPickRange(boolean original, @Local(ordinal = 0) Entity entity) {
        if (entity instanceof Player player) {
            ItemStack itemStack = player.getMainHandItem();
            if (itemStack.getItem() instanceof PickRangeModifiable)
                return false;
        }
        return original;
    }

    @ModifyExpressionValue(method = "pick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;getPickRange()F"))
    private float modifyPickRange(float original, @Local(ordinal = 0) Entity entity) {
        if (entity instanceof Player player) {
            ItemStack itemStack = player.getMainHandItem();
            if (itemStack.getItem() instanceof PickRangeModifiable pickRangeModifiable)
                return pickRangeModifiable.getPickRange();
        }
        return original;
    }

    @ModifyVariable(method = "pick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ProjectileUtil;getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;"))
    private boolean setEntityPick(boolean bl, @Local(ordinal = 0) Entity entity) {
        if (entity instanceof Player player) {
            ItemStack itemStack = player.getMainHandItem();
            if (itemStack.getItem() instanceof PickRangeModifiable)
                return false;
        }
        return bl;
    }
}
