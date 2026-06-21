package com.yummy.naraka.client.color;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.util.ComponentStyles;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class RainbowTintSource implements ItemTintSource {
    public static final RainbowTintSource INSTANCE = new RainbowTintSource();
    public static final MapCodec<RainbowTintSource> MAP_CODEC = MapCodec.unit(() -> INSTANCE);

    private RainbowTintSource() {

    }

    @Override
    public int calculate(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        return ComponentStyles.RAINBOW_COLOR.getCurrentColor().withAlpha(0xff).pack();
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
