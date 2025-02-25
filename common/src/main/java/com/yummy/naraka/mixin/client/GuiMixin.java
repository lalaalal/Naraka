package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class GuiMixin {
    @ModifyExpressionValue(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D"))
    public double modifyPlayerMaxHealthForDisplaying(double original) {
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return original;

        double lockedHealth = EntityDataHelper.getEntityData(player, NarakaEntityDataTypes.LOCKED_HEALTH.get());
        return original + lockedHealth;
    }
}
