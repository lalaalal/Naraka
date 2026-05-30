package com.yummy.naraka.world.entity.ai.skill.absolute_herobrine;

import com.yummy.naraka.world.entity.AbsoluteHerobrine;
import com.yummy.naraka.world.entity.ai.skill.Skill;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;

public class InjectColorSkill extends Skill<AbsoluteHerobrine> {
    public static final Identifier IDENTIFIER = skillIdentifier("absolute_herobrine.inject_color");

    public InjectColorSkill(AbsoluteHerobrine mob) {
        super(IDENTIFIER, mob, 200, 0);
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return false;
    }

    @Override
    protected void skillTick(ServerLevel level) {

    }

    private void createColorCrystal() {

    }

    private void injectColor() {

    }
}
