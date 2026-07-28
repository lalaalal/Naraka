package com.yummy.naraka.world.item.equipmentset;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.world.effect.NarakaMobEffects;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.NbtCondition;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
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

    public static List<EquipmentSet.Effect> createBlessedEffect() {
        MobEffectData strength = new MobEffectData(MobEffects.DAMAGE_BOOST, -1, 1);
        MobEffectData speed = new MobEffectData(MobEffects.MOVEMENT_SPEED, -1, 1);
        return List.of(
                EquipmentSet.Effect.of(4, MobEffectEquipmentSetEffect.of(strength, speed))
        );
    }

    public static EquipmentSetGroup createBlessedSet() {
        CompoundTag nbt = new CompoundTag();
        NarakaNbtUtils.store(nbt, NarakaItemUtils.TAG_BLESSED, Codec.BOOL, true);
        NbtCondition condition = NbtCondition.all(nbt);

        return EquipmentSetGroup.of(
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

    private static final Map<SoulType, EquipmentSet> SET_BY_SOUL_TYPE = new HashMap<>();

    public static EquipmentSetGroup createChallengerSet(SoulType soulType) {
        if (SET_BY_SOUL_TYPE.containsKey(soulType))
            return EquipmentSetGroup.of(SET_BY_SOUL_TYPE.get(soulType));

        Holder<Item> swordItem = NarakaItems.getSoulSwordHolderOf(soulType);
        if (swordItem == null)
            return EquipmentSetGroup.EMPTY;
        CompoundTag nbt = new CompoundTag();
        NarakaNbtUtils.store(nbt, NarakaItemUtils.TAG_SOUL_TYPE, SoulType.CODEC, soulType);
        NbtCondition condition = NbtCondition.all(nbt);

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
        return EquipmentSetGroup.of(equipmentSet);
    }

    public static List<EquipmentSet.Effect> createChallengerSetEffect(SoulType soulType) {
        if (SOUL_EFFECT_MAP.containsKey(soulType)) {
            MobEffectData speed = new MobEffectData(MobEffects.MOVEMENT_SPEED, -1, 1);
            MobEffectData blessing = new MobEffectData(SOUL_EFFECT_MAP.get(soulType).value(), -1, 0);
            return List.of(
                    EquipmentSet.Effect.of(5, MobEffectEquipmentSetEffect.of(blessing)),
                    EquipmentSet.Effect.of(4, MobEffectEquipmentSetEffect.of(speed))
            );
        }
        return List.of();
    }
}
