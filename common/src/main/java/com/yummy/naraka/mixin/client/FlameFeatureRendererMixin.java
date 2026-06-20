package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaSprites;
import com.yummy.naraka.client.renderer.FlameFeatureExtension;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import net.minecraft.client.renderer.feature.RenderTypeFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(FlameFeatureRenderer.class)
public abstract class FlameFeatureRendererMixin extends RenderTypeFeatureRenderer<FlameFeatureRenderer.Submit> {
    @Shadow
    protected abstract void prepare(FlameFeatureRenderer.Submit submit, VertexConsumer buffer, TextureAtlasSprite fire1, TextureAtlasSprite fire2);

    @Override
    protected void buildGroup(FeatureFrameContext context, List<FlameFeatureRenderer.Submit> submits) {
        @SuppressWarnings("deprecation")
        VertexConsumer builder = this.getVertexBuilder(RenderTypes.entityCutoutCull(TextureAtlas.LOCATION_BLOCKS));
        TextureAtlasSprite fire0 = context.atlasManager().get(ModelBakery.FIRE_0);
        TextureAtlasSprite fire1 = context.atlasManager().get(ModelBakery.FIRE_1);
        TextureAtlasSprite purifiedSoulFire0 = context.atlasManager().get(NarakaSprites.PURIFIED_SOUL_FIRE_0);
        TextureAtlasSprite purifiedSoulFire1 = context.atlasManager().get(NarakaSprites.PURIFIED_SOUL_FIRE_1);

        for (FlameFeatureRenderer.Submit submit : submits) {
            if (((FlameFeatureExtension) (Object) submit).naraka$isPurifiedSoulFire()) {
                this.prepare(submit, builder, purifiedSoulFire0, purifiedSoulFire1);
            } else {
                this.prepare(submit, builder, fire0, fire1);
            }
        }
    }
}
