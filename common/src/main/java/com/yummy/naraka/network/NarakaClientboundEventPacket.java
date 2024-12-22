package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.item.NarakaItems;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record NarakaClientboundEventPacket(Event event) implements CustomPacketPayload {
    public static final Type<NarakaClientboundEventPacket> TYPE = new Type<>(NarakaMod.location("clientbound_event_packet"));

    public static final StreamCodec<ByteBuf, NarakaClientboundEventPacket> CODEC = StreamCodec.composite(
            Event.STREAM_CODEC,
            NarakaClientboundEventPacket::event,
            NarakaClientboundEventPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(NarakaClientboundEventPacket packet, NetworkManager.PacketContext context) {
        Minecraft.getInstance().execute(packet.event::handle);
    }

    public static void handleDeathCountUsed() {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.gameRenderer.displayItemActivation(new ItemStack(NarakaItems.STIGMA_ROD));

        Player player = minecraft.player;
        if (player != null)
            player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.TOTEM_USE, player.getSoundSource(), 1.0F, 1.0F, false);
    }

    public enum Event implements StringRepresentable {
        DEATH_COUNT_USED(NarakaClientboundEventPacket::handleDeathCountUsed);

        public static final Codec<Event> CODEC = StringRepresentable.fromEnum(Event::values);
        public static final StreamCodec<ByteBuf, Event> STREAM_CODEC = ByteBufCodecs.idMapper(Event::byId, Event::getId);

        public final int id;
        public final Runnable handler;

        Event(Runnable handler) {
            this.id = ordinal();
            this.handler = handler;
        }

        public static Event byId(int id) {
            for (Event event : values()) {
                if (event.id == id)
                    return event;
            }
            throw new IllegalArgumentException("Unknown event id: " + id);
        }

        public int getId() {
            return id;
        }

        public void handle() {
            this.handler.run();
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
