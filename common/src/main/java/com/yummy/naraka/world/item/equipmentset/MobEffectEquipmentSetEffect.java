package com.yummy.naraka.world.item.equipmentset;

import com.mojang.serialization.Codec;
import com.yummy.naraka.data.lang.LanguageKey;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public record MobEffectEquipmentSetEffect(List<MobEffectData> mobEffects) implements EquipmentSetEffect {
    public static final Codec<MobEffectEquipmentSetEffect> CODEC = MobEffectData.CODEC.listOf()
            .xmap(MobEffectEquipmentSetEffect::new, MobEffectEquipmentSetEffect::mobEffects);
    public static final StreamCodec<RegistryFriendlyByteBuf, MobEffectEquipmentSetEffect> STREAM_CODEC = MobEffectData.STREAM_CODEC.apply(ByteBufCodecs.list())
            .map(MobEffectEquipmentSetEffect::new, MobEffectEquipmentSetEffect::mobEffects);

    public static MobEffectEquipmentSetEffect of(MobEffectData... mobEffects) {
        return new MobEffectEquipmentSetEffect(List.of(mobEffects));
    }

    @Override
    public Type<?> type() {
        return NarakaEquipmentSetEffectTypes.MOB_EFFECT_EQUIPMENT_SET_EFFECT.get();
    }

    @Override
    public void activate(LivingEntity livingEntity) {
        for (MobEffectData mobEffectData : mobEffects) {
            if (!livingEntity.hasEffect(mobEffectData.effect()))
                livingEntity.addEffect(new MobEffectInstance(mobEffectData.effect(), mobEffectData.duration(), mobEffectData.amplifier()));
        }
    }

    @Override
    public void deactivate(LivingEntity livingEntity) {
        for (MobEffectData mobEffectData : mobEffects)
            livingEntity.removeEffect(mobEffectData.effect());
    }

    @Override
    public List<Component> getDescriptions() {
        List<Component> components = new ArrayList<>();
        for (MobEffectData mobEffect : mobEffects) {
            MutableComponent component = Component.translatable(LanguageKey.mobEffect(mobEffect.effect()));
            if (mobEffect.amplifier() > 0)
                component.append(CommonComponents.SPACE)
                        .append(Component.translatable("enchantment.level." + (mobEffect.amplifier() + 1)));
            components.add(component);
        }
        return components;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MobEffectEquipmentSetEffect(List<MobEffectData> effects))) return false;

        return mobEffects.equals(effects);
    }

    @Override
    public int hashCode() {
        return mobEffects.hashCode();
    }
}
