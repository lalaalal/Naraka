package com.yummy.naraka.world.item.alchemy;

import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;

public class NarakaPotionBrew {
    public static void bootstrap(PotionBrewing.Builder builder) {
        builder.addMix(Potions.THICK, NarakaItems.GOD_BLOOD.get(), NarakaPotions.GOD_BLESS);
    }
}
