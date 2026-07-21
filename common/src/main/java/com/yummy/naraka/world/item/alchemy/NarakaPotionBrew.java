package com.yummy.naraka.world.item.alchemy;

import com.yummy.naraka.init.PotionBrewRecipeRegistry;
import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;

public class NarakaPotionBrew {
    public static void bootstrap(PotionBrewRecipeRegistry.Builder builder) {
        builder.addMix(Potions.AWKWARD, NarakaItems.GOD_BLOOD.getConcreteValue(), NarakaPotions.BLESS.value());
        builder.addMix(NarakaPotions.BLESS.value(), Items.NETHER_STAR, NarakaPotions.CHALLENGER.value());
    }
}
