package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.Lifecycle;
import com.yummy.naraka.config.NarakaConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(WorldOpenFlows.class)
public abstract class WorldOpenFlowsMixin {
    @ModifyExpressionValue(method = "openWorldCheckWorldStemCompatibility", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/WorldData;worldGenSettingsLifecycle()Lcom/mojang/serialization/Lifecycle;"))
    private static Lifecycle disableExperimentalWarning(Lifecycle original) {
        if (NarakaConfig.CLIENT.disableWorldOpenExperimentalWarning.getValue())
            return Lifecycle.stable();
        return original;
    }
}
