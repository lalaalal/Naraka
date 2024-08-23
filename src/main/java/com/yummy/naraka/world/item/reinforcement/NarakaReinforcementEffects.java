package com.yummy.naraka.world.item.reinforcement;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.NarakaRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class NarakaReinforcementEffects {
    public static final Holder<ReinforcementEffect> DAMAGE_INCREASE = register(
            "damage_increase", new DamageIncrease()
    );

    private static Holder<ReinforcementEffect> register(String name, ReinforcementEffect effect) {
        return Registry.registerForHolder(NarakaRegistries.REINFORCEMENT_EFFECT, NarakaMod.location(name), effect);
    }

    private static final Map<Item, HolderSet<ReinforcementEffect>> ITEM_REINFORCEMENT_EFFECTS = new HashMap<>();

    @SafeVarargs
    public static void add(Item item, Holder<ReinforcementEffect>... effects) {
        ITEM_REINFORCEMENT_EFFECTS.put(item, HolderSet.direct(effects));
    }

    public static HolderSet<ReinforcementEffect> get(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (ITEM_REINFORCEMENT_EFFECTS.containsKey(item))
            return ITEM_REINFORCEMENT_EFFECTS.get(item);
        return HolderSet.direct(DAMAGE_INCREASE);
    }

    public static void initialize() {

    }
}
