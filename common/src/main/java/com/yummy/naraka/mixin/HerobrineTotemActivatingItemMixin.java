package com.yummy.naraka.mixin;

import com.yummy.naraka.world.block.HerobrineTotem;
import com.yummy.naraka.world.block.entity.HerobrineTotemBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({FlintAndSteelItem.class, FireChargeItem.class})
public abstract class HerobrineTotemActivatingItemMixin {
    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    public void checkHerobrineTotemActivation(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        BlockState blockState = level.getBlockState(pos);
        BlockState totem = level.getBlockState(pos.below());
        if (blockState.is(Blocks.NETHERRACK)
                && HerobrineTotemBlockEntity.isTotemStructure(level, pos.below())
                && HerobrineTotemBlockEntity.isSanctuaryExists(level, pos)
                && HerobrineTotemBlockEntity.isSleeping(totem)
        ) {
            ItemStack itemStack = context.getItemInHand();
            if (player != null)
                itemStack.hurtAndBreak(1, player, context.getHand());
            if (itemStack.is(Items.FIRE_CHARGE))
                itemStack.shrink(1);

            level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1, level.getRandom().nextFloat() * 0.4F + 0.8F);
            level.setBlockAndUpdate(pos.above(), Blocks.FIRE.defaultBlockState());
            HerobrineTotem.crack(level, pos.below(), totem);
            level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
            cir.setReturnValue(InteractionResult.SUCCESS_SERVER);
            cir.cancel();
        }
    }
}
