package com.yummy.naraka.entity;

import net.minecraft.core.Position;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class Spear extends AbstractArrow {
    protected Spear(EntityType<? extends Spear> entityType, Level level) {
        super(entityType, level);
    }

    public Spear(Supplier<EntityType<Spear>> type, Level level, Position position) {
        super(type.get(), level);
        setPos(position.x(), position.y(), position.z());
    }

    public Spear(Supplier<EntityType<Spear>> type,  Level level, Position position, ItemStack stack) {
        super(type.get(), level, stack);
        setPos(position.x(), position.y(), position.z());
    }

    public Spear(Supplier<EntityType<Spear>> type, Level level, LivingEntity owner, ItemStack stack) {
        super(type.get(), owner, level, stack);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.AIR);
    }
}