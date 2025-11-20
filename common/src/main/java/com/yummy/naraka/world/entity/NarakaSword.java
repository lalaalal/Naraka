package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.item.SoulType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class NarakaSword extends LightTailEntity {
    public static final EntityDataAccessor<SoulType> SOUL_TYPE = SynchedEntityData.defineId(NarakaSword.class, NarakaEntityDataSerializers.SOUL_TYPE);

    public final List<BeamEffect> beamEffects = new ArrayList<>();

    public NarakaSword(EntityType<? extends LightTailEntity> entityType, Level level) {
        super(entityType, level);
    }

    public NarakaSword(Level level, SoulType soulType) {
        this(NarakaEntityTypes.NARAKA_SWORD.get(), level);
        setSoulType(soulType);
    }

    @Override
    public void tick() {
        if (tickCount % 30 == 0) {
            beamEffects.clear();
            beamEffects.add(BeamEffect.spin(this, BeamEffect.Speed.FAST, 0.25, 0, 0, 0.6, 0xff0000));
            beamEffects.add(BeamEffect.spin(this, BeamEffect.Speed.FAST, 0.25, 0, Math.PI, 0.6, 0xff0000));
            beamEffects.add(BeamEffect.spin(this, BeamEffect.Speed.NORMAL, 0.4, 0, 0, 0.4, 0xff0000));
            beamEffects.add(BeamEffect.spin(this, BeamEffect.Speed.NORMAL, 0.4, Math.PI, 0, 0.4, 0xff0000));
            beamEffects.add(BeamEffect.spin(this, BeamEffect.Speed.NORMAL, 0.4, 0, Math.PI, 0.4, 0xff0000));
            beamEffects.add(BeamEffect.spin(this, BeamEffect.Speed.SLOW, 0.7, 0, 0, 0.4, 0xff0000));
        }
        super.tick();
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
