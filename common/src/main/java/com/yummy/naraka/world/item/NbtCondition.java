package com.yummy.naraka.world.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

public record NbtCondition(Type type, List<CompoundTag> conditions) {
    public static final Codec<NbtCondition> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Type.CODEC.fieldOf("type").forGetter(NbtCondition::type),
                    CompoundTag.CODEC.listOf().fieldOf("conditions").forGetter(NbtCondition::conditions)
            ).apply(instance, NbtCondition::new)
    );

    public static final NbtCondition EMPTY = any();

    public static NbtCondition any(CompoundTag... conditions) {
        return new NbtCondition(Type.ANY, List.of(conditions));
    }

    public static NbtCondition all(CompoundTag... conditions) {
        return new NbtCondition(Type.ALL, List.of(conditions));
    }

    public boolean test(ItemStack item) {
        return type.test(item, conditions);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NbtCondition nbtCondition))
            return false;

        return type == nbtCondition.type && conditions.equals(nbtCondition.conditions);
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

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);

        private final BiPredicate<ItemStack, List<CompoundTag>> predicate;

        Type(BiPredicate<ItemStack, List<CompoundTag>> predicate) {
            this.predicate = predicate;
        }

        private static boolean testSingle(ItemStack item, CompoundTag condition) {
            for (String key : condition.getAllKeys()) {
                CompoundTag itemNbt = item.getTag();
                if (itemNbt == null)
                    return false;
                Tag value = condition.get(key);
                if (value == null)
                    continue;
                if (!Objects.equals(itemNbt.get(key), value))
                    return false;
            }
            return true;
        }

        private static boolean any(ItemStack item, List<CompoundTag> conditions) {
            if (conditions.isEmpty())
                return true;
            for (CompoundTag condition : conditions) {
                if (testSingle(item, condition))
                    return true;
            }
            return false;
        }

        private static boolean all(ItemStack item, List<CompoundTag> conditions) {
            for (CompoundTag condition : conditions) {
                if (!testSingle(item, condition))
                    return false;
            }
            return true;
        }

        public boolean test(ItemStack item, List<CompoundTag> conditions) {
            return predicate.test(item, conditions);
        }

        @Override
        public String getSerializedName() {
            return name();
        }
    }
}
