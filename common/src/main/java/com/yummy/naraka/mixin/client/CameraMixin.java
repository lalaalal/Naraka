package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.event.ClientEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Camera.class)
public abstract class CameraMixin {
    @Unique @Nullable
    private ClientEvents.CameraSetup.Context naraka$context;

    @Shadow
    protected abstract void move(float zoom, float dy, float dx);

    @Shadow
    protected abstract void setRotation(float yRot, float xRot);

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

    @Inject(method = "setup", at = @At("RETURN"))
    public void setup(Level level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        if (naraka$context == null)
            naraka$context = naraka$createContext();
        ClientEvents.CAMERA_SETUP.invoker().setup(naraka$context, level, entity, detached, thirdPersonReverse, partialTick);
    }

    @Unique
    private Camera naraka$self() {
        return (Camera) (Object) this;
    }
}
