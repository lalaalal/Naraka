package com.yummy.naraka.world.entity.ai.skill;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.world.entity.SkillUsingMob;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

/**
 * Skill can randomly link to next combo skill.
 *
 * @param <T> Type of mob
 */
public abstract class ComboSkill<T extends SkillUsingMob> extends AttackSkill<T> {
    protected final float linkChance;
    @Nullable
    protected final Skill<?> comboSkill;
    protected final int comboDuration;
    protected final int nonComboDuration;

    protected ComboSkill(Identifier location, T mob, int duration, int cooldown, float linkChance, int comboDuration, @Nullable Skill<?> nextSkill) {
        super(location, mob, duration, cooldown);
        this.linkChance = linkChance;
        this.comboSkill = nextSkill;
        this.comboDuration = comboDuration;
        this.nonComboDuration = duration;
    }

    @Override
    public boolean canUse(ServerLevel level) {
        return false;
    }

    private boolean canCombo() {
        boolean checkDisabled = NarakaConfig.COMMON.breakComboWhenSkillDisabled.getValue();
        boolean randomResult = mob.getRandom().nextFloat() < Math.clamp(linkChance + getBonusChance(), 0, 1);
        if (checkDisabled)
            return randomResult && (comboSkill == null || comboSkill.isEnabled());
        return comboSkill != null && randomResult;
    }

    @Override
    public void prepare() {
        super.prepare();
        if (canCombo()) {
            duration = comboDuration;
            linkedSkill = comboSkill;
        } else {
            duration = nonComboDuration;
            linkedSkill = null;
        }
    }

    protected float getBonusChance() {
        if (NarakaConfig.COMMON.alwaysCombo.getValue())
            return 1;
        return 0;
    }
}
