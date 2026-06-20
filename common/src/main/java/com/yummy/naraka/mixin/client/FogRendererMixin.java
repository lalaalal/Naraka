package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.renderer.WhiteFogRenderHelper;
import com.yummy.naraka.client.renderer.fog.WhiteFogEnvironment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
    @Shadow
    @Final
    private static List<FogEnvironment> FOG_ENVIRONMENTS;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addWhiteFogEnvironment(CallbackInfo ci) {
        FOG_ENVIRONMENTS.addFirst(new WhiteFogEnvironment());
    }

    @Inject(method = "computeFogColor", at = @At("RETURN"))
    private void computeFogColor(Camera camera, float partialTicks, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector4f dest, CallbackInfo ci) {
        if (WhiteFogRenderHelper.shouldApplyWhiteFog()) {
            dest.set(WhiteFogRenderHelper.getFogColor(dest, partialTicks));
        }
    }
}
