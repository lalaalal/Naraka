package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaRenderTypes;
import com.yummy.naraka.client.layer.HerobrineScarfLayer;
import com.yummy.naraka.client.renderer.entity.state.WavingScarfPose;
import com.yummy.naraka.client.renderer.entity.state.WavingScarfRenderState;
import com.yummy.naraka.client.renderer.entity.state.WavingScarfTexture;
import com.yummy.naraka.core.component.NarakaDataComponentTypes;
import com.yummy.naraka.world.entity.ScarfWavingData;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity> {
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At("RETURN"))
    private void renderHerobrineScarf(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        ItemStack itemStack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if (itemStack.getOrDefault(NarakaDataComponentTypes.HEROBRINE_SCARF.get(), false)) {
            for (WavingScarfRenderState.ModelData modelData : WavingScarfRenderState.ModelType.HUMANOID.modelData) {
                poseStack.pushPose();
                WavingScarfPose scarfPose = modelData.pose();
                WavingScarfTexture textureInfo = modelData.textureInfo();
                RenderType outsideRenderType = naraka$getOutsideRenderType(textureInfo);
                RenderType insideRenderType = naraka$getInsideRenderType(textureInfo);
                ScarfWavingData scarfWavingData = EntityDataHelper.getRawEntityData(livingEntity, NarakaEntityDataTypes.SCARF_WAVING_DATA.get());
                float rotationDegree = scarfWavingData.getScarfRotationDegree();
                float scale = scarfPose.scale();
                Vec3 translation = scarfPose.translation();
                poseStack.scale(-scale, -scale, scale);
                poseStack.translate(translation.x, translation.y, translation.z);

                HerobrineScarfLayer.renderScarf(poseStack, insideRenderType, outsideRenderType, buffer, packedLight, -1, partialTicks, rotationDegree, scarfWavingData, modelData);
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
