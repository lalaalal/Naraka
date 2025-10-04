package com.yummy.naraka.world.item.equipmentset;

import com.yummy.naraka.advancements.NarakaCriteriaTriggers;
import com.yummy.naraka.advancements.criterion.SimpleTrigger;
import com.yummy.naraka.core.component.NarakaDataComponentTypes;
import com.yummy.naraka.world.effect.NarakaMobEffects;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

import java.util.Map;
import java.util.Optional;

public class SoulEquipmentSet extends EquipmentSet {
    private static boolean test(LivingEntity livingEntity) {
        ItemStack handItem = livingEntity.getMainHandItem();
        SoulType soulType = handItem.getOrDefault(NarakaDataComponentTypes.SOUL.get(), SoulType.NONE);
        if (soulType == SoulType.NONE)
            return false;

        for (EquipmentSlot slot : EquipmentSlotGroup.ARMOR.slots()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR)
                continue;
            ItemStack armorItemStack = livingEntity.getItemBySlot(slot);
            ArmorTrim armorTrim = armorItemStack.get(DataComponents.TRIM);
            if (armorTrim == null)
                return false;

            Optional<ResourceKey<TrimMaterial>> material = armorTrim.material().unwrapKey();
            if (material.isPresent() && !soulType.material.equals(material.get()))
                return false;
        }

        return true;
    }

    public SoulEquipmentSet() {
        super(SoulEquipmentSet::test, new ChallengersBlessingEffect());
    }

    private static class ChallengersBlessingEffect implements EquipmentSetEffect {
        private static final Map<SoulType, Holder<MobEffect>> SOUL_EFFECT_MAP = Map.of(
                SoulType.AMETHYST, NarakaMobEffects.CHALLENGERS_BLESSING_AMETHYST,
                SoulType.COPPER, NarakaMobEffects.CHALLENGERS_BLESSING_COPPER,
                SoulType.DIAMOND, NarakaMobEffects.CHALLENGERS_BLESSING_DIAMOND,
                SoulType.EMERALD, NarakaMobEffects.CHALLENGERS_BLESSING_EMERALD,
                SoulType.GOLD, NarakaMobEffects.CHALLENGERS_BLESSING_GOLD,
                SoulType.LAPIS, NarakaMobEffects.CHALLENGERS_BLESSING_LAPIS,
                SoulType.NECTARIUM, NarakaMobEffects.CHALLENGERS_BLESSING_NECTARIUM,
                SoulType.REDSTONE, NarakaMobEffects.CHALLENGERS_BLESSING_REDSTONE
        );

        public ChallengersBlessingEffect() {

        }

        @Override
        public void activate(LivingEntity livingEntity) {
            ItemStack handItem = livingEntity.getMainHandItem();
            SoulType soulType = handItem.getOrDefault(NarakaDataComponentTypes.SOUL.get(), SoulType.NONE);
            if (SOUL_EFFECT_MAP.containsKey(soulType)) {
                Holder<MobEffect> effect = SOUL_EFFECT_MAP.get(soulType);
                if (livingEntity.hasEffect(effect))
                    return;
                livingEntity.addEffect(new MobEffectInstance(effect, -1));
                if (livingEntity instanceof ServerPlayer serverPlayer)
                    NarakaCriteriaTriggers.SIMPLE_TRIGGER.get().trigger(serverPlayer, SimpleTrigger.CHALLENGERS_BLESSING);
                livingEntity.addEffect(new MobEffectInstance(MobEffects.SPEED, -1, 1));
            }
        }

        @Override
        public void deactivate(LivingEntity livingEntity) {
            NarakaMobEffects.getChallengersBlessing(livingEntity)
                    .map(MobEffectInstance::getEffect)
                    .ifPresent(effect -> {
                        livingEntity.removeEffect(effect);
                        livingEntity.removeEffect(MobEffects.SPEED);
                    });
        }
    }
}
