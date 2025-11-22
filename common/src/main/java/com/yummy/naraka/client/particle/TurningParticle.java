package com.yummy.naraka.client.particle;

import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationLocations;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Environment(EnvType.CLIENT)
public class TurningParticle extends TextureSheetParticle {
    private final Predicate<TurningParticle> spreadPredicate;
    private boolean spread = false;
    private final double startX;
    private final double startZ;
    private final double startAngle;
    private double speed;
    private double radius;

    public TurningParticle(ClientLevel clientLevel, Predicate<TurningParticle> spreadPredicate, int baseLifetime, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed);
        this.spreadPredicate = spreadPredicate;
        lifetime = baseLifetime + random.nextInt(0, 20);
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
            if (spreadPredicate.test(this)) {
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
    protected int getLightColor(float partialTick) {
        return LightTexture.pack(15, 15);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static Provider herobrineSpawn(SpriteSet sprites) {
        return new Provider(sprites, 100, TurningParticle::isHerobrineExist);
    }

    public static Provider parrying(SpriteSet sprites) {
        return new Provider(sprites, 10, TurningParticle::isParryingSucceeded)
                .withModifier(particle -> {
                    particle.radius = 2;
                    particle.speed *= 2;
                    particle.setColor(0.839f, 0.361f, 0.839f);
                });
    }

    private static boolean isHerobrineExist(TurningParticle particle) {
        return !particle.level.getEntities(EntityTypeTest.forClass(Herobrine.class),
                        AABB.ofSize(new Vec3(particle.x, particle.y, particle.z), 10, 10, 10),
                        entity -> true
                )
                .isEmpty();
    }

    private static boolean isParryingSucceeded(TurningParticle particle) {
        return particle.level.getEntitiesOfClass(Herobrine.class, AABB.ofSize(new Vec3(particle.x, particle.y, particle.z), 10, 10, 10))
                .stream()
                .findAny()
                .map(herobrine -> herobrine.getCurrentAnimation().equals(HerobrineAnimationLocations.PARRYING_SUCCEED))
                .orElse(false);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        private final Predicate<TurningParticle> spreadPredicate;
        private Consumer<TurningParticle> modifier = particle -> {};
        private final int baseLifetime;

        public Provider(SpriteSet sprites, int baseLifetime, Predicate<TurningParticle> spreadPredicate) {
            this.sprites = sprites;
            this.spreadPredicate = spreadPredicate;
            this.baseLifetime = baseLifetime;
        }

        public Provider withModifier(Consumer<TurningParticle> modifier) {
            this.modifier = modifier;
            return this;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            TurningParticle particle = new TurningParticle(level, spreadPredicate, baseLifetime, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(sprites);
            modifier.accept(particle);
            return particle;
        }
    }
}
