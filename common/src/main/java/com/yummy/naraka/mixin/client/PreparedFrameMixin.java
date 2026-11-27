package com.yummy.naraka.mixin.client;

import com.mojang.renderpearl.api.commands.RenderPass;
import com.yummy.naraka.client.renderer.RyoikiMaskExtension;
import com.yummy.naraka.client.renderer.feature.RyoikiMaskExecutor;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.feature.phase.FeatureRenderPhase;
import net.minecraft.client.renderer.feature.phase.SimpleFeatureRenderPhase;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Objects;

@Mixin(FeatureRenderDispatcher.PreparedFrame.class)
public abstract class PreparedFrameMixin implements RyoikiMaskExecutor {
    @Shadow
    private @Nullable FeatureFrameContext context;

    @Shadow
    private @Nullable SubmitNodeStorage submitNodeStorage;

    @Shadow
    protected abstract void executePhase(FeatureRenderPhase<?> phase, FeatureFrameContext context, RenderPass renderPass);

    @Override
    public void naraka$executeRyoikiMask(RenderPass renderPass) {
        FeatureFrameContext context = Objects.requireNonNull(this.context);
        SubmitNodeStorage submitNodeStorage = Objects.requireNonNull(this.submitNodeStorage);

        for (SubmitNodeCollection collection : submitNodeStorage.getSubmitsPerOrder().values()) {
            this.executePhase(naraka$getRyoikiMask(collection), context, renderPass);
        }
    }

    @SuppressWarnings("unchecked")
    @Unique
    private static SimpleFeatureRenderPhase naraka$getRyoikiMask(SubmitNodeCollection collection) {
        return ((RyoikiMaskExtension<SimpleFeatureRenderPhase>) collection).naraka$getRyoikiMask();
    }
}
