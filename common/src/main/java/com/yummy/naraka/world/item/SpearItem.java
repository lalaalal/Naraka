package com.yummy.naraka.world.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.yummy.naraka.core.registries.ValueGetter;
import com.yummy.naraka.world.entity.Spear;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SpearItem extends TieredItem {
    protected final ValueGetter<? extends EntityType<? extends Spear>> spearType;
    private final boolean enchantable;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public SpearItem(Tier tier, boolean enchantable, float attackDamage, float attackSpeed, Properties properties, ValueGetter<? extends EntityType<? extends Spear>> spearType) {
        super(tier, properties);
        this.spearType = spearType;
        this.enchantable = enchantable;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return this.enchantable;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (getUseDuration(stack) - timeCharged >= TridentItem.THROW_THRESHOLD_TIME) {
            throwSpear(level, livingEntity, stack);
        }
    }

    protected void throwSpear(Level level, LivingEntity livingEntity, ItemStack stack) {
        if (level.isClientSide())
            return;
        if (livingEntity instanceof Player player) {
            stack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(LivingEntity.getEquipmentSlotForItem(stack)));
            Spear spear = createSpear(level, player, stack);
            spear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2.5f, 1);
            if (player.isCreative())
                spear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

            level.addFreshEntity(spear);
            level.playSound(null, spear, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!player.isCreative())
                player.getInventory().removeItem(stack);
        }
    }

    protected Spear createSpear(Level level, LivingEntity owner, ItemStack stack) {
        return new Spear(spearType.getConcreteValue(), level, owner, stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (state.getDestroySpeed(level, pos) != 0)
            stack.hurtAndBreak(2, miningEntity, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }
}