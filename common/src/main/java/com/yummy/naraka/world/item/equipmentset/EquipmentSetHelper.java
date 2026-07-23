package com.yummy.naraka.world.item.equipmentset;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.component.DataComponentCondition;
import com.yummy.naraka.core.component.NarakaDataComponentTypes;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.world.effect.NarakaMobEffects;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipmentSetHelper {
    private static final Map<SoulType, HolderProxy<MobEffect, MobEffect>> SOUL_EFFECT_MAP = Map.of(
            SoulType.AMETHYST, NarakaMobEffects.CHALLENGERS_BLESSING_AMETHYST,
            SoulType.COPPER, NarakaMobEffects.CHALLENGERS_BLESSING_COPPER,
            SoulType.DIAMOND, NarakaMobEffects.CHALLENGERS_BLESSING_DIAMOND,
            SoulType.EMERALD, NarakaMobEffects.CHALLENGERS_BLESSING_EMERALD,
            SoulType.GOLD, NarakaMobEffects.CHALLENGERS_BLESSING_GOLD,
            SoulType.LAPIS, NarakaMobEffects.CHALLENGERS_BLESSING_LAPIS,
            SoulType.NECTARIUM, NarakaMobEffects.CHALLENGERS_BLESSING_NECTARIUM,
            SoulType.REDSTONE, NarakaMobEffects.CHALLENGERS_BLESSING_REDSTONE
    );

    public static final ResourceLocation ID_CHALLENGER = NarakaMod.location("challenger");
    public static final ResourceLocation ID_BLESSED = NarakaMod.location("blessed");
    private static final Map<SoulType, EquipmentSet> SET_BY_SOUL_TYPE = new HashMap<>();

    public static List<EquipmentSet.Effect> createBlessedEffect() {
        MobEffectData strength = new MobEffectData(MobEffects.DAMAGE_BOOST, -1, 1);
        MobEffectData speed = new MobEffectData(MobEffects.MOVEMENT_SPEED, -1, 1);
        return List.of(
                EquipmentSet.Effect.of(4, MobEffectEquipmentSetEffect.of(strength, speed))
        );
    }

    public static List<EquipmentSet> createBlessedSet() {
        DataComponentPatch components = DataComponentPatch.builder()
                .set(NarakaDataComponentTypes.BLESSED.get(), true)
                .build();
        DataComponentCondition condition = DataComponentCondition.all(components);

        return List.of(
                new EquipmentSet(
                        ID_BLESSED,
                        List.of(
                                new EquipmentSet.Requirement(
                                        NarakaItems.PURIFIED_SOUL_HELMET,
                                        EquipmentSlot.HEAD,
                                        condition
                                ),
                                new EquipmentSet.Requirement(
                                        NarakaItems.PURIFIED_SOUL_CHESTPLATE,
                                        EquipmentSlot.CHEST,
                                        condition
                                ),
                                new EquipmentSet.Requirement(
                                        NarakaItems.PURIFIED_SOUL_LEGGINGS,
                                        EquipmentSlot.LEGS,
                                        condition
                                ),
                                new EquipmentSet.Requirement(
                                        NarakaItems.PURIFIED_SOUL_BOOTS,
                                        EquipmentSlot.FEET,
                                        condition
                                )
                        ),
                        createBlessedEffect()
                )
        );
    }

    public static List<EquipmentSet> createChallengerSet(SoulType soulType) {
        if (SET_BY_SOUL_TYPE.containsKey(soulType))
            return List.of(SET_BY_SOUL_TYPE.get(soulType));

        Holder<Item> swordItem = NarakaItems.getSoulSwordHolderOf(soulType);
        if (swordItem == null)
            return List.of();
        DataComponentPatch components = DataComponentPatch.builder()
                .set(NarakaDataComponentTypes.SOUL.get(), soulType)
                .build();
        DataComponentCondition condition = DataComponentCondition.all(components);
        EquipmentSet equipmentSet = new EquipmentSet(
                ID_CHALLENGER,
                List.of(
                        new EquipmentSet.Requirement(
                                NarakaItems.PURIFIED_SOUL_HELMET,
                                EquipmentSlot.HEAD,
                                condition
                        ),
                        new EquipmentSet.Requirement(
                                NarakaItems.PURIFIED_SOUL_CHESTPLATE,
                                EquipmentSlot.CHEST,
                                condition
                        ),
                        new EquipmentSet.Requirement(
                                NarakaItems.PURIFIED_SOUL_LEGGINGS,
                                EquipmentSlot.LEGS,
                                condition
                        ),
                        new EquipmentSet.Requirement(
                                NarakaItems.PURIFIED_SOUL_BOOTS,
                                EquipmentSlot.FEET,
                                condition
                        ),
                        new EquipmentSet.Requirement(
                                swordItem,
                                EquipmentSlot.MAINHAND,
                                condition
                        )
                ),
                createChallengerSetEffect(soulType)
        );
        SET_BY_SOUL_TYPE.put(soulType, equipmentSet);
        return List.of(equipmentSet);
    }

    public static List<EquipmentSet.Effect> createChallengerSetEffect(SoulType soulType) {
        if (SOUL_EFFECT_MAP.containsKey(soulType)) {
            MobEffectData speed = new MobEffectData(MobEffects.MOVEMENT_SPEED, -1, 1);
            MobEffectData blessing = new MobEffectData(SOUL_EFFECT_MAP.get(soulType).delegate(), -1, 0);
            return List.of(
                    EquipmentSet.Effect.of(5, MobEffectEquipmentSetEffect.of(blessing)),
                    EquipmentSet.Effect.of(4, MobEffectEquipmentSetEffect.of(speed))
            );
        }
        return List.of();
    }
}
