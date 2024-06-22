package com.yummy.naraka.world.item;

import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.NarakaEntities;
import com.yummy.naraka.world.entity.Spear;
import com.yummy.naraka.world.entity.SpearOfLonginus;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        entity.setInvulnerable(true);
        if (entity.getAge() > 20) {
            entity.setNoGravity(true);
            entity.setOnGround(true);
        }
        Vec3 delta = entity.getDeltaMovement();
        entity.setDeltaMovement(delta.multiply(0.9, 0.8, 0.9));


        return false;
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, Level level) {
        return Integer.MAX_VALUE;
    }

    @Override
    public Component getName(ItemStack stack) {
        return super.getName(stack).copy()
                .withStyle(ChatFormatting.ITALIC, ChatFormatting.RED);
    }
}