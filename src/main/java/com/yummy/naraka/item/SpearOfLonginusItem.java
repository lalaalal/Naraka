package com.yummy.naraka.item;

import java.util.function.Supplier;

import com.yummy.naraka.entity.Spear;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class SpearOfLonginusItem extends SpearItem {

    public SpearOfLonginusItem(Tier tier, Properties properties, Supplier<EntityType<Spear>> spearType) {
        super(tier, properties, spearType);
        
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        
        return super.hurtEnemy(stack, target, attacker);
    }
}