package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.event.ClientEvents;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Unique
    @Nullable
    private ClientEvents.CameraSetup.Context naraka$context;

    @Shadow
    protected abstract void move(float forwards, float up, float right);

    @Shadow
    protected abstract void setRotation(float yRot, float xRot);

    @Shadow
    private @org.jspecify.annotations.Nullable Entity entity;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(CallbackInfo ci) {
        naraka$context = naraka$createContext();
    }

    @Unique
    private ClientEvents.CameraSetup.Context naraka$createContext() {
        return new ClientEvents.CameraSetup.Context() {
            @Override
            public Camera getCamera() {
                return naraka$self();
            }

            @Override
            public void move(float zoom, float dy, float dx) {
                CameraMixin.this.move(zoom, dy, dx);
            }

            @Override
            public void setRotation(float yRot, float xRot) {
                CameraMixin.this.setRotation(yRot, xRot);
            }
        };
    }

    @Inject(method = "update", at = @At("RETURN"))
    public void update(DeltaTracker deltaTracker, CallbackInfo ci) {
        if (naraka$context == null)
            naraka$context = naraka$createContext();
        if (entity != null)
            ClientEvents.CAMERA_SETUP.invoker().setup(naraka$context, entity, deltaTracker);
    }

    @Unique
    private Camera naraka$self() {
        return (Camera) (Object) this;
    }
}
