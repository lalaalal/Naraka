package com.yummy.naraka.world.block.entity;

import com.yummy.naraka.tags.NarakaItemTags;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.SoulType;
import com.yummy.naraka.world.item.component.NarakaDataComponentTypes;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
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
            soulStabilizer.saveToItem(itemStack, level.registryAccess());
            NarakaItemUtils.summonItemEntity(level, itemStack, getBlockPos());

            isStabilizerAttached = false;
            setChanged();
        }
    }

    public SoulStabilizerBlockEntity getSoulStabilizer() {
        return soulStabilizer;
    }

    public boolean tryAttachTemplate(ItemStack template) {
        if (templateItem.isEmpty() && (template.is(ItemTags.TRIM_TEMPLATES) || template.is(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get()))) {
            this.templateItem = template.copyWithCount(1);
            setChanged();
            return true;
        }
        return false;
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

    @Override
    public boolean tryReinforce() {
        if (level != null && forgingItem.is(NarakaItemTags.SOUL_REINFORCEABLE)
                && cooldownTick <= 0
                && isStabilizerAttached && soulStabilizer.getSouls() >= 9 * 16) {
            SoulType soulType = soulStabilizer.getSoulType();
            if (soulType == null)
                return false;

            if (templateItem.is(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get())
                    && forgingItem.is(NarakaItems.PURIFIED_SOUL_SWORD.get())) {
                forgingItem = new ItemStack(NarakaItems.getSoulSwordOf(soulType));
                setChanged();
                level.playSound(null, getBlockPos(), SoundEvents.ANVIL_USE, SoundSource.BLOCKS);
                return true;
            }

            if (soulType == SoulType.GOD_BLOOD)
                forgingItem.set(NarakaDataComponentTypes.BLESSED.get(), true);
            soulStabilizer.consumeSoul(9 * 16);
            while (Reinforcement.canReinforce(forgingItem))
                Reinforcement.increase(forgingItem, NarakaReinforcementEffects.byItem(forgingItem));

            level.playSound(null, getBlockPos(), SoundEvents.ANVIL_USE, SoundSource.BLOCKS);
            cooldownTick = COOLDOWN;

            Optional<Holder.Reference<TrimMaterial>> material = TrimMaterials.getFromIngredient(level.registryAccess(), soulType.getItem().getDefaultInstance());
            Optional<Holder.Reference<TrimPattern>> pattern = TrimPatterns.getFromTemplate(level.registryAccess(), templateItem);
            if (material.isPresent() && pattern.isPresent()) {
                ArmorTrim armorTrim = new ArmorTrim(material.get(), pattern.get());
                forgingItem.set(DataComponents.TRIM, armorTrim);
                setChanged();
            }
            return true;
        }
        return false;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        tag.putBoolean("IsStabilizerAttached", isStabilizerAttached);
        if (isStabilizerAttached) {
            tag.put("StabilizerData", soulStabilizer.getUpdateTag(provider));
            soulStabilizer.setLevel(level);
        }
        if (!templateItem.isEmpty())
            tag.put("TemplateItem", templateItem.save(provider));

        return tag;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        compoundTag.putBoolean("IsStabilizerAttached", isStabilizerAttached);
        if (isStabilizerAttached) {
            CompoundTag stabilizerData = new CompoundTag();
            soulStabilizer.saveAdditional(stabilizerData, provider);
            compoundTag.put("StabilizerData", stabilizerData);
        }
        if (!templateItem.isEmpty())
            compoundTag.put("TemplateItem", templateItem.save(provider));
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        isStabilizerAttached = compoundTag.getBoolean("IsStabilizerAttached");
        if (isStabilizerAttached) {
            soulStabilizer.loadAdditional(compoundTag.getCompound("StabilizerData"), provider);
            soulStabilizer.setLevel(level);
        }
        if (compoundTag.contains("TemplateItem"))
            templateItem = ItemStack.parseOptional(provider, compoundTag.getCompound("TemplateItem"));
    }
}
