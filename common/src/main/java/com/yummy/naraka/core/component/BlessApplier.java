package com.yummy.naraka.core.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record BlessApplier(boolean blessed) implements DataComponentApplier {
    public static final Codec<BlessApplier> CODEC = Codec.BOOL.xmap(BlessApplier::new, BlessApplier::blessed);

    public static final StreamCodec<ByteBuf, BlessApplier> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            BlessApplier::blessed,
            BlessApplier::new
    );

    public static BlessApplier bless() {
        return new BlessApplier(true);
    }

    @Override
    public void apply(ItemStack itemStack) {
        itemStack.set(NarakaDataComponentTypes.BLESSED.get(), blessed);
    }
}
