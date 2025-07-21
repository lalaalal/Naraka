package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.world.entity.data.LockedHealthHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class GuiMixin {
    @Shadow
    @Nullable
    protected abstract Player getCameraPlayer();

    @Shadow
    private int tickCount;

    @ModifyExpressionValue(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D"))
    public double modifyMaxHealth(double original) {
        Player player = getCameraPlayer();
        if (player != null) {
            double maxHealth = player.getMaxHealth();
            double lockedHealth = LockedHealthHelper.get(player);
            return maxHealth + lockedHealth;
        }
        return original;
    }

    @ModifyArg(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderHearts(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V"), index = 5)
    public int modifyOffsetHeartIndex(int original) {
        Player player = getCameraPlayer();
        if (player != null && original != -1) {
            double heartCount = player.getMaxHealth() / 2;
            int offsetHeartIndex = this.tickCount % Mth.ceil(heartCount + 5.0F);
            if (offsetHeartIndex > heartCount)
                return -1;
            return offsetHeartIndex;
        }
        return original;
    }

    @ModifyArg(method = "renderHearts", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderHeart(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Gui$HeartType;IIZZZ)V"), index = 3)
    public int modifyRandomValue(int original, @Local(name = "y") int y, @Local(name = "height") int height, @Local(name = "m") int m, @Local(name = "l") int l) {
        Player player = getCameraPlayer();
        if (player != null) {
            int originalHeartCount = Mth.ceil(player.getMaxHealth() + LockedHealthHelper.get(player) / 2);
            int heartCount = Mth.ceil(player.getMaxHealth() / 2);
            if (heartCount <= l && l < originalHeartCount)
                return y - m * height;
            return original;
        }
        return original;
    }
}
