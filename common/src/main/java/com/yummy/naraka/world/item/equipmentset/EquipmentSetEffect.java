package com.yummy.naraka.world.item.equipmentset;

import com.mojang.serialization.Codec;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.util.NarakaExtraCodecs;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Map;

public interface EquipmentSetEffect {
    Codec<Map<Type<?>, EquipmentSetEffect>> MULTIPLE_CODEC = NarakaExtraCodecs.dispatchMap(Type.CODEC, Type::codec);

    static <T extends EquipmentSetEffect> Type<T> type(Codec<T> codec) {
        return new ConcreteType<>(codec);
    }

    Type<?> type();

    void activate(LivingEntity livingEntity);

    void deactivate(LivingEntity livingEntity);

    List<Component> getDescriptions();

    interface Type<T extends EquipmentSetEffect> {
        Codec<Type<?>> CODEC = RegistryFixedCodec.create(NarakaRegistries.Keys.EQUIPMENT_SET_EFFECT_TYPE).xmap(Holder::value, type -> NarakaRegistries.EQUIPMENT_SET_EFFECT_TYPE.wrapHolder(type).orElseThrow());

        Codec<T> codec();
    }

    record ConcreteType<T extends EquipmentSetEffect>(Codec<T> codec) implements Type<T> {
    }
}
