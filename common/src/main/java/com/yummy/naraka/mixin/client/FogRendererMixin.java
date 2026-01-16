package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.renderer.WhiteFogRenderHelper;
import com.yummy.naraka.client.renderer.fog.WhiteFogEnvironment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
    @Shadow @Final private static List<FogEnvironment> FOG_ENVIRONMENTS;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addWhiteFogEnvironment(CallbackInfo ci) {
        FOG_ENVIRONMENTS.addFirst(new WhiteFogEnvironment());
    }

    @Inject(method = "computeFogColor", at = @At("RETURN"), cancellable = true)
    private static void computeFogColor(Camera camera, float partialTick, ClientLevel clientLevel, int distance, float g, CallbackInfoReturnable<Vector4f> cir) {
        Vector4f color = cir.getReturnValue();
        if (WhiteFogRenderHelper.shouldApplyWhiteFog()) {
            cir.setReturnValue(WhiteFogRenderHelper.getFogColor(color, partialTick));
            cir.cancel();
        }
    }
}
