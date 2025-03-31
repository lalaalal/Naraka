package com.yummy.naraka.mixin;

import com.yummy.naraka.world.block.HerobrineTotem;
import com.yummy.naraka.world.block.entity.HerobrineTotemBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlintAndSteelItem.class)
public abstract class FlintAndSteelItemMixin {
    @Inject(method = "useOn", at = @At("HEAD"))
    public void checkHerobrineTotemActivation(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);
        Direction direction = context.getClickedFace();
        if (direction == Direction.UP
                && blockState.is(Blocks.NETHERRACK)
                && HerobrineTotemBlockEntity.isTotemStructure(level, pos.below())
                && HerobrineTotemBlockEntity.isSanctuaryExists(level, pos)) {
            BlockState totem = level.getBlockState(pos.below());
            if (HerobrineTotemBlockEntity.isSleeping(totem))
                HerobrineTotem.crack(level, pos.below(), totem);
        }
    }
}
