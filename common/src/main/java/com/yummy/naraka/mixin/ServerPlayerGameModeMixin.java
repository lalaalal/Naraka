package com.yummy.naraka.mixin;

import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.entity.ai.attribute.NarakaAttributeModifiers;
import com.yummy.naraka.world.item.PickRangeModifiable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin {
    @Shadow @Final
    protected ServerPlayer player;

    @SuppressWarnings("ConstantValue")
    @Inject(method = "setGameModeForPlayer", at = @At("RETURN"))
    public void updateReinforcementEffects(CallbackInfo ci) {
        if (player.connection != null)
            NarakaItemUtils.updateAllReinforcementEffects(player);
    }

    @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
    public void preventItemUseDuringStun(ServerPlayer player, Level level, ItemStack stack, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (NarakaAttributeModifiers.hasAttributeModifier(player, Attributes.MOVEMENT_SPEED, NarakaAttributeModifiers.STUN_PREVENT_MOVING)) {
            player.stopUsingItem();
            cir.cancel();
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }

    @Redirect(method = "handleBlockBreakAction", at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;MAX_INTERACTION_DISTANCE:D", opcode = Opcodes.GETSTATIC))
    private double modifyMaxInteractionDistance() {
        ItemStack itemStack = player.getMainHandItem();
        if (itemStack.getItem() instanceof PickRangeModifiable pickRangeModifiable)
            return Mth.square(pickRangeModifiable.getPickRange());
        return ServerGamePacketListenerImpl.MAX_INTERACTION_DISTANCE;
    }
}
