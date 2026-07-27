package com.yummy.naraka.fabric.data.tags;

import com.yummy.naraka.tags.ConventionalTags;
import com.yummy.naraka.tags.NarakaEntityTypeTags;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

public class NarakaEntityTypeTagsProvider extends FabricTagsProvider.EntityTypeTagsProvider {
    public NarakaEntityTypeTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    private static ResourceKey<EntityType<?>> vanillaEntityTypeId(EntityType<?> entityType) {
        return BuiltInRegistries.ENTITY_TYPE.getResourceKey(entityType)
                .orElseThrow();
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        builder(NarakaEntityTypeTags.DEATH_COUNTABLE)
                .addTag(ConventionalTags.Entities.BOSSES)
                .add(vanillaEntityTypeId(EntityType.PLAYER));
        builder(NarakaEntityTypeTags.HEROBRINE)
                .add(NarakaEntityTypes.HEROBRINE.key())
                .add(NarakaEntityTypes.SHADOW_HEROBRINE.key())
                .add(NarakaEntityTypes.ORIGIN_HEROBRINE.key());
        builder(NarakaEntityTypeTags.DEATH_COUNTING)
                .addTag(NarakaEntityTypeTags.HEROBRINE);

        builder(ConventionalTags.Entities.BOSSES)
                .add(NarakaEntityTypes.HEROBRINE.key())
                .add(NarakaEntityTypes.ORIGIN_HEROBRINE.key());
        builder(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
                .add(NarakaEntityTypes.HEROBRINE.key());
        builder(EntityTypeTags.FALL_DAMAGE_IMMUNE)
                .add(NarakaEntityTypes.HEROBRINE.key());

        builder(EntityTypeTags.REDIRECTABLE_PROJECTILE)
                .add(NarakaEntityTypes.CORRUPTED_STAR.key())
                .add(NarakaEntityTypes.NARAKA_FIREBALL.key());

        builder(NarakaEntityTypeTags.STIGMA_IMMUNE)
                .addTag(NarakaEntityTypeTags.HEROBRINE)
                .add(NarakaEntityTypes.NARAKA_PICKAXE.key());
        builder(NarakaEntityTypeTags.STUN_IMMUNE)
                .addTag(NarakaEntityTypeTags.HEROBRINE)
                .add(NarakaEntityTypes.NARAKA_PICKAXE.key());

        builder(NarakaEntityTypeTags.NARAKA_PORTAL_IGNORE)
                .add(vanillaEntityTypeId(EntityType.EXPERIENCE_ORB))
                .addTag(NarakaEntityTypeTags.HEROBRINE);
    }
}
