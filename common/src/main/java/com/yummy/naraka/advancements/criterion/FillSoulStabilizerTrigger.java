package com.yummy.naraka.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.advancements.NarakaCriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class FillSoulStabilizerTrigger extends SimpleCriterionTrigger<FillSoulStabilizerTrigger.TriggerInstance> {
    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, boolean full) {
        this.trigger(player, triggerInstance -> triggerInstance.test(full));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, boolean checkFull) implements SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
                                .forGetter(TriggerInstance::player),
                        Codec.BOOL.fieldOf("check_full").forGetter(TriggerInstance::checkFull)
                ).apply(instance, TriggerInstance::new)
        );

        public static Criterion<TriggerInstance> filled() {
            return NarakaCriteriaTriggers.FILL_SOUL_STABILIZER.get().createCriterion(
                    new TriggerInstance(Optional.empty(), false)
            );
        }

        public static Criterion<TriggerInstance> fullFilled() {
            return NarakaCriteriaTriggers.FILL_SOUL_STABILIZER.get().createCriterion(
                    new TriggerInstance(Optional.empty(), true)
            );
        }

        public boolean test(boolean full) {
            if (checkFull)
                return full;
            return true;
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return player;
        }
    }
}
