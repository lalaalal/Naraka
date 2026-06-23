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
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class NarakaAdvancementProvider extends FabricAdvancementProvider {
    protected Consumer<Advancement> generator = holder -> {
        throw new IllegalStateException("Generator is not set");
    };

    public static Advancement.Builder advancement(Advancement parent, ItemLike icon, AdvancementComponent component, FrameType type) {
        return Advancement.Builder.advancement()
                .parent(parent)
                .display(icon, component.title(), component.description(), null, type, true, true, false);
    }

    public static Advancement.Builder task(Advancement parent, ItemLike icon, AdvancementComponent component) {
        return advancement(parent, icon, component, FrameType.TASK);
    }

    public static Advancement.Builder goal(Advancement parent, ItemLike icon, AdvancementComponent component) {
        return advancement(parent, icon, component, FrameType.GOAL);
    }

    public static Advancement.Builder challenge(Advancement parent, ItemLike icon, AdvancementComponent component) {
        return advancement(parent, icon, component, FrameType.CHALLENGE);
    }

    public String location(String path) {
        return NarakaMod.MOD_ID + ':' + path;
    }

    public NarakaAdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    public Advancement task(Advancement parent, ItemLike icon, AdvancementComponent component, UnaryOperator<Advancement.Builder> builder) {
        return builder.apply(task(parent, icon, component))
                .save(generator, location(component.advancementName()));
    }

    public Advancement goal(Advancement parent, ItemLike icon, AdvancementComponent component, UnaryOperator<Advancement.Builder> builder) {
        return builder.apply(goal(parent, icon, component))
                .save(generator, location(component.advancementName()));
    }

    public Advancement challenge(Advancement parent, ItemLike icon, AdvancementComponent component, UnaryOperator<Advancement.Builder> builder) {
        return builder.apply(challenge(parent, icon, component))
                .save(generator, location(component.advancementName()));
    }

    @SuppressWarnings("unused")
    @Override
    public void generateAdvancement(Consumer<Advancement> generator) {
        this.generator = generator;

        Advancement root = Advancement.Builder.advancement()
                .display(NarakaItems.STIGMA_ROD.get(),
                        AdvancementNarakaComponents.ROOT.title(),
                        AdvancementNarakaComponents.ROOT.description(),
                        NarakaTextures.NARAKA_ADVANCEMENT_ROOT_BACKGROUND,
                        FrameType.TASK,
                        false,
                        false,
                        false
                )
                .requirements(RequirementsStrategy.OR)
                .addCriterion("killed_something", KilledTrigger.TriggerInstance.playerKilledEntity())
                .addCriterion("killed_by_something", KilledTrigger.TriggerInstance.entityKilledPlayer())
                .save(generator, location("root"));
        Advancement sanctuaryCompass = task(root, NarakaItems.SANCTUARY_COMPASS.get(), AdvancementNarakaComponents.SANCTUARY_COMPASS,
                builder -> builder.addCriterion(
                        "has_sanctuary_compass",
                        InventoryChangeTrigger.TriggerInstance.hasItems(NarakaItems.SANCTUARY_COMPASS.get())
                ).rewards(AdvancementRewards.Builder.experience(6))
        );
        Advancement findHerobrineSanctuary = task(sanctuaryCompass, NarakaItems.SANCTUARY_COMPASS.get(), AdvancementNarakaComponents.FIND_HEROBRINE_SANCTUARY,
                builder -> builder.addCriterion(
                        "find_herobrine_sanctuary",
                        PlayerTrigger.TriggerInstance.located(LocationPredicate.Builder.location().setStructure(NarakaStructures.HEROBRINE_SANCTUARY).build())
                ).rewards(AdvancementRewards.Builder.experience(3))
        );
        Advancement summonHerobrine = goal(findHerobrineSanctuary, NarakaBlocks.HEROBRINE_TOTEM.get(), AdvancementNarakaComponents.SUMMON_HEROBRINE,
                builder -> builder.addCriterion(
                        "summon_herobrine",
                        SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(NarakaEntityTypes.HEROBRINE.get()))
                ).rewards(AdvancementRewards.Builder.experience(6))
        );
        Advancement killHerobrine = challenge(summonHerobrine, NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK.get(), AdvancementNarakaComponents.KILL_HEROBRINE,
                builder -> builder.addCriterion(
                        "kill_herobrine",
                        KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(NarakaEntityTypes.HEROBRINE.get()))
                ).rewards(AdvancementRewards.Builder.experience(66))
        );
        Advancement killOriginHerobrine = challenge(killHerobrine, NarakaItems.STIGMA_ROD.get(), AdvancementNarakaComponents.KILL_ORIGIN_HEROBRINE,
                builder -> builder.addCriterion(
                        "kill_origin_herobrine",
                        KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(NarakaEntityTypes.ORIGIN_HEROBRINE.get()))
                )
        );
        Advancement godBlood = challenge(killHerobrine, NarakaItems.GOD_BLOOD.get(), AdvancementNarakaComponents.GOD_BLOOD,
                builder -> builder.addCriterion(
                        "has_god_blood",
                        InventoryChangeTrigger.TriggerInstance.hasItems(NarakaItems.GOD_BLOOD.get())
                ).rewards(AdvancementRewards.Builder.experience(66))
        );
        Advancement purifiedSoulMetal = task(killHerobrine, NarakaItems.PURIFIED_SOUL_METAL.get(), AdvancementNarakaComponents.PURIFIED_SOUL_METAL,
                builder -> builder.addCriterion(
                        "decompose_purified_soul_metal",
                        RecipeCraftedTrigger.TriggerInstance.craftedItem(NarakaMod.location("purified_soul_metal_from_purified_soul_metal_block"))
                ).rewards(AdvancementRewards.Builder.experience(6))
        );
        Advancement purifiedSoulSword = task(purifiedSoulMetal, NarakaItems.PURIFIED_SOUL_SWORD.get(), AdvancementNarakaComponents.PURIFIED_SOUL_SWORD,
                builder -> builder.addCriterion(
                        "has_purified_soul_sword",
                        InventoryChangeTrigger.TriggerInstance.hasItems(NarakaItems.PURIFIED_SOUL_SWORD.get())
                ).rewards(AdvancementRewards.Builder.experience(6))
        );
        Advancement soulInfusedMaterials = task(purifiedSoulMetal, NarakaItems.PURIFIED_SOUL_SHARD.get(), AdvancementNarakaComponents.SOUL_INFUSED_MATERIALS,
                builder -> {
                    NarakaItems.forEachSoulInfusedItemHolder(item -> {
                        ResourceLocation recipeLocation = item.unwrapKey().orElseThrow().location();
                        builder.addCriterion("craft_" + recipeLocation.getPath(),
                                RecipeCraftedTrigger.TriggerInstance.craftedItem(recipeLocation)
                        );
                    });
                    return builder.requirements(RequirementsStrategy.OR);
                }
        );
        Advancement stabilizer = task(soulInfusedMaterials, NarakaBlocks.SOUL_STABILIZER.get(), AdvancementNarakaComponents.STABILIZER,
                builder -> builder.addCriterion(
                        "craft_soul_stabilizer",
                        RecipeCraftedTrigger.TriggerInstance.craftedItem(NarakaMod.location("soul_stabilizer"))
                ).rewards(AdvancementRewards.Builder.experience(6))
        );
        Advancement fillSoulStabilizer = task(stabilizer, NarakaBlocks.SOUL_STABILIZER.get(), AdvancementNarakaComponents.FILL_SOUL_STABILIZER,
                builder -> builder.addCriterion(
                        "fill_soul_stabilizer",
                        FillSoulStabilizerTrigger.TriggerInstance.fullFilled()
                ).rewards(AdvancementRewards.Builder.experience(9))
        );
        Advancement challengersBlessing = goal(purifiedSoulSword, NarakaItems.STIGMA_ROD.get(), AdvancementNarakaComponents.CHALLENGERS_BLESSING,
                builder -> builder.addCriterion(
                        "has_challengers_blessing_with_equipments",
                        SimpleTrigger.TriggerInstance.simple(SimpleTrigger.CHALLENGERS_BLESSING)
                ).rewards(AdvancementRewards.Builder.experience(6))
        );
        Advancement soulSwords = challenge(challengersBlessing, NarakaItems.RAINBOW_SWORD.get(), AdvancementNarakaComponents.SOUL_SWORDS,
                builder -> {
                    NarakaItems.forEachSoulInfusedSwordHolder(sword -> {
                        String name = sword.unwrapKey().orElseThrow().location().getPath();
                        builder.addCriterion(
                                "has" + name,
                                InventoryChangeTrigger.TriggerInstance.hasItems(sword.value())
                        );
                    });
                    return builder.rewards(AdvancementRewards.Builder.experience(6))
                            .requirements(RequirementsStrategy.AND);
                }
        );
        Advancement ultimateSpear = challenge(soulSwords, NarakaItems.SPEAR_OF_LONGINUS_ITEM.get(), AdvancementNarakaComponents.ULTIMATE_SPEAR,
                builder -> builder.addCriterion(
                        "has_ultimate_spear",
                        InventoryChangeTrigger.TriggerInstance.hasItems(NarakaItems.SPEAR_OF_LONGINUS_ITEM.get())
                ).rewards(AdvancementRewards.Builder.experience(666))
        );

        Advancement buyNectariumCore = task(root, NarakaBlocks.NECTARIUM_CORE_BLOCK.get(), AdvancementExtraComponents.BUY_NECTARIUM_CORE,
                builder -> builder.addCriterion(
                        "buy_nectarium_core",
                        InventoryChangeTrigger.TriggerInstance.hasItems(NarakaBlocks.NECTARIUM_CORE_BLOCK.get())
                )
        );
        Advancement activateNectariumCore = task(buyNectariumCore, Items.HONEY_BOTTLE, AdvancementExtraComponents.ACTIVATE_NECTARIUM_CORE,
                builder -> builder.addCriterion(
                        "activate_nectarium_core",
                        SimpleTrigger.TriggerInstance.simple(SimpleTrigger.ACTIVATE_NECTARIUM_CORE)
                )
        );
        Advancement eatNectarium = task(activateNectariumCore, NarakaItems.NECTARIUM.get(), AdvancementExtraComponents.EAT_NECTARIUM,
                builder -> builder.addCriterion(
                        "eat_nectarium",
                        ConsumeItemTrigger.TriggerInstance.usedItem(NarakaItems.NECTARIUM.get())
                )
        );
        Advancement craftSoulInfusedNectarium = task(eatNectarium, NarakaItems.SOUL_INFUSED_NECTARIUM.get(), AdvancementExtraComponents.CRAFT_SOUL_INFUSED_NECTARIUM,
                builder -> builder.addCriterion(
                        "craft_soul_infused_nectarium",
                        RecipeCraftedTrigger.TriggerInstance.craftedItem(NarakaMod.location("soul_infused_nectarium"))
                )
        );
    }
}
