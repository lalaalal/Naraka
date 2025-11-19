package com.yummy.naraka.world.item;

import com.yummy.naraka.tags.NarakaBlockTags;
import net.minecraft.world.item.DiggerItem;

public class NarakaPickaxeItem extends DiggerItem {
    public NarakaPickaxeItem(Properties properties) {
        super(NarakaTiers.LONGINUS, NarakaBlockTags.MINABLE_WITH_NARAKA_PICKAXE, properties);
    }
}
