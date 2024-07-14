package com.yummy.naraka.data.tags;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.tags.NarakaStructureSetTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class NarakaStructureSetsTagProvider extends TagsProvider<StructureSet> {
    public NarakaStructureSetsTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper existingFileHelper) {
        super(output, Registries.STRUCTURE_SET, provider, NarakaMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(NarakaStructureSetTags.HEROBRINE_SANCTUARY_EXCLUSIVE)
                .add(BuiltinStructureSets.VILLAGES)
                .add(BuiltinStructureSets.STRONGHOLDS);
    }
}
