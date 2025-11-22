package com.yummy.naraka.mixin;

import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.world.entity.data.EntityData;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
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

    @Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"))
    public void saveEntityData(CompoundTag output, CallbackInfoReturnable<CompoundTag> cir) {
        if (EntityDataHelper.hasEntityData(naraka$self())) {
            List<EntityData<?, ?>> data = EntityDataHelper.getEntityDataList(naraka$self());
            NarakaNbtUtils.store(output, "EntityData", EntityData.CODEC.listOf(), RegistryOps.create(NbtOps.INSTANCE, level().registryAccess()), data);
        }
    }

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"))
    public void readEntityData(CompoundTag input, CallbackInfo ci) {
        List<EntityData<?, ?>> data = NarakaNbtUtils.read(input, "EntityData", EntityData.CODEC.listOf(), RegistryOps.create(NbtOps.INSTANCE, level().registryAccess()))
                .orElse(List.of());
        EntityDataHelper.loadEntityDataList(naraka$self(), data);
    }

    @Inject(method = "remove", at = @At("RETURN"))
    public void removeEntityData(Entity.RemovalReason removalReason, CallbackInfo ci) {
        if (removalReason.shouldDestroy())
            EntityDataHelper.removeEntityData(naraka$self());
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tickEntityData(CallbackInfo ci) {
        EntityDataHelper.getEntityDataTypes(naraka$self())
                .forEach(type -> type.tick(naraka$self()));
    }

    @Unique
    private Entity naraka$self() {
        return (Entity) (Object) this;
    }
}
