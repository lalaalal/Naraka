package com.yummy.naraka.sounds;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

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

    public static final Holder<SoundEvent> RYOIKI_TENKAI = register("ryoiki_tenkai");

    private static Holder<SoundEvent> register(String name) {
        ResourceLocation location = NarakaMod.location(name);
        return RegistryProxy.register(Registries.SOUND_EVENT, name, () -> SoundEvent.createVariableRangeEvent(location));
    }

    public static void initialize() {

    }

    public static void playHerobrineSwingSound(ServerLevel level, Vec3 position) {
        level.playSound(null, position.x(), position.y(), position.z(), SoundEvents.WITCH_THROW, SoundSource.HOSTILE, 0.7f, 1);
        level.playSound(null, position.x(), position.y(), position.z(), SoundEvents.WITCH_THROW, SoundSource.HOSTILE, 1, 1.5f);
        level.playSound(null, position.x(), position.y(), position.z(), SoundEvents.WITCH_THROW, SoundSource.HOSTILE, 1, 1.2f);
    }
}
