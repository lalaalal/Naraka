package com.yummy.naraka.fabric.data.advancement;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.advancements.criterion.ChallengersBlessingTrigger;
import com.yummy.naraka.advancements.criterion.FillSoulStabilizerTrigger;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class NarakaAdvancementProvider extends FabricAdvancementProvider {
    @Nullable
    protected Consumer<AdvancementHolder> generator;

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

    public String location(String path) {
        return NarakaMod.MOD_ID + ':' + path;
    }

    public NarakaAdvancementProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    private void checkGenerator() {
        if (generator == null)
            throw new IllegalStateException("Generator is not set");
    }

    public AdvancementHolder task(AdvancementHolder parent, ItemLike icon, AdvancementComponent component, UnaryOperator<Advancement.Builder> builder) {
        checkGenerator();
        return builder.apply(task(parent, icon, component))
                .save(generator, location(component.advancementName()));
    }

    public AdvancementHolder goal(AdvancementHolder parent, ItemLike icon, AdvancementComponent component, UnaryOperator<Advancement.Builder> builder) {
        checkGenerator();
        return builder.apply(goal(parent, icon, component))
                .save(generator, location(component.advancementName()));
    }

    public AdvancementHolder challenge(AdvancementHolder parent, ItemLike icon, AdvancementComponent component, UnaryOperator<Advancement.Builder> builder) {
        checkGenerator();
        return builder.apply(challenge(parent, icon, component))
                .save(generator, location(component.advancementName()));
    }

    @SuppressWarnings("unused")
    @Override
    public void generateAdvancement(HolderLookup.Provider registries, Consumer<AdvancementHolder> generator) {
        this.generator = generator;

        HolderLookup.RegistryLookup<Structure> structures = registries.lookupOrThrow(Registries.STRUCTURE);

        Holder<Structure> herobrineSanctuaryStructure = structures.getOrThrow(NarakaStructures.HEROBRINE_SANCTUARY);

        AdvancementHolder root = Advancement.Builder.advancement()
                .display(NarakaItems.STIGMA_ROD.get(),
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
                .save(generator, location("root"));
        AdvancementHolder wayToHim = task(root, NarakaItems.SANCTUARY_COMPASS.get(), AdvancementNarakaComponents.WAY_TO_HIM,
                builder -> builder.addCriterion(
                        "has_sanctuary_compass",
                        InventoryChangeTrigger.TriggerInstance.hasItems(NarakaItems.SANCTUARY_COMPASS.get())
                ).rewards(AdvancementRewards.Builder.experience(6))
        );
        AdvancementHolder herobrineSanctuary = task(wayToHim, NarakaItems.SANCTUARY_COMPASS.get(), AdvancementNarakaComponents.HEROBRINE_SANCTUARY,
                builder -> builder.addCriterion(
                        "find_herobrine_sanctuary",
                        PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(herobrineSanctuaryStructure))
                ).rewards(AdvancementRewards.Builder.experience(3))
        );
        AdvancementHolder summonHerobrine = goal(herobrineSanctuary, NarakaBlocks.HEROBRINE_TOTEM.get(), AdvancementNarakaComponents.SUMMON_HEROBRINE,
                builder -> builder.addCriterion(
                        "summon_herobrine",
                        SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(NarakaEntityTypes.HEROBRINE.get()))
                ).rewards(AdvancementRewards.Builder.experience(6))
        );
        AdvancementHolder killHerobrine = challenge(summonHerobrine, NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.get(), AdvancementNarakaComponents.KILL_HEROBRINE,
                builder -> builder.addCriterion(
                        "kill_herobrine",
                        KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(NarakaEntityTypes.HEROBRINE.get()))
                ).rewards(AdvancementRewards.Builder.experience(66))
        );
        AdvancementHolder godBlood = challenge(killHerobrine, NarakaItems.GOD_BLOOD.get(), AdvancementNarakaComponents.GOD_BLOOD,
                builder -> builder.addCriterion(
                        "has_god_blood",
                        InventoryChangeTrigger.TriggerInstance.hasItems(NarakaItems.GOD_BLOOD.get())
                ).rewards(AdvancementRewards.Builder.experience(66))
        );
        AdvancementHolder soap = task(killHerobrine, NarakaItems.PURIFIED_SOUL_METAL.get(), AdvancementNarakaComponents.SOAP,
                builder -> builder.addCriterion(
                        "decompose_purified_soul_metal",
                        RecipeCraftedTrigger.TriggerInstance.craftedItem(NarakaMod.location("purified_soul_metal_from_purified_soul_metal_block"))
                ).rewards(AdvancementRewards.Builder.experience(6))
        );
        AdvancementHolder pureVessel = task(soap, NarakaItems.PURIFIED_SOUL_SWORD.get(), AdvancementNarakaComponents.PURE_VESSEL,
                builder -> builder.addCriterion(
                        "has_purified_soul_sword",
                        InventoryChangeTrigger.TriggerInstance.hasItems(NarakaItems.PURIFIED_SOUL_SWORD.get())
                ).rewards(AdvancementRewards.Builder.experience(6))
        );
        AdvancementHolder soulInfusedMaterials = task(soap, NarakaItems.PURIFIED_SOUL_SHARD.get(), AdvancementNarakaComponents.SOUL_INFUSED_MATERIALS,
                builder -> {
                    NarakaItems.forEachSoulInfusedItemHolder(item -> {
                        ResourceLocation recipe = item.unwrapKey().orElseThrow().location();
                        builder.addCriterion("craft_" + recipe.getPath(), RecipeCraftedTrigger.TriggerInstance.craftedItem(recipe));
                    });
                    return builder.requirements(AdvancementRequirements.Strategy.OR);
                }
        );
        AdvancementHolder stabilizer = task(root, NarakaBlocks.SOUL_STABILIZER.get(), AdvancementNarakaComponents.STABILIZER,
                builder -> builder.addCriterion(
                        "craft_soul_stabilizer",
                        RecipeCraftedTrigger.TriggerInstance.craftedItem(NarakaMod.location("soul_stabilizer"))
                ).rewards(AdvancementRewards.Builder.experience(6))
        );
        AdvancementHolder fullyCharged = task(stabilizer, NarakaBlocks.SOUL_STABILIZER.get(), AdvancementNarakaComponents.FULLY_CHARGED,
                builder -> builder.addCriterion(
                        "fill_soul_stabilizer",
                        FillSoulStabilizerTrigger.TriggerInstance.fullFilled()
                ).rewards(AdvancementRewards.Builder.experience(9))
        );
        AdvancementHolder rainbow = challenge(pureVessel, NarakaItems.RAINBOW_SWORD.get(), AdvancementNarakaComponents.RAINBOW,
                builder -> {
                    NarakaItems.forEachSoulInfusedSwordHolder(sword -> {
                        String name = sword.unwrapKey().orElseThrow().location().getPath();
                        builder.addCriterion(
                                "has" + name,
                                InventoryChangeTrigger.TriggerInstance.hasItems(sword.value())
                        );
                    });
                    return builder.rewards(AdvancementRewards.Builder.experience(6))
                            .requirements(AdvancementRequirements.Strategy.AND);
                }
        );
        AdvancementHolder challengersBlessing = task(pureVessel, NarakaItems.STIGMA_ROD.get(), AdvancementNarakaComponents.CHALLENGERS_BLESSING,
                builder -> builder.addCriterion(
                        "has_challengers_blessing_with_equipments",
                        ChallengersBlessingTrigger.TriggerInstance.simple()
                ).rewards(AdvancementRewards.Builder.experience(6))
        );
        AdvancementHolder ultimateSword = challenge(rainbow, NarakaItems.EBONY_SWORD.get(), AdvancementNarakaComponents.ULTIMATE_SWORD,
                builder -> builder.addCriterion(
                        "has_ultimate_sword",
                        InventoryChangeTrigger.TriggerInstance.hasItems(NarakaItems.EBONY_SWORD.get())
                ).rewards(AdvancementRewards.Builder.experience(666))
        );
    }
}
