package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.entity.Afterimage;
import com.yummy.naraka.world.entity.AfterimageEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public record SyncAfterimagePacket(int entityId, Afterimage afterimage)
        implements CustomPacketPayload<SyncAfterimagePacket> {
    public static final Codec<SyncAfterimagePacket> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("entity_id").forGetter(SyncAfterimagePacket::entityId),
                    Afterimage.CODEC.fieldOf("afterimage").forGetter(SyncAfterimagePacket::afterimage)
            ).apply(instance, SyncAfterimagePacket::new)
    );

    public static final Type<SyncAfterimagePacket> TYPE = new CodecType<>(NarakaMod.location("sync_afterimage_payload"),
            SyncAfterimagePacket.class,
            CODEC
    );

    public SyncAfterimagePacket(Entity entity, Afterimage afterimage) {
        this(entity.getId(), afterimage);
    }

    @Override
    public Type<SyncAfterimagePacket> type() {
        return TYPE;
    }

    public void handle(NetworkManager.Context context) {
        Level level = context.level();
        Entity entity = level.getEntity(entityId);

        if (entity instanceof AfterimageEntity afterimageEntity)
            afterimageEntity.addAfterimage(afterimage);
    }
}
