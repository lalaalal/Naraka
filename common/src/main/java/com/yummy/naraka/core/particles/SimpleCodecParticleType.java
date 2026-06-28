package com.yummy.naraka.core.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class SimpleCodecParticleType<T extends ParticleOptions> extends ParticleType<T> {
    private final Codec<T> codec;

    public static <T extends ParticleOptions> SimpleCodecParticleType<T> of(
            boolean force,
            Codec<T> codec,
            CommandDeserializer<T> commandDeserializer,
            NetworkDeserializer<T> networkDeserializer
    ) {
        return new SimpleCodecParticleType<>(force, codec, new SimpleDeserializer<>(commandDeserializer, networkDeserializer));
    }

    @SuppressWarnings("deprecation")
    protected SimpleCodecParticleType(boolean force, Codec<T> codec, ParticleOptions.Deserializer<T> deserializer) {
        super(force, deserializer);
        this.codec = codec;
    }

    @Override
    public Codec<T> codec() {
        return codec;
    }

    public interface CommandDeserializer<T extends ParticleOptions> {
        T deserialize(ParticleType<T> particleType, StringReader value) throws CommandSyntaxException;
    }

    public interface NetworkDeserializer<T extends ParticleOptions> {
        T deserialize(ParticleType<T> particleType, FriendlyByteBuf value);
    }

    @SuppressWarnings("deprecation")
    private record SimpleDeserializer<T extends ParticleOptions>(
            CommandDeserializer<T> commandDeserializer,
            NetworkDeserializer<T> networkDeserializer
    ) implements ParticleOptions.Deserializer<T> {

        @Override
        public T fromCommand(ParticleType<T> particleType, StringReader reader) throws CommandSyntaxException {
            return commandDeserializer.deserialize(particleType, reader);
        }

        @Override
        public T fromNetwork(ParticleType<T> particleType, FriendlyByteBuf buffer) {
            return networkDeserializer.deserialize(particleType, buffer);
        }
    }
}
