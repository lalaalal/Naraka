package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaSprites;
import com.yummy.naraka.client.renderer.feature.FlameFeatureSubmitExtension;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import net.minecraft.client.renderer.feature.RenderTypeFeatureRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(FlameFeatureRenderer.class)
public abstract class FlameFeatureRendererMixin extends RenderTypeFeatureRenderer<FlameFeatureRenderer.Submit> {
    @Shadow
    protected abstract void prepare(FlameFeatureRenderer.Submit submit, VertexConsumer buffer, TextureAtlasSprite fire1, TextureAtlasSprite fire2);

    @ModifyArgs(method = "buildGroup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer;prepare(Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer$Submit;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V"))
    protected void buildGroup(Args args, @Local(argsOnly = true, name = "context") final FeatureFrameContext context, @Local(name = "submit") FlameFeatureRenderer.Submit submit) {
        if (((FlameFeatureSubmitExtension) (Object) submit).naraka$isPurifiedSoulFire()) {
            TextureAtlasSprite purifiedSoulFire0 = context.atlasManager().get(NarakaSprites.PURIFIED_SOUL_FIRE_0);
            TextureAtlasSprite purifiedSoulFire1 = context.atlasManager().get(NarakaSprites.PURIFIED_SOUL_FIRE_1);

            args.set(2, purifiedSoulFire0);
            args.set(3, purifiedSoulFire1);
        }
    }
}
