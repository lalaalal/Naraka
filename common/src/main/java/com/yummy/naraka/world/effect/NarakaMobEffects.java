package com.yummy.naraka.world.effect;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryWriter;
import com.yummy.naraka.mixin.invoker.MobEffectInvoker;
import com.yummy.naraka.tags.NarakaMobEffectTags;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;
import java.util.function.Supplier;

public class NarakaMobEffects {
    public static final HolderProxy<MobEffect, MobEffect> CHALLENGERS_BLESSING_AMETHYST = register(
            "challengers_blessing_amethyst",
            () -> MobEffectInvoker.create(MobEffectCategory.NEUTRAL, SoulType.AMETHYST.color)
    );

    public static final HolderProxy<MobEffect, MobEffect> CHALLENGERS_BLESSING_COPPER = register(
            "challengers_blessing_copper",
            () -> MobEffectInvoker.create(MobEffectCategory.NEUTRAL, SoulType.COPPER.color)
    );

    public static final HolderProxy<MobEffect, MobEffect> CHALLENGERS_BLESSING_DIAMOND = register(
            "challengers_blessing_diamond",
            () -> MobEffectInvoker.create(MobEffectCategory.NEUTRAL, SoulType.DIAMOND.color)
    );

    public static final HolderProxy<MobEffect, MobEffect> CHALLENGERS_BLESSING_EMERALD = register(
            "challengers_blessing_emerald",
            () -> MobEffectInvoker.create(MobEffectCategory.NEUTRAL, SoulType.EMERALD.color)
    );

    public static final HolderProxy<MobEffect, MobEffect> CHALLENGERS_BLESSING_GOLD = register(
            "challengers_blessing_gold",
            () -> MobEffectInvoker.create(MobEffectCategory.NEUTRAL, SoulType.GOLD.color)
    );

    public static final HolderProxy<MobEffect, MobEffect> CHALLENGERS_BLESSING_LAPIS = register(
            "challengers_blessing_lapis",
            () -> MobEffectInvoker.create(MobEffectCategory.NEUTRAL, SoulType.LAPIS.color)
    );

    public static final HolderProxy<MobEffect, MobEffect> CHALLENGERS_BLESSING_NECTARIUM = register(
            "challengers_blessing_nectarium",
            () -> MobEffectInvoker.create(MobEffectCategory.NEUTRAL, SoulType.NECTARIUM.color)
    );

    public static final HolderProxy<MobEffect, MobEffect> CHALLENGERS_BLESSING_REDSTONE = register(
            "challengers_blessing_redstone",
            () -> MobEffectInvoker.create(MobEffectCategory.NEUTRAL, SoulType.REDSTONE.color)
    );

    public static final HolderProxy<MobEffect, MobEffect> GOD_BLESS = register(
            "god_bless",
            () -> MobEffectInvoker.create(MobEffectCategory.BENEFICIAL, 0)
    );

    public static Optional<MobEffectInstance> getChallengersBlessing(LivingEntity livingEntity) {
        for (MobEffectInstance instance : livingEntity.getActiveEffects()) {
            MobEffect effect = instance.getEffect();
            boolean result = BuiltInRegistries.MOB_EFFECT.getResourceKey(effect)
                    .flatMap(BuiltInRegistries.MOB_EFFECT::getHolder)
                    .map(holder -> holder.is(NarakaMobEffectTags.CHALLENGERS_BLESSING))
                    .orElse(false);
            if (result)
                return Optional.of(instance);
        }
        return Optional.empty();
    }

    public static void initialize() {

    }

    private static HolderProxy<MobEffect, MobEffect> register(String name, Supplier<MobEffect> effect) {
        return RegistryWriter.register(Registries.MOB_EFFECT, name, effect);
    }
}
