package com.yummy.naraka.world.item.equipmentset;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.advancements.NarakaCriteriaTriggers;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.event.ItemEvents;
import com.yummy.naraka.util.NarakaExtraCodecs;
import com.yummy.naraka.util.NarakaItemUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class EquipmentSet implements ItemEvents.ItemTooltip {
    public static final Codec<EquipmentSet> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(EquipmentSet::getId),
                    Requirement.CODEC.listOf().fieldOf("requirements").forGetter(set -> set.requirements),
                    Effect.CODEC.listOf().fieldOf("effects").forGetter(set -> set.effects)
            ).apply(instance, EquipmentSet::new)
    );

    private final ResourceLocation id;
    private final List<Requirement> requirements;
    private final List<Effect> effects;

    public EquipmentSet(ResourceLocation id, List<Requirement> requirements, List<Effect> effects) {
        this.id = id;
        this.requirements = requirements;
        this.effects = effects;
    }

    public static EquipmentSet empty() {
        return new EquipmentSet(NarakaMod.location("empty"), List.of(), List.of());
    }

    public ResourceLocation getId() {
        return id;
    }

    private long countSucceed(LivingEntity entity) {
        return requirements.stream().filter(requirement -> requirement.test(entity, this)).count();
    }

    public void updateEffect(LivingEntity livingEntity) {
        long succeed = countSucceed(livingEntity);
        for (Effect effect : effects)
            effect.update(livingEntity, succeed);
        if (livingEntity instanceof ServerPlayer player)
            NarakaCriteriaTriggers.EQUIPMENT_SET.trigger(player, id, succeed);
    }

    @Override
    public void addToTooltip(ItemStack itemStack, Player player, TooltipFlag tooltipFlag, Consumer<Component> builder) {
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

    public record Requirement(Holder<Item> item, EquipmentSlot slot, CompoundTag nbt) {
        public static final Codec<Requirement> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        BuiltInRegistries.ITEM.holderByNameCodec().fieldOf("item").forGetter(Requirement::item),
                        NarakaExtraCodecs.EQUIPMENT_SLOT.fieldOf("slot").forGetter(Requirement::slot),
                        CompoundTag.CODEC.optionalFieldOf("nbt", new CompoundTag()).forGetter(Requirement::nbt)
                ).apply(instance, Requirement::new)
        );

        public boolean test(LivingEntity livingEntity, EquipmentSet equipmentSet) {
            ItemStack itemStack = livingEntity.getItemBySlot(slot);
            List<EquipmentSet> equipmentSets = NarakaItemUtils.readNbtDataOrDefault(itemStack, NarakaItemUtils.TAG_EQUIPMENT_SET, EquipmentSet.CODEC.listOf(), livingEntity.level().registryAccess(), List.of());
            return itemStack.is(item.value())
                    && equipmentSets.stream().map(EquipmentSet::getId).anyMatch(id -> equipmentSet.getId().equals(id))
                    && nbt.getAllKeys().stream().allMatch(key -> {
                Tag required = nbt.get(key);
                return Objects.equals(itemStack.getTagElement(key), required);
            });
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Requirement requirement))
                return false;
            return item.value().equals(requirement.item.value()) && slot == requirement.slot && nbt.equals(requirement.nbt);
        }

        @Override
        public int hashCode() {
            int result = item.hashCode();
            result = 31 * result + slot.hashCode();
            result = 31 * result + nbt.hashCode();
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

        public void addToTooltip(ResourceLocation id, long succeed, Consumer<Component> builder) {
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
            if (!(o instanceof Effect effect))
                return false;
            return require == effect.require() && effects.equals(effect.effects());
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