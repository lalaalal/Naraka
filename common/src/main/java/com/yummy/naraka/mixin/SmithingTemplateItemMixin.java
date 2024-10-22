package com.yummy.naraka.mixin;

import com.yummy.naraka.world.item.NarakaSmithingTemplateItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SmithingTemplateItem.class)
public abstract class SmithingTemplateItemMixin {
    @Inject(method = "createTrimmableMaterialIconList", at = @At("HEAD"), cancellable = true)
    private static void createTrimmableMaterialIconList(CallbackInfoReturnable<List<ResourceLocation>> cir) {
        List<ResourceLocation> result = List.of(
                NarakaSmithingTemplateItems.EMPTY_SLOT_INGOT,
                NarakaSmithingTemplateItems.EMPTY_SLOT_REDSTONE_DUST,
                NarakaSmithingTemplateItems.EMPTY_SLOT_LAPIS_LAZULI,
                NarakaSmithingTemplateItems.EMPTY_SLOT_QUARTZ,
                NarakaSmithingTemplateItems.EMPTY_SLOT_DIAMOND,
                NarakaSmithingTemplateItems.EMPTY_SLOT_EMERALD,
                NarakaSmithingTemplateItems.EMPTY_SLOT_AMETHYST_SHARD,
                NarakaSmithingTemplateItems.EMPTY_SLOT_SOUL_INFUSED_REDSTONE,
                NarakaSmithingTemplateItems.EMPTY_SLOT_BEAD,
                NarakaSmithingTemplateItems.EMPTY_SLOT_NECTARIUM
        );
        cir.setReturnValue(result);
        cir.cancel();
    }
}
