package com.yummy.naraka.world.item;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.sounds.NarakaSoundEvents;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;

public class NarakaJukeboxSongs {
    public static final ResourceKey<JukeboxSong> HEROBRINE_PHASE_1 = create("herobrine_phase_1");
    public static final ResourceKey<JukeboxSong> HEROBRINE_PHASE_2 = create("herobrine_phase_2");
    public static final ResourceKey<JukeboxSong> HEROBRINE_PHASE_3 = create("herobrine_phase_3");
    public static final ResourceKey<JukeboxSong> HEROBRINE_PHASE_4 = create("herobrine_phase_4");

    public static void bootstrap(BootstrapContext<JukeboxSong> context) {
        register(context, HEROBRINE_PHASE_1, NarakaSoundEvents.HEROBRINE_PHASE_1, 120, 11);
        register(context, HEROBRINE_PHASE_2, NarakaSoundEvents.HEROBRINE_PHASE_2, 160, 12);
        register(context, HEROBRINE_PHASE_3, NarakaSoundEvents.HEROBRINE_PHASE_3, 120, 13);
        register(context, HEROBRINE_PHASE_4, NarakaSoundEvents.HEROBRINE_PHASE_4, 150, 10);
    }

    private static void register(BootstrapContext<JukeboxSong> context, ResourceKey<JukeboxSong> key, Holder<SoundEvent> sound, float lengthInSeconds, int comparatorOutput) {
        context.register(key, new JukeboxSong(sound, component(key), lengthInSeconds, comparatorOutput));
    }

    private static Component component(ResourceKey<JukeboxSong> key) {
        return Component.translatable(Util.makeDescriptionId("jukebox_song", key.location()));
    }

    private static ResourceKey<JukeboxSong> create(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, NarakaMod.location(name));
    }
}
