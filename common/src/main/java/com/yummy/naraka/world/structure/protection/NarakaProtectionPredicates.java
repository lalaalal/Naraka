package com.yummy.naraka.world.structure.protection;

import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.core.registries.RegistryProxyProvider;
import com.yummy.naraka.util.NarakaUtils;
import net.minecraft.core.Holder;

public class NarakaProtectionPredicates {
    public static final Holder<ProtectionPredicate> BOX = register(
            "box", (box, pos) -> true
    );

    public static final Holder<ProtectionPredicate> HEROBRINE_SANCTUARY_PROTECTION = register(
            "herobrine_sanctuary_protection", (box, pos) -> NarakaUtils.isInSphere(box, 0.8f, pos)
    );

    private static Holder<ProtectionPredicate> register(String name, ProtectionPredicate predicate) {
        return RegistryProxy.register(NarakaRegistries.Keys.PROTECTION_PREDICATE, name, () -> predicate);
    }

    public static void initialize() {
        RegistryProxyProvider.get(NarakaRegistries.Keys.PROTECTION_PREDICATE)
                .onRegistrationFinished();
    }
}
