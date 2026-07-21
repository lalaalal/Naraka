package com.yummy.naraka.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.advancements.NarakaCriteriaTriggers;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class EquipmentSetTrigger extends SimpleCriterionTrigger<EquipmentSetTrigger.TriggerInstance> {
    public void trigger(ServerPlayer player, ResourceLocation id, long requirement) {
        this.trigger(player, triggerInstance -> triggerInstance.test(id, requirement));
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, ResourceLocation equipmentSetId,
                                  long requirement) implements SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                        ResourceLocation.CODEC.fieldOf("equipment_set_id").forGetter(TriggerInstance::equipmentSetId),
                        Codec.LONG.fieldOf("requirement").forGetter(TriggerInstance::requirement)
                ).apply(instance, instance.stable(TriggerInstance::new))
        );

        public static Criterion<TriggerInstance> equipmentSet(ResourceLocation equipmentSetId, long requirement) {
            return NarakaCriteriaTriggers.EQUIPMENT_SET.get().createCriterion(
                    new TriggerInstance(Optional.empty(), equipmentSetId, requirement)
            );
        }

        public boolean test(ResourceLocation equipmentSetId, long succeed) {
            return this.equipmentSetId.equals(equipmentSetId) && this.requirement == succeed;
        }
    }
}
