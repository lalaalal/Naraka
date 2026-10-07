package com.yummy.naraka.mixin.client;

import com.yummy.naraka.tags.NarakaItemTags;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin extends EntityRenderer<ItemEntity, ItemEntityRenderState> {
    protected ItemEntityRendererMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(ItemEntity entity, Frustum culler, double camX, double camY, double camZ, float partialTicks) {
        return entity.getItem().is(NarakaItemTags.ALWAYS_RENDER_ITEM_ENTITY) || super.shouldRender(entity, culler, camX, camY, camZ, partialTicks);
    }
}
