package com.yummy.naraka.sounds;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class NarakaSoundEvents {
    public static final Holder<SoundEvent> HEROBRINE_PHASE_1 = registerForHolder("herobrine_phase_1");
    public static final Holder<SoundEvent> HEROBRINE_PHASE_2 = registerForHolder("herobrine_phase_2");
    public static final Holder<SoundEvent> HEROBRINE_PHASE_3 = registerForHolder("herobrine_phase_3");
    public static final Holder<SoundEvent> HEROBRINE_PHASE_4 = registerForHolder("herobrine_phase_4");

    private static SoundEvent register(String name) {
        ResourceLocation location = NarakaMod.location(name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, location, SoundEvent.createVariableRangeEvent(location));
    }

    private static Holder<SoundEvent> registerForHolder(String name) {
        ResourceLocation location = NarakaMod.location(name);
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, location, SoundEvent.createVariableRangeEvent(location));
    }

    public static void initialize() {

    }
}
