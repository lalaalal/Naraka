package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Unique
    private boolean naraka$renderSoulFire = false;

    @Inject(method = "render(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/EntityRenderer;)V", at = @At("HEAD"))
    public <E extends Entity, S extends EntityRenderState> void checkSoulFire(E entity, double xOffset, double yOffset, double zOffset, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, EntityRenderer<? super E, S> renderer, CallbackInfo ci) {
        if (entity instanceof LivingEntity livingEntity) {
            naraka$renderSoulFire = EntityDataHelper.getEntityData(livingEntity, NarakaEntityDataTypes.IS_ON_PURIFIED_SOUL_FIRE.get());
        }
    }

    @ModifyExpressionValue(method = "renderFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 0))
    public TextureAtlasSprite modifyFire0(TextureAtlasSprite original) {
        if (naraka$renderSoulFire)
            return PurifiedSoulFireTextureProvider.modifyFire0();
        return original;
    }

    @ModifyExpressionValue(method = "renderFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/Material;sprite()Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", ordinal = 1))
    public TextureAtlasSprite modifyFire1(TextureAtlasSprite original) {
        if (naraka$renderSoulFire)
            return PurifiedSoulFireTextureProvider.modifyFire1();
        return original;
    }
}
