package com.yummy.naraka.mixin;

import com.yummy.naraka.world.entity.data.EntityDataExtension;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public abstract class ServerEntityMixin {
    @Shadow
    @Final
    private Entity entity;

    /**
     * Synchronize entity data
     */
    @Inject(method = "addPairing", at = @At("RETURN"))
    public void addParingEntityData(ServerPlayer player, CallbackInfo ci) {
        if (entity instanceof EntityDataExtension entityDataExtension)
            entityDataExtension.naraka$syncEntityData(player.serverLevel());
    }
}
