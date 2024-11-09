package com.yummy.naraka.neoforge.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.NarakaBiomes;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class NarakaBiomeModifiers implements NarakaBiomes.Modifier {
    public static final NarakaBiomeModifiers INSTANCE = new NarakaBiomeModifiers();

    private final List<BiomeModifierRecord> modifiers = new ArrayList<>();

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        for (BiomeModifierRecord modifier : INSTANCE.modifiers)
            modifier.register(context);
    }

    private NarakaBiomeModifiers() {
    }

    @Override
    public void addFeatures(String name, TagKey<Biome> target, GenerationStep.Decoration generationStep, List<ResourceKey<PlacedFeature>> features) {
        modifiers.add(context -> {
            HolderGetter<Biome> biomeGetter = context.lookup(Registries.BIOME);
            HolderGetter<PlacedFeature> featureGetter = context.lookup(Registries.PLACED_FEATURE);

            HolderSet<Biome> targetBiomes = biomeGetter.getOrThrow(target);

            context.register(create(name),
                    new BiomeModifiers.AddFeaturesBiomeModifier(
                            targetBiomes,
                            HolderSet.direct(featureGetter::getOrThrow, features),
                            generationStep
                    )
            );
        });
    }

    private static ResourceKey<BiomeModifier> create(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, NarakaMod.location(name));
    }

    public interface BiomeModifierRecord {
        void register(BootstrapContext<BiomeModifier> context);
    }
}
