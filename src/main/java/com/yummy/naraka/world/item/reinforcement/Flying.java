package com.yummy.naraka.world.item.reinforcement;

import com.yummy.naraka.util.NarakaItemUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class Flying extends SimpleReinforcementEffect {
    public Flying() {
        super(10, EquipmentSlot.CHEST);
    }

    @Override
    public void onEquipped(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack) {
        if (entity instanceof Player player) {
            player.getAbilities().mayfly = true;
            player.onUpdateAbilities();
        }
    }

    @Override
    public void onEquippedItemChanged(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack) {
        if (entity instanceof Player player
                && !NarakaItemUtils.canApplyReinforcementEffect(player, NarakaReinforcementEffects.KNOCKBACK_RESISTANCE)) {
            player.getAbilities().flying = false;
            player.onUpdateAbilities();
        }
    }

    @Override
    public void onUnequipped(LivingEntity entity, EquipmentSlot equipmentSlot, ItemStack itemStack) {
        if (entity instanceof Player player && !player.isCreative() && !player.isSpectator()) {
            player.getAbilities().flying = false;
            player.getAbilities().mayfly = false;
            player.onUpdateAbilities();
        }
    }
}
