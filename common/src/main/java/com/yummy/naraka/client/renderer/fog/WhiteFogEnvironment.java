package com.yummy.naraka.client.renderer.fog;

import com.yummy.naraka.client.renderer.WhiteFogRenderHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class WhiteFogEnvironment extends FogEnvironment {
    @Override
    public void setupFog(FogData fogData, Camera camera, ClientLevel clientLevel, float renderDistance, DeltaTracker deltaTracker) {
        WhiteFogRenderHelper.setupWhiteFog(fogData, renderDistance, deltaTracker.getGameTimeDeltaPartialTick(false));
    }

    @Override
    public boolean isApplicable(@Nullable FogType fogType, Entity entity) {
        return WhiteFogRenderHelper.shouldApplyWhiteFog();
    }
}
