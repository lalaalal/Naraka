package com.yummy.naraka.client.renderer.entity.state;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.world.entity.Herobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.List;

@Environment(EnvType.CLIENT)
public class HerobrineRenderState extends AbstractHerobrineRenderState {
    public boolean renderScarf = false;
    public float scarfRotationDegree = 90;
    public List<Float> scarfWaveSpeedList = List.of();

    public void updateScarfRenderState(Herobrine herobrine, float partialTick) {
        renderScarf = herobrine.getPhase() == 2 || NarakaConfig.CLIENT.alwaysDisplayHerobrineScarf.getValue();
        scarfRotationDegree = herobrine.getScarfRotationDegree(partialTick) - NarakaConfig.CLIENT.herobrineScarfDefaultRotation.getValue();
        scarfWaveSpeedList = herobrine.getScarfWaveSpeedList();
    }
}
