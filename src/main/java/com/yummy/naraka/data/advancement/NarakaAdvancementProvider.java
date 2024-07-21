package com.yummy.naraka.data.advancement;

import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.data.lang.AdvancementNarakaComponents;
import com.yummy.naraka.data.worldgen.NarakaStructures;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class NarakaAdvancementProvider extends FabricAdvancementProvider {
    public NarakaAdvancementProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider registries, Consumer<AdvancementHolder> generator) {
        HolderLookup.RegistryLookup<Structure> structures = registries.lookupOrThrow(Registries.STRUCTURE);

        Holder<Structure> herobrineSanctuaryStructure = structures.getOrThrow(NarakaStructures.HEROBRINE_SANCTUARY);

        AdvancementHolder root = Advancement.Builder.advancement()
                .display(
                        NarakaItems.NECTARIUM,
                        AdvancementNarakaComponents.ROOT.title(),
                        AdvancementNarakaComponents.ROOT.description(),
                        NarakaTextures.NARAKA_ADVANCEMENT_ROOT_BACKGROUND,
                        AdvancementType.TASK,
                        false,
                        false,
                        false
                )
                .requirements(AdvancementRequirements.Strategy.OR)
                .addCriterion("killed_something", KilledTrigger.TriggerInstance.playerKilledEntity())
                .addCriterion("killed_by_something", KilledTrigger.TriggerInstance.entityKilledPlayer())
                .save(generator, "root");
        AdvancementHolder herobrineSanctuary = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        NarakaBlocks.PURIFIED_SOUL_BLOCK,
                        AdvancementNarakaComponents.HEROBRINE_SANCTUARY.title(),
                        AdvancementNarakaComponents.HEROBRINE_SANCTUARY.description(),
                        null,
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion("herobrine_sanctuary", PlayerTrigger.TriggerInstance.located(
                        LocationPredicate.Builder.inStructure(herobrineSanctuaryStructure)
                ))
                .rewards(AdvancementRewards.Builder.experience(3))
                .save(generator, ("herobrine_sanctuary"));
        AdvancementHolder summonHerobrine = Advancement.Builder.advancement()
                .parent(herobrineSanctuary)
                .display(
                        NarakaBlocks.HEROBRINE_TOTEM,
                        AdvancementNarakaComponents.SUMMON_HEROBRINE.title(),
                        AdvancementNarakaComponents.SUMMON_HEROBRINE.description(),
                        null,
                        AdvancementType.GOAL,
                        true,
                        true,
                        false

                )
                .addCriterion("summon_herobrine", SummonedEntityTrigger.TriggerInstance.summonedEntity(
                                EntityPredicate.Builder.entity()
                                        .of(NarakaEntityTypes.HEROBRINE)
                        )
                )
                .rewards(AdvancementRewards.Builder.experience(6))
                .save(generator, ("summon_herobrine"));
        AdvancementHolder killHerobrine = Advancement.Builder.advancement()
                .parent(summonHerobrine)
                .display(
                        NarakaItems.PURIFIED_SOUL_SHARD,
                        AdvancementNarakaComponents.KILL_HEROBRINE.title(),
                        AdvancementNarakaComponents.KILL_HEROBRINE.description(),
                        null,
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion("kill_herobrine", KilledTrigger.TriggerInstance.playerKilledEntity(
                                EntityPredicate.Builder.entity()
                                        .of(NarakaEntityTypes.HEROBRINE)
                        )
                )
                .rewards(AdvancementRewards.Builder.experience(66))
                .save(generator, "kill_herobrine");
    }
}
