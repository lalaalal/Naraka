package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.Herobrine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

public class RolePlayShadowSkill extends Skill<Herobrine> {
    public static final ResourceLocation LOCATION = createLocation("role_play_shadow");

    public RolePlayShadowSkill(Herobrine mob) {
        super(LOCATION, 1, 200, mob);
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    protected void skillTick(ServerLevel level) {

    }

    @Override
    protected void onLastTick(ServerLevel level) {
        mob.getShadowController().updateRolePlaying(level);
    }
}
