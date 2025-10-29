package com.yummy.naraka.core.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.core.registries.NarakaRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public interface DataComponentApplier {
    StreamCodec<RegistryFriendlyByteBuf, Single<?>> SINGLE_STREAM_CODEC = ByteBufCodecs.registry(NarakaRegistries.Keys.DATA_COMPONENT_APPLIER)
            .dispatch(Single::type, Type::singleStreamCodec);

    Codec<Single<?>> SINGLE_CODEC = NarakaRegistries.DATA_COMPONENT_APPLIER.byNameCodec()
            .dispatch(Single::type, Type::wrappedCodec);

    static Single<Empty> empty() {
        return new Single<>(NarakaDataComponentAppliers.EMPTY.get(), Empty.getInstance());
    }

    void apply(ItemStack itemStack);

    class Empty implements DataComponentApplier {
        private static final Empty EMPTY = new Empty();

        public static Empty getInstance() {
            return EMPTY;
        }

        private Empty() {

        }

        @Override
        public void apply(ItemStack itemStack) {

        }
    }

    record Single<T extends DataComponentApplier>(Type<T> type, T applier) {
        public ItemStack apply(ItemStack itemStack) {
            applier.apply(itemStack);
            return itemStack;
        }
    }

    final class Type<T extends DataComponentApplier> {
        private final MapCodec<Single<T>> wrappedCodec;
        private final StreamCodec<RegistryFriendlyByteBuf, Single<T>> singleStreamCodec;

        public Type(Codec<T> codec) {
            this.wrappedCodec = RecordCodecBuilder.mapCodec(
                    instance -> instance.group(
                            codec.fieldOf("value").forGetter(Single::applier)
                    ).apply(instance, applier -> new Single<>(this, applier))
            );
            this.singleStreamCodec = ByteBufCodecs.fromCodecWithRegistries(codec)
                    .map(applier -> new Single<>(this, applier), Single::applier);
        }

        public MapCodec<Single<T>> wrappedCodec() {
            return wrappedCodec;
        }

        public StreamCodec<RegistryFriendlyByteBuf, Single<T>> singleStreamCodec() {
            return singleStreamCodec;
        }
    }
}
