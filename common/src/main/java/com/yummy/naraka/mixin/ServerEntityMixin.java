package com.yummy.naraka.mixin;

import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.network.SyncEntityDataPayload;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.EntityDataType;
import dev.architectury.networking.NetworkManager;
import net.minecraft.core.HolderSet;
import net.minecraft.nbt.CompoundTag;
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

@Mixin(ServerEntity.class)
public abstract class ServerEntityMixin {
    @Shadow
    @Final
    private Entity entity;

    /**
     * Synchronize entity data
     */
    @Inject(method = "addPairing", at = @At("RETURN"))
    public void addParingEntityData(ServerPlayer serverPlayer, CallbackInfo ci) {
        if (entity instanceof LivingEntity livingEntity) {
            HolderSet<EntityDataType<?>> entityDataTypes = HolderSet.direct(
                    NarakaRegistries.ENTITY_DATA_TYPE.holders()
                            .filter(holder -> EntityDataHelper.hasEntityData(livingEntity, holder.value()))
                            .toList()
            );
            CompoundTag data = new CompoundTag();
            EntityDataHelper.saveEntityData(livingEntity, data);
            NetworkManager.sendToPlayer(serverPlayer, new SyncEntityDataPayload(livingEntity, entityDataTypes, data));
        }
    }
}
