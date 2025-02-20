package com.yummy.naraka.client.renderer;

import com.yummy.naraka.world.entity.NarakaFireball;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class NarakaFireballRenderer extends EntityRenderer<NarakaFireball> {
    public NarakaFireballRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(NarakaFireball entity) {
        return null;
    }
}
