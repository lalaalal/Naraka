package com.yummy.naraka.core.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

public record DataComponentCondition(Type type, List<DataComponentMap> conditions) {
    public static final Codec<DataComponentCondition> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Type.CODEC.fieldOf("type").forGetter(DataComponentCondition::type),
                    DataComponentMap.CODEC.listOf().fieldOf("conditions").forGetter(DataComponentCondition::conditions)
            ).apply(instance, DataComponentCondition::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, DataComponentCondition> STREAM_CODEC = StreamCodec.composite(
            Type.STREAM_CODEC,
            DataComponentCondition::type,
            ByteBufCodecs.fromCodecWithRegistries(DataComponentMap.CODEC).apply(ByteBufCodecs.list()),
            DataComponentCondition::conditions,
            DataComponentCondition::new
    );

    public static final DataComponentCondition EMPTY = any();

    public static DataComponentCondition any(DataComponentMap... conditions) {
        return new DataComponentCondition(Type.ANY, List.of(conditions));
    }

    public static DataComponentCondition all(DataComponentMap... conditions) {
        return new DataComponentCondition(Type.ALL, List.of(conditions));
    }

    public boolean test(DataComponentHolder item) {
        return type.test(item, conditions);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DataComponentCondition(Type otherType, List<DataComponentMap> otherConditions)))
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

        private final BiPredicate<DataComponentHolder, List<DataComponentMap>> predicate;

        Type(BiPredicate<DataComponentHolder, List<DataComponentMap>> predicate) {
            this.predicate = predicate;
        }

        private static boolean testSingle(DataComponentHolder item, DataComponentMap condition) {
            for (TypedDataComponent<?> component : condition) {
                DataComponentType<?> type = component.type();
                Object value = component.value();
                if (!Objects.equals(item.get(type), value))
                    return false;
            }
            return true;
        }

        private static boolean any(DataComponentHolder item, List<DataComponentMap> conditions) {
            if (conditions.isEmpty())
                return true;
            for (DataComponentMap condition : conditions) {
                if (testSingle(item, condition))
                    return true;
            }
            return false;
        }

        private static boolean all(DataComponentHolder item, List<DataComponentMap> conditions) {
            for (DataComponentMap condition : conditions) {
                if (!testSingle(item, condition))
                    return false;
            }
            return true;
        }

        public boolean test(DataComponentHolder item, List<DataComponentMap> conditions) {
            return predicate.test(item, conditions);
        }

        @Override
        public String getSerializedName() {
            return name();
        }
    }

}
