package com.yummy.naraka.world.item.alchemy;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.init.PotionBrewRecipeRegistry;
import com.yummy.naraka.world.effect.NarakaMobEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

import java.util.function.Supplier;

public class NarakaPotions {
    public static final Holder<Potion> GOD_BLESS = register("god_bless", () -> new Potion(
                    "god_bless",
                    new MobEffectInstance(NarakaMobEffects.GOD_BLESS, 3600, 255),
            new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3600),
            new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3600),
            new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 3600),
            new MobEffectInstance(MobEffects.DAMAGE_BOOST, 3600),
            new MobEffectInstance(MobEffects.JUMP, 3600),
                    new MobEffectInstance(MobEffects.REGENERATION, 3600),
                    new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 3600),
                    new MobEffectInstance(MobEffects.WATER_BREATHING, 3600),
                    new MobEffectInstance(MobEffects.INVISIBILITY, 3600),
                    new MobEffectInstance(MobEffects.NIGHT_VISION, 3600),
                    new MobEffectInstance(MobEffects.WEAKNESS, 3600),
                    new MobEffectInstance(MobEffects.POISON, 3600),
                    new MobEffectInstance(MobEffects.WITHER, 3600),
            new MobEffectInstance(MobEffects.DIG_SPEED, 3600),
            new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 3600),
                    new MobEffectInstance(MobEffects.LEVITATION, 3600),
                    new MobEffectInstance(MobEffects.GLOWING, 3600),
                    new MobEffectInstance(MobEffects.ABSORPTION, 3600),
                    new MobEffectInstance(MobEffects.HUNGER, 3600),
            new MobEffectInstance(MobEffects.CONFUSION, 3600),
            new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3600),
                    new MobEffectInstance(MobEffects.SLOW_FALLING, 3600),
                    new MobEffectInstance(MobEffects.CONDUIT_POWER, 3600),
                    new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 3600),
                    new MobEffectInstance(MobEffects.BLINDNESS, 3600),
                    new MobEffectInstance(MobEffects.BAD_OMEN, 3600),
                    new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 3600),
                    new MobEffectInstance(MobEffects.DARKNESS, 3600),
                    new MobEffectInstance(MobEffects.OOZING, 3600),
                    new MobEffectInstance(MobEffects.INFESTED, 3600),
                    new MobEffectInstance(MobEffects.WIND_CHARGED, 3600),
                    new MobEffectInstance(MobEffects.WEAVING, 3600),
                    new MobEffectInstance(MobEffects.TRIAL_OMEN, 3600),
                    new MobEffectInstance(MobEffects.RAID_OMEN, 3600)
            )
    );

    private static HolderProxy<Potion, Potion> register(String name, Supplier<Potion> potion) {
        return RegistryProxy.register(Registries.POTION, name, potion);
    }

    public static void initialize() {
        PotionBrewRecipeRegistry.register(NarakaPotionBrew::bootstrap);
    }
}
