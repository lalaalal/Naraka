package com.yummy.naraka.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public class EquipmentSetTrigger extends SimpleCriterionTrigger<EquipmentSetTrigger.TriggerInstance> {
    public void trigger(ServerPlayer player, Identifier identifier, long requirement) {
        this.trigger(player, triggerInstance -> triggerInstance.test(identifier, requirement));
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public record TriggerInstance(Optional<Holder<LootItemCondition>> player, Identifier equipmentSetId,
                                  long requirement) implements SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        LootItemCondition.CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                        Identifier.CODEC.fieldOf("equipment_set_id").forGetter(TriggerInstance::equipmentSetId),
                        Codec.LONG.fieldOf("requirement").forGetter(TriggerInstance::requirement)
                ).apply(instance, instance.stable(TriggerInstance::new))
        );

        public static Criterion<TriggerInstance> equipmentSet(Identifier equipmentSetId, long requirement) {
            return NarakaCriteriaTriggers.EQUIPMENT_SET.get().createCriterion(
                    new TriggerInstance(Optional.empty(), equipmentSetId, requirement)
            );
        }

        public boolean test(Identifier equipmentSetId, long succeed) {
            return this.equipmentSetId.equals(equipmentSetId) && this.requirement == succeed;
        }
    }
}
