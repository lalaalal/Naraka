package com.yummy.naraka.fabric.data.tags;

import com.yummy.naraka.tags.ConventionalTags;
import com.yummy.naraka.tags.NarakaEntityTypeTags;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class NarakaEntityTypeTagsProvider extends FabricTagProvider.EntityTypeTagProvider {
    public NarakaEntityTypeTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(NarakaEntityTypeTags.DEATH_COUNTABLE)
                .addTag(ConventionalTags.Entities.BOSSES)
                .add(EntityType.PLAYER);
        getOrCreateTagBuilder(NarakaEntityTypeTags.HEROBRINE)
                .add(NarakaEntityTypes.HEROBRINE.get())
                .add(NarakaEntityTypes.SHADOW_HEROBRINE.get());
        getOrCreateTagBuilder(NarakaEntityTypeTags.DEATH_COUNTING)
                .addTag(NarakaEntityTypeTags.HEROBRINE);

        getOrCreateTagBuilder(ConventionalTags.Entities.BOSSES)
                .add(NarakaEntityTypes.HEROBRINE.get());
        getOrCreateTagBuilder(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
                .add(NarakaEntityTypes.HEROBRINE.get());
        getOrCreateTagBuilder(EntityTypeTags.FALL_DAMAGE_IMMUNE)
                .add(NarakaEntityTypes.HEROBRINE.get());

        getOrCreateTagBuilder(EntityTypeTags.REDIRECTABLE_PROJECTILE)
                .add(NarakaEntityTypes.NARAKA_FIREBALL.get());

        getOrCreateTagBuilder(NarakaEntityTypeTags.STIGMA_IMMUNE)
                .addTags(NarakaEntityTypeTags.HEROBRINE)
                .add(NarakaEntityTypes.NARAKA_PICKAXE.get());
        getOrCreateTagBuilder(NarakaEntityTypeTags.STUN_IMMUNE)
                .addTags(NarakaEntityTypeTags.HEROBRINE)
                .add(NarakaEntityTypes.NARAKA_PICKAXE.get());
    }
}
