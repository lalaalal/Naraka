package com.yummy.naraka.world.block.entity;

import com.mojang.serialization.Codec;
import com.yummy.naraka.tags.NarakaItemTags;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.SoulType;
import com.yummy.naraka.world.item.equipment.trim.NarakaTrimPatterns;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.RegistryOps;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

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

    public SoulType getSoulType() {
        if (isStabilizerAttached)
            return soulStabilizer.getSoulType();
        return SoulType.NONE;
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
            NarakaItemUtils.loadBlockEntity(stack, soulStabilizer);
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
            NarakaItemUtils.saveBlockEntity(itemStack, soulStabilizer);
            NarakaItemUtils.summonItemEntity(level, itemStack, getBlockPos());
            soulStabilizer.clear();
            isStabilizerAttached = false;
            setChanged();
        }
    }

    public SoulStabilizerBlockEntity getSoulStabilizer() {
        return soulStabilizer;
    }

    private boolean isValidTemplate(ItemStack template) {
        return template.getItem() instanceof SmithingTemplateItem
                || template.is(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get())
                || template.is(NarakaItems.HEROBRINE_SCARF.get());
    }

    public boolean tryAttachTemplate(ItemStack template) {
        if (templateItem.isEmpty() && isValidTemplate(template)) {
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

    private boolean reinforceSword(SoulType soulType, int requiredSoul) {
        if (!forgingItem.is(NarakaItems.PURIFIED_SOUL_SWORD.get()))
            return false;
        Item swordItem = NarakaItems.getSoulSwordOf(soulType);
        if (swordItem == null)
            return false;
        forgingItem = new ItemStack(swordItem);
        NarakaItemUtils.storeNbtData(forgingItem, NarakaItemUtils.TAG_BLESSED, SoulType.CODEC, soulType);
        NarakaItemUtils.makeUnbreakable(forgingItem);
        soulStabilizer.consumeSoul(requiredSoul);
        cooldownTick = COOLDOWN;
        if (level != null)
            level.playSound(null, getBlockPos(), SoundEvents.ANVIL_USE, SoundSource.BLOCKS);

        setChanged();
        return true;
    }

    private boolean attachScarf() {
        if (forgingItem.getItem() instanceof Equipable equipable && equipable.getEquipmentSlot() == EquipmentSlot.CHEST) {
            NarakaItemUtils.storeNbtData(forgingItem, "HerobrineScarf", Codec.BOOL, true);
            return true;
        }
        return false;
    }

    private boolean reinforceArmor(SoulType soulType, int requiredSoul) {
        if (!forgingItem.is(NarakaItemTags.PURIFIED_SOUL_ARMOR) || level == null)
            return false;
        if (soulType == SoulType.GOD_BLOOD) {
            NarakaItemUtils.storeNbtData(forgingItem, "Blessed", Codec.BOOL, true);
        }

        soulStabilizer.consumeSoul(requiredSoul);
        while (Reinforcement.canReinforce(forgingItem, level.registryAccess()))
            Reinforcement.increase(forgingItem, NarakaReinforcementEffects.byItem(forgingItem), level.registryAccess());

        level.playSound(null, getBlockPos(), SoundEvents.ANVIL_USE, SoundSource.BLOCKS);
        cooldownTick = COOLDOWN;

        Optional<Holder.Reference<TrimMaterial>> material = TrimMaterials.getFromIngredient(level.registryAccess(), soulType.getItem().getDefaultInstance());
        Optional<Holder.Reference<TrimPattern>> pattern = NarakaTrimPatterns.fromItem(level.registryAccess(), templateItem);
        if (material.isPresent() && pattern.isPresent()) {
            ArmorTrim armorTrim = new ArmorTrim(material.get(), pattern.get());
            ArmorTrim.setTrim(level.registryAccess(), forgingItem, armorTrim);
        }
        NarakaItemUtils.makeUnbreakable(forgingItem);
        setChanged();
        return true;
    }

    @Override
    public boolean tryReinforce() {
        int requiredSoul = SoulStabilizerBlockEntity.getConsume();
        if (forgingItem.is(NarakaItemTags.SOUL_REINFORCEABLE)
                && !templateItem.isEmpty()
                && cooldownTick <= 0
                && isStabilizerAttached && soulStabilizer.getSouls() >= requiredSoul) {
            SoulType soulType = soulStabilizer.getSoulType();

            if (templateItem.is(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE.get()))
                return reinforceSword(soulType, requiredSoul);
            if (templateItem.is(NarakaItems.HEROBRINE_SCARF.get()))
                return attachScarf();

            return reinforceArmor(soulType, requiredSoul);
        }
        return false;
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putBoolean("IsStabilizerAttached", isStabilizerAttached);
        if (isStabilizerAttached)
            tag.merge(soulStabilizer.getUpdateTag());
        if (!templateItem.isEmpty() && level != null)
            NarakaNbtUtils.store(tag, "TemplateItem", ItemStack.CODEC, RegistryOps.create(NbtOps.INSTANCE, level.registryAccess()), templateItem);
        return tag;
    }

    @Override
    protected void saveAdditional(CompoundTag output) {
        super.saveAdditional(output);
        output.putBoolean("IsStabilizerAttached", isStabilizerAttached);
        if (isStabilizerAttached)
            soulStabilizer.saveAdditional(output);
        if (!templateItem.isEmpty() && level != null)
            NarakaNbtUtils.store(output, "TemplateItem", ItemStack.CODEC, RegistryOps.create(NbtOps.INSTANCE, level.registryAccess()), templateItem);
    }

    @Override
    public void load(CompoundTag input) {
        super.load(input);
        isStabilizerAttached = input.getBoolean("IsStabilizerAttached");
        if (isStabilizerAttached) {
            soulStabilizer.load(input);
            if (level != null)
                soulStabilizer.setLevel(level);
        }
        if (level != null)
            NarakaNbtUtils.read(input, "TemplateItem", ItemStack.CODEC, RegistryOps.create(NbtOps.INSTANCE, level.registryAccess()))
                    .ifPresent(item -> templateItem = item);
    }
}
