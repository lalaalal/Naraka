package com.yummy.naraka.fabric.mixin;

import com.yummy.naraka.world.item.PickRangeModifiable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Shadow
    @Final
    public static double MAX_INTERACTION_DISTANCE;
    @Shadow
    public ServerPlayer player;

    @Redirect(method = "handleInteract", at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;MAX_INTERACTION_DISTANCE:D", opcode = Opcodes.GETSTATIC))
    private double modifyMaxInteractionDistance() {
        ItemStack itemStack = player.getMainHandItem();
        if (itemStack.getItem() instanceof PickRangeModifiable pickRangeModifiable)
            return Mth.square(pickRangeModifiable.getPickRange());
        return MAX_INTERACTION_DISTANCE;
    }
}
