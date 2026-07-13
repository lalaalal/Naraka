package com.yummy.naraka.world.item.equipmentset;

import com.mojang.serialization.Codec;
import com.yummy.naraka.core.registries.NarakaRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;

public record EquipmentSetEffect<T>(EquipmentSetEffectType<T> type, T data) {
    public static final Codec<EquipmentSetEffect<?>> CODEC = NarakaRegistries.EQUIPMENT_SET_EFFECT_TYPE.byNameCodec()
            .dispatch(EquipmentSetEffect::type, EquipmentSetEffectType::mapCodec);

    public static final StreamCodec<RegistryFriendlyByteBuf, EquipmentSetEffect<?>> STREAM_CODEC = ByteBufCodecs.registry(NarakaRegistries.Keys.EQUIPMENT_SET_EFFECT_TYPE)
            .dispatch(EquipmentSetEffect::type, EquipmentSetEffectType::streamCodec);

    public void activate(LivingEntity livingEntity) {
        this.type.activate(livingEntity, data);
    }

    public void deactivate(LivingEntity livingEntity) {
        this.type.deactivate(livingEntity, data);
    }
}
