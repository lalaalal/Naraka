package com.yummy.naraka.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.*;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class NarakaExtraCodecs {
    public static final Codec<EquipmentSlot> EQUIPMENT_SLOT = ExtraCodecs.stringResolverCodec(EquipmentSlot::getName, EquipmentSlot::byName);

    public static <K, V> Codec<Map<K, V>> dispatchMap(Codec<K> keyCodec, Function<K, Codec<? extends V>> elementCodec) {
        return new DynamicMapCodec<>(keyCodec, elementCodec);
    }

    private record DynamicMapCodec<K, V>(Codec<K> keyCodec, Function<K, Codec<? extends V>> elementCodec)
            implements Codec<Map<K, V>> {
        private Codec<? extends V> forDecode(K key) {
            return elementCodec.apply(key);
        }

        @SuppressWarnings("unchecked")
        private Codec<V> forEncode(K key) {
            return (Codec<V>) elementCodec.apply(key);
        }

        private <T> DataResult<Map<K, V>> decode(final DynamicOps<T> ops, final MapLike<T> input) {
            final ImmutableMap.Builder<K, V> read = ImmutableMap.builder();
            final ImmutableList.Builder<Pair<T, T>> failed = ImmutableList.builder();

            final DataResult<Unit> result = input.entries().reduce(
                    DataResult.success(Unit.INSTANCE, Lifecycle.stable()),
                    (r, pair) -> {
                        final DataResult<K> k = keyCodec.parse(ops, pair.getFirst());
                        Optional<K> optionalKey = k.result();
                        if (optionalKey.isEmpty())
                            return r;
                        final DataResult<? extends V> v = forDecode(optionalKey.get()).parse(ops, pair.getSecond());

                        final DataResult<Pair<K, V>> entry = k.apply2stable(Pair::of, v);
                        entry.error().ifPresent(e -> failed.add(pair));

                        return r.apply2stable((u, p) -> {
                            read.put(p.getFirst(), p.getSecond());
                            return u;
                        }, entry);
                    },
                    (r1, r2) -> r1.apply2stable((u1, u2) -> u1, r2)
            );

            final Map<K, V> elements = read.build();
            final T errors = ops.createMap(failed.build().stream());

            return result.map(unit -> elements).setPartial(elements).mapError(e -> e + " missed input: " + errors);
        }

        private <T> RecordBuilder<T> encode(final Map<K, V> input, final DynamicOps<T> ops, final RecordBuilder<T> prefix) {
            for (final Map.Entry<K, V> entry : input.entrySet()) {
                K key = entry.getKey();
                prefix.add(keyCodec.encodeStart(ops, entry.getKey()), forEncode(key).encodeStart(ops, entry.getValue()));
            }
            return prefix;
        }

        @Override
        public <T> DataResult<Pair<Map<K, V>, T>> decode(DynamicOps<T> ops, T input) {
            return ops.getMap(input).flatMap(map -> decode(ops, map)).map(r -> Pair.of(r, input));
        }

        @Override
        public <T> DataResult<T> encode(Map<K, V> input, DynamicOps<T> ops, T prefix) {
            return encode(input, ops, ops.mapBuilder()).build(prefix);
        }
    }
}
