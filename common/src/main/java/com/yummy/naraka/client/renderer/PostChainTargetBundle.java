package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.yummy.naraka.NarakaMod;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class PostChainTargetBundle implements PostChain.TargetBundle {
    public static final Identifier RYOIKI_MASK = NarakaMod.identifier("ryoiki_mask");

    private ResourceHandle<RenderTarget> main;
    private ResourceHandle<RenderTarget> ryoikiMask;

    public PostChainTargetBundle(ResourceHandle<RenderTarget> main, ResourceHandle<RenderTarget> ryoikiMask) {
        this.main = main;
        this.ryoikiMask = ryoikiMask;
    }

    @Override
    public void replace(Identifier id, ResourceHandle<RenderTarget> handle) {
        if (id.equals(LevelTargetBundle.MAIN_TARGET_ID)) {
            main = handle;
        } else if (id.equals(RYOIKI_MASK)) {
            ryoikiMask = handle;
        }
    }

    @Override
    public @Nullable ResourceHandle<RenderTarget> get(Identifier id) {
        if (id.equals(LevelTargetBundle.MAIN_TARGET_ID)) {
            return main;
        } else if (id.equals(RYOIKI_MASK)) {
            return ryoikiMask;
        } else {
            return null;
        }
    }
}
