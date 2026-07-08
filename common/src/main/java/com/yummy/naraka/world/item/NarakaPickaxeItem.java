package com.yummy.naraka.world.item;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.tags.NarakaBlockTags;
import com.yummy.naraka.util.NarakaItemUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;

public class NarakaPickaxeItem extends DiggerItem {
    public NarakaPickaxeItem(Properties properties) {
        super(calculateAttackDamageModifier(), -2.4f, NarakaTiers.LONGINUS, NarakaBlockTags.MINABLE_WITH_NARAKA_PICKAXE, properties);
    }

    private static float calculateAttackDamageModifier() {
        return NarakaConfig.COMMON.narakaPickaxeDamage.getValue() - (NarakaTiers.LONGINUS.getAttackDamageBonus() + 1);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, livingEntity -> livingEntity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public ItemStack getDefaultInstance() {
        return NarakaItemUtils.makeUnbreakable(super.getDefaultInstance());
    }
}
