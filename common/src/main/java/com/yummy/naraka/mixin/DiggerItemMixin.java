package com.yummy.naraka.mixin;

import com.yummy.naraka.tags.NarakaBlockTags;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DiggerItem.class)
public abstract class DiggerItemMixin extends TieredItem {
    public DiggerItemMixin(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Inject(method = "isCorrectToolForDrops", at = @At("HEAD"), cancellable = true)
    public void checkForNetherite(BlockState block, CallbackInfoReturnable<Boolean> cir) {
        int level = getTier().getLevel();
        if (level < 4 && block.is(NarakaBlockTags.NEEDS_NETHERITE_TOOL)) {
            cir.setReturnValue(false);
        }
    }
}
