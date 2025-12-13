package com.yummy.naraka.client.renderer;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;

public class NarakaDimensionSpecialEffects {
    public static final NarakaEffects NARAKA = new NarakaEffects();

    public static class NarakaEffects extends DimensionSpecialEffects {
        public NarakaEffects() {
            super(SkyType.NONE, true, true);
        }

        @Override
        public Vec3 getBrightnessDependentFogColor(Vec3 fogColor, float brightness) {
            return new Vec3(1, 1, 1);
        }

        @Override
        public boolean isFoggyAt(int x, int y) {
            return false;
        }
    }
}
