package com.yummy.naraka.world.item.equipmentset;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;

public abstract class EquipmentSetEffectType<T> {
    private final MapCodec<EquipmentSetEffect<T>> mapCodec;
    private final StreamCodec<RegistryFriendlyByteBuf, EquipmentSetEffect<T>> streamCodec;

    public EquipmentSetEffectType(Codec<T> dataCodec) {
        this.mapCodec = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        dataCodec.fieldOf("data").forGetter(EquipmentSetEffect::data)
                ).apply(instance, data -> new EquipmentSetEffect<>(this, data))
        );
        this.streamCodec = ByteBufCodecs.fromCodecWithRegistries(dataCodec)
                .map(data -> new EquipmentSetEffect<>(this, data), EquipmentSetEffect::data);
    }

    public abstract void activate(LivingEntity livingEntity, T data);

    public abstract void deactivate(LivingEntity livingEntity, T data);

    public MapCodec<EquipmentSetEffect<T>> mapCodec() {
        return mapCodec;
    }

    public StreamCodec<RegistryFriendlyByteBuf, EquipmentSetEffect<T>> streamCodec() {
        return streamCodec;
    }
}
