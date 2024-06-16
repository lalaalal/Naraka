package com.yummy.naraka.data.tags;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.entity.NarakaEntities;
import com.yummy.naraka.tags.NarakaEntityTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class NarakaEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public NarakaEntityTypeTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, provider, NarakaMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(NarakaEntityTypeTags.APPLY_DEATH_COUNT)
                .addTag(Tags.EntityTypes.BOSSES)
                .add(EntityType.PLAYER);
        tag(NarakaEntityTypeTags.DEATH_COUNTING_ENTITY)
                .add(NarakaEntities.HEROBRINE.get());
    }
}
