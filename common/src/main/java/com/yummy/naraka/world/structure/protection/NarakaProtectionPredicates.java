package com.yummy.naraka.world.structure.protection;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.structure.piece.HerobrineSanctuaryOutline;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Holder;

public class NarakaProtectionPredicates {
    private static final DeferredRegister<ProtectionPredicate> PROTECTION_PREDICATES = DeferredRegister.create(NarakaMod.MOD_ID, NarakaRegistries.Keys.PROTECTION_PREDICATE);

    public static final Holder<ProtectionPredicate> BOX = register(
            "box", (box, pos) -> true
    );

    public static final Holder<ProtectionPredicate> HEROBRINE_SANCTUARY_PROTECTION = register(
            "herobrine_sanctuary_protection", (box, pos) -> NarakaUtils.isInSphere(box, HerobrineSanctuaryOutline.SPHERE_SIZE, pos)
    );

    private static Holder<ProtectionPredicate> register(String name, ProtectionPredicate predicate) {
        return PROTECTION_PREDICATES.register(name, () -> predicate);
    }

    public static void initialize() {
        PROTECTION_PREDICATES.register();
    }
}
