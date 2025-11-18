package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.item.SoulType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class NarakaSword extends LightTailEntity {
    public static final EntityDataAccessor<SoulType> SOUL_TYPE = SynchedEntityData.defineId(NarakaSword.class, NarakaEntityDataSerializers.SOUL_TYPE);

    public NarakaSword(EntityType<? extends LightTailEntity> entityType, Level level) {
        super(entityType, level);
    }

    public NarakaSword(Level level, SoulType soulType) {
        this(NarakaEntityTypes.NARAKA_SWORD.get(), level);
        setSoulType(soulType);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SOUL_TYPE, SoulType.NONE);
    }

    public void setSoulType(SoulType soulType) {
        entityData.set(SOUL_TYPE, soulType);
        setTailColor(soulType.getColor());
    }
}
