package com.yummy.naraka.data.advancement;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.data.lang.AdvancementNarakaComponents;
import com.yummy.naraka.data.worldgen.NarakaStructures;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class NarakaAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {
    public static final String NAME = "naraka";

    private static ResourceLocation location(String path) {
        return NarakaMod.location(NAME, path);
    }

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> output, ExistingFileHelper existingFileHelper) {
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
                .save(output, location("root"), existingFileHelper);
        AdvancementHolder herobrineSanctuary = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        NarakaBlocks.PURIFIED_SOUL_BLOCK,
                        AdvancementNarakaComponents.HEROBRINE_SANCTUARY.title(),
                        AdvancementNarakaComponents.HEROBRINE_SANCTUARY.description(),
                        null,
                        AdvancementType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("herobrine_sanctuary", PlayerTrigger.TriggerInstance.located(
                        LocationPredicate.Builder.inStructure(herobrineSanctuaryStructure)
                ))
                .rewards(AdvancementRewards.Builder.experience(3))
                .save(output, location("herobrine_sanctuary"), existingFileHelper);
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
                                        .of(NarakaEntityTypes.HEROBRINE.get())
                        )
                )
                .rewards(AdvancementRewards.Builder.experience(6))
                .save(output, location("summon_herobrine"), existingFileHelper);
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
                                        .of(NarakaEntityTypes.HEROBRINE.get())
                        )
                )
                .rewards(AdvancementRewards.Builder.experience(66))
                .save(output, "kill_herobrine");
    }
}
