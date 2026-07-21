package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {
    @Shadow
    @Final
    EntityTickList tickingEntities;

    protected ClientLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, profiler, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
    }

    @Shadow
    public abstract void tickNonPassenger(Entity entity);

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void blockIfFrozen(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        if (NarakaClientContext.TICK_FROZEN.getValue())
            ci.cancel();
    }

    @Inject(method = "tickEntities", at = @At("HEAD"), cancellable = true)
    public void tickOnlyAllowedEntities(CallbackInfo ci) {
        if (!NarakaClientContext.TICK_FROZEN.getValue())
            return;
        ci.cancel();
        ProfilerFiller profilerFiller = this.getProfiler();
        profilerFiller.push("entities");
        this.tickingEntities.forEach(entity -> {
            if ((!entity.isRemoved() && !entity.isPassenger()
                    && EntityDataHelper.getRawEntityData(entity, NarakaEntityDataTypes.KEEP_UNFROZEN.getConcreteValue()))
                    || entity instanceof Player) {
                this.guardEntityTick(this::tickNonPassenger, entity);
            }
        });
        profilerFiller.pop();
    }
}
