package com.yummy.naraka.fabric.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.world.item.PickRangeModifiable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
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
