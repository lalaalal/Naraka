package com.yummy.naraka.mixin;

import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.world.NarakaDimensions;
import com.yummy.naraka.world.block.NarakaPortalBlock;
import com.yummy.naraka.world.entity.data.EntityData;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract Level level();

    @Shadow
    private Level level;

    @Shadow
    public abstract EntityType<?> getType();

    @Shadow
    public abstract Vec3 getDeltaMovement();

    @Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"))
    public void saveEntityData(CompoundTag compound, CallbackInfoReturnable<CompoundTag> cir) {
        if (EntityDataHelper.hasEntityData(naraka$self())) {
            List<EntityData<?, ?>> data = EntityDataHelper.getEntityDataList(naraka$self());
            NarakaNbtUtils.store(compound, "EntityData", EntityData.CODEC.listOf(), RegistryOps.create(NbtOps.INSTANCE, level().registryAccess()), data);
        }
    }

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"))
    public void readEntityData(CompoundTag compound, CallbackInfo ci) {
        List<EntityData<?, ?>> data = NarakaNbtUtils.read(compound, "EntityData", EntityData.CODEC.listOf(), RegistryOps.create(NbtOps.INSTANCE, level().registryAccess()))
                .orElse(List.of());
        EntityDataHelper.loadEntityDataList(naraka$self(), data);
    }

    @Inject(method = "remove", at = @At("RETURN"))
    public void removeEntityData(Entity.RemovalReason reason, CallbackInfo ci) {
        if (reason.shouldDestroy())
            EntityDataHelper.removeEntityData(naraka$self());
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tickEntityData(CallbackInfo ci) {
        EntityDataHelper.getEntityDataTypes(naraka$self())
                .forEach(type -> type.tick(naraka$self()));
    }

    @Inject(method = "findDimensionEntryPoint", at = @At("HEAD"), cancellable = true)
    public void findDimensionEntryPoint(ServerLevel destination, CallbackInfoReturnable<PortalInfo> cir) {
        if (destination.dimension() == NarakaDimensions.NARAKA) {
            BlockPos destinationPosition = NarakaPortalBlock.createRandomNarakaSpawnPosition(level.getRandom());
            Vec3 destinationPositionVec = Vec3.atBottomCenterOf(destinationPosition);
            cir.setReturnValue(new PortalInfo(destinationPositionVec, Vec3.ZERO, 180, 0));
            cir.cancel();
        }
        if (destination.dimension() == Level.OVERWORLD && level.dimension() == NarakaDimensions.NARAKA) {
            BlockPos spawnBlockPos = destination.getSharedSpawnPos();
            if (naraka$self() instanceof ServerPlayer player)
                spawnBlockPos = Objects.requireNonNullElse(player.getRespawnPosition(), spawnBlockPos);

            Vec3 destinationPositionVec = Vec3.atBottomCenterOf(spawnBlockPos);
            cir.setReturnValue(new PortalInfo(destinationPositionVec, Vec3.ZERO, 180, 0));
            cir.cancel();
        }
    }

    @Unique
    private Entity naraka$self() {
        return (Entity) (Object) this;
    }
}
