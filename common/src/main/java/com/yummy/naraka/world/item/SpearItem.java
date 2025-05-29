package com.yummy.naraka.world.item;

import com.yummy.naraka.world.entity.Spear;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class SpearItem extends Item implements ProjectileItem {
    protected final Supplier<? extends EntityType<? extends Spear>> spearType;

    @Deprecated
    public static ItemAttributeModifiers createAttributes(double attackDamage, double attackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, attackDamage, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                ).add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                ).build();
    }

    public SpearItem(ToolMaterial material, boolean enchantable, float attackDamage, float attackSpeed, float interactionRange, Properties properties, Supplier<? extends EntityType<? extends Spear>> spearType) {
        super(NarakaToolMaterials.applySpearProperties(properties, material, enchantable, attackDamage, attackSpeed, interactionRange));
        this.spearType = spearType;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity livingEntity) {
        return 72000;
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (getUseDuration(stack, livingEntity) - timeCharged >= TridentItem.THROW_THRESHOLD_TIME) {
            throwSpear(level, livingEntity, stack);
            return true;
        }
        return false;
    }

    protected void throwSpear(Level level, LivingEntity livingEntity, ItemStack stack) {
        if (level.isClientSide)
            return;
        if (livingEntity instanceof Player player) {
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(livingEntity.getUsedItemHand()));
            Spear spear = createSpear(level, player, stack);
            spear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2.5f, 1);
            if (player.hasInfiniteMaterials())
                spear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

            level.addFreshEntity(spear);
            level.playSound(null, spear, SoundEvents.TRIDENT_THROW.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!player.hasInfiniteMaterials())
                player.getInventory().removeItem(stack);
        }
    }

    protected Spear createSpear(Level level, LivingEntity owner, ItemStack stack) {
        return new Spear(spearType.get(), level, owner, stack);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (state.getDestroySpeed(level, pos) != 0)
            stack.hurtAndBreak(2, miningEntity, EquipmentSlot.MAINHAND);
        return true;
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack stack, Direction direction) {
        return new Spear(spearType.get(), level, position, stack);
    }
}