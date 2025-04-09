package com.yummy.naraka.advancements;

import com.yummy.naraka.advancements.criterion.FillSoulStabilizerTrigger;
import com.yummy.naraka.advancements.criterion.SimpleTrigger;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.core.registries.RegistryProxyProvider;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;

import java.util.function.Supplier;

public class NarakaCriteriaTriggers {
    public static final HolderProxy<CriterionTrigger<?>, FillSoulStabilizerTrigger> FILL_SOUL_STABILIZER = register("fill_soul_stabilizer", FillSoulStabilizerTrigger::new);
    public static final HolderProxy<CriterionTrigger<?>, SimpleTrigger> SIMPLE_TRIGGER = register("challengers_blessing", SimpleTrigger::new);

    public static <T extends CriterionTrigger<?>> HolderProxy<CriterionTrigger<?>, T> register(String name, Supplier<T> value) {
        return RegistryProxy.register(Registries.TRIGGER_TYPE, name, value);
    }

    public static void initialize() {
        RegistryProxyProvider.get(Registries.TRIGGER_TYPE)
                .onRegistrationFinished();
    }
}
