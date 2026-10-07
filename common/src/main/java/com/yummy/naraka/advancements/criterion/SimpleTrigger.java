package com.yummy.naraka.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public class SimpleTrigger extends SimpleCriterionTrigger<SimpleTrigger.TriggerInstance> {
    public static final String CHALLENGERS_BLESSING = "challengers_blessing";
    public static final String ACTIVATE_NECTARIUM_CORE = "activate_nectarium_core";

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, String name) {
        super.trigger(player, instance -> instance.test(name));
    }

    public record TriggerInstance(Optional<Holder<LootItemCondition>> player, String name) implements SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                        Codec.STRING.fieldOf("name").forGetter(TriggerInstance::name)
                ).apply(instance, instance.stable(TriggerInstance::new))
        );

        public static Criterion<TriggerInstance> simple(String name) {
            return NarakaCriteriaTriggers.SIMPLE_TRIGGER.get().createCriterion(
                    new TriggerInstance(Optional.empty(), name)
            );
        }

        public boolean test(String name) {
            return name.equals(this.name);
        }
    }
}
