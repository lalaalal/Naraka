package com.yummy.naraka.sounds;

import com.yummy.naraka.NarakaMod;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class NarakaSoundEvents {
    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(NarakaMod.MOD_ID, Registries.SOUND_EVENT);

    public static final Holder<SoundEvent> HEROBRINE_PHASE_1 = registerForHolder("herobrine_phase_1");
    public static final Holder<SoundEvent> HEROBRINE_PHASE_2 = registerForHolder("herobrine_phase_2");
    public static final Holder<SoundEvent> HEROBRINE_PHASE_3 = registerForHolder("herobrine_phase_3");
    public static final Holder<SoundEvent> HEROBRINE_PHASE_4 = registerForHolder("herobrine_phase_4");

    private static Holder<SoundEvent> registerForHolder(String name) {
        ResourceLocation location = NarakaMod.location(name);
        return SOUND_EVENTS.register(location, () -> SoundEvent.createVariableRangeEvent(location));
    }

    public static void initialize() {
        SOUND_EVENTS.register();
    }
}
