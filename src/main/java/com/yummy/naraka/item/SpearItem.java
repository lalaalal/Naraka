package com.yummy.naraka.item;

import com.yummy.naraka.client.renderer.NarakaCustomRenderer;
import com.yummy.naraka.entity.Spear;

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
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SpearItem extends TieredItem implements ProjectileItem {
    protected static final Set<Enchantment> ENCHANTABLE = Set.of(
        Enchantments.MENDING, Enchantments.LOYALTY, Enchantments.UNBREAKING, Enchantments.IMPALING
    );

    protected static final UUID IMPALING_DAMAGE_BONUS_UUID = UUID.fromString("4e27a79e-1c1d-454f-9f33-df8362e356ca");

    protected final Supplier<EntityType<Spear>> spearType;
    private final ItemAttributeModifiers attributeModifiers;
    private final Map<Integer, ItemAttributeModifiers> cache = new HashMap<>();

    public SpearItem(Tier tier, Properties properties, Supplier<EntityType<Spear>> spearType) {
        super(tier, properties);
        this.spearType = spearType;
        attributeModifiers = ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon Modifier", tier.getAttackDamageBonus(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon Modifier", tier.getSpeed(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
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
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (getUseDuration(stack) - timeCharged >= TridentItem.THROW_THRESHOLD_TIME)
            throwSpear(level, livingEntity, stack);
    }

    protected void throwSpear(Level level, LivingEntity livingEntity, ItemStack stack) {
        if (livingEntity instanceof Player player) {
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(livingEntity.getUsedItemHand()));
            Spear spear = new Spear(spearType, level, player, stack);
            spear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2.5f, 1);
            if (player.hasInfiniteMaterials())
                spear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

            level.addFreshEntity(spear);
            level.playSound(null, spear, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!player.hasInfiniteMaterials()) {
                player.getInventory().removeItem(stack);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(ABSOLUTE_MAX_STACK_SIZE, attacker, EquipmentSlot.MAINHAND);
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
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return ENCHANTABLE.contains(enchantment);
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }

    @Override
    public ItemAttributeModifiers getAttributeModifiers(ItemStack stack) {
        int impalingLevel = stack.getEnchantmentLevel(Enchantments.IMPALING);
        if (cache.containsKey(impalingLevel))
            return cache.get(impalingLevel);
        ItemAttributeModifiers modifiersWithImpaling = attributeModifiers.withModifierAdded(
            Attributes.ATTACK_DAMAGE,
            new AttributeModifier(IMPALING_DAMAGE_BONUS_UUID, "Impaling Damage", impalingLevel, AttributeModifier.Operation.ADD_VALUE), 
            EquipmentSlotGroup.MAINHAND
        );
        cache.put(impalingLevel, modifiersWithImpaling);
        return modifiersWithImpaling;
    }
}