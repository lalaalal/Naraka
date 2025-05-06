package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

/**
 * Skill can randomly link to next combo skill.
 *
 * @param <T> Type of mob
 */
public abstract class ComboSkill<T extends SkillUsingMob> extends AttackSkill<T> {
    protected final float linkChance;
    @Nullable
    protected final ComboSkill<T> comboSkill;
    protected final int comboDuration;
    protected final int nonComboDuration;

    protected ComboSkill(ResourceLocation location, int duration, int cooldown, float linkChance, @Nullable ComboSkill<T> comboSkill, int comboDuration, T mob) {
        super(location, duration, cooldown, mob);
        this.linkChance = linkChance;
        this.comboSkill = comboSkill;
        this.comboDuration = comboDuration;
        this.nonComboDuration = duration;
    }

    @Override
    public boolean canUse() {
        return false;
    }

    @Override
    public void prepare() {
        super.prepare();
        if (mob.getRandom().nextFloat() < Math.clamp(linkChance + getBonusChance(), 0, 1)) {
            duration = comboDuration;
            linkedSkill = comboSkill;
        } else {
            duration = nonComboDuration;
            linkedSkill = null;
        }
    }

    protected float getBonusChance() {
        return 0;
    }
}
