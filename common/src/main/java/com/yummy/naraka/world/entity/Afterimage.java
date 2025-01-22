package com.yummy.naraka.world.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class Afterimage {
    public static final Codec<Afterimage> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Vec3.CODEC.fieldOf("position").forGetter(Afterimage::getPosition),
                    Codec.INT.fieldOf("max_tick_count").forGetter(Afterimage::getMaxTickCount),
                    Codec.INT.fieldOf("tick").forGetter(afterimage -> afterimage.tickCount)
            ).apply(instance, Afterimage::new)
    );

    public static final StreamCodec<ByteBuf, Afterimage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(Vec3.CODEC),
            Afterimage::getPosition,
            ByteBufCodecs.VAR_INT,
            Afterimage::getMaxTickCount,
            ByteBufCodecs.VAR_INT,
            afterimage -> afterimage.tickCount,
            Afterimage::new
    );

    private final Vec3 position;
    private final int maxTickCount;
    private int tickCount;

    public Afterimage(Vec3 position, int maxTickCount) {
        this.position = position;
        this.maxTickCount = Math.max(maxTickCount, 1);
        this.tickCount = 0;
    }

    protected Afterimage(Vec3 position, int maxTickCount, int tickCount) {
        this.position = position;
        this.maxTickCount = maxTickCount;
        this.tickCount = tickCount;
    }

    public Vec3 translation(Entity entity) {
        return position.subtract(entity.position());
    }

    public Vec3 getPosition() {
        return position;
    }

    public int getMaxTickCount() {
        return maxTickCount;
    }

    public boolean tick() {
        tickCount += 1;
        return tickCount >= maxTickCount;
    }

    public int getAlpha() {
        float alpha01 = 1 - tickCount / (float) maxTickCount;
        return (int) (alpha01 * 0.8 * 0xff);
    }
}
