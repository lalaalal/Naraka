package com.yummy.naraka.world.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class Afterimage {
    public static final Codec<Afterimage> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Vec3.CODEC.fieldOf("position").forGetter(Afterimage::getPosition),
                    Codec.FLOAT.fieldOf("y_rot").forGetter(Afterimage::getYRot),
                    Codec.INT.fieldOf("max_tick_count").forGetter(Afterimage::getMaxTickCount),
                    Codec.INT.fieldOf("tick").forGetter(afterimage -> afterimage.tickCount)
            ).apply(instance, Afterimage::new)
    );

    public static final StreamCodec<ByteBuf, Afterimage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(Vec3.CODEC),
            Afterimage::getPosition,
            ByteBufCodecs.FLOAT,
            Afterimage::getYRot,
            ByteBufCodecs.VAR_INT,
            Afterimage::getMaxTickCount,
            ByteBufCodecs.VAR_INT,
            afterimage -> afterimage.tickCount,
            Afterimage::new
    );

    private final Vec3 position;
    private final float yRot;
    private final int maxTickCount;
    private int tickCount;

    public static <T extends Entity & AfterimageEntity> Afterimage of(T entity, int maxTickCount) {
        return new Afterimage(entity.position(), entity.getYRot(), maxTickCount);
    }

    public Afterimage(Vec3 position, float yRot, int maxTickCount) {
        this(position, yRot, maxTickCount, -4);
    }

    protected Afterimage(Vec3 position, float yRot, int maxTickCount, int tickCount) {
        this.yRot = yRot;
        this.position = position;
        this.maxTickCount = Math.max(maxTickCount, 1);
        this.tickCount = tickCount;
    }

    public Vec3 translation(Entity entity, float partialTicks) {
        return position.subtract(entity.getPosition(partialTicks));
    }

    public Vec3 getPosition() {
        return position;
    }

    public float getYRot() {
        return yRot;
    }

    public int getMaxTickCount() {
        return maxTickCount;
    }

    public boolean tick() {
        tickCount += 1;
        return tickCount >= maxTickCount;
    }

    public int getAlpha() {
        if (tickCount < 0)
            return 0;
        float alpha01 = 1 - tickCount / (float) maxTickCount;
        return Mth.clamp(Mth.ceil(alpha01 * 0xff), 0, 0xff);
    }
}
