package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.renderer.feature.FlameFeatureSubmitExtension;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(FlameFeatureRenderer.Submit.class)
public abstract class FlameFeatureSubmitMixin implements FlameFeatureSubmitExtension {
    @Unique
    private boolean naraka$isPurifiedSoulFire = false;

    @Override
    public boolean naraka$isPurifiedSoulFire() {
        return naraka$isPurifiedSoulFire;
    }

    @Override
    public void naraka$setPurifiedSoulFire() {
        naraka$isPurifiedSoulFire = true;
    }
}
