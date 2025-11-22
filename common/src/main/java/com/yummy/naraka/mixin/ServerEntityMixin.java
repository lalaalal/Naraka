package com.yummy.naraka.mixin;

import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SyncEntityDataPacket;
import com.yummy.naraka.world.entity.data.EntityData;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerEntity.class)
public abstract class ServerEntityMixin {
    @Shadow @Final
    private Entity entity;

    /**
     * Synchronize entity data
     */
    @Inject(method = "addPairing", at = @At("RETURN"))
    public void addParingEntityData(ServerPlayer serverPlayer, CallbackInfo ci) {
        if (entity instanceof LivingEntity livingEntity) {
            List<EntityData<?, ? extends Entity>> data = EntityDataHelper.getEntityDataList(livingEntity);
            NetworkManager.sendToClient(serverPlayer, new SyncEntityDataPacket(livingEntity, data));
        }
    }
}
