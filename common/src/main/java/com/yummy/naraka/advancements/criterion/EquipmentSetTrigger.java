package com.yummy.naraka.advancements.criterion;

import com.google.gson.JsonObject;
import com.yummy.naraka.NarakaMod;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class EquipmentSetTrigger extends SimpleCriterionTrigger<EquipmentSetTrigger.TriggerInstance> {
    public static final ResourceLocation ID = NarakaMod.location("equipment_set");

    public void trigger(ServerPlayer player, ResourceLocation id, long requirement) {
        this.trigger(player, triggerInstance -> triggerInstance.test(id, requirement));
    }

    @Override
    protected TriggerInstance createInstance(JsonObject json, ContextAwarePredicate predicate, DeserializationContext deserializationContext) {
        String equipmentSetId = json.get("equipmentSetId").getAsString();
        long requirement = json.get("requirement").getAsLong();
        return new TriggerInstance(predicate, Objects.requireNonNullElse(ResourceLocation.tryParse(equipmentSetId), NarakaMod.location("empty")), requirement);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static final class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final ContextAwarePredicate player;
        private final ResourceLocation equipmentSetId;
        private final long requirement;

        public TriggerInstance(ContextAwarePredicate player, ResourceLocation equipmentSetId, long requirement) {
            super(ID, player);
            this.player = player;
            this.equipmentSetId = equipmentSetId;
            this.requirement = requirement;
        }

        public static TriggerInstance equipmentSet(ResourceLocation equipmentSetId, long requirement) {
            return new TriggerInstance(ContextAwarePredicate.ANY, equipmentSetId, requirement);
        }

        public boolean test(ResourceLocation equipmentSetId, long succeed) {
            return this.equipmentSetId.equals(equipmentSetId) && this.requirement == succeed;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject root = super.serializeToJson(context);
            root.addProperty("equipmentSetId", this.equipmentSetId.toString());
            root.addProperty("requirement", this.requirement);
            return root;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (TriggerInstance) obj;
            return Objects.equals(this.player, that.player) &&
                    Objects.equals(this.equipmentSetId, that.equipmentSetId) &&
                    this.requirement == that.requirement;
        }

        @Override
        public int hashCode() {
            return Objects.hash(player, equipmentSetId, requirement);
        }

        @Override
        public String toString() {
            return "TriggerInstance[" +
                    "player=" + player + ", " +
                    "equipmentSetId=" + equipmentSetId + ", " +
                    "requirement=" + requirement + ']';
        }
    }
}
