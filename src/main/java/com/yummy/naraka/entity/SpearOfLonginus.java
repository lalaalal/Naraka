package com.yummy.naraka.entity;

import com.yummy.naraka.damagesource.NarakaDamageSources;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.entity.PartEntity;

public class SpearOfLonginus extends Spear {
    protected SpearOfLonginus(EntityType<? extends SpearOfLonginus> entityType, Level level) {
        super(entityType, level);
    }

    public SpearOfLonginus(Level level, Position position, ItemStack stack) {
        super(NarakaEntities.THROWN_SPEAR_OF_LONGINUS, level, position, stack);
    }

    public SpearOfLonginus(Level level, LivingEntity owner, ItemStack stack) {
        super(NarakaEntities.THROWN_SPEAR_OF_LONGINUS, level, owner, stack);
    }

    @Override
    protected boolean canHurtEntity(Entity entity) {
        return true;
    }

    @Override
    protected void hurtHitEntity(Entity entity) {
        Entity target = getValidTarget(entity);
        killEntity(target);
    }

    private Entity getValidTarget(Entity entity) {
        if (entity instanceof LivingEntity livingEntity)
            return livingEntity;
        if (entity instanceof PartEntity<?> partEntity)
            return getValidTarget(partEntity.getParent());
        return entity;
    }

    private void killEntity(Entity entity) {
        DamageSource source = NarakaDamageSources.longinus(this);
        entity.hurt(source, Float.MAX_VALUE);
        if (entity.isAlive())
            entity.kill();
    }

    @Override
    public Component getName() {
        return super.getName().copy()
                .withStyle(ChatFormatting.ITALIC, ChatFormatting.RED);
    }
}
