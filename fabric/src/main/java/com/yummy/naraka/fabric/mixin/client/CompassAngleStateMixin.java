package com.yummy.naraka.fabric.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import com.yummy.naraka.world.item.component.SanctuaryTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.CompassAngleState;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CompassAngleState.class)
public abstract class CompassAngleStateMixin {
    @Shadow @Final
    private CompassAngleState.CompassTarget compassTarget;

    @Unique
    private ItemStack naraka$target = ItemStack.EMPTY;

    @Inject(method = "calculate", at = @At("HEAD"))
    protected void calculate(ItemStack stack, ClientLevel level, int seed, Entity entity, CallbackInfoReturnable<Float> cir) {
        naraka$target = stack;
    }

    @ModifyExpressionValue(method = "calculate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/properties/numeric/CompassAngleState$CompassTarget;get(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/core/GlobalPos;"))
    protected GlobalPos modifyTargetPosition(GlobalPos original) {
        if (compassTarget != CompassAngleState.CompassTarget.NONE)
            return original;
        SanctuaryTracker tracker = naraka$target.get(NarakaDataComponentTypes.SANCTUARY_TRACKER.get());
        if (tracker == null)
            return original;
        return tracker.sanctuaryPos().orElse(original);
    }
}
