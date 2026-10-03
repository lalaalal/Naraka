package com.yummy.naraka.fabric.data;

import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.alchemy.NarakaPotions;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBrewingProvider;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;

public class NarakaBrewingProvider extends FabricBrewingProvider {
    protected NarakaBrewingProvider(RecipeOutput output) {
        super(output);
    }

    @Override
    protected void buildMixes() {
        buildMix(Potions.AWKWARD, NarakaItems.GOD_BLOOD.get(), NarakaPotions.CHALLENGER);
        buildMix(NarakaPotions.CHALLENGER, Items.NETHER_STAR, NarakaPotions.BLESS);
    }
}
