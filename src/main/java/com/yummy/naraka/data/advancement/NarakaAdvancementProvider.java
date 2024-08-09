package com.yummy.naraka.data.advancement;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.data.lang.AdvancementComponent;
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
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class NarakaAdvancementProvider extends FabricAdvancementProvider {
    public static Advancement.Builder advancement(AdvancementHolder parent, ItemLike icon, AdvancementComponent component, AdvancementType type) {
        return Advancement.Builder.advancement()
                .parent(parent)
                .display(icon, component.title(), component.description(), null, type, true, true, false);
    }

    public static Advancement.Builder task(AdvancementHolder parent, ItemLike icon, AdvancementComponent component) {
        return advancement(parent, icon, component, AdvancementType.TASK);
    }

    public static Advancement.Builder goal(AdvancementHolder parent, ItemLike icon, AdvancementComponent component) {
        return advancement(parent, icon, component, AdvancementType.GOAL);
    }

    public static Advancement.Builder challenge(AdvancementHolder parent, ItemLike icon, AdvancementComponent component) {
        return advancement(parent, icon, component, AdvancementType.CHALLENGE);
    }

    public NarakaAdvancementProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider registries, Consumer<AdvancementHolder> generator) {
        HolderLookup.RegistryLookup<Structure> structures = registries.lookupOrThrow(Registries.STRUCTURE);

        Holder<Structure> herobrineSanctuaryStructure = structures.getOrThrow(NarakaStructures.HEROBRINE_SANCTUARY);

        AdvancementHolder root = Advancement.Builder.advancement()
                .display(NarakaItems.NECTARIUM,
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
        AdvancementHolder sanctuaryCompass = task(root, NarakaItems.SANCTUARY_COMPASS, AdvancementNarakaComponents.SANCTUARY_COMPASS)
                .rewards(AdvancementRewards.Builder.experience(6))
                .addCriterion("has_sanctuary_compass", InventoryChangeTrigger.TriggerInstance.hasItems(
                        NarakaItems.SANCTUARY_COMPASS
                ))
                .save(generator, "sanctuary_compass");
        AdvancementHolder herobrineSanctuary = task(sanctuaryCompass, NarakaBlocks.PURIFIED_SOUL_BLOCK, AdvancementNarakaComponents.HEROBRINE_SANCTUARY)
                .addCriterion("herobrine_sanctuary", PlayerTrigger.TriggerInstance.located(
                                LocationPredicate.Builder.inStructure(herobrineSanctuaryStructure)
                        )
                )
                .rewards(AdvancementRewards.Builder.experience(3))
                .save(generator, "herobrine_sanctuary");
        AdvancementHolder summonHerobrine = goal(herobrineSanctuary, NarakaBlocks.HEROBRINE_TOTEM, AdvancementNarakaComponents.SUMMON_HEROBRINE)
                .addCriterion("summon_herobrine", SummonedEntityTrigger.TriggerInstance.summonedEntity(
                                EntityPredicate.Builder.entity()
                                        .of(NarakaEntityTypes.HEROBRINE)
                        )
                )
                .rewards(AdvancementRewards.Builder.experience(6))
                .save(generator, ("summon_herobrine"));
        AdvancementHolder killHerobrine = challenge(summonHerobrine, NarakaItems.PURIFIED_SOUL_SHARD, AdvancementNarakaComponents.KILL_HEROBRINE)
                .addCriterion("kill_herobrine", KilledTrigger.TriggerInstance.playerKilledEntity(
                                EntityPredicate.Builder.entity()
                                        .of(NarakaEntityTypes.HEROBRINE)
                        )
                )
                .rewards(AdvancementRewards.Builder.experience(66))
                .save(generator, "kill_herobrine");
        AdvancementHolder takeEbonyRoot = task(killHerobrine, NarakaBlocks.EBONY_ROOTS, AdvancementNarakaComponents.TAKE_EBONY_ROOT)
                .rewards(AdvancementRewards.Builder.experience(3))
                .addCriterion("take_ebony_root", InventoryChangeTrigger.TriggerInstance.hasItems(
                        NarakaBlocks.EBONY_ROOTS
                ))
                .save(generator, "take_ebony_root");
        AdvancementHolder godBlood = challenge(killHerobrine, NarakaItems.GOD_BLOOD, AdvancementNarakaComponents.GOD_BLOOD)
                .rewards(AdvancementRewards.Builder.experience(66))
                .addCriterion("has_god_blood", InventoryChangeTrigger.TriggerInstance.hasItems(
                        NarakaItems.GOD_BLOOD
                ))
                .save(generator, "god_blood");
        AdvancementHolder ebonyMetal = goal(takeEbonyRoot, NarakaItems.EBONY_METAL_INGOT, AdvancementNarakaComponents.EBONY_METAL)
                .rewards(AdvancementRewards.Builder.experience(6))
                .addCriterion("has_ebony_metal_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(
                        NarakaItems.EBONY_METAL_INGOT
                ))
                .save(generator, "ebony_metal");
        AdvancementHolder soulInfusing = task(ebonyMetal, NarakaBlocks.SOUL_CRAFTING_BLOCK, AdvancementNarakaComponents.SOUL_INFUSING)
                .rewards(AdvancementRewards.Builder.experience(6))
                .addCriterion("craft_soul_crafting_block", RecipeCraftedTrigger.TriggerInstance.craftedItem(
                        NarakaMod.location("soul_crafting_block")
                ))
                .save(generator, "soul_infusing");
        AdvancementHolder purifiedSoulMetal = task(soulInfusing, NarakaItems.PURIFIED_SOUL_METAL, AdvancementNarakaComponents.PURIFIED_SOUL_METAL)
                .rewards(AdvancementRewards.Builder.experience(6))
                .addCriterion("craft_purified_soul_metal", RecipeCraftedTrigger.TriggerInstance.craftedItem(
                        NarakaMod.location("purified_soul_metal_soul_crafting")
                ))
                .save(generator, "purified_soul_metal");
    }
}
