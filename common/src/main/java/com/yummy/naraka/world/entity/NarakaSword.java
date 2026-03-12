package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.item.SoulType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.List;

public class NarakaSword extends MotionEntity {
    public static final EntityDataAccessor<SoulType> SOUL_TYPE = SynchedEntityData.defineId(NarakaSword.class, NarakaEntityDataSerializers.SOUL_TYPE);
    public static final EntityDataAccessor<Float> ALPHA = SynchedEntityData.defineId(NarakaSword.class, EntityDataSerializers.FLOAT);

    public static final Vector3f DIRECTION = new Vector3f(0, 0.1f, 0);
    public static final float LENGTH = 2.75f;

    private final List<SwordEffectData> swordEffectData = new ArrayList<>();
    private float prevAlpha = 0f;

    public NarakaSword(EntityType<? extends NarakaSword> entityType, Level level) {
        super(entityType, level);
    }

    public NarakaSword(Level level, Vec3 position, SoulType soulType) {
        this(NarakaEntityTypes.NARAKA_SWORD.get(), level);
        setSoulType(soulType);
        setPos(position);
    }

    public List<SwordEffectData> getSwordEffectData(float partialTicks) {
        int startIndex = (int) (getSwordEffectUpdateCount() * (1 - partialTicks));
        if (swordEffectData.size() > startIndex)
            return swordEffectData.subList(startIndex, swordEffectData.size() - 1);
        return swordEffectData;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SOUL_TYPE, SoulType.NONE)
                .define(ALPHA, 0f);
    }

    public int getSwordEffectUpdateCount() {
        return 8;
    }

    @Override
    public void tick() {
        super.tick();
        prevAlpha = getAlpha();

        if (tickCount == 5) {
            for (int i = 0; i < 64; i++)
                swordEffectData.add(new SwordEffectData(position().toVector3f(), DIRECTION, getRotation(), 0, 0));
        }

        if (level().isClientSide() && tickCount > 10) {
            final int updateCount = getSwordEffectUpdateCount();
            SwordEffectData prevData = swordEffectData.getFirst();
            for (float count = 1; count <= updateCount; count++) {
                float delta = count / (float) updateCount;
                Vector3fc base = prevData.base().lerp(position().toVector3f(), delta, new Vector3f());
                Quaternionfc rotation = prevData.rotation().slerp(getRotation(), delta, new Quaternionf());
                swordEffectData.removeLast();
                swordEffectData.addFirst(new SwordEffectData(base, DIRECTION, rotation, LENGTH, getScale()));
            }
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return false;
    }

    public void setSoulType(SoulType soulType) {
        entityData.set(SOUL_TYPE, soulType);
    }

    public SoulType getSoulType() {
        return entityData.get(SOUL_TYPE);
    }

    public int getColor() {
        return getSoulType().getColor();
    }

    public float getAlpha() {
        return entityData.get(ALPHA);
    }

    public float getAlpha(float partialTicks) {
        return Mth.lerp(partialTicks, prevAlpha, getAlpha());
    }

    public void setAlpha(float alpha) {
        entityData.set(ALPHA, alpha);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        SoulType soulType = input.read("SoulType", SoulType.CODEC).orElse(SoulType.NONE);
        setSoulType(soulType);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store("SoulType", SoulType.CODEC, getSoulType());
    }
}
