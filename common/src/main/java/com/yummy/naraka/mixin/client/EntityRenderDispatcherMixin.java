package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.PurifiedSoulFireTextureProvider;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Shadow
    private Quaternionf cameraOrientation;

    @Shadow
    protected abstract void renderFlame(PoseStack poseStack, MultiBufferSource bufferSource, EntityRenderState renderState, Quaternionf quaternion);

    @Inject(method = "render(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/EntityRenderer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;render(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;DDDLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/EntityRenderer;)V"))
    public <E extends Entity, S extends EntityRenderState> void renderPurifiedSoulFire(E entity, double xOffset, double yOffset, double zOffset, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, EntityRenderer<? super E, S> renderer, CallbackInfo ci, @Local S renderState) {
        if (entity instanceof LivingEntity livingEntity && !livingEntity.isSpectator()
                && EntityDataHelper.getEntityData(livingEntity, NarakaEntityDataTypes.PURIFIED_SOUL_FIRE_TICK.get()) > 0) {
            PurifiedSoulFireTextureProvider.setUsePurifiedSoulFireTexture(true);
        }
    }

    @Inject(
            method = "render(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;DDDLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/EntityRenderer;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
                    shift = At.Shift.AFTER
            )
    )
    public <S extends EntityRenderState> void renderPurifiedSoulFire(S renderState, double xOffset, double yOffset, double zOffset, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, EntityRenderer<?, S> renderer, CallbackInfo ci) {
        if (PurifiedSoulFireTextureProvider.isUsingPurifiedSoulFireTexture()) {
            renderFlame(poseStack, bufferSource, renderState, Mth.rotationAroundAxis(Mth.Y_AXIS, this.cameraOrientation, new Quaternionf()));
            PurifiedSoulFireTextureProvider.setUsePurifiedSoulFireTexture(false);
        }
    }

    @ModifyExpressionValue(method = "renderFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 0))
    public TextureAtlasSprite modifyFire0(TextureAtlasSprite original) {
        if (PurifiedSoulFireTextureProvider.isUsingPurifiedSoulFireTexture())
            return PurifiedSoulFireTextureProvider.modifyFire0();
        return original;
    }

    @ModifyExpressionValue(method = "renderFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 1))
    public TextureAtlasSprite modifyFire1(TextureAtlasSprite original) {
        if (PurifiedSoulFireTextureProvider.isUsingPurifiedSoulFireTexture())
            return PurifiedSoulFireTextureProvider.modifyFire1();
        return original;
    }
}
