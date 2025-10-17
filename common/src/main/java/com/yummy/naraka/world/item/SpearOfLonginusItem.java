package com.yummy.naraka.world.item;

import com.yummy.naraka.util.ComponentStyles;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.entity.Spear;
import com.yummy.naraka.world.entity.SpearOfLonginus;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpearOfLonginusItem extends SpearItem implements ItemDamageSourceProvider {
    public SpearOfLonginusItem(Properties properties) {
        super(NarakaTiers.LONGINUS, false, 600, -3, 66, properties, NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS);
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
    public DamageSource naraka$getDamageSource(LivingEntity user) {
        return NarakaDamageSources.longinus(user);
    }

    @Override
    public float getAttackDamageBonus(Entity target, float damage, DamageSource damageSource) {
        return 6.66e6f;
    }

    @Override
    public Component getName(ItemStack stack) {
        return super.getName(stack).copy()
                .withStyle(ChatFormatting.BOLD)
                .withStyle(ComponentStyles.RAINBOW_COLOR);
    }
}