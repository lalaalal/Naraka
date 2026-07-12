package com.yummy.naraka.world.item.equipmentset;

import com.yummy.naraka.core.component.NarakaDataComponentTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class BlessedSoulEquipmentSet extends EquipmentSet {
    public BlessedSoulEquipmentSet() {
        super(BlessedSoulEquipmentSet::test, new BlessedSoulEquipmentSetEffect());
    }

    private static boolean test(LivingEntity livingEntity) {
        for (EquipmentSlot slot : EquipmentSlotGroup.ARMOR.slots()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR)
                continue;
            ItemStack armorItemStack = livingEntity.getItemBySlot(slot);
            if (!armorItemStack.getOrDefault(NarakaDataComponentTypes.BLESSED.get(), false))
                return false;
        }

        return true;
    }

    private static class BlessedSoulEquipmentSetEffect implements EquipmentSetEffect {
        @Override
        public void activate(LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.SPEED, -1, 1));
            livingEntity.addEffect(new MobEffectInstance(MobEffects.STRENGTH, -1, 1));
        }

        @Override
        public void deactivate(LivingEntity livingEntity) {
            livingEntity.removeEffect(MobEffects.SPEED);
            livingEntity.removeEffect(MobEffects.STRENGTH);
        }
    }
}
