package com.yummy.naraka.forge.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.world.item.PickRangeModifiable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Shadow
    public ServerPlayer player;

    @ModifyExpressionValue(method = "handleInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;canReachRaw(Lnet/minecraft/world/entity/Entity;D)Z"))
    private boolean modifyMaxInteractionDistance(boolean original, @Local Entity entity) {
        ItemStack itemStack = player.getMainHandItem();
        if (itemStack.getItem() instanceof PickRangeModifiable pickRangeModifiable)
            return player.isCloseEnough(entity, pickRangeModifiable.getPickRange());
        return original;
    }
}
