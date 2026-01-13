package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.client.gui.hud.LockedHealthHud;
import com.yummy.naraka.world.entity.data.LockedHealthHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class GuiMixin {
    @Shadow
    private int tickCount;

    @SuppressWarnings("UnresolvedMixinReference")
    @ModifyExpressionValue(method = {"renderHealthLevel", "renderPlayerHealth"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D"), require = 1)
    public double modifyMaxHealth(double original, @Local Player player) {
        return original + LockedHealthHelper.get(player);
    }

    @SuppressWarnings("UnresolvedMixinReference")
    @ModifyArg(method = {"renderHealthLevel", "renderPlayerHealth"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderHearts(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V"), index = 5, require = 1)
    public int modifyOffsetHeartIndex(int original, @Local Player player) {
        return LockedHealthHud.modifyOffsetHeartIndex(player, tickCount, original);
    }

    @ModifyArg(method = "renderHearts", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderHeart(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Gui$HeartType;IIZZZ)V"), index = 3)
    public int modifyHeartY(int original, @Local(argsOnly = true) Player player, @Local(index = 4, argsOnly = true) int baseY, @Local(index = 5, argsOnly = true) int height, @Local(index = 17) int heartIndex, @Local(index = 18) int lineCount) {
        return LockedHealthHud.modifyHeartY(player, original, baseY, height, lineCount, heartIndex);
    }
}
