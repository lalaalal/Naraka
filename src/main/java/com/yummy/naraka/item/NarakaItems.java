package com.yummy.naraka.item;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.entity.NarakaEntities;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NarakaItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NarakaMod.MOD_ID);

    public static final DeferredItem<TestItem> TEST_ITEM = ITEMS.register("test_item", TestItem::new);
    public static final DeferredItem<SpearItem> SPEAR_ITEM = ITEMS.registerItem(
            "spear", properties -> new SpearItem(Tiers.IRON, properties, NarakaEntities.THROWN_SPEAR)
    );
    public static final DeferredItem<SpearItem> MIGHTY_HOLY_SPEAR_ITEM = ITEMS.registerItem(
            "mighty_holy_spear", properties -> new SpearItem(Tiers.NETHERITE, properties, NarakaEntities.THROWN_MIGHTY_HOLY_SPEAR)
    );

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
