package com.yummy.naraka.datagen;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.damagesource.NarakaDamageTypes;
import com.yummy.naraka.tags.NarakaDamageTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class NarakaDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public NarakaDamageTypeTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, provider, NarakaMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        addTags(NarakaDamageTypes.STIGMA,
                DamageTypeTags.BYPASSES_INVULNERABILITY,
                DamageTypeTags.BYPASSES_EFFECTS,
                DamageTypeTags.BYPASSES_ENCHANTMENTS,
                DamageTypeTags.BYPASSES_RESISTANCE,
                DamageTypeTags.BYPASSES_SHIELD,
                DamageTypeTags.BYPASSES_COOLDOWN
        );
        addTags(NarakaDamageTypes.DEATH_COUNT_ZERO,
                DamageTypeTags.BYPASSES_INVULNERABILITY,
                DamageTypeTags.BYPASSES_EFFECTS,
                DamageTypeTags.BYPASSES_ENCHANTMENTS,
                DamageTypeTags.BYPASSES_RESISTANCE,
                DamageTypeTags.BYPASSES_SHIELD,
                DamageTypeTags.BYPASSES_COOLDOWN
        );
        tag(NarakaDamageTypeTags.DEATH_COUNTING_ATTACK)
                .addOptionalTag(NarakaDamageTypeTags.HEROBRINE_ATTACK)
                .addOptional(NarakaDamageTypes.STIGMA.location());
    }

    @SafeVarargs
    private void addTags(ResourceKey<DamageType> type, TagKey<DamageType>... tags) {
        for (TagKey<DamageType> key : tags)
            tag(key).addOptional(type.location());
    }
}
