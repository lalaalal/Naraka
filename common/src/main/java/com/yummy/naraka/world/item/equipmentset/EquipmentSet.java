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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class EquipmentSet implements ItemEvents.ItemTooltip {
    public static final Codec<EquipmentSet> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(EquipmentSet::getId),
                    Requirement.CODEC.listOf().fieldOf("requirements").forGetter(set -> set.requirements),
                    EquipmentSetEffect.CODEC.fieldOf("effect").forGetter(set -> set.effect)
            ).apply(instance, EquipmentSet::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, EquipmentSet> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            EquipmentSet::getId,
            Requirement.STREAM_CODEC.apply(ByteBufCodecs.list()),
            set -> set.requirements,
            EquipmentSetEffect.STREAM_CODEC,
            set -> set.effect,
            EquipmentSet::new
    );
    private final ResourceLocation id;
    private final List<Requirement> requirements;
    private final EquipmentSetEffect<?> effect;
    public EquipmentSet(ResourceLocation id, List<Requirement> requirements, EquipmentSetEffect<?> effect) {
        this.id = id;
        this.requirements = requirements;
        this.effect = effect;
    }

    public static EquipmentSet empty() {
        return new EquipmentSet(NarakaMod.location("empty"), List.of(), EquipmentSetEffect.empty());
    }

    public ResourceLocation getId() {
        return id;
    }

    public boolean canApply(LivingEntity entity) {
        return requirements.stream().allMatch(requirement -> requirement.test(entity, this));
    }

    private long countSucceed(LivingEntity entity) {
        return requirements.stream().filter(requirement -> requirement.test(entity, this)).count();
    }

    public void updateEffect(LivingEntity entity) {
        Set<EquipmentSet> activeEquipmentSets = new HashSet<>(EntityDataHelper.getRawEntityData(entity, NarakaEntityDataTypes.EQUIPMENT_SET.get()));
        if (canApply(entity)) {
            effect.activate(entity);
            activeEquipmentSets.add(this);
        } else {
            effect.deactivate(entity);
            activeEquipmentSets.remove(this);
        }
        EntityDataHelper.setEntityData(entity, NarakaEntityDataTypes.EQUIPMENT_SET.get(), activeEquipmentSets.stream().toList());
    }

    @Override
    public void addToTooltip(DataComponentHolder item, Item.TooltipContext context, Player player, TooltipFlag tooltipFlag, Consumer<Component> builder) {
        long succeed = countSucceed(player);
        Component component = Component.translatable(LanguageKey.equipmentSet(id), succeed, requirements.size())
                .withStyle(styleUpdaterByEquipment(succeed))
                .append(" (%d/%d)".formatted(succeed, requirements.size()));
        builder.accept(component);
    }

    private UnaryOperator<Style> styleUpdaterByEquipment(long succeed) {
        if (succeed == requirements.size())
            return style -> style.withColor(ChatFormatting.GREEN);
        return style -> style.withColor(ChatFormatting.DARK_GRAY);
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
                ByteBufCodecs.fromCodec(EquipmentSlot.CODEC),
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
    }
}