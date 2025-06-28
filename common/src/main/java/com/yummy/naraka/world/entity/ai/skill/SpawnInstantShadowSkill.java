package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.ShadowHerobrine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class SpawnInstantShadowSkill extends ComboSkill<Herobrine> {
    @Nullable
    protected ShadowHerobrine shadowHerobrine;

    protected SpawnInstantShadowSkill(ResourceLocation location, Herobrine mob, int duration, int cooldown, float linkChance, @Nullable Skill<?> nextSkill, int comboDuration) {
        super(location, mob, duration, cooldown, linkChance, comboDuration, nextSkill);
    }

    protected void spawnShadowHerobrine(ServerLevel level) {
        shadowHerobrine = ShadowHerobrine.createInstantFinalShadow(level, mob);
    }

    protected void spawnShadowHerobrine(ServerLevel level, Vec3 position) {
        shadowHerobrine = ShadowHerobrine.createInstantFinalShadow(level, mob, position);
    }

    protected void shadowUseSkill(ResourceLocation skillLocation) {
        if (shadowHerobrine != null && shadowHerobrine.isAlive()) {
            shadowHerobrine.useSkill(skillLocation);
            shadowHerobrine.setDisplayPickaxe(true);
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        if (shadowHerobrine != null && shadowHerobrine.isAlive()) {
            shadowHerobrine.discard();
        }
    }
}
