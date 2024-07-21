package com.yummy.naraka.data.tags;

import com.yummy.naraka.tags.NarakaStructureSetTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.BuiltinStructureSets;
import net.minecraft.world.level.levelgen.structure.StructureSet;

import java.util.concurrent.CompletableFuture;

public class NarakaStructureSetsTagProvider extends FabricTagProvider<StructureSet> {
    public NarakaStructureSetsTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.STRUCTURE_SET, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(NarakaStructureSetTags.HEROBRINE_SANCTUARY_EXCLUSIVE)
                .add(BuiltinStructureSets.VILLAGES)
                .add(BuiltinStructureSets.STRONGHOLDS);
    }
}
