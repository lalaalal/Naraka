package com.yummy.naraka.client.renderer.entity.state;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.world.entity.Herobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class HerobrineRenderState extends AbstractHerobrineRenderState {
    public int phase;
    public boolean renderScarf = false;
    public WavingScarfRenderState scarfRenderState = new WavingScarfRenderState();

    public void updateScarfRenderState(Herobrine herobrine, float partialTick) {
        renderScarf = herobrine.getPhase() >= 2 || NarakaConfig.CLIENT.alwaysDisplayHerobrineScarf.getValue();
        scarfRenderState.extract(herobrine, partialTick);
    }
}
