package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaRenderTypes;
import com.yummy.naraka.client.layer.HerobrineScarfLayer;
import com.yummy.naraka.client.renderer.entity.state.HerobrineScarfRenderState;
import com.yummy.naraka.client.renderer.entity.state.WavingScarfPose;
import com.yummy.naraka.client.renderer.entity.state.WavingScarfRenderState;
import com.yummy.naraka.client.renderer.entity.state.WavingScarfTexture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<S extends HumanoidRenderState> {
    @Inject(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V", at = @At("RETURN"))
    public void submitHerobrineScarf(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, S renderState, float yRot, float xRot, CallbackInfo ci) {
        if (renderState instanceof HerobrineScarfRenderState herobrineScarfRenderState) {
            WavingScarfRenderState wavingScarfRenderState = herobrineScarfRenderState.naraka$getScarfRenderState();
            if (!wavingScarfRenderState.display)
                return;
            for (WavingScarfRenderState.ModelData modelData : wavingScarfRenderState.modelDataList) {
                poseStack.pushPose();
                WavingScarfPose scarfPose = modelData.pose();
                WavingScarfTexture textureInfo = modelData.textureInfo();
                RenderType outsideRenderType = naraka$getOutsideRenderType(textureInfo);
                RenderType insideRenderType = naraka$getInsideRenderType(textureInfo);
                float scale = scarfPose.scale();
                Vec3 translation = scarfPose.translation();
                poseStack.scale(-scale, -scale, scale);
                poseStack.translate(translation);

                HerobrineScarfLayer.submitScarf(poseStack, insideRenderType, outsideRenderType, nodeCollector, packedLight, -1, wavingScarfRenderState, modelData);
                poseStack.popPose();
            }
        }
    }

    @Unique
    private RenderType naraka$getOutsideRenderType(WavingScarfTexture textureInfo) {
        return RenderType.entityCutout(textureInfo.texture(false));
    }

    @Unique
    private RenderType naraka$getInsideRenderType(WavingScarfTexture textureInfo) {
        if (NarakaClientContext.SHADER_ENABLED.getValue())
            return naraka$getOutsideRenderType(textureInfo);
        return NarakaRenderTypes.longinusCutout(textureInfo.texture(false));
    }
}
