package com.yummy.naraka.forge.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.world.item.PickRangeModifiable;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @ModifyExpressionValue(method = "pick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getEntityReach()D"))
    private double modifyEntityRange(double original, @Local(ordinal = 0) Entity entity) {
        if (entity instanceof Player player) {
            ItemStack itemStack = player.getMainHandItem();
            if (itemStack.getItem() instanceof PickRangeModifiable pickRangeModifiable)
                return pickRangeModifiable.getPickRange();
        }
        return original;
    }

    @ModifyExpressionValue(method = "pick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getBlockReach()D"))
    private double modifyBlockRange(double original, @Local(ordinal = 0) Entity entity) {
        if (entity instanceof Player player) {
            ItemStack itemStack = player.getMainHandItem();
            if (itemStack.getItem() instanceof PickRangeModifiable pickRangeModifiable)
                return pickRangeModifiable.getPickRange();
        }
        return original;
    }
}
