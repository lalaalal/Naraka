package com.yummy.naraka.mixin;

import com.yummy.naraka.world.TickFreezeManager;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level {
    @Shadow
    @Final
    private PersistentEntitySectionManager<Entity> entityManager;

    @Shadow
    @Final
    EntityTickList entityTickList;

    @Shadow
    protected abstract boolean shouldDiscardEntity(Entity entity);

    @Shadow
    @Final
    private ServerChunkCache chunkSource;

    @Shadow
    public abstract void tickNonPassenger(Entity entity);

    protected ServerLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, profiler, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void checkTickFreeze(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        ProfilerFiller profilerFiller = this.getProfiler();
        if (TickFreezeManager.INSTANCE.shouldFreezeLevel(this)) {
            ci.cancel();
            entityTickList.forEach(entity -> {
                if (!entity.isRemoved() && !TickFreezeManager.INSTANCE.shouldFreezeEntity(entity)) {
                    if (this.shouldDiscardEntity(entity)) {
                        entity.discard();
                    } else {
                        profilerFiller.push("checkDespawn");
                        entity.checkDespawn();
                        profilerFiller.pop();
                        if (this.chunkSource.chunkMap.getDistanceManager().inEntityTickingRange(entity.chunkPosition().toLong())) {
                            Entity entity2 = entity.getVehicle();
                            if (entity2 != null) {
                                if (!entity2.isRemoved() && entity2.hasPassenger(entity)) {
                                    return;
                                }
                                entity.stopRiding();
                            }

                            profilerFiller.push("tick");
                            this.guardEntityTick(this::tickNonPassenger, entity);
                            profilerFiller.pop();
                        }
                    }
                }
            });
            profilerFiller.push("entityManagement");
            this.entityManager.tick();
            profilerFiller.pop();
        }
    }
}
