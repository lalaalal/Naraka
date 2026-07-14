package com.yummy.naraka.world.item.equipmentset;

import com.mojang.serialization.Codec;
import com.yummy.naraka.core.registries.NarakaRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Map;

public interface EquipmentSetEffect {
    Codec<Map<Type<?>, EquipmentSetEffect>> MULTIPLE_CODEC = Codec.dispatchedMap(Type.CODEC, Type::codec);
    StreamCodec<RegistryFriendlyByteBuf, Map<Type<?>, EquipmentSetEffect>> MULTIPLE_STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(MULTIPLE_CODEC);

    static <T extends EquipmentSetEffect> Type<T> type(Codec<T> codec) {
        return new ConcreteType<>(codec, ByteBufCodecs.fromCodecWithRegistries(codec));
    }

    static <T extends EquipmentSetEffect> Type<T> type(Codec<T> codec, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
        return new ConcreteType<>(codec, streamCodec);
    }

    Type<?> type();

    void activate(LivingEntity livingEntity);

    void deactivate(LivingEntity livingEntity);

    List<Component> getDescriptions();

    interface Type<T extends EquipmentSetEffect> {
        Codec<Type<?>> CODEC = NarakaRegistries.EQUIPMENT_SET_EFFECT_TYPE.byNameCodec();
        StreamCodec<RegistryFriendlyByteBuf, Type<?>> STREAM_CODEC = ByteBufCodecs.registry(NarakaRegistries.Keys.EQUIPMENT_SET_EFFECT_TYPE);

        Codec<T> codec();

        StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();
    }

    record ConcreteType<T extends EquipmentSetEffect>(Codec<T> codec,
                                                      StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) implements Type<T> {
    }
}
