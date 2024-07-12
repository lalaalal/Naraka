package com.yummy.naraka.util;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.NarakaRegistries;
import com.yummy.naraka.world.structure.HerobrineSanctuaryOutline;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NarakaProtectionPredicates {
    private static final DeferredRegister<ProtectionPredicate> PROTECTION_PREDICATES = DeferredRegister.create(NarakaRegistries.PROTECTION_PREDICATE, NarakaMod.MOD_ID);

    public static final DeferredHolder<ProtectionPredicate, ProtectionPredicate> BOX = PROTECTION_PREDICATES.register(
            "box",
            () -> (box, pos) -> true
    );

    public static final DeferredHolder<ProtectionPredicate, ProtectionPredicate> HEROBRINE_SANCTUARY_PROTECTION = PROTECTION_PREDICATES.register(
            "herobrine_sanctuary_protection",
            () -> (box, pos) -> NarakaUtils.isInSphere(box, HerobrineSanctuaryOutline.SPHERE_SIZE, pos)
    );

    public static void register(IEventBus bus) {
        PROTECTION_PREDICATES.register(bus);
    }
}
