package com.yummy.naraka.world.item.equipmentset;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.component.NarakaDataComponentTypes;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.event.ItemEvents;
import com.yummy.naraka.world.entity.data.EntityDataHelper;
import com.yummy.naraka.world.entity.data.NarakaEntityDataTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class EquipmentSet implements ItemEvents.ItemTooltip {
    public static final Codec<EquipmentSet> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(EquipmentSet::getId),
                    Requirement.CODEC.listOf().fieldOf("requirements").forGetter(set -> set.requirements),
                    Effect.CODEC.listOf().fieldOf("effects").forGetter(set -> set.effects)
            ).apply(instance, EquipmentSet::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, EquipmentSet> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            EquipmentSet::getId,
            Requirement.STREAM_CODEC.apply(ByteBufCodecs.list()),
            set -> set.requirements,
            Effect.STREAM_CODEC.apply(ByteBufCodecs.list()),
            set -> set.effects,
            EquipmentSet::new
    );

    private final Identifier id;
    private final List<Requirement> requirements;
    private final List<Effect> effects;

    public EquipmentSet(Identifier id, List<Requirement> requirements, List<Effect> effects) {
        this.id = id;
        this.requirements = requirements;
        this.effects = effects;
    }

    public static EquipmentSet empty() {
        return new EquipmentSet(NarakaMod.identifier("empty"), List.of(), List.of());
    }

    public Identifier getId() {
        return id;
    }

    private long countSucceed(LivingEntity entity) {
        return requirements.stream().filter(requirement -> requirement.test(entity, this)).count();
    }

    public void updateEffect(LivingEntity livingEntity) {
        Set<EquipmentSet> activeEquipmentSets = new HashSet<>(EntityDataHelper.getRawEntityData(livingEntity, NarakaEntityDataTypes.EQUIPMENT_SET.get()));
        long succeed = countSucceed(livingEntity);
        long activated = effects.stream().filter(effect -> effect.update(livingEntity, succeed))
                .count();
        if (activated > 0) {
            activeEquipmentSets.add(this);
        } else {
            activeEquipmentSets.remove(this);
        }
        EntityDataHelper.setEntityData(livingEntity, NarakaEntityDataTypes.EQUIPMENT_SET.get(), activeEquipmentSets.stream().toList());
    }

    @Override
    public void addToTooltip(DataComponentHolder item, Item.TooltipContext context, Player player, TooltipFlag tooltipFlag, Consumer<Component> builder) {
        long succeed = countSucceed(player);
        effects.stream().sorted().forEach(effect -> effect.addToTooltip(id, succeed, builder));
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof EquipmentSet that))
            return false;
        return id.equals(that.id) && requirements.equals(that.requirements) && effects.equals(that.effects);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + requirements.hashCode();
        result = 31 * result + effects.hashCode();
        return result;
    }

    public record Requirement(Holder<Item> item, EquipmentSlot slot, DataComponentPatch components) {
        public static final Codec<Requirement> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("item").forGetter(Requirement::item),
                        EquipmentSlot.CODEC.fieldOf("slot").forGetter(Requirement::slot),
                        DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(Requirement::components)
                ).apply(instance, Requirement::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, Requirement> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.holderRegistry(Registries.ITEM),
                Requirement::item,
                EquipmentSlot.STREAM_CODEC,
                Requirement::slot,
                DataComponentPatch.STREAM_CODEC,
                Requirement::components,
                Requirement::new
        );

        public boolean test(LivingEntity livingEntity, EquipmentSet equipmentSet) {
            ItemStack itemStack = livingEntity.getItemBySlot(slot);
            List<EquipmentSet> equipmentSets = itemStack.getOrDefault(NarakaDataComponentTypes.EQUIPMENT_SET.get(), List.of());
            return itemStack.is(item.value())
                    && equipmentSets.stream().map(EquipmentSet::getId).anyMatch(id -> equipmentSet.getId().equals(id))
                    && components.entrySet().stream().allMatch(entry -> {
                DataComponentType<?> type = entry.getKey();
                return entry.getValue()
                        .filter(value -> Objects.equals(itemStack.get(type), value))
                        .isPresent();
            });
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Requirement(
                    Holder<Item> otherItem, EquipmentSlot otherSlot, DataComponentPatch otherComponents
            )))
                return false;
            return item.equals(otherItem) && slot == otherSlot && components.equals(otherComponents);
        }

        @Override
        public int hashCode() {
            int result = item.hashCode();
            result = 31 * result + slot.hashCode();
            result = 31 * result + components.hashCode();
            return result;
        }
    }

    public record Effect(int require,
                         Map<EquipmentSetEffect.Type<?>, EquipmentSetEffect> effects) implements Comparable<Effect> {
        public static final Codec<Effect> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Codec.INT.fieldOf("require").forGetter(Effect::require),
                        EquipmentSetEffect.MULTIPLE_CODEC.optionalFieldOf("effects", Map.of()).forGetter(Effect::effects)
                ).apply(instance, Effect::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, Effect> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT,
                Effect::require,
                EquipmentSetEffect.MULTIPLE_STREAM_CODEC,
                Effect::effects,
                Effect::new
        );

        public static Effect of(int require, EquipmentSetEffect... effects) {
            Map<EquipmentSetEffect.Type<?>, EquipmentSetEffect> result = new HashMap<>();
            for (EquipmentSetEffect effect : effects)
                result.put(effect.type(), effect);
            return new Effect(require, result);
        }

        public boolean canActivate(long succeed) {
            return succeed >= require;
        }

        public boolean update(LivingEntity livingEntity, long succeed) {
            if (canActivate(succeed)) {
                effects.values().forEach(effect -> effect.activate(livingEntity));
                return true;
            }
            effects.values().forEach(effect -> effect.deactivate(livingEntity));
            return false;
        }

        public void addToTooltip(Identifier id, long succeed, Consumer<Component> builder) {
            Component head = Component.translatable(LanguageKey.equipmentSet(id))
                    .withStyle(styleUpdater(succeed, ChatFormatting.GREEN))
                    .append(" (%d/%d)".formatted(Math.min(succeed, require), require));
            builder.accept(head);

            for (EquipmentSetEffect effect : effects.values()) {
                for (Component component : effect.getDescriptions()) {
                    Component body = Component.literal(" ").append(
                            component.copy()
                                    .withStyle(styleUpdater(succeed, ChatFormatting.WHITE))
                    );
                    builder.accept(body);
                }
            }
        }

        private UnaryOperator<Style> styleUpdater(long succeed, ChatFormatting succeedFormat) {
            if (succeed >= require)
                return style -> style.withColor(succeedFormat);
            return style -> style.withColor(ChatFormatting.DARK_GRAY);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Effect(
                    int otherRequire, Map<EquipmentSetEffect.Type<?>, EquipmentSetEffect> otherEffects
            )))
                return false;
            return require == otherRequire && effects.equals(otherEffects);
        }

        @Override
        public int hashCode() {
            int result = require;
            result = 31 * result + effects.hashCode();
            return result;
        }

        @Override
        public int compareTo(EquipmentSet.Effect o) {
            return require - o.require;
        }
    }
}