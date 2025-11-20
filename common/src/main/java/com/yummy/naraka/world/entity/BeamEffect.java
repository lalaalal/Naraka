package com.yummy.naraka.world.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public record BeamEffect(long startTick, double tickLength, double beamLength,
                         double radius, double thetaOffset, double yRot,
                         double stretch, double yInterval, double rotationInterval, int color,
                         PositionFunction positionFunction) {
    public static final Codec<BeamEffect> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.LONG.fieldOf("start_tick").forGetter(BeamEffect::startTick),
                    Codec.DOUBLE.fieldOf("tick_length").forGetter(BeamEffect::tickLength),
                    Codec.DOUBLE.fieldOf("beam_length").forGetter(BeamEffect::beamLength),
                    Codec.DOUBLE.fieldOf("radius").forGetter(BeamEffect::radius),
                    Codec.DOUBLE.fieldOf("theta_offset").forGetter(BeamEffect::thetaOffset),
                    Codec.DOUBLE.fieldOf("y_rot").forGetter(BeamEffect::yRot),
                    Codec.DOUBLE.fieldOf("stretch").forGetter(BeamEffect::stretch),
                    Codec.DOUBLE.fieldOf("y_interval").forGetter(BeamEffect::yInterval),
                    Codec.DOUBLE.fieldOf("rotationInterval").forGetter(BeamEffect::rotationInterval),
                    Codec.INT.fieldOf("color").forGetter(BeamEffect::color),
                    PositionFunction.CODEC.fieldOf("position_function").forGetter(BeamEffect::positionFunction)
            ).apply(instance, BeamEffect::new)
    );

    public static final Codec<List<BeamEffect>> MUTABLE_LIST_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    BeamEffect.CODEC.listOf().fieldOf("beam_effects").forGetter(beamEffects -> beamEffects)
            ).apply(instance, ArrayList::new)
    );

    public static BeamEffect spin(Entity entity, Speed speed, double radius, double thetaOffset, double yRot, double yInterval, int color) {
        return new BeamEffect(entity.tickCount, speed.tickLength, speed.beamLength, radius, thetaOffset, yRot, 1, yInterval, Math.PI / 180, color, PositionFunction.SPIN);
    }

    public static Vec3 spin(BeamEffect beamEffect, double theta) {
        double x = Math.cos(theta) * beamEffect.radius();
        double z = Math.sin(theta) * beamEffect.radius();
        double y = theta * beamEffect.yInterval();

        return new Vec3(x, y, z);
    }

    public static Vec3 spinOpposite(BeamEffect beamEffect, double theta) {
        double x = Math.cos(-theta) * beamEffect.radius();
        double z = Math.sin(-theta) * beamEffect.radius();
        double y = theta * beamEffect.yInterval();

        return new Vec3(x, y, z);
    }

    public boolean isFinished(long tickCount) {
        return tickCount - startTick >= tickLength;
    }

    public Vec3 calculatePosition(double theta) {
        return positionFunction.calculate(this, theta);
    }

    public enum PositionFunction implements StringRepresentable {
        SPIN(BeamEffect::spin),
        SPIN_OPPOSITE(BeamEffect::spinOpposite);

        public static final Codec<PositionFunction> CODEC = StringRepresentable.fromValues(PositionFunction::values);

        private final BiFunction<BeamEffect, Double, Vec3> function;

        PositionFunction(BiFunction<BeamEffect, Double, Vec3> function) {
            this.function = function;
        }

        public Vec3 calculate(BeamEffect effect, double theta) {
            Vec3 position = function.apply(effect, theta + effect.thetaOffset());
            double x = position.x * Math.cos(effect.yRot()) - position.z * Math.sin(effect.yRot());
            double z = position.x * Math.sin(effect.yRot()) + position.z * Math.cos(effect.yRot());
            return new Vec3(x, position.y, z);
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }

    public enum Speed {
        SLOW(20, 10),
        NORMAL(15, 10),
        FAST(10, 5);

        private final double tickLength;
        private final double beamLength;

        Speed(double tickLength, double beamLength) {
            this.tickLength = tickLength;
            this.beamLength = beamLength;
        }
    }
}
