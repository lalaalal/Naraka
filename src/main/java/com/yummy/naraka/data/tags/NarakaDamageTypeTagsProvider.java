package com.yummy.naraka.data.tags;

import com.yummy.naraka.tags.NarakaDamageTypeTags;
import com.yummy.naraka.world.damagesource.NarakaDamageTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public class NarakaDamageTypeTagsProvider extends FabricTagProvider<DamageType> {
    public NarakaDamageTypeTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.DAMAGE_TYPE, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        addTags(NarakaDamageTypes.STIGMA,
                DamageTypeTags.BYPASSES_INVULNERABILITY,
                DamageTypeTags.BYPASSES_ARMOR,
                DamageTypeTags.BYPASSES_EFFECTS,
                DamageTypeTags.BYPASSES_ENCHANTMENTS,
                DamageTypeTags.BYPASSES_RESISTANCE,
                DamageTypeTags.BYPASSES_SHIELD,
                DamageTypeTags.BYPASSES_COOLDOWN
        );
        addTags(NarakaDamageTypes.SPEAR_OF_LONGINUS,
                DamageTypeTags.BYPASSES_INVULNERABILITY,
                DamageTypeTags.BYPASSES_ARMOR,
                DamageTypeTags.BYPASSES_EFFECTS,
                DamageTypeTags.BYPASSES_ENCHANTMENTS,
                DamageTypeTags.BYPASSES_RESISTANCE,
                DamageTypeTags.BYPASSES_SHIELD,
                DamageTypeTags.BYPASSES_COOLDOWN
        );
        addTags(NarakaDamageTypes.MOB_ATTACK_FIXED,
                DamageTypeTags.BYPASSES_EFFECTS,
                DamageTypeTags.BYPASSES_RESISTANCE
        );
        tag(NarakaDamageTypeTags.DEATH_COUNTING_ATTACK)
                .add(NarakaDamageTypes.STIGMA);
    }

    @SafeVarargs
    private void addTags(ResourceKey<DamageType> type, TagKey<DamageType>... tags) {
        for (TagKey<DamageType> key : tags)
            tag(key).add(type);
    }
}
