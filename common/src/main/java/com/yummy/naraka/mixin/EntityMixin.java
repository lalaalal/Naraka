package com.yummy.naraka.mixin;

import com.yummy.naraka.event.EntityEvents;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.SyncEntityDataPacket;
import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.world.entity.data.EntityData;
import com.yummy.naraka.world.entity.data.EntityDataExtension;
import com.yummy.naraka.world.entity.data.EntityDataType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityDataExtension {
    @Shadow
    public abstract Level level();

    @Unique
    private final Map<EntityDataType<?, ?>, EntityData<?, ?>> naraka$entityData = new HashMap<>();

    @Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"))
    public void saveEntityData(CompoundTag compound, CallbackInfoReturnable<CompoundTag> cir) {
        NarakaNbtUtils.store(compound, "EntityData", EntityData.CODEC.listOf(), level().registryAccess(), naraka$getEntityDataList());
    }

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"))
    public void readEntityData(CompoundTag compound, CallbackInfo ci) {
        List<EntityData<?, ?>> data = NarakaNbtUtils.read(compound, "EntityData", EntityData.CODEC.listOf(), RegistryOps.create(NbtOps.INSTANCE, level().registryAccess()))
                .orElse(List.of());
        naraka$loadEntityData(data);
        if (level() instanceof ServerLevel serverLevel)
            naraka$syncEntityData(serverLevel);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tickEntityData(CallbackInfo ci) {
        Collection<EntityDataType<?, ?>> types = Set.copyOf(naraka$entityData.keySet());
        types.forEach(type -> type.tick(naraka$self()));
    }

    @Override
    public <T, E extends Entity> boolean naraka$hasEntityData(EntityDataType<T, E> entityDataType) {
        return naraka$entityData.containsKey(entityDataType);
    }

    @Override
    public List<EntityData<?, ?>> naraka$getEntityDataList() {
        return List.copyOf(naraka$entityData.values());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, E extends Entity> EntityData<T, E> naraka$getEntityData(EntityDataType<T, E> entityDataType) {
        return (EntityData<T, E>) naraka$entityData.getOrDefault(entityDataType, entityDataType.getDefault());
    }

    @Override
    public <T, E extends Entity> void naraka$setEntityData(EntityData<T, E> entityData, boolean sync) {
        EntityDataType<T, E> entityDataType = entityData.type();
        E entity = entityDataType.getCastedTarget(naraka$self())
                .orElseThrow(() -> new IllegalArgumentException(naraka$self() + " is not a valid entity for " + entityDataType));
        T originalValue = naraka$getRawEntityData(entityDataType);
        naraka$entityData.put(entityDataType, entityData);

        EntityEvents.ENTITY_DATA_CHANGE.invoker(entityDataType)
                .onChange(entity, originalValue, entityData.value());

        if (sync && level() instanceof ServerLevel serverLevel)
            naraka$syncEntityData(serverLevel, List.of(entityData));
    }

    @Override
    public void naraka$loadEntityData(List<EntityData<?, ?>> dataList) {
        for (EntityData<?, ?> data : dataList)
            naraka$setEntityData(data, false);
    }

    @Override
    public void naraka$removeEntityData(EntityDataType<?, ?> entityDataType) {
        if (entityDataType.shouldSynchronize() && level() instanceof ServerLevel serverLevel) {
            SyncEntityDataPacket packet = SyncEntityDataPacket.sync(naraka$self(), SyncEntityDataPacket.Action.REMOVE_GIVEN, naraka$getEntityData(entityDataType));
            NetworkManager.clientbound().send(serverLevel.players(), packet);
        }
        naraka$entityData.remove(entityDataType);
    }

    @Override
    public void naraka$syncEntityData(ServerLevel level) {
        naraka$syncEntityData(level, naraka$getEntityDataList());
    }

    @Unique
    private void naraka$syncEntityData(ServerLevel level, List<EntityData<?, ?>> dataList) {
        List<EntityData<?, ?>> synchronizeData = dataList.stream()
                .filter(entityData -> entityData.type().shouldSynchronize())
                .toList();
        if (synchronizeData.isEmpty())
            return;
        SyncEntityDataPacket packet = SyncEntityDataPacket.sync(naraka$self(), SyncEntityDataPacket.Action.LOAD, synchronizeData);
        NetworkManager.clientbound().send(level.players(), packet);
    }

    @Unique
    private Entity naraka$self() {
        return (Entity) (Object) this;
    }
}
