package com.yummy.naraka.advancements.criterion;

import com.google.gson.JsonObject;
import com.yummy.naraka.NarakaMod;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class SimpleTrigger extends SimpleCriterionTrigger<SimpleTrigger.TriggerInstance> {
    public static final String CHALLENGERS_BLESSING = "challengers_blessing";
    public static final String ACTIVATE_NECTARIUM_CORE = "activate_nectarium_core";

    public static final ResourceLocation ID = NarakaMod.location("simple_trigger");

    public void trigger(ServerPlayer player, String name) {
        super.trigger(player, instance -> instance.test(name));
    }

    @Override
    protected TriggerInstance createInstance(JsonObject json, ContextAwarePredicate predicate, DeserializationContext deserializationContext) {
        return new TriggerInstance(predicate, deserializeName(json));
    }

    private static String deserializeName(JsonObject json) {
        if (json.has("name"))
            return json.get("name").getAsString();
        return "empty";
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final ContextAwarePredicate player;
        private final String name;

        public TriggerInstance(ContextAwarePredicate player, String name) {
            super(ID, player);
            this.player = player;
            this.name = name;
        }

        public static TriggerInstance simple(String name) {
            return new TriggerInstance(ContextAwarePredicate.ANY, name);
        }

        public boolean test(String name) {
            return name.equals(this.name);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject root = super.serializeToJson(context);
            root.addProperty("name", this.name);
            return root;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (TriggerInstance) obj;
            return Objects.equals(this.player, that.player) &&
                    Objects.equals(this.name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(player, name);
        }

        @Override
        public String toString() {
            return "TriggerInstance[" +
                    "player=" + player + ", " +
                    "name=" + name + ']';
        }

    }
}
