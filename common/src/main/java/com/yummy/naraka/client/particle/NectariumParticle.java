package com.yummy.naraka.client.particle;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.particles.NarakaParticleTypes;
import com.yummy.naraka.mixin.invoker.DripParticleInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.material.Fluids;

@Environment(EnvType.CLIENT)
public class NectariumParticle {
    public static TextureSheetParticle createNectariumHangParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        NarakaMod.LOGGER.info("createNectariumHangParticle");
        DripParticle particle = DripParticleInvoker.DripHangParticleInvoker.create(level, x, y, z, Fluids.EMPTY, NarakaParticleTypes.FALLING_NECTARIUM.get());
        NarakaMod.LOGGER.info("setting...");
        particle.setLifetime(100);
        particle.setColor(0.7f, 0.4f, 0.7f);
        return particle;
    }

    public static TextureSheetParticle createNectariumFallParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        DripParticle particle = DripParticleInvoker.HoneyFallAndLandParticleInvoker.create(level, x, y, z, Fluids.EMPTY, NarakaParticleTypes.LANDING_NECTARIUM.get());
        particle.setColor(0.7f, 0.4f, 0.7f);
        return particle;
    }

    public static TextureSheetParticle createNectariumLandParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        DripParticle particle = DripParticleInvoker.DripLandParticleInvoker.create(level, x, y, z, Fluids.EMPTY);
        particle.setLifetime((int) (128 / Math.random() * 0.8 + 0.2));
        particle.setColor(0.7f, 0.4f, 0.7f);
        return particle;
    }
}
