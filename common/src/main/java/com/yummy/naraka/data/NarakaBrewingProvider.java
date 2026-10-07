package com.yummy.naraka.data;

import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.alchemy.NarakaPotions;
import net.minecraft.data.recipes.BrewingProvider;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;

public class NarakaBrewingProvider extends BrewingProvider {
    protected NarakaBrewingProvider(RecipeOutput output) {
        super(output);
    }

    @Override
    protected void addContainers() {
        this.addContainer(Items.LINGERING_POTION);
        this.addContainer(Items.POTION);
        this.addContainer(Items.SPLASH_POTION);
    }

    @Override
    protected void addContainerTransformations() {
        this.addContainerTransformation(Items.POTION, Items.GUNPOWDER, Items.SPLASH_POTION);
        this.addContainerTransformation(Items.SPLASH_POTION, Items.DRAGON_BREATH, Items.LINGERING_POTION);
    }

    @Override
    protected void buildMixes() {
        buildMix(Potions.AWKWARD, NarakaItems.GOD_BLOOD.get(), NarakaPotions.CHALLENGER);
        buildMix(NarakaPotions.CHALLENGER, Items.NETHER_STAR, NarakaPotions.BLESS);
    }
}
