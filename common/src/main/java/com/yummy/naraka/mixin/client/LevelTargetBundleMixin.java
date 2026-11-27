package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.yummy.naraka.client.renderer.PostChainTargetBundle;
import com.yummy.naraka.client.renderer.RyoikiMaskExtension;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Mixin(LevelTargetBundle.class)
public abstract class LevelTargetBundleMixin implements RyoikiMaskExtension<ResourceHandle<RenderTarget>> {
    @Shadow
    @Final
    @Mutable
    public static Set<Identifier> MAIN_TARGETS;

    @Unique
    private ResourceHandle<RenderTarget> naraka$ryoikiMask = ResourceHandle.invalid();

    @Override
    public ResourceHandle<RenderTarget> naraka$getRyoikiMask() {
        return naraka$ryoikiMask;
    }

    @Override
    public void naraka$setRyoikiMask(ResourceHandle<RenderTarget> ryoikiMask) {
        this.naraka$ryoikiMask = ryoikiMask;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void addRyoikiMask(CallbackInfo ci) {
        Set<Identifier> expanded = new HashSet<>(MAIN_TARGETS);
        expanded.add(PostChainTargetBundle.RYOIKI_MASK);
        MAIN_TARGETS = Collections.unmodifiableSet(expanded);
    }

    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void getRyoikiMask(Identifier id, CallbackInfoReturnable<ResourceHandle<RenderTarget>> cir) {
        if (id.equals(PostChainTargetBundle.RYOIKI_MASK)) {
            cir.cancel();
            cir.setReturnValue(naraka$ryoikiMask);
        }
    }

    @Inject(method = "replace", at = @At("HEAD"), cancellable = true)
    private void replaceRyoikiMask(Identifier id, ResourceHandle<RenderTarget> handle, CallbackInfo ci) {
        if (id.equals(PostChainTargetBundle.RYOIKI_MASK)) {
            ci.cancel();
            this.naraka$ryoikiMask = handle;
        }
    }

    @Inject(method = "clear", at = @At("RETURN"))
    private void clearRyoikiMask(CallbackInfo ci) {
        this.naraka$ryoikiMask = ResourceHandle.invalid();
    }
}
