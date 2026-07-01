package com.yummy.naraka.forge.data;

import com.yummy.naraka.tags.ConventionalTags;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeEntityTypeTagsProvider;

import java.util.concurrent.CompletableFuture;

public class NarakaForgeEntityTypeTagsProvider extends ForgeEntityTypeTagsProvider {
    public NarakaForgeEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        tag(ConventionalTags.Entities.BOSSES)
                .addTag(Tags.EntityTypes.BOSSES)
                .add(NarakaEntityTypes.HEROBRINE.value())
                .add(NarakaEntityTypes.ORIGIN_HEROBRINE.value());
    }
}
