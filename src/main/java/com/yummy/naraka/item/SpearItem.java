package com.yummy.naraka.item;

import com.yummy.naraka.client.renderer.NarakaCustomRenderer;
import com.yummy.naraka.entity.Spear;
import com.yummy.naraka.entity.ai.attribute.NarakaAttributeModifiers;
import com.yummy.naraka.item.enchantment.NarakaEnchantments;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
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
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SpearItem extends TieredItem implements ProjectileItem {
    protected final Supplier<? extends EntityType<? extends Spear>> spearType;
    private final ItemAttributeModifiers defaultModifiers;
    private final Map<Integer, ItemAttributeModifiers> modifiersCache = new HashMap<>();

    public SpearItem(Tier tier, Properties properties, Supplier<? extends EntityType<? extends Spear>> spearType) {
        super(tier, properties);
        this.spearType = spearType;
        defaultModifiers = ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, tier.getAttackDamageBonus(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, tier.getSpeed(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
        modifiersCache.put(0, defaultModifiers);
    }

    @Override
    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity livingEntity) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (getUseDuration(stack, livingEntity) - timeCharged >= TridentItem.THROW_THRESHOLD_TIME)
            throwSpear(level, livingEntity, stack);
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
        return new Spear(spearType, level, owner, stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (state.getDestroySpeed(level, pos) != 0)
            stack.hurtAndBreak(2, miningEntity, EquipmentSlot.MAINHAND);
        return true;
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack stack, Direction direction) {
        return new Spear(spearType, level, position, stack);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return NarakaCustomRenderer.getInstance();
            }
        });
    }

    @Override
    public ItemAttributeModifiers getAttributeModifiers(ItemStack stack) {
        int level = stack.getEnchantmentLevel(NarakaEnchantments.get(Enchantments.IMPALING));
        if (!modifiersCache.containsKey(level)) {
            AttributeModifier modifier = NarakaAttributeModifiers.impaling(level);
            ItemAttributeModifiers modifiers = defaultModifiers.withModifierAdded(Attributes.ATTACK_DAMAGE, modifier, EquipmentSlotGroup.MAINHAND);
            modifiersCache.put(level, modifiers);
        }
        return modifiersCache.get(level);
    }
}