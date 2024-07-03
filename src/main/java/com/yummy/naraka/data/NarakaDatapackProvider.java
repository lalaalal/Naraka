package com.yummy.naraka.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.data.worldgen.NarakaBiomeModifiers;
import com.yummy.naraka.data.worldgen.NarakaStructureSets;
import com.yummy.naraka.data.worldgen.NarakaStructures;
import com.yummy.naraka.data.worldgen.features.NarakaFeatures;
import com.yummy.naraka.data.worldgen.placement.NarakaPlacements;
import com.yummy.naraka.world.damagesource.NarakaDamageTypes;
import com.yummy.naraka.world.item.enchantment.NarakaEnchantments;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class NarakaDatapackProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.DAMAGE_TYPE, NarakaDamageTypes::bootstrap)
            .add(Registries.ENCHANTMENT, NarakaEnchantments::bootstrap)
            .add(Registries.CONFIGURED_FEATURE, NarakaFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, NarakaPlacements::bootstrap)
            .add(Registries.STRUCTURE, NarakaStructures::bootstrap)
            .add(Registries.STRUCTURE_SET, NarakaStructureSets::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, NarakaBiomeModifiers::bootstrap);

    public NarakaDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of("minecraft", NarakaMod.MOD_ID));
    }
}
