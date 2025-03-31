package com.yummy.naraka.advancements;

import com.yummy.naraka.advancements.criterion.FillSoulStabilizerTrigger;
import com.yummy.naraka.advancements.criterion.SimpleTrigger;
import com.yummy.naraka.core.registries.LazyHolder;
import com.yummy.naraka.core.registries.RegistryInitializer;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;

import java.util.function.Supplier;

public class NarakaCriteriaTriggers {
    public static final LazyHolder<CriterionTrigger<?>, FillSoulStabilizerTrigger> FILL_SOUL_STABILIZER = register("fill_soul_stabilizer", FillSoulStabilizerTrigger::new);
    public static final LazyHolder<CriterionTrigger<?>, SimpleTrigger> SIMPLE_TRIGGER = register("challengers_blessing", SimpleTrigger::new);

    public static <T extends CriterionTrigger<?>> LazyHolder<CriterionTrigger<?>, T> register(String name, Supplier<T> value) {
        return RegistryProxy.register(Registries.TRIGGER_TYPE, name, value);
    }

    public static void initialize() {
        RegistryInitializer.get(Registries.TRIGGER_TYPE)
                .onRegistrationFinished();
    }
}
