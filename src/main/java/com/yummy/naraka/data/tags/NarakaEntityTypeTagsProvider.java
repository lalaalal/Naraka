package com.yummy.naraka.data.tags;

import com.yummy.naraka.tags.NarakaEntityTypeTags;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class NarakaEntityTypeTagsProvider extends FabricTagProvider<EntityType<?>> {
    public NarakaEntityTypeTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ENTITY_TYPE, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        getOrCreateTagBuilder(NarakaEntityTypeTags.APPLY_DEATH_COUNT)
                .addTag(ConventionalEntityTypeTags.BOSSES)
                .add(EntityType.PLAYER);
        getOrCreateTagBuilder(NarakaEntityTypeTags.DEATH_COUNTING_ENTITY)
                .add(NarakaEntityTypes.HEROBRINE);

        getOrCreateTagBuilder(ConventionalEntityTypeTags.BOSSES)
                .add(NarakaEntityTypes.HEROBRINE);
        getOrCreateTagBuilder(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
                .add(NarakaEntityTypes.HEROBRINE);
        getOrCreateTagBuilder(EntityTypeTags.FALL_DAMAGE_IMMUNE)
                .add(NarakaEntityTypes.HEROBRINE);
    }
}
