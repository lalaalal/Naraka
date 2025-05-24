package com.yummy.naraka.client.particle;

import com.yummy.naraka.world.entity.Herobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;

@Environment(EnvType.CLIENT)
public class HerobrineSpawnParticle extends TextureSheetParticle {
    private final double radius;
    private final double speed;
    private boolean spread = false;
    private final double startX;
    private final double startZ;
    private final double startAngle;

    public HerobrineSpawnParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed);
        lifetime = 200 + random.nextInt(0, 20);
        speed = Math.PI / random.nextInt(5, 10);
        int randomNumber = random.nextInt(2, 5);
        radius = 1 + (randomNumber * randomNumber) / 5.0;
        startAngle = random.nextDouble() * Math.PI * 2;
        this.startX = x;
        this.startZ = z;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (age >= lifetime)
            remove();

        if (!spread) {
            Collection<Herobrine> entities = level.getEntities(EntityTypeTest.forClass(Herobrine.class), AABB.ofSize(new Vec3(x, y, z), 10, 10, 10), entity -> true);
            if (!entities.isEmpty()) {
                spread = true;
                this.xd = xd * 5;
                this.zd = zd * 5;
            }
            double t = age * speed + startAngle;
            this.xd = -speed * Math.sin(t) * radius;
            this.zd = speed * Math.cos(t) * radius;
            this.x = Math.cos(t) * radius + startX;
            this.z = Math.sin(t) * radius + startZ;
        } else {
            this.x += xd;
            this.z += zd;
        }

        this.age += 1;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            HerobrineSpawnParticle particle = new HerobrineSpawnParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(sprites);
            return particle;
        }
    }
}
