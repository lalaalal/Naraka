package com.yummy.naraka.fabric.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.shaders.Program;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EffectInstance.class)
@Environment(EnvType.CLIENT)
public class EffectInstanceMixin {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;withDefaultNamespace(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"))
    private static ResourceLocation fixProgramModNamespace(String location, @Local(argsOnly = true) String name) {
        ResourceLocation resourceLocation = ResourceLocation.parse(name);
        return ResourceLocation.fromNamespaceAndPath(resourceLocation.getNamespace(), "shaders/program/" + resourceLocation.getPath() + ".json");
    }

    @Redirect(method = "getOrCreate", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;withDefaultNamespace(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;"))
    private static ResourceLocation fixFragmentModNamespace(String location, @Local(argsOnly = true) Program.Type type, @Local(argsOnly = true) String name) {
        ResourceLocation resourceLocation = ResourceLocation.parse(name);
        return ResourceLocation.fromNamespaceAndPath(resourceLocation.getNamespace(), "shaders/program/" + resourceLocation.getPath() + type.getExtension());
    }
}
