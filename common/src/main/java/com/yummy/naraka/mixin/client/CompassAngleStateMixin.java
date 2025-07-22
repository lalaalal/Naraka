package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import com.yummy.naraka.world.item.component.SanctuaryTracker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.properties.numeric.CompassAngleState;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(CompassAngleState.class)
public abstract class CompassAngleStateMixin {
    @Shadow @Final
    private CompassAngleState.CompassTarget compassTarget;

    @ModifyExpressionValue(method = "calculate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/properties/numeric/CompassAngleState$CompassTarget;get(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/core/GlobalPos;"))
    protected GlobalPos modifyTargetPosition(GlobalPos original, @Local(argsOnly = true) ItemStack stack) {
        if (compassTarget != CompassAngleState.CompassTarget.NONE)
            return original;
        SanctuaryTracker tracker = stack.get(NarakaDataComponentTypes.SANCTUARY_TRACKER.get());
        if (tracker == null)
            return original;
        return tracker.sanctuaryPos().orElse(original);
    }
}
