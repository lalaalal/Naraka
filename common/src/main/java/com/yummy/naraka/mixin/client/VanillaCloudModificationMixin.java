package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.config.NarakaConfig;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LevelRenderer.class)
public abstract class VanillaCloudModificationMixin {
    @ModifyVariable(method = "renderClouds", at = @At(value = "STORE"), ordinal = 4)
    private double speedUpClouds(double e) {
        if (naraka$isHerobrineSkyEnabled())
            return e * NarakaConfig.CLIENT.herobrineSkyCloudSpeed.getValue();
        return e;
    }

    @ModifyExpressionValue(method = "renderClouds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getCloudColor(F)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 modifyCloudColor(Vec3 original) {
        if (naraka$isHerobrineSkyEnabled())
            return new Vec3(1, 1, 1);
        return original;
    }

    @Unique
    private static boolean naraka$isHerobrineSkyEnabled() {
        return NarakaClientContext.ENABLE_HEROBRINE_SKY.getValue();
    }
}
