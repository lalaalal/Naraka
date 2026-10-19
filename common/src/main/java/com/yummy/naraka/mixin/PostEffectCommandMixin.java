package com.yummy.naraka.mixin;

import com.yummy.naraka.core.posteffects.DynamicPostEffectType;
import com.yummy.naraka.network.NetworkManager;
import com.yummy.naraka.network.packet.PostEffectConfigPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.Identifier;
import net.minecraft.server.commands.PostEffectCommand;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(PostEffectCommand.class)
public abstract class PostEffectCommandMixin {
    @Inject(method = "addPostEffect", at = @At("HEAD"))
    private static void checkDynamicPostEffect(CommandSourceStack source, Collection<ServerPlayer> players, Identifier posteffect, CallbackInfoReturnable<Integer> cir) {
        DynamicPostEffectType.fromId(posteffect).ifPresent(type -> {
            NetworkManager.clientbound().send(players, new PostEffectConfigPacket(type.defaultConfig()));
        });
    }
}
