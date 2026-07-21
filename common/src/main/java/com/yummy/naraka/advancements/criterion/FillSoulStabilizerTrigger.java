package com.yummy.naraka.advancements.criterion;

import com.google.gson.JsonObject;
import com.yummy.naraka.NarakaMod;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;
import java.util.function.Predicate;

public class FillSoulStabilizerTrigger extends SimpleCriterionTrigger<FillSoulStabilizerTrigger.TriggerInstance> {
    public static final ResourceLocation ID = NarakaMod.location("fill_soul_stabilizer");

    public void trigger(ServerPlayer player, boolean full) {
        this.trigger(player, triggerInstance -> triggerInstance.test(full));
    }

    @Override
    protected TriggerInstance createInstance(JsonObject json, ContextAwarePredicate predicate, DeserializationContext deserializationContext) {
        return new TriggerInstance(predicate, deserializeCheckFull(json));
    }

    private static boolean deserializeCheckFull(JsonObject json) {
        if (json.has("checkFull"))
            return json.get("checkFull").getAsBoolean();
        return false;
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance implements Predicate<Boolean> {
        private final ContextAwarePredicate player;
        private final boolean checkFull;

        public TriggerInstance(ContextAwarePredicate player, boolean checkFull) {
            super(ID, player);
            this.player = player;
            this.checkFull = checkFull;
        }

        public static TriggerInstance filled() {
            return new TriggerInstance(ContextAwarePredicate.ANY, false);
        }

        public static TriggerInstance fullFilled() {
            return new TriggerInstance(ContextAwarePredicate.ANY, true);
        }

        @Override
        public boolean test(Boolean full) {
            if (checkFull)
                return full;
            return true;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject root = super.serializeToJson(context);
            root.addProperty("checkFull", checkFull);
            return root;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (TriggerInstance) obj;
            return Objects.equals(this.player, that.player) &&
                    this.checkFull == that.checkFull;
        }

        @Override
        public int hashCode() {
            return Objects.hash(player, checkFull);
        }

        @Override
        public String toString() {
            return "TriggerInstance[" +
                    "player=" + player + ", " +
                    "checkFull=" + checkFull + ']';
        }

    }
}
