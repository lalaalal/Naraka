package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ShadowHerobrine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Consumer;

public interface InstantShadowSpawner {
    InstantShadowSpawner EMPTY = Optional::empty;

    static InstantShadowSpawner simple(Mob spawner) {
        final ShadowHerobrine shadowHerobrine = ShadowHerobrine.createInstantFinalShadow(spawner);
        return () -> Optional.of(shadowHerobrine);
    }

    Optional<ShadowHerobrine> get();

    default InstantShadowSpawner spawnAndUseSkill(Level level, Herobrine herobrine, ResourceLocation skill) {
        return this.spawn(level, herobrine).useSkill(skill);
    }

    default InstantShadowSpawner useSkill(ResourceLocation skill) {
        get().filter(ShadowHerobrine::isAlive)
                .ifPresent(shadowHerobrine -> shadowHerobrine.useSkill(skill));
        return this;
    }

    default InstantShadowSpawner control(Consumer<ShadowHerobrine> consumer) {
        get().ifPresent(consumer);
        return this;
    }

    default InstantShadowSpawner spawn(final Level level) {
        get().ifPresent(shadowHerobrine -> {
            shadowHerobrine.getShadowController().ifPresent(
                    controller -> controller.addShadowHerobrine(shadowHerobrine)
            );
            level.addFreshEntity(shadowHerobrine);
        });
        return this;
    }

    default InstantShadowSpawner spawn(final Level level, Mob mob) {
        return spawn(level, mob.position(), mob.getYRot());
    }

    default InstantShadowSpawner spawn(final Level level, Vec3 position, float yRot) {
        get().ifPresent(shadowHerobrine -> {
            shadowHerobrine.setPos(position);
            shadowHerobrine.setYRot(yRot);
            shadowHerobrine.setYHeadRot(yRot);
            shadowHerobrine.setOldPosAndRot();
            shadowHerobrine.getShadowController().ifPresent(
                    controller -> controller.addShadowHerobrine(shadowHerobrine)
            );
            level.addFreshEntity(shadowHerobrine);
        });
        return this;
    }
}
