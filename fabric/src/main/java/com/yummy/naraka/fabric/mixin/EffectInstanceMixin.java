package com.yummy.naraka.fabric.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.shaders.Program;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EffectInstance.class)
@Environment(EnvType.CLIENT)
public abstract class EffectInstanceMixin {
    @Unique
    private static final String PREFIX = "shaders/program/";
    @Unique
    private static final String POSTFIX = ".json";

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;<init>(Ljava/lang/String;)V"))
    private String fixProgramModNamespace(String original, @Local(argsOnly = true) String name) {
        ResourceLocation location = new ResourceLocation(name);
        return location.getNamespace() + ":" + PREFIX + location.getPath() + POSTFIX;
    }

    @ModifyArg(method = "getOrCreate", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;<init>(Ljava/lang/String;)V"))
    private static String fixFragmentModNamespace(String original, @Local(argsOnly = true) Program.Type programType, @Local(argsOnly = true) String name) {
        ResourceLocation location = new ResourceLocation(name);
        return location.getNamespace() + ":" + PREFIX + location.getPath() + programType.getExtension();
    }
}
