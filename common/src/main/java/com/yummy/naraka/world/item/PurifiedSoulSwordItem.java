package com.yummy.naraka.world.item;

import com.yummy.naraka.world.block.NarakaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class PurifiedSoulSwordItem extends Item {
    public PurifiedSoulSwordItem(ToolMaterial material, float attackDamage, float attackSpeed, Properties properties) {
        super(NarakaToolMaterials.applyWeaponPropertiesWithoutEnchantable(properties, material, attackDamage, attackSpeed));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if (state.isFaceSturdy(level, pos, Direction.UP)) {
            level.setBlock(pos.above(), NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.get().defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }
}
