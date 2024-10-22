package com.yummy.naraka.world.structure.protection;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.structure.piece.HerobrineSanctuaryOutline;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;

public class NarakaProtectionPredicates {
    public static final Holder<ProtectionPredicate> BOX = register(
            "box", (box, pos) -> true
    );

    public static final Holder<ProtectionPredicate> HEROBRINE_SANCTUARY_PROTECTION = register(
            "herobrine_sanctuary_protection", (box, pos) -> NarakaUtils.isInSphere(box, HerobrineSanctuaryOutline.SPHERE_SIZE, pos)
    );

    private static Holder<ProtectionPredicate> register(String name, ProtectionPredicate predicate) {
        return Registry.registerForHolder(NarakaRegistries.PROTECTION_PREDICATE, NarakaMod.location(name), predicate);
    }

    public static void initialize() {

    }
}
