package com.yummy.naraka.world.item;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.tags.NarakaBlockTags;
import net.minecraft.world.item.DiggerItem;

public class NarakaPickaxeItem extends DiggerItem {
    public static float calculateAttackDamageModifier() {
        return NarakaConfig.COMMON.narakaPickaxeDamage.getValue() - (NarakaTiers.LONGINUS.getAttackDamageBonus() + 1);
    }

    public NarakaPickaxeItem(Properties properties) {
        super(NarakaTiers.LONGINUS, NarakaBlockTags.MINABLE_WITH_NARAKA_PICKAXE, properties);
    }
}
