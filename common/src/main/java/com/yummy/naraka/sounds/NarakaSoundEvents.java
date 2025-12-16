package com.yummy.naraka.sounds;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class NarakaSoundEvents {
    public static final Holder<SoundEvent> HEROBRINE_PHASE_1 = register("music.herobrine_phase_1");
    public static final Holder<SoundEvent> HEROBRINE_PHASE_2 = register("music.herobrine_phase_2");
    public static final Holder<SoundEvent> HEROBRINE_PHASE_3 = register("music.herobrine_phase_3");
    public static final Holder<SoundEvent> HEROBRINE_PHASE_4 = register("music.herobrine_phase_4");

    public static final Holder<SoundEvent> DIAMOND_GOLEM_IDLE = register("diamond_golem.idle");
    public static final Holder<SoundEvent> DIAMOND_GOLEM_BASIC = register("diamond_golem.basic");
    public static final Holder<SoundEvent> DIAMOND_GOLEM_ATTACK = register("diamond_golem.attack");
    public static final Holder<SoundEvent> DIAMOND_GOLEM_DEATH = register("diamond_golem.death");
    public static final Holder<SoundEvent> DIAMOND_GOLEM_HURT = register("diamond_golem.hurt");
    public static final Holder<SoundEvent> DIAMOND_GOLEM_STRONG = register("diamond_golem.strong");
    public static final Holder<SoundEvent> DIAMOND_GOLEM_SWIPE = register("diamond_golem.swipe");

    private static Holder<SoundEvent> register(String name) {
        Identifier location = NarakaMod.location(name);
        return RegistryProxy.register(Registries.SOUND_EVENT, name, () -> SoundEvent.createVariableRangeEvent(location));
    }

    public static void initialize() {

    }
}
