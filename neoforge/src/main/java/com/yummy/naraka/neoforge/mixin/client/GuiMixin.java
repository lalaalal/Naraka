package com.yummy.naraka.neoforge.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.client.gui.hud.LockedHealthHud;
import com.yummy.naraka.world.entity.data.LockedHealthHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@OnlyIn(Dist.CLIENT)
@Mixin(Gui.class)
public abstract class GuiMixin {
    @Shadow
    private int tickCount;

    @ModifyExpressionValue(method = "renderHealthLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D"))
    public double modifyMaxHealth(double original, @Local(name = "player") Player player) {
        return original + LockedHealthHelper.get(player);
    }

    @ModifyArg(method = "renderHealthLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderHearts(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V"), index = 5)
    public int modifyOffsetHeartIndex(int original, @Local(name = "player") Player player) {
        return LockedHealthHud.modifyOffsetHeartIndex(player, tickCount, original);
    }

    @ModifyArg(method = "renderHearts", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderHeart(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Gui$HeartType;IIZZZ)V"), index = 3)
    public int modifyHeartY(int original, @Local(name = "player") Player player, @Local(name = "y") int baseY, @Local(name = "height") int height, @Local(name = "i1") int lineCount, @Local(name = "l") int heartIndex) {
        return LockedHealthHud.modifyHeartY(player, original, baseY, height, lineCount, heartIndex);
    }
}
