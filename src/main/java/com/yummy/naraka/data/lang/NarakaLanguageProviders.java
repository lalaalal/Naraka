package com.yummy.naraka.data.lang;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.damagesource.NarakaDamageTypes;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.NarakaJukeboxSongs;
import com.yummy.naraka.world.item.armortrim.NarakaTrimMaterials;
import com.yummy.naraka.world.item.armortrim.NarakaTrimPatterns;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class NarakaLanguageProviders {
    public static final String PURIFIED_SOUL_UPGRADE_KEY = Util.makeDescriptionId("upgrade", NarakaMod.location("purified_soul_upgrade"));
    public static final String PURIFIED_SOUL_UPGRADE_APPLIES_TO_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_soul_upgrade.applies_to"));
    public static final String PURIFIED_SOUL_UPGRADE_INGREDIENTS_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_soul_upgrade.ingredients"));
    public static final String PURIFIED_SOUL_UPGRADE_BASE_SLOT_DESCRIPTION_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_soul_upgrade.base_slot_description"));
    public static final String PURIFIED_SOUL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_soul_upgrade.additions_slot_description"));
    public static final String JADE_SOUL_CRAFTING_FUEL_KEY = "jade.naraka.soul_crafting.fuel";
    public static final String JADE_STIGMA_KEY = "jade.naraka.stigma";
    public static final String JADE_DEATH_COUNT_KEY = "jade.naraka.death_count";
    private final String[] languageCodes;
    private final Map<String, String[]> translationMap = new HashMap<>();

    public NarakaLanguageProviders(String... languageCodes) {
        this.languageCodes = languageCodes;
        generate();
    }

    protected void generate() {
        add("itemGroup.naraka", "Naraka", "Naraka");
        add("itemGroup.naraka.test", "Naraka Test", "나락!");
        add("container.soul_crafting", "Soul Crafter", "영혼 세공기");
        add(PURIFIED_SOUL_UPGRADE_KEY, "Purified Soul Upgrade", "정화된 영혼 강화");
        add(PURIFIED_SOUL_UPGRADE_APPLIES_TO_KEY, "Ebony Tools, Purified Soul Weapons", "흑단나무 검, 정화된 영혼 무기");
        add(PURIFIED_SOUL_UPGRADE_INGREDIENTS_KEY, "Purified Soul Metal, Soul Infused Materials", "정화된 영혼 금속, 영혼이 주입된 재료");
        add(PURIFIED_SOUL_UPGRADE_BASE_SLOT_DESCRIPTION_KEY, "Add Ebony Sword, Soul Weapon", "흑단나무 무기, 정화된 영혼 검 또는 창를 놓으세요");
        add(PURIFIED_SOUL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_KEY, "Add Purified Soul Metal, Soul Infused Materials", "정화된 영혼 금속, 영혼이 주입된 재료 또는 신의 피를 놓으세요");

        add(JADE_SOUL_CRAFTING_FUEL_KEY, "Fuel: %d", "연료: %d");
        add(NarakaJadeProviderComponents.SOUL_CRAFTING_BLOCK.translationKey, "Soul Crafting Block", "영혼 세공기");
        add(JADE_STIGMA_KEY, "Stigma: %d", "낙인: %d");
        add(JADE_DEATH_COUNT_KEY, "Death Count: %d", "데스카운트: %d");
        add(NarakaJadeProviderComponents.ENTITY_DATA.translationKey, "Stigma", "낙인");

        addTrimPattern(NarakaTrimPatterns.PURIFIED_SOUL_SILENCE, "Purified Soul Silence Armor Trim", "정화된 영혼 고요 갑옷 장식");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_REDSTONE, "Soul Infused Redstone Material", "영혼이 주입된 레드스톤 소재");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_COPPER, "Soul Infused Copper Material", "영혼이 주입된 구리 소재");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_GOLD, "Soul Infused Gold Material", "영혼이 주입된 금 소재");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_EMERALD, "Soul Infused Emerald Material", "영혼이 주입된 에메랄드 소재");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_DIAMOND, "Soul Infused Diamond Material", "영혼이 주입된 다이아몬드 소재");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_LAPIS, "Soul Infused Lapis Material", "영혼이 주입된 청금석 소재");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_AMETHYST, "Soul Infused Amethyst Material", "영혼이 주입된 자수정 소재");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_NECTARIUM, "Soul Infused Nectarium Material", "영혼이 주입된 넥타륨 소재");

        addAdvancement(AdvancementNarakaComponents.ROOT,
                List.of("Naraka", "나라카"),
                List.of("Lasciate ogni speranza, voi ch'entrate", "여기 들어오는 자, 모든 희망을 버려라"));
        addAdvancement(AdvancementNarakaComponents.SANCTUARY_COMPASS,
                List.of("Way to him", "그에게로 가는 길"),
                List.of("Get Sanctuary Compass", "생츄어리 나침반을 얻으세요"));
        addAdvancement(AdvancementNarakaComponents.HEROBRINE_SANCTUARY,
                List.of("Herobrine Sanctuary", "폭군의 성역"),
                List.of("Too Big!", "뭐가 이렇게 커?"));
        addAdvancement(AdvancementNarakaComponents.SUMMON_HEROBRINE,
                List.of("Naraka Tyrant"),
                List.of("Summon Herobrine"));
        addAdvancement(AdvancementNarakaComponents.KILL_HEROBRINE,
                List.of("Purified Soul", "정화된 영혼"),
                List.of("Defeat the lord of Naraka", "나락의 군주를 쓰러트리세요"));
        addAdvancement(AdvancementNarakaComponents.GOD_BLOOD,
                List.of("Blood of God", "신의 피"),
                List.of("Very AWESOME!", "광장히 엄청나"));

        addJukeboxSound(NarakaJukeboxSongs.HEROBRINE_PHASE_1, "Herobrine Phase 1", "히로빈 1 페이즈");
        addJukeboxSound(NarakaJukeboxSongs.HEROBRINE_PHASE_2, "Herobrine Phase 2", "히로빈 2 페이즈");
        addJukeboxSound(NarakaJukeboxSongs.HEROBRINE_PHASE_3, "Herobrine Phase 3", "히로빈 3 페이즈");
        addJukeboxSound(NarakaJukeboxSongs.HEROBRINE_PHASE_4, "Herobrine Phase 4", "히로빈 4 페이즈");

        addItem(NarakaItems.STIGMA_ROD, "Stigma Rod", "낙인 막대기");
        addItem(NarakaItems.PURIFIED_SOUL_SHARD, "Purified Soul Shard", "정화된 영혼 조각");
        addItem(NarakaItems.NECTARIUM, "Nectarium", "넥타륨");
        addItem(NarakaItems.GOD_BLOOD, "§lGod Blood", "§l신의 피");
        addItem(NarakaItems.COMPRESSED_IRON_INGOT, "Compressed Iron Ingot", "압축된 철 주괴");
        addItem(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, "Smithing Template", "대장장이 형판");
        addItem(NarakaItems.PURIFIED_SOUL_SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, "Smithing Template", "대장장이 형판");

        addItem(NarakaItems.SPEAR_ITEM, "Spear", "창");
        addItem(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, "Mighty Holy Spear", "강력한 성스러운 창");
        addItem(NarakaItems.SPEAR_OF_LONGINUS_ITEM, "Spear of Longinus", "롱기누스의 창");

        addItem(NarakaItems.EBONY_METAL_HELMET, "Ebony Metal Helmet", "흑단나무 금속 투구");
        addItem(NarakaItems.EBONY_METAL_CHESTPLATE, "Ebony Metal Chestplate", "흑단나무 금속 흉갑");
        addItem(NarakaItems.EBONY_METAL_LEGGINGS, "Ebony Metal Leggings", "흑단나무 금속 레깅스");
        addItem(NarakaItems.EBONY_METAL_BOOTS, "Ebony Metal Boots", "흑단나무 금속 부츠");
        addItem(NarakaItems.PURIFIED_SOUL_HELMET, "Purified Soul Helmet", "정화된 영혼 투구");
        addItem(NarakaItems.PURIFIED_SOUL_CHESTPLATE, "Purified Soul Chestplate", "정화된 영혼 흉갑");
        addItem(NarakaItems.PURIFIED_SOUL_LEGGINGS, "Purified Soul Leggings", "정화된 영혼 레깅스");
        addItem(NarakaItems.PURIFIED_SOUL_BOOTS, "Purified Soul Boots", "정화된 영혼 부츠");

        addItem(NarakaItems.SOUL_INFUSED_REDSTONE, "Soul Infused Redstone", "영혼이 주입된 레드스톤");
        addItem(NarakaItems.SOUL_INFUSED_COPPER, "Soul Infused Copper", "영혼이 주입된 구리");
        addItem(NarakaItems.SOUL_INFUSED_GOLD, "Soul Infused Gold", "영혼이 주입된 금");
        addItem(NarakaItems.SOUL_INFUSED_EMERALD, "Soul Infused Emerald", "영혼이 주입된 에메랄드");
        addItem(NarakaItems.SOUL_INFUSED_DIAMOND, "Soul Infused Diamond", "영혼이 주입된 다이아몬드");
        addItem(NarakaItems.SOUL_INFUSED_LAPIS, "Soul Infused Lapis", "영혼이 주입된 청금석");
        addItem(NarakaItems.SOUL_INFUSED_AMETHYST, "Soul Infused Amethyst", "영혼이 주입된 자수정");
        addItem(NarakaItems.SOUL_INFUSED_NECTARIUM, "Soul Infused Nectarium", "영혼이 주입된 넥타륨");
        addItem(NarakaItems.PURIFIED_SOUL_METAL, "Purified Soul Metal", "정화된 영혼 금속");

        addItem(NarakaItems.SOUL_INFUSED_REDSTONE_SWORD, "Soul Infused Redstone Sword", "영혼이 주입된 레드스톤 검");
        addItem(NarakaItems.SOUL_INFUSED_COPPER_SWORD, "Soul Infused Copper Sword", "영혼이 주입된 구리 검");
        addItem(NarakaItems.SOUL_INFUSED_GOLD_SWORD, "Soul Infused Gold Sword", "영혼이 주입된 금 검");
        addItem(NarakaItems.SOUL_INFUSED_EMERALD_SWORD, "Soul Infused Emerald Sword", "영혼이 주입된 에메랄드 검");
        addItem(NarakaItems.SOUL_INFUSED_DIAMOND_SWORD, "Soul Infused Diamond Sword", "영혼이 주입된 다이아몬드 검");
        addItem(NarakaItems.SOUL_INFUSED_LAPIS_SWORD, "Soul Infused Lapis Sword", "영혼이 주입된 청금석 검");
        addItem(NarakaItems.SOUL_INFUSED_AMETHYST_SWORD, "Soul Infused Amethyst Sword", "영혼이 주입된 자수정 검");
        addItem(NarakaItems.SOUL_INFUSED_NECTARIUM_SWORD, "Soul Infused Nectarium Sword", "영혼이 주입된 넥타륨 검");
        addItem(NarakaItems.PURIFIED_SOUL_SWORD, "Purified Soul Sword", "정화된 영혼 검");

        addItem(NarakaItems.EBONY_SWORD, "Ebony Sword", "흑단나무 검");
        addItem(NarakaItems.EBONY_ROOTS_SCRAP, "Ebony Roots Scrap", "흑단나무 뿌리 파편");
        addItem(NarakaItems.EBONY_METAL_INGOT, "Ebony Metal Ingot", "흑단나무 금속 주괴");
        addItem(NarakaItems.SANCTUARY_COMPASS, "Sanctuary Compass", "생츄어리 나침반");

        addItem(NarakaItems.HEROBRINE_PHASE_1_DISC, "Herobrine Phase 1 Disc", "히로빈 1 페이즈 음반");
        addItem(NarakaItems.HEROBRINE_PHASE_2_DISC, "Herobrine Phase 2 Disc", "히로빈 2 페이즈 음반");
        addItem(NarakaItems.HEROBRINE_PHASE_3_DISC, "Herobrine Phase 3 Disc", "히로빈 3 페이즈 음반");
        addItem(NarakaItems.HEROBRINE_PHASE_4_DISC, "Herobrine Phase 4 Disc", "히로빈 4 페이즈 음반");

        addBlock(NarakaBlocks.NECTARIUM_ORE, "Nectarium Ore", "넥타륨 광석");
        addBlock(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE, "Deepslate Nectarium Ore", "심층암 넥타륨 광석");
        addBlock(NarakaBlocks.NECTARIUM_BLOCK, "Block of Nectarium", "넥타륨 블록");
        addBlock(NarakaBlocks.TRANSPARENT_BLOCK, "Block of Transparent", "투명 블록");
        addBlock(NarakaBlocks.COMPRESSED_IRON_BLOCK, "Block of Compressed Iron", "압축된 철 블록");
        addBlock(NarakaBlocks.FAKE_GOLD_BLOCK, "Block of Fake Gold", "거짓된 금 블록");
        addBlock(NarakaBlocks.AMETHYST_SHARD_BLOCK, "Block of Amethyst Shard", "자수정 조각 블록");
        addBlock(NarakaBlocks.SOUL_CRAFTING_BLOCK, "Block of Soul Crafting", "영혼 세공기");

        addBlock(NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK, "Block of Soul Infused Redstone", "영혼이 주입된 레드스톤 블록");
        addBlock(NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK, "Block of Soul Infused Copper", "영혼이 주입된 구리 블록");
        addBlock(NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK, "Block of Soul Infused Gold", "영혼이 주입된 금 블록");
        addBlock(NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK, "Block of Soul Infused Emerald", "영혼이 주입된 에메랄드 블록");
        addBlock(NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK, "Block of Soul Infused Diamond", "영혼이 주입된 다이아몬드 블록");
        addBlock(NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK, "Block of Soul Infused Lapis", "영혼이 주입된 청금석 블록");
        addBlock(NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK, "Block of Soul Infused Amethyst", "영혼이 주입된 자수정 블록");
        addBlock(NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK, "Block of Soul Infused Nectarium", "영혼이 주입된 넥타륨 블록");
        addBlock(NarakaBlocks.PURIFIED_SOUL_BLOCK, "Block of Purified Soul", "정화된 영혼 블록");

        addBlock(NarakaBlocks.HEROBRINE_TOTEM, "Herobrine Totem", "히로빈 토템");
        addBlock(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK, "Purified Soul Fire", "정화된 영혼 불");
        addBlock(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK, "Block of Purified Soul Metal", "정화된 영혼 금속 블록");
        addBlock(NarakaBlocks.EBONY_LOG, "Ebony Log", "흑단나무 원목");
        addBlock(NarakaBlocks.STRIPPED_EBONY_LOG, "Stripped Ebony Log", "껍질 벗긴 흑단나무 원목");
        addBlock(NarakaBlocks.EBONY_WOOD, "Ebony Wood", "흑단나무");
        addBlock(NarakaBlocks.STRIPPED_EBONY_WOOD, "Stripped Ebony Wood", "껍질 벗긴 흑단나무");
        addBlock(NarakaBlocks.EBONY_LEAVES, "Ebony Leaves", "흑단나무 나뭇잎");
        addBlock(NarakaBlocks.EBONY_SAPLING, "Ebony Sapling", "흑단나무 묘목");
        addBlock(NarakaBlocks.POTTED_EBONY_SAPLING, "Potted Ebony Sapling", "화분에 심은 흑단나무 묘목");
        addBlock(NarakaBlocks.HARD_EBONY_PLANKS, "Hard Ebony Planks", "단단한 흑단나무 판자");
        addBlock(NarakaBlocks.EBONY_ROOTS, "Ebony Roots", "흑단나무 뿌리");
        addBlock(NarakaBlocks.EBONY_METAL_BLOCK, "Block of Ebony Metal", "흑단나무 금속 블록");

        addEntityType(NarakaEntityTypes.HEROBRINE, "Naraka: Herobrine", "히로빈");
        addEntityType(NarakaEntityTypes.THROWN_SPEAR, "Spear", "창");
        addEntityType(NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR, "Mighty Holy Spear", "강력한 성스러운 창");
        addEntityType(NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS, "Spear of Longinus", "롱기누스의 창");

        addDamageType(NarakaDamageTypes.SPEAR_OF_LONGINUS,
                List.of("%1$s's AT Field was torn by %2$s", "%1$s의 AT 필드가 %2$s에 찢어졌습니다."),
                List.of("%1$s's AT Field was torn by %2$s thrown %3$s", "%1$s의 AT 필드가 %3$s가 던진 %2$s에 찢어졌습니다."));
        addDamageType(NarakaDamageTypes.STIGMA,
                List.of("Stigma"),
                List.of("Stigma"));
    }

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


    public void addItem(Item item, String... translations) {
        add(item.getDescriptionId(), translations);
    }

    public void addBlock(Block block, String... translations) {
        add(block.getDescriptionId(), translations);
    }

    public void addEntityType(EntityType<?> entityType, String... translations) {
        add(entityType.getDescriptionId(), translations);
    }

    public void addJukeboxSound(ResourceKey<JukeboxSong> key, String... translations) {
        add(Util.makeDescriptionId("jukebox_song", key.location()), translations);
    }

    public void addTrimPattern(ResourceKey<TrimPattern> trimPattern, String... translations) {
        String key = trimPattern.location().toLanguageKey("trim_pattern");
        add(key, translations);
    }

    public void addTrimMaterial(ResourceKey<TrimMaterial> trimMaterial, String... translations) {
        String key = trimMaterial.location().toLanguageKey("trim_material");
        add(key, translations);
    }

    public void addAdvancement(AdvancementComponent advancementComponent, List<String> titles, List<String> descriptions) {
        add(advancementComponent.titleKey(), titles.toArray(new String[0]));
        add(advancementComponent.descriptionKey(), descriptions.toArray(new String[0]));
    }

    public void addDamageType(ResourceKey<DamageType> damageType, List<String> directMessages, List<String> indirectMessages) {
        String directKey = "death.attack." + damageType.location().getPath();
        String indirectKey = directKey + ".player";

        add(directKey, directMessages.toArray(new String[0]));
        add(indirectKey, indirectMessages.toArray(new String[0]));
    }

    private class LanguageProvider extends FabricLanguageProvider {
        private final int languageCodeIndex;

        protected LanguageProvider(FabricDataOutput dataOutput, int languageCodeIndex, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(dataOutput, languageCodes[languageCodeIndex], registryLookup);
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
