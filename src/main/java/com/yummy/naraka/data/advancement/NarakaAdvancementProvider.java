package com.yummy.naraka.data.advancement;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NarakaAdvancementProvider extends AdvancementProvider {
    private static final List<AdvancementGenerator> GENERATORS = List.of(
            new NarakaAdvancementGenerator()
    );

    public NarakaAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, GENERATORS);
    }
}
