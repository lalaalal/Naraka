package com.yummy.naraka.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.yummy.naraka.client.NarakaModels;
import com.yummy.naraka.tags.NarakaItemTags;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
	@Final
	@Shadow
	private ItemModelShaper itemModelShaper;

	@Inject(method = "getModel", at = @At("HEAD"), cancellable = true)
	public void getModel(ItemStack itemStack, Level level,  LivingEntity livingEntity, int i, CallbackInfoReturnable<BakedModel> cir) {
		if (itemStack.is(NarakaItemTags.SPEAR)) {
			ModelResourceLocation modelLocation = NarakaModels.getInHandModel(itemStack);
			BakedModel model = itemModelShaper.getModelManager().getModel(modelLocation);
			cir.setReturnValue(model);
			cir.cancel();
		}
	}
}
