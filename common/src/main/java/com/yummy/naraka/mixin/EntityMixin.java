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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

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

    @Inject(method = "changeDimension", at = @At("HEAD"), cancellable = true)
    public void changeDimensionToNaraka(ServerLevel destination, CallbackInfoReturnable<Entity> cir) {
        if (level() instanceof ServerLevel currentLevel) {
            if (destination.dimension() == NarakaDimensions.NARAKA) {
                naraka$moveToNaraka(destination);
                cir.cancel();
            }

            if (currentLevel.dimension() == NarakaDimensions.NARAKA && destination.dimension() == Level.OVERWORLD) {
                naraka$moveToOverworldFromNaraka(destination);
                cir.cancel();
            }
        }
    }

    @Unique
    private void naraka$moveToNaraka(ServerLevel naraka) {
        BlockPos destinationPosition = NarakaPortalBlock.createRandomNarakaSpawnPosition(level.getRandom());
        Vec3 destinationPositionVec = Vec3.atBottomCenterOf(destinationPosition);
        naraka$spawnEntityToDestination(naraka, destinationPositionVec);
    }

    @Unique
    private void naraka$moveToOverworldFromNaraka(ServerLevel overworld) {
        if (naraka$self() instanceof ServerPlayer player) {
            BlockPos spawnBlockPos = player.getRespawnPosition();
            if (spawnBlockPos == null)
                spawnBlockPos = overworld.getSharedSpawnPos();
            Player.findRespawnPositionAndUseSpawnBlock(overworld, spawnBlockPos, player.getRespawnAngle(), player.isRespawnForced(), true);
        }
    }

    @Unique
    private void naraka$spawnEntityToDestination(ServerLevel destination, Vec3 position) {
        Entity entity = getType().create(destination);
        if (entity != null) {
            entity.restoreFrom(naraka$self());
            entity.moveTo(position.x, position.y, position.z, 180, entity.getXRot());
            entity.setDeltaMovement(getDeltaMovement());
        }
    }

    @Unique
    private Entity naraka$self() {
        return (Entity) (Object) this;
    }
}
