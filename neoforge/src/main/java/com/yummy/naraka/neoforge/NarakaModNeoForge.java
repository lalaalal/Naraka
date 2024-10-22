package com.yummy.naraka.neoforge;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.NarakaRegisterProxy;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(NarakaMod.MOD_ID)
public final class NarakaModNeoForge {
    public NarakaModNeoForge(IEventBus modBus, ModContainer container) {
        ItemRegistry.INSTANCE.register();
//        ItemRegistry.INSTANCE.registerForHolder("test3", new Item(new Item.Properties()));

    }

    public static class ItemRegistry implements NarakaRegisterProxy<Item> {

        public static final ItemRegistry INSTANCE = new ItemRegistry();

        private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(NarakaMod.MOD_ID, Registries.ITEM);

        public static final RegistrySupplier<Item> TEST = ITEMS.register("test", () -> new Item(new Item.Properties()));

        @Override
        public Holder<Item> registerForHolder(String name, Item value) {
            return ITEMS.register(name, () -> value);
        }

        public void register() {
            ITEMS.register();
        }
    }
}
