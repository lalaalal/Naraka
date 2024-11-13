package com.yummy.naraka.client.particle;

import com.yummy.naraka.core.particles.SoulParticleOption;
import com.yummy.naraka.util.Color;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.PortalParticle;
import net.minecraft.client.particle.TextureSheetParticle;

public class SoulParticle extends PortalParticle {
    public static TextureSheetParticle create(SoulParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        Color color = Color.of(type.soulType().getColor());
        SoulParticle particle = new SoulParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
        particle.setColor(color.red01(), color.green01(), color.blue01());
        return particle;
    }

    protected SoulParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    }
}
