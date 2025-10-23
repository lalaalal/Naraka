package com.yummy.naraka.world.item.alchemy;

import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;

public class NarakaPotionBrew {
    public static void bootstrap(PotionBrewing.Builder builder) {
        builder.addMix(Potions.AWKWARD, NarakaItems.GOD_BLOOD.get(), NarakaPotions.BLESS);
        builder.addMix(NarakaPotions.BLESS, Items.NETHER_STAR, NarakaPotions.CHALLENGER);
    }
}
