package com.yummy.naraka.core.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public record SoulParticleOption(SoulType soulType) implements ParticleOptions {
    public static final Codec<SoulParticleOption> CODEC = SoulType.CODEC.xmap(
            SoulParticleOption::new,
            option -> option.soulType
    );

    public static SoulParticleOption with(SoulType soulType) {
        return new SoulParticleOption(soulType);
    }

    public static SoulParticleOption fromCommand(ParticleType<SoulParticleOption> particleType, StringReader reader) throws CommandSyntaxException {
        return new SoulParticleOption(SoulType.valueOf(reader.readString()));
    }

    public static SoulParticleOption fromNetwork(ParticleType<SoulParticleOption> particleType, FriendlyByteBuf buffer) {
        return buffer.readJsonWithCodec(CODEC);
    }

    public static ParticleType<SoulParticleOption> type(boolean force) {
        return SimpleCodecParticleType.of(force, CODEC, SoulParticleOption::fromCommand, SoulParticleOption::fromNetwork);
    }

    @Override
    public ParticleType<?> getType() {
        return NarakaParticleTypes.SOUL.getConcreteValue();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeJsonWithCodec(CODEC, this);
    }

    @Override
    public String writeToString() {
        return soulType.getSerializedName();
    }
}
