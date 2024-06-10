package com.yummy.naraka.client.renderer;

import com.yummy.naraka.tags.NarakaItemTags;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.world.entity.item.ItemEntity;

public class NarakaItemEntityRenderer extends ItemEntityRenderer {
    public NarakaItemEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(ItemEntity itemEntity, Frustum camera, double camX, double camY, double camZ) {
        if (itemEntity.getItem().is(NarakaItemTags.ALWAYS_RENDER_ITEM_ENTITY))
            return true;
        return super.shouldRender(itemEntity, camera, camX, camY, camZ);
    }
}
