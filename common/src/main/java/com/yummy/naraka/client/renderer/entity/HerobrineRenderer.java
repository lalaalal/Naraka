package com.yummy.naraka.client.renderer.entity;

import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.layer.HerobrineScarfLayer;
import com.yummy.naraka.client.renderer.entity.state.HerobrineRenderState;
import com.yummy.naraka.world.entity.Herobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@Environment(EnvType.CLIENT)
public class HerobrineRenderer extends AbstractHerobrineRenderer<Herobrine, HerobrineRenderState> {
    public HerobrineRenderer(EntityRendererProvider.Context context) {
        super(context, NarakaModelLayers.HEROBRINE);
        addLayer(new HerobrineScarfLayer(this, context));
    }

    @Override
    public HerobrineRenderState createRenderState() {
        return new HerobrineRenderState();
    }

    @Override
    public void extractRenderState(Herobrine herobrine, HerobrineRenderState renderState, float partialTicks) {
        super.extractRenderState(herobrine, renderState, partialTicks);
        renderState.updateScarfRenderState(herobrine, partialTicks);
    }
}
