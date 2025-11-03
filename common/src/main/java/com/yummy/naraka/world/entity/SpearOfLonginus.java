package com.yummy.naraka.world.entity;

import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class SpearOfLonginus extends Spear {
    protected SpearOfLonginus(EntityType<? extends SpearOfLonginus> entityType, Level level) {
        super(entityType, level);
        setInvulnerable(true);
    }

    public SpearOfLonginus(Level level, Position position, ItemStack stack) {
        super(NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS.get(), level, position, stack);
        setInvulnerable(true);
    }

    public SpearOfLonginus(Level level, LivingEntity owner, ItemStack stack) {
        super(NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS.get(), level, owner, stack);
        setInvulnerable(true);
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return true;
    }

    @Override
    public boolean hasFoil() {
        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        dealtDamage = true;
    }

    @Override
    protected boolean canHurtEntity(Entity entity) {
        return true;
    }

    @Override
    protected void hurtHitEntity(ServerLevel serverLevel, Entity entity) {
        DamageSource source = NarakaDamageSources.longinus(this);
        entity.hurt(source, 6.66e6f);
        if (entity.isAlive())
            entity.kill();
    }

    @Override
    public Component getName() {
        return super.getName().copy()
                .withStyle(ChatFormatting.ITALIC, ChatFormatting.RED);
    }

    @Override
    public int getLoyalty() {
        if (!level().isClientSide())
            return 3;
        return 0;
    }

    @Override
    public void remove(RemovalReason reason) {
        if (getY() <= -60 && reason.shouldDestroy()) {
            if (dealtDamage)
                return;
            dealtDamage = true;
            setDeltaMovement(Vec3.ZERO);
            return;
        }
        super.remove(reason);
    }
}
