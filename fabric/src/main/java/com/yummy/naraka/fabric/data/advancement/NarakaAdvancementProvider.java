package com.yummy.naraka.fabric.data.advancement;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.advancements.criterion.FillSoulStabilizerTrigger;
import com.yummy.naraka.advancements.criterion.SimpleTrigger;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.data.lang.AdvancementComponent;
import com.yummy.naraka.data.lang.AdvancementExtraComponents;
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
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class NarakaAdvancementProvider extends FabricAdvancementProvider {
    protected Consumer<AdvancementHolder> generator = holder -> {
        throw new IllegalStateException("Generator is not set");
    };

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

    public ResourceKey<Recipe<?>> recipe(ResourceLocation location) {
        return ResourceKey.create(Registries.RECIPE, location);
    }

    public NarakaAdvancementProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    public AdvancementHolder task(AdvancementHolder parent, ItemLike icon, AdvancementComponent component, UnaryOperator<Advancement.Builder> builder) {
        return builder.apply(task(parent, icon, component))
                .save(generator, location(component.advancementName()));
    }

    public AdvancementHolder goal(AdvancementHolder parent, ItemLike icon, AdvancementComponent component, UnaryOperator<Advancement.Builder> builder) {
        return builder.apply(goal(parent, icon, component))
                .save(generator, location(component.advancementName()));
    }

    public AdvancementHolder challenge(AdvancementHolder parent, ItemLike icon, AdvancementComponent component, UnaryOperator<Advancement.Builder> builder) {
        return builder.apply(challenge(parent, icon, component))
                .save(generator, location(component.advancementName()));
    }

    @SuppressWarnings("unused")
    @Override
    public void generateAdvancement(HolderLookup.Provider registries, Consumer<AdvancementHolder> generator) {
        this.generator = generator;

        HolderGetter<Structure> structures = registries.lookupOrThrow(Registries.STRUCTURE);
        HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);
        HolderGetter<EntityType<?>> entities = registries.lookupOrThrow(Registries.ENTITY_TYPE);

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
        AdvancementHolder sanctuaryCompass = task(root, NarakaItems.SANCTUARY_COMPASS.get(), AdvancementNarakaComponents.SANCTUARY_COMPASS,
                builder -> builder.addCriterion(
                        "has_sanctuary_compass",
                        InventoryChangeTrigger.TriggerInstance.hasItems(NarakaItems.SANCTUARY_COMPASS.get())
                ).rewards(AdvancementRewards.Builder.experience(6))
        );
        AdvancementHolder findHerobrineSanctuary = task(sanctuaryCompass, NarakaItems.SANCTUARY_COMPASS.get(), AdvancementNarakaComponents.FIND_HEROBRINE_SANCTUARY,
                builder -> builder.addCriterion(
                        "find_herobrine_sanctuary",
                        PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.inStructure(herobrineSanctuaryStructure))
                ).rewards(AdvancementRewards.Builder.experience(3))
        );
        AdvancementHolder summonHerobrine = goal(findHerobrineSanctuary, NarakaBlocks.HEROBRINE_TOTEM.get(), AdvancementNarakaComponents.SUMMON_HEROBRINE,
                builder -> builder.addCriterion(
                        "summon_herobrine",
                        SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(entities, NarakaEntityTypes.HEROBRINE.get()))
                ).rewards(AdvancementRewards.Builder.experience(6))
        );
        AdvancementHolder killHerobrine = challenge(summonHerobrine, NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.get(), AdvancementNarakaComponents.KILL_HEROBRINE,
                builder -> builder.addCriterion(
                        "kill_herobrine",
                        KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entities, NarakaEntityTypes.HEROBRINE.get()))
                ).rewards(AdvancementRewards.Builder.experience(66))
        );
        AdvancementHolder godBlood = challenge(killHerobrine, NarakaItems.GOD_BLOOD.get(), AdvancementNarakaComponents.GOD_BLOOD,
                builder -> builder.addCriterion(
                        "has_god_blood",
                        InventoryChangeTrigger.TriggerInstance.hasItems(NarakaItems.GOD_BLOOD.get())
                ).rewards(AdvancementRewards.Builder.experience(66))
        );
        AdvancementHolder purifiedSoulMetal = task(killHerobrine, NarakaItems.PURIFIED_SOUL_METAL.get(), AdvancementNarakaComponents.PURIFIED_SOUL_METAL,
                builder -> builder.addCriterion(
                        "decompose_purified_soul_metal",
                        RecipeCraftedTrigger.TriggerInstance.craftedItem(recipe(NarakaMod.location("purified_soul_metal_from_purified_soul_metal_block")))
                ).rewards(AdvancementRewards.Builder.experience(6))
        );
        AdvancementHolder purifiedSoulSword = task(purifiedSoulMetal, NarakaItems.PURIFIED_SOUL_SWORD.get(), AdvancementNarakaComponents.PURIFIED_SOUL_SWORD,
                builder -> builder.addCriterion(
                        "has_purified_soul_sword",
                        InventoryChangeTrigger.TriggerInstance.hasItems(NarakaItems.PURIFIED_SOUL_SWORD.get())
                ).rewards(AdvancementRewards.Builder.experience(6))
        );
        AdvancementHolder soulInfusedMaterials = task(purifiedSoulMetal, NarakaItems.PURIFIED_SOUL_SHARD.get(), AdvancementNarakaComponents.SOUL_INFUSED_MATERIALS,
                builder -> {
                    NarakaItems.forEachSoulInfusedItemHolder(item -> {
                        ResourceLocation recipeLocation = item.unwrapKey().orElseThrow().location();
                        builder.addCriterion("craft_" + recipeLocation.getPath(),
                                RecipeCraftedTrigger.TriggerInstance.craftedItem(recipe(recipeLocation))
                        );
                    });
                    return builder.requirements(AdvancementRequirements.Strategy.OR);
                }
        );
        AdvancementHolder stabilizer = task(soulInfusedMaterials, NarakaBlocks.SOUL_STABILIZER.get(), AdvancementNarakaComponents.STABILIZER,
                builder -> builder.addCriterion(
                        "craft_soul_stabilizer",
                        RecipeCraftedTrigger.TriggerInstance.craftedItem(recipe(NarakaMod.location("soul_stabilizer")))
                ).rewards(AdvancementRewards.Builder.experience(6))
        );
        AdvancementHolder fillSoulStabilizer = task(stabilizer, NarakaBlocks.SOUL_STABILIZER.get(), AdvancementNarakaComponents.FILL_SOUL_STABILIZER,
                builder -> builder.addCriterion(
                        "fill_soul_stabilizer",
                        FillSoulStabilizerTrigger.TriggerInstance.fullFilled()
                ).rewards(AdvancementRewards.Builder.experience(9))
        );
        AdvancementHolder challengersBlessing = goal(purifiedSoulSword, NarakaItems.STIGMA_ROD.get(), AdvancementNarakaComponents.CHALLENGERS_BLESSING,
                builder -> builder.addCriterion(
                        "has_challengers_blessing_with_equipments",
                        SimpleTrigger.TriggerInstance.simple(SimpleTrigger.CHALLENGERS_BLESSING)
                ).rewards(AdvancementRewards.Builder.experience(6))
        );
        AdvancementHolder soulSwords = challenge(challengersBlessing, NarakaItems.RAINBOW_SWORD.get(), AdvancementNarakaComponents.SOUL_SWORDS,
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
        AdvancementHolder ultimateSword = challenge(soulSwords, NarakaItems.ULTIMATE_SWORD.get(), AdvancementNarakaComponents.ULTIMATE_SWORD,
                builder -> builder.addCriterion(
                        "has_ultimate_sword",
                        InventoryChangeTrigger.TriggerInstance.hasItems(NarakaItems.ULTIMATE_SWORD.get())
                ).rewards(AdvancementRewards.Builder.experience(666))
        );

        AdvancementHolder buyNectariumCore = task(root, NarakaBlocks.NECTARIUM_CORE_BLOCK.get(), AdvancementExtraComponents.BUY_NECTARIUM_CORE,
                builder -> builder.addCriterion(
                        "buy_nectarium_core",
                        InventoryChangeTrigger.TriggerInstance.hasItems(NarakaBlocks.NECTARIUM_CORE_BLOCK.get())
                )
        );
        AdvancementHolder activateNectariumCore = task(buyNectariumCore, Items.HONEY_BOTTLE, AdvancementExtraComponents.ACTIVATE_NECTARIUM_CORE,
                builder -> builder.addCriterion(
                        "activate_nectarium_core",
                        SimpleTrigger.TriggerInstance.simple(SimpleTrigger.ACTIVATE_NECTARIUM_CORE)
                )
        );
        AdvancementHolder eatNectarium = task(activateNectariumCore, NarakaItems.NECTARIUM.get(), AdvancementExtraComponents.EAT_NECTARIUM,
                builder -> builder.addCriterion(
                        "eat_nectarium",
                        ConsumeItemTrigger.TriggerInstance.usedItem(items, NarakaItems.NECTARIUM.get())
                )
        );
        AdvancementHolder craftSoulInfusedNectarium = task(eatNectarium, NarakaItems.SOUL_INFUSED_NECTARIUM.get(), AdvancementExtraComponents.CRAFT_SOUL_INFUSED_NECTARIUM,
                builder -> builder.addCriterion(
                        "craft_soul_infused_nectarium",
                        RecipeCraftedTrigger.TriggerInstance.craftedItem(recipe(NarakaMod.location("soul_infused_nectarium")))
                )
        );
    }
}
