package com.yummy.naraka.mixin;

import com.yummy.naraka.world.entity.data.EntityData;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/world/level/storage/ValueOutput;)V"))
    public void saveEntityData(ValueOutput output, CallbackInfo ci) {
        if (EntityDataHelper.hasEntityData(naraka$self())) {
            List<EntityData<?, ?>> data = EntityDataHelper.getEntityDataList(naraka$self());
            output.store("EntityData", EntityData.CODEC.listOf(), data);
        }
    }

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/world/level/storage/ValueInput;)V"))
    public void readEntityData(ValueInput input, CallbackInfo ci) {
        List<EntityData<?, ?>> data = input.read("EntityData", EntityData.CODEC.listOf())
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
