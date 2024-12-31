package com.yummy.naraka.sounds;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.init.RegistryInitializer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class NarakaSoundEvents {
    public static final Holder<SoundEvent> HEROBRINE_PHASE_1 = register("herobrine_phase_1");
    public static final Holder<SoundEvent> HEROBRINE_PHASE_2 = register("herobrine_phase_2");
    public static final Holder<SoundEvent> HEROBRINE_PHASE_3 = register("herobrine_phase_3");
    public static final Holder<SoundEvent> HEROBRINE_PHASE_4 = register("herobrine_phase_4");

    private static Holder<SoundEvent> register(String name) {
        ResourceLocation location = NarakaMod.location(name);
        return RegistryProxy.register(Registries.SOUND_EVENT, name, () -> SoundEvent.createVariableRangeEvent(location));
    }

    public static void initialize() {
        RegistryInitializer.get(Registries.SOUND_EVENT)
                .onRegistrationFinished();
    }
}
