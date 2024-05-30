package com.yummy.naraka.networking.payload;

import com.yummy.naraka.NarakaContext;
import com.yummy.naraka.NarakaMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ChangeDeathCountVisibilityPayload(boolean visible) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ChangeDeathCountVisibilityPayload> TYPE = new CustomPacketPayload.Type<>(NarakaMod.location("change_death_count_visibility"));
    public static final StreamCodec<ByteBuf, ChangeDeathCountVisibilityPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            ChangeDeathCountVisibilityPayload::visible,
            ChangeDeathCountVisibilityPayload::new
    );

    public static void handle(ChangeDeathCountVisibilityPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> NarakaMod.context()
                .set(NarakaContext.KEY_CLIENT_DEATH_COUNT_VISIBILITY, payload.visible)
        );
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
