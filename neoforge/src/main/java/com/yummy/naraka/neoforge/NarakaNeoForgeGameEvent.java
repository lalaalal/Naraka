package com.yummy.naraka.neoforge;

import com.yummy.naraka.NarakaMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = NarakaMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class NarakaNeoForgeGameEvent {
    @SubscribeEvent
    public static void click(PlayerInteractEvent.RightClickBlock event) {
    }
}
