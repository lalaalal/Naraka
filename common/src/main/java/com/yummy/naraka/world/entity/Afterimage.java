package com.yummy.naraka.world.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class Afterimage {
    public static final StreamCodec<ByteBuf, Afterimage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(Vec3.CODEC),
            Afterimage::getPosition,
            ByteBufCodecs.FLOAT,
            Afterimage::getYRot,
            ByteBufCodecs.VAR_INT,
            Afterimage::getMaxTickCount,
            ByteBufCodecs.FLOAT,
            afterimage -> afterimage.tickCount,
            Afterimage::new
    );

    private final Vec3 position;
    private final float yRot;
    private final int maxTickCount;
    private float tickCount;

    public static <T extends Entity & AfterimageEntity> Afterimage of(T entity, int maxTickCount) {
        return new Afterimage(entity.position(), entity.getYRot(), maxTickCount);
    }

    public static Afterimage lerp(float delta, Afterimage start, Afterimage end) {
        double x = Mth.lerp(delta, start.position.x, end.position.x);
        double y = Mth.lerp(delta, start.position.y, end.position.y);
        double z = Mth.lerp(delta, start.position.z, end.position.z);
        Vec3 position = new Vec3(x, y, z);
        float yRot = Mth.lerp(delta, start.yRot, end.yRot);
        float tickCount = Mth.lerp(delta, start.tickCount, end.tickCount);

        return new Afterimage(position, yRot, start.maxTickCount, tickCount);
    }

    public Afterimage(Vec3 position, float yRot, int maxTickCount) {
        this(position, yRot, maxTickCount, -5f);
    }

    protected Afterimage(Vec3 position, float yRot, int maxTickCount, float tickCount) {
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

    public float getPartialTicks() {
        return tickCount - Mth.floor(tickCount);
    }

    public boolean tick() {
        tickCount += 1;
        return tickCount >= maxTickCount;
    }

    public float getAlpha01(float partialTicks) {
        if (tickCount < 0)
            return 0;
        return Mth.clamp(1 - (tickCount + partialTicks) / (float) maxTickCount, 0, 1);
    }

    public int getAlpha(float partialTicks) {
        return Mth.ceil(getAlpha01(partialTicks) * 0xff);
    }
}
