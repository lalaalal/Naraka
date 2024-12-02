package com.yummy.naraka.world.item.equipmentset;

import com.yummy.naraka.advancements.NarakaCriteriaTriggers;
import com.yummy.naraka.world.effect.NarakaMobEffects;
import com.yummy.naraka.world.item.SoulType;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;

public class SoulEquipmentSet extends EquipmentSet {
    private static boolean test(LivingEntity livingEntity) {
        ItemStack handItem = livingEntity.getMainHandItem();
        SoulType soulType = handItem.get(NarakaDataComponentTypes.SOUL.get());
        if (soulType == null)
            return false;

        for (ItemStack armorItemStack : livingEntity.getArmorSlots()) {
            ArmorTrim armorTrim = armorItemStack.get(DataComponents.TRIM);
            if (armorTrim == null)
                return false;
            TrimMaterial material = armorTrim.material().value();
            if (material.ingredient().value() != soulType.getItem())
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
                NarakaCriteriaTriggers.CHALLENGERS_BLESSING.get().trigger(serverPlayer);
        }
    }
}
