package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.entity.data.Stigma;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class AbsoluteHerobrine extends AbstractHerobrine {
    public AbsoluteHerobrine(EntityType<? extends AbsoluteHerobrine> entityType, Level level) {
        super(entityType, level, false);
    }

    @Override
    protected Fireball createFireball(ServerLevel level) {
        return new NarakaFireball(this, Vec3.ZERO, level);
    }

    @Override
    public Optional<ShadowController> getShadowController() {
        return Optional.empty();
    }

    @Override
    public void addAfterimage(Afterimage afterimage) {

    }

    @Override
    public List<Afterimage> getAfterimages() {
        return List.of();
    }

    @Override
    public void stigmatizeEntity(ServerLevel level, LivingEntity target) {

    }

    @Override
    public void collectStigma(ServerLevel level, LivingEntity target, Stigma original) {

    }
}
