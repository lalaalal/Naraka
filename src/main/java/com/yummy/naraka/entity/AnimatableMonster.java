package com.yummy.naraka.entity;

import com.yummy.naraka.client.animation.Animation;
import com.yummy.naraka.client.animation.AnimationInstance;
import com.yummy.naraka.client.animation.NarakaAnimations;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public abstract class AnimatableMonster extends Monster implements Animatable {
    protected static final EntityDataAccessor<String> ID_CURRENT_ANIMATION = SynchedEntityData.defineId(AnimatableMonster.class, EntityDataSerializers.STRING);

    private final Animation defaultAnimation;
    protected AnimationInstance animationInstance;
    private final boolean repeatDefault = true;
    private int syncId = 0;

    protected AnimatableMonster(EntityType<? extends Monster> entityType, Level level, String defaultAnimationName) {
        super(entityType, level);
        this.defaultAnimation = NarakaAnimations.get(defaultAnimationName);
        this.animationInstance = defaultAnimation.instance(repeatDefault);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_CURRENT_ANIMATION, "empty");
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (level().isClientSide && ID_CURRENT_ANIMATION.equals(key))
            animationInstance = NarakaAnimations.instance(getAnimationName());
    }

    @Override
    public boolean isAnimationFinished() {
        return animationInstance.isFinished();
    }

    @Override
    public boolean isAnimationRepeating() {
        return animationInstance.isRepeat();
    }

    private String syncId() {
        return "#" + syncId++;
    }

    public void playDefaultAnimation() {
        setAnimation(defaultAnimation.name(), repeatDefault);
    }

    @Override
    public void setAnimation(String animationName) {
        entityData.set(ID_CURRENT_ANIMATION, animationName + syncId());
    }

    public void setAnimation(String animationName, boolean repeat) {
        if (repeat)
            animationName += ".repeat";
        this.setAnimation(animationName);
    }

    @Override
    public AnimationInstance getAnimationInstance() {
        return animationInstance;
    }

    public String getAnimationName() {
        return entityData.get(ID_CURRENT_ANIMATION).split("#")[0];
    }
}
