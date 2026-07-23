package com.yummy.naraka.core.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

public record DataComponentCondition(Type type, List<DataComponentPatch> conditions) {
    public static final Codec<DataComponentCondition> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Type.CODEC.fieldOf("type").forGetter(DataComponentCondition::type),
                    DataComponentPatch.CODEC.listOf().fieldOf("conditions").forGetter(DataComponentCondition::conditions)
            ).apply(instance, DataComponentCondition::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, DataComponentCondition> STREAM_CODEC = StreamCodec.composite(
            Type.STREAM_CODEC,
            DataComponentCondition::type,
            DataComponentPatch.STREAM_CODEC.apply(ByteBufCodecs.list()),
            DataComponentCondition::conditions,
            DataComponentCondition::new
    );

    public static final DataComponentCondition EMPTY = any();

    public static DataComponentCondition any(DataComponentPatch... conditions) {
        return new DataComponentCondition(Type.ANY, List.of(conditions));
    }

    public static DataComponentCondition all(DataComponentPatch... conditions) {
        return new DataComponentCondition(Type.ALL, List.of(conditions));
    }

    public boolean test(DataComponentHolder item) {
        return type.test(item, conditions);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DataComponentCondition(Type otherType, List<DataComponentPatch> otherConditions)))
            return false;

        return type == otherType && conditions.equals(otherConditions);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + conditions.hashCode();
        return result;
    }

    public enum Type implements StringRepresentable {
        ANY(Type::any),
        ALL(Type::all);

        public static final Codec<Type> CODEC = StringRepresentable.fromValues(Type::values);
        public static final StreamCodec<ByteBuf, Type> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

        private final BiPredicate<DataComponentHolder, List<DataComponentPatch>> predicate;

        Type(BiPredicate<DataComponentHolder, List<DataComponentPatch>> predicate) {
            this.predicate = predicate;
        }

        private static boolean testSingle(DataComponentHolder item, DataComponentPatch condition) {
            for (Map.Entry<DataComponentType<?>, Optional<?>> entry : condition.entrySet()) {
                DataComponentType<?> type = entry.getKey();
                Optional<?> value = entry.getValue();
                if (value.isEmpty())
                    continue;
                if (!Objects.equals(item.get(type), value.get()))
                    return false;
            }
            return true;
        }

        private static boolean any(DataComponentHolder item, List<DataComponentPatch> conditions) {
            if (conditions.isEmpty())
                return true;
            for (DataComponentPatch condition : conditions) {
                if (testSingle(item, condition))
                    return true;
            }
            return false;
        }

        private static boolean all(DataComponentHolder item, List<DataComponentPatch> conditions) {
            for (DataComponentPatch condition : conditions) {
                if (!testSingle(item, condition))
                    return false;
            }
            return true;
        }

        public boolean test(DataComponentHolder item, List<DataComponentPatch> conditions) {
            return predicate.test(item, conditions);
        }

        @Override
        public String getSerializedName() {
            return name();
        }
    }

}
