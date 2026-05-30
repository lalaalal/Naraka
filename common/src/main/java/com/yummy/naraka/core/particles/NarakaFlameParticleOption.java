package com.yummy.naraka.core.particles;

import com.mojang.serialization.Codec;
import com.yummy.naraka.world.item.SoulType;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import java.util.Map;

public enum NarakaFlameParticleOption implements ParticleOptions, StringRepresentable {
    REDSTONE, COPPER, GOLD, EMERALD, DIAMOND, LAPIS, NECTARIUM, AMETHYST, GOD_BLOOD;

    public static final Codec<NarakaFlameParticleOption> CODEC = StringRepresentable.fromEnum(NarakaFlameParticleOption::values);
    public static final StreamCodec<ByteBuf, NarakaFlameParticleOption> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
    private static final Map<SoulType, NarakaFlameParticleOption> BY_SOUL_TYPE = Map.of(
            SoulType.REDSTONE, REDSTONE,
            SoulType.COPPER, COPPER,
            SoulType.GOLD, GOLD,
            SoulType.EMERALD, EMERALD,
            SoulType.DIAMOND, DIAMOND,
            SoulType.LAPIS, LAPIS,
            SoulType.NECTARIUM, NECTARIUM,
            SoulType.AMETHYST, AMETHYST,
            SoulType.GOD_BLOOD, GOD_BLOOD
    );

    public static NarakaFlameParticleOption fromSoulType(SoulType soulType) {
        return BY_SOUL_TYPE.getOrDefault(soulType, NarakaFlameParticleOption.REDSTONE);
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }

    @Override
    public ParticleType<?> getType() {
        return NarakaParticleTypes.NARAKA_FLAME.get();
    }

    public static ParticleType<NarakaFlameParticleOption> type(boolean force) {
        return SimpleCodecParticleType.of(force, CODEC.fieldOf("style"), STREAM_CODEC);
    }
}
