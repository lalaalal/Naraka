package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.entity.ai.skill.Skill;
import com.yummy.naraka.world.entity.ai.skill.SkillManager;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class SkillUsingMob extends PathfinderMob implements AfterimageEntity {
    public static final EntityDataAccessor<List<Afterimage>> AFTERIMAGES = SynchedEntityData.defineId(SkillUsingMob.class, NarakaEntityDataSerializers.AFTERIMAGES);

    protected final SkillManager skillManager = new SkillManager();

    protected SkillUsingMob(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        registerSkills();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(AFTERIMAGES, new ArrayList<>());
    }

    public boolean isUsingSkill() {
        return skillManager.getCurrentSkill() != null;
    }

    @Nullable
    public Skill getCurrentSkill() {
        return skillManager.getCurrentSkill();
    }

    protected abstract void registerSkills();

    public void registerSkill(Skill skill) {
        skillManager.addSkill(skill);
    }

    @Override
    protected void customServerAiStep() {
        skillManager.tick();
    }

    @Override
    public void tick() {
        super.tick();
        List<Afterimage> afterimages = new ArrayList<>(getAfterimages());
        afterimages.removeIf(Afterimage::tick);
        this.entityData.set(AFTERIMAGES, afterimages);
    }

    @Override
    public void createAfterimage() {
        List<Afterimage> afterimages = new ArrayList<>(getAfterimages());
        if (!level().isClientSide) {
            afterimages.add(new Afterimage(this.position(), 10));
            this.entityData.set(AFTERIMAGES, afterimages, true);
        }
    }

    @Override
    public List<Afterimage> getAfterimages() {
        return this.entityData.get(AFTERIMAGES);
    }
}
