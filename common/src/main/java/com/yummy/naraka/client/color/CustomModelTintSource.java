package com.yummy.naraka.client.color;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import org.jspecify.annotations.Nullable;

public record CustomModelTintSource(int index, int defaultColor) implements ItemTintSource {
    public static final MapCodec<CustomModelTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("index").forGetter(CustomModelTintSource::index),
                    ExtraCodecs.ARGB_COLOR_CODEC.fieldOf("default").forGetter(CustomModelTintSource::defaultColor)
            ).apply(instance, CustomModelTintSource::new)
    );

    public static CustomModelTintSource of(int index, int alpha, int color) {
        return new CustomModelTintSource(index, ARGB.color(color, alpha));
    }

    public static CustomModelTintSource of(int index) {
        return new CustomModelTintSource(index, -1);
    }

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        CustomModelData customModelData = itemStack.getOrDefault(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.EMPTY);
        Integer color = customModelData.getColor(index);
        if (color == null)
            return defaultColor;
        return color;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
