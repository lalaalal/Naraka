package com.yummy.naraka.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.util.ComponentStyles;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class NarakaClientEvents {
    public static void initialize() {
        CoreShaderRegistrationCallback.EVENT.register(NarakaClientEvents::registerShaders);
        ClientTickEvents.START_CLIENT_TICK.register(NarakaClientEvents::onClientTick);
        ItemTooltipCallback.EVENT.register(NarakaClientEvents::addReinforcementToTooltip);
    }

    private static void registerShaders(CoreShaderRegistrationCallback.RegistrationContext context) throws IOException {
        context.register(NarakaMod.location("longinus"), DefaultVertexFormat.POSITION, shaderInstance -> {
            NarakaShaders.longinus = shaderInstance;
        });
    }

    private static void onClientTick(Minecraft minecraft) {
        ComponentStyles.tick();
    }

    private static void addReinforcementToTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, List<Component> lines) {
        if (lines.isEmpty())
            return;

        List<Component> additions = new ArrayList<>();
        TooltipProvider tooltipProvider = Reinforcement.get(stack);
        tooltipProvider.addToTooltip(tooltipContext, additions::add, tooltipFlag);

        int insertingIndex = Mth.clamp(1, 0, lines.size() - 1);
        if (insertingIndex > 0 && !additions.isEmpty() && !lines.get(insertingIndex).equals(CommonComponents.EMPTY))
            additions.add(CommonComponents.EMPTY);
        if (insertingIndex == 0)
            lines.addAll(additions);
        else
            lines.addAll(insertingIndex, additions);
    }
}
