package com.yummy.naraka.fabric.data.lang;

import com.yummy.naraka.config.Configuration;
import com.yummy.naraka.data.lang.AdvancementComponent;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.world.item.ConditionalComponents;
import com.yummy.naraka.world.item.DynamicItemLoreHolder;
import com.yummy.naraka.world.item.NarakaItemTooltip;
import com.yummy.naraka.world.item.reinforcement.ReinforcementEffect;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Util;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class NarakaLanguageProviders {
    private final String[] languageCodes;
    private final Map<String, String[]> translationMap = new HashMap<>();

    public NarakaLanguageProviders(String... languageCodes) {
        this.languageCodes = languageCodes;
        generate();
    }

    protected abstract void generate();

    public void addProvidersTo(Consumer<FabricDataGenerator.Pack.RegistryDependentFactory<FabricLanguageProvider>> consumer) {
        for (int index = 0; index < languageCodes.length; index++) {
            final int languageCodeIndex = index;
            consumer.accept(((output, registriesFuture) -> new LanguageProvider(output, languageCodeIndex, registriesFuture)));
        }
    }

    public void add(String key, String... translations) {
        if (translations.length == 0)
            throw new IllegalStateException("Translations must not be empty");
        translationMap.put(key, translations);
    }

    public void addPotion(Item item, Holder<Potion> potion, String... effectTranslations) {
        add(
                item.getDescriptionId() + ".effect." + potion.value().name(),
                effectTranslations
        );
    }

    public void addConfig(Configuration.ConfigValue<?> configValue, String... nameTranslations) {
        add(configValue.getTranslationKey(), nameTranslations);
    }

    public void addConfig(Configuration.ConfigValue<?> configValue, List<String> nameTranslations, List<List<String>> comments) {
        add(configValue.getTranslationKey(), nameTranslations.toArray(String[]::new));
        for (int index = 0; index < comments.size(); index++) {
            List<String> commentTranslations = comments.get(index);
            add(configValue.getCommentTranslationKey(index), commentTranslations.toArray(String[]::new));
        }
    }

    public void addItem(Supplier<? extends Item> item, String... translations) {
        add(item.get().getDescriptionId(), translations);
    }

    public void addBlock(Supplier<? extends Block> block, String... translations) {
        add(block.get().asItem().getDescriptionId(), translations);
        addRawBlock(block, translations);
    }

    public void addRawBlock(Supplier<? extends Block> block, String... translations) {
        add(block.get().getDescriptionId(), translations);
    }

    public void addTooltip(ConditionalComponents conditionalComponents, List<List<String>> lines) {
        List<String> translationKeys = conditionalComponents.collectTranslationKeys();
        if (translationKeys.size() != lines.size())
            throw new IllegalStateException("Translations must have same number of keys (required=%d / given=%d)".formatted(translationKeys.size(), lines.size()));
        for (int index = 0; index < lines.size(); index++) {
            String translationKey = translationKeys.get(index);
            List<String> translations = lines.get(index);
            add(translationKey, translations.toArray(String[]::new));
        }
    }

    public void addTooltip(DynamicItemLoreHolder.Single tooltipHolder, List<List<String>> lines) {
        addTooltip(tooltipHolder.getConditionalComponents(), lines);
    }

    public void addEntityType(Supplier<? extends EntityType<?>> entityType, String... translations) {
        add(entityType.get().getDescriptionId(), translations);
    }

    public void addJukeboxSound(ResourceKey<JukeboxSong> key, String... translations) {
        add(Util.makeDescriptionId("jukebox_song", key.identifier()), translations);
    }

    public void addTrimPattern(ResourceKey<TrimPattern> trimPattern, String... translations) {
        String key = trimPattern.identifier().toLanguageKey("trim_pattern");
        add(key, translations);
    }

    public void addTrimMaterial(ResourceKey<TrimMaterial> trimMaterial, String... translations) {
        String key = trimMaterial.identifier().toLanguageKey("trim_material");
        add(key, translations);
    }

    public void addAdvancement(AdvancementComponent advancementComponent, List<String> titles, List<String> descriptions) {
        add(advancementComponent.titleKey(), titles.toArray(String[]::new));
        add(advancementComponent.descriptionKey(), descriptions.toArray(String[]::new));
    }

    public void addDamageType(ResourceKey<DamageType> damageType, String... message) {
        String directKey = "death.attack." + damageType.identifier().getPath();
        String indirectKey = directKey + ".player";

        add(directKey, message);
        add(indirectKey, message);
    }

    public void addReinforcementEffect(Holder<ReinforcementEffect> effect, String... translations) {
        add(LanguageKey.reinforcementEffect(effect), translations);
    }

    private class LanguageProvider extends FabricLanguageProvider {
        private final int languageCodeIndex;

        protected LanguageProvider(FabricPackOutput output, int languageCodeIndex, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(output, languageCodes[languageCodeIndex], registryLookup);
            this.languageCodeIndex = languageCodeIndex;
        }

        @Override
        public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder builder) {
            for (String key : translationMap.keySet())
                builder.add(key, getTranslation(key));
        }

        public String getTranslation(String key) {
            String[] translations = translationMap.get(key);
            if (translations.length - 1 < languageCodeIndex)
                return translations[0];
            return translations[languageCodeIndex];
        }
    }
}
