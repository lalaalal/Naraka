package com.yummy.naraka.world.item.equipmentset;

import com.yummy.naraka.advancements.NarakaCriteriaTriggers;
import com.yummy.naraka.advancements.criterion.SimpleTrigger;
import com.yummy.naraka.world.effect.NarakaMobEffects;
import com.yummy.naraka.world.item.SoulType;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;

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

    private static class ChallengersBlessingEffect extends MobEffectEquipmentSetEffect {
        public ChallengersBlessingEffect() {
            super(NarakaMobEffects.CHALLENGERS_BLESSING, false);
        }

        @Override
        public void activate(LivingEntity livingEntity) {
            super.activate(livingEntity);
            if (livingEntity instanceof ServerPlayer serverPlayer)
                NarakaCriteriaTriggers.SIMPLE_TRIGGER.get().trigger(serverPlayer, SimpleTrigger.CHALLENGERS_BLESSING);
        }
    }
}
