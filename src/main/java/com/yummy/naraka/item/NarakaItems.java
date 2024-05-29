package com.yummy.naraka.item;

import com.yummy.naraka.NarakaMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NarakaItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NarakaMod.MOD_ID);

    public static final DeferredItem<TestItem> TEST_ITEM = ITEMS.register("test_item", TestItem::new);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
