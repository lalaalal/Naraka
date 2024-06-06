package com.yummy.naraka.item;

import com.yummy.naraka.damagesource.NarakaDamageSources;
import com.yummy.naraka.entity.NarakaEntities;
import com.yummy.naraka.entity.Spear;
import com.yummy.naraka.entity.SpearOfLonginus;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpearOfLonginusItem extends SpearItem {
    public SpearOfLonginusItem(Properties properties) {
        super(NarakaTiers.LONGINUS, properties, NarakaEntities.THROWN_SPEAR_OF_LONGINUS);
    }

    @Override
    protected Spear createSpear(Level level, LivingEntity owner, ItemStack stack) {
        return new SpearOfLonginus(level, owner, stack);
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack stack, Direction direction) {
        return new SpearOfLonginus(level, position, stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        DamageSource source = NarakaDamageSources.longinus(attacker);
        target.hurt(source, Float.MAX_VALUE);

        return true;
    }
}