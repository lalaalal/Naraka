package com.yummy.naraka.client.renderer.entity;

import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.layer.ShadowHerobrineArmorLayer;
import com.yummy.naraka.client.layer.ShadowHerobrineHeadLayer;
import com.yummy.naraka.client.renderer.entity.state.ShadowHerobrineRenderState;
import com.yummy.naraka.world.entity.ShadowHerobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@Environment(EnvType.CLIENT)
public class ShadowHerobrineRenderer extends AbstractHerobrineRenderer<ShadowHerobrine, ShadowHerobrineRenderState> {
    public ShadowHerobrineRenderer(EntityRendererProvider.Context context) {
        super(context, NarakaModelLayers.HEROBRINE);
        this.addLayer(new ShadowHerobrineArmorLayer(this, context));
        this.addLayer(new ShadowHerobrineHeadLayer(this));
    }

    @Override
    public ShadowHerobrineRenderState createRenderState() {
        return new ShadowHerobrineRenderState();
    }
}
