package com.yummy.naraka.world.block.entity;

import com.yummy.naraka.core.component.NarakaDataComponentTypes;
import com.yummy.naraka.tags.NarakaItemTags;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.SoulType;
import com.yummy.naraka.world.item.equipment.trim.NarakaTrimPatterns;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SoulSmithingBlockEntity extends ForgingBlockEntity {
    public static final int COOLDOWN = 40;

    private final SoulStabilizerBlockEntity soulStabilizer;
    private boolean isStabilizerAttached;
    private ItemStack templateItem = ItemStack.EMPTY;

    public SoulSmithingBlockEntity(BlockPos pos, BlockState state) {
        this(NarakaBlockEntityTypes.SOUL_SMITHING.get(), pos, state, 1);
    }

    protected SoulSmithingBlockEntity(BlockEntityType<? extends SoulSmithingBlockEntity> type, BlockPos pos, BlockState state, float successChance) {
        super(type, pos, state, successChance);
        soulStabilizer = new SoulStabilizerBlockEntity(pos, NarakaBlocks.SOUL_STABILIZER.get().defaultBlockState());
    }

    public boolean isStabilizerAttached() {
        return isStabilizerAttached;
    }

    @Nullable
    public SoulType getSoulType() {
        if (isStabilizerAttached)
            return soulStabilizer.getSoulType();
        return null;
    }

    public int getSouls() {
        if (isStabilizerAttached)
            return soulStabilizer.getSouls();
        return 0;
    }

    @Override
    public boolean canReinforce(ItemStack stack) {
        return stack.is(NarakaItemTags.SOUL_REINFORCEABLE) && !stack.isEmpty() && !stack.isStackable();
    }

    public boolean tryAttachSoulStabilizer(ItemStack stack) {
        if (level != null && !isStabilizerAttached && stack.is(NarakaBlocks.SOUL_STABILIZER.get().asItem())) {
            NarakaItemUtils.loadBlockEntity(stack, soulStabilizer, level.registryAccess());
            soulStabilizer.setLevel(level);
            isStabilizerAttached = true;
            setChanged();
            return true;
        }
        return false;
    }

    public void detachSoulStabilizer() {
        if (isStabilizerAttached && level != null) {
            ItemStack itemStack = new ItemStack(NarakaBlocks.SOUL_STABILIZER.get());
            NarakaItemUtils.saveBlockEntity(itemStack, soulStabilizer, level.registryAccess());
            NarakaItemUtils.summonItemEntity(level, itemStack, getBlockPos());
            soulStabilizer.clear();
            isStabilizerAttached = false;
            setChanged();
        }
    }

    public SoulStabilizerBlockEntity getSoulStabilizer() {
        return soulStabilizer;
    }

    public boolean tryAttachTemplate(ItemStack template) {
        if (templateItem.isEmpty() && (template.getItem() instanceof SmithingTemplateItem || template.is(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get()))) {
            this.templateItem = template.copyWithCount(1);
            setChanged();
            return true;
        }
        return false;
    }

    private int getRequiredSoul() {
        if (forgingItem.is(NarakaItems.PURIFIED_SOUL_SWORD.get()))
            return 14976;
        if (getSoulType() != null && getSoulType() == SoulType.GOD_BLOOD)
            return 3888;
        return 9 * 16;
    }

    public void detachTemplateItem() {
        if (!templateItem.isEmpty() && level != null) {
            NarakaItemUtils.summonItemEntity(level, templateItem, getBlockPos());
            templateItem = ItemStack.EMPTY;
            setChanged();
        }
    }

    public ItemStack getTemplateItem() {
        return templateItem;
    }

    @Override
    public void dropItems() {
        super.dropItems();
        detachSoulStabilizer();
        detachTemplateItem();
    }

    private boolean reinforceSword(SoulType soulType, int requiredSoul) {
        if (!forgingItem.is(NarakaItems.PURIFIED_SOUL_SWORD.get()))
            return false;
        Item swordItem = NarakaItems.getSoulSwordOf(soulType);
        if (swordItem == null)
            return false;
        forgingItem = new ItemStack(swordItem);
        soulStabilizer.consumeSoul(requiredSoul);
        cooldownTick = COOLDOWN;
        if (level != null)
            level.playSound(null, getBlockPos(), SoundEvents.ANVIL_USE, SoundSource.BLOCKS);

        setChanged();
        return true;
    }

    private boolean reinforceArmor(SoulType soulType, int requiredSoul) {
        if (!forgingItem.is(NarakaItemTags.PURIFIED_SOUL_ARMOR) || level == null)
            return false;
        if (soulType == SoulType.GOD_BLOOD)
            forgingItem.set(NarakaDataComponentTypes.BLESSED.get(), true);
        forgingItem.set(DataComponents.ENCHANTABLE, new Enchantable(9));

        soulStabilizer.consumeSoul(requiredSoul);
        while (Reinforcement.canReinforce(forgingItem))
            Reinforcement.increase(forgingItem, NarakaReinforcementEffects.byItem(forgingItem));

        level.playSound(null, getBlockPos(), SoundEvents.ANVIL_USE, SoundSource.BLOCKS);
        cooldownTick = COOLDOWN;

        Optional<Holder<TrimMaterial>> material = TrimMaterials.getFromIngredient(level.registryAccess(), soulType.getItem().getDefaultInstance());
        Optional<Holder.Reference<TrimPattern>> pattern = NarakaTrimPatterns.fromItem(level.registryAccess(), templateItem);
        if (material.isPresent() && pattern.isPresent()) {
            ArmorTrim armorTrim = new ArmorTrim(material.get(), pattern.get());
            forgingItem.set(DataComponents.TRIM, armorTrim);
        }
        setChanged();
        return true;
    }

    @Override
    public boolean tryReinforce() {
        int requiredSoul = getRequiredSoul();
        if (forgingItem.is(NarakaItemTags.SOUL_REINFORCEABLE)
                && !templateItem.isEmpty()
                && cooldownTick <= 0
                && isStabilizerAttached && soulStabilizer.getSouls() >= requiredSoul) {
            SoulType soulType = soulStabilizer.getSoulType();

            if (templateItem.is(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get()))
                return reinforceSword(soulType, requiredSoul);

            return reinforceArmor(soulType, requiredSoul);
        }
        return false;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        tag.putBoolean("IsStabilizerAttached", isStabilizerAttached);
        if (isStabilizerAttached) {
            tag.put("StabilizerData", soulStabilizer.getUpdateTag(provider));
            if (level != null)
                soulStabilizer.setLevel(level);
        }
        if (!templateItem.isEmpty())
            tag.store("TemplateItem", ItemStack.STRICT_CODEC, templateItem);
        return tag;
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putBoolean("IsStabilizerAttached", isStabilizerAttached);
        if (isStabilizerAttached)
            soulStabilizer.saveAdditional(output);
        if (!templateItem.isEmpty())
            output.store("TemplateItem", ItemStack.STRICT_CODEC, templateItem);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        isStabilizerAttached = input.getBooleanOr("IsStabilizerAttached", false);
        if (isStabilizerAttached) {
            soulStabilizer.loadAdditional(input);
            if (level != null)
                soulStabilizer.setLevel(level);
        }
        input.read("TemplateItem", ItemStack.STRICT_CODEC)
                .ifPresent(item -> templateItem = item);
    }
}
