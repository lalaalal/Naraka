package com.yummy.naraka.fabric.data.lang;

import com.yummy.naraka.data.lang.*;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.damagesource.NarakaDamageTypes;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.NarakaJukeboxSongs;
import com.yummy.naraka.world.item.SoulType;
import com.yummy.naraka.world.item.equipment.trim.NarakaTrimMaterials;
import com.yummy.naraka.world.item.equipment.trim.NarakaTrimPatterns;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import com.yummy.naraka.world.item.reinforcement.ReinforcementEffect;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NarakaLanguageProviders {
    private final String[] languageCodes;
    private final Map<String, String[]> translationMap = new HashMap<>();

    public static void add(Consumer<FabricDataGenerator.Pack.RegistryDependentFactory<FabricLanguageProvider>> adder, String... languageCodes) {
        new NarakaLanguageProviders(languageCodes)
                .addProvidersTo(adder);
    }

    public NarakaLanguageProviders(String... languageCodes) {
        this.languageCodes = languageCodes;
        generate();
    }

    protected void generate() {
        add(LanguageKey.ITEM_GROUP_NARAKA, "Naraka", "Naraka");
        add(LanguageKey.ITEM_GROUP_SOUL_MATERIALS, "Soul Materials", "영혼 재료");
        add(LanguageKey.ITEM_GROUP_TEST, "Naraka Test", "나락! 테스트");

        add(LanguageKey.KEY_CATEGORIES_NARAKA, "Naraka", "Naraka");
        add(LanguageKey.KEY_TOGGLE_ORE_SEE_THROUGH, "Toggle Ore See Through", "광물 투시 켜기/끄기");

        add(LanguageKey.toggleOreSeeThroughMessage(false), "Ore See Through is now enabled", "광물 투시 활성화됨");
        add(LanguageKey.toggleOreSeeThroughMessage(true), "Ore See Through is now disabled", "광물 투시 비활성화됨");

        add("container.soul_crafting", "Soul Crafter", "영혼 세공기");
        add(LanguageKey.REINFORCEMENT_KEY, "Reinforcement: %d", "강화: %d");
        add(LanguageKey.BLESSED_KEY, "Blessed", "축복받음");
        add(LanguageKey.PURIFIED_SOUL_UPGRADE_KEY, "Purified Soul Upgrade", "정화된 영혼 강화");
        add(LanguageKey.PURIFIED_SOUL_UPGRADE_APPLIES_TO_KEY, "Ebony Tools, Purified Soul Weapons", "흑단나무 검, 정화된 영혼 무기");
        add(LanguageKey.PURIFIED_SOUL_UPGRADE_INGREDIENTS_KEY, "Purified Soul Metal, Soul Infused Materials", "정화된 영혼 금속, 영혼이 주입된 재료");
        add(LanguageKey.PURIFIED_SOUL_UPGRADE_BASE_SLOT_DESCRIPTION_KEY, "Add Ebony Sword, Soul Weapon", "흑단나무 무기, 정화된 영혼 검 또는 창를 놓으세요");
        add(LanguageKey.PURIFIED_SOUL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_KEY, "Add Purified Soul Metal, Soul Infused Materials", "정화된 영혼 금속, 영혼이 주입된 재료 또는 신의 피를 놓으세요");

        add(LanguageKey.JADE_SOUL_CRAFTING_FUEL_KEY, "Fuel: %d", "연료: %d");
        add(NarakaJadeProviderComponents.SOUL_CRAFTING_BLOCK.translationKey, "Soul Crafting Block", "영혼 세공기");
        add(LanguageKey.JADE_SOUL_STABILIZER_KEY, "%d");
        add(NarakaJadeProviderComponents.SOUL_STABILIZER.translationKey, "Soul Stabilizer", "영혼 안정기");
        add(LanguageKey.JADE_STIGMA_KEY, "Stigma: %d", "낙인: %d");
        add(LanguageKey.JADE_DEATH_COUNT_KEY, "Death Count: %d", "데스카운트: %d");
        add(NarakaJadeProviderComponents.SOUL_SMITHING_BLOCK.translationKey, "Soul Smithing Block", "영혼 대장장이 블록");
        add(NarakaJadeProviderComponents.ENTITY_DATA.translationKey, "Stigma", "낙인");

        add(LanguageKey.DISABLE_SKILL_USE_KEY, "Disable skill using", "스킬 사용 중지");

        add(SoulType.REDSTONE.translationKey(), "Redstone", "레드스톤");
        add(SoulType.COPPER.translationKey(), "Copper", "구리");
        add(SoulType.GOLD.translationKey(), "Gold", "금");
        add(SoulType.EMERALD.translationKey(), "Emerald", "에메랄드");
        add(SoulType.DIAMOND.translationKey(), "Diamond", "다이아몬드");
        add(SoulType.LAPIS.translationKey(), "Lapis", "청금석");
        add(SoulType.AMETHYST.translationKey(), "Amethyst", "자수정");
        add(SoulType.NECTARIUM.translationKey(), "Nectarium", "넥타륨");
        add(SoulType.GOD_BLOOD.translationKey(), "God Blood", "신의 피");

        add(LanguageKey.CHALLENGERS_BLESSING, "Challenger's Blessing", "도전자의 축복");

        addReinforcementEffect(NarakaReinforcementEffects.INCREASE_ATTACK_DAMAGE, "Increase attack damage", "공격력 증가");
        addReinforcementEffect(NarakaReinforcementEffects.INCREASE_ARMOR, "Increase armor", "방어력 증가");
        addReinforcementEffect(NarakaReinforcementEffects.INCREASE_ARMOR_TOUGHNESS, "Increase armor toughness", "방어 강도 증가");
        addReinforcementEffect(NarakaReinforcementEffects.KNOCKBACK_RESISTANCE, "Knockback resistance", "넉백 저항");
        addReinforcementEffect(NarakaReinforcementEffects.FASTER_LIQUID_SWIMMING, "Increase liquid swimming speed", "액체 수영 속도 증가");
        addReinforcementEffect(NarakaReinforcementEffects.IGNORE_LIQUID_PUSHING, "Ignore liquid pushing", "유체 흐름 무시");
        addReinforcementEffect(NarakaReinforcementEffects.FLYING, "Flying (Scroll!)", "비행 (스크롤!)");
        addReinforcementEffect(NarakaReinforcementEffects.ORE_SEE_THROUGH, "Ore see through", "광물 투시");
        addReinforcementEffect(NarakaReinforcementEffects.LAVA_VISION, "Lava vision", "용암 투시");
        addReinforcementEffect(NarakaReinforcementEffects.FIRE_RESISTANCE, "Fire Resistance", "화염 저항");
        addReinforcementEffect(NarakaReinforcementEffects.EFFICIENT_MINING_IN_WATER, "Efficient mining in water", "수중 채쿨 효율");
        addReinforcementEffect(NarakaReinforcementEffects.EFFICIENT_MINING_IN_AIR, "Efficient mining in air", "공중 채굴 효율");
        addReinforcementEffect(NarakaReinforcementEffects.WATER_BREATHING, "Water breathing", "수중 호흡");

        addTrimPattern(NarakaTrimPatterns.PURIFIED_SOUL_SILENCE, "Purified Soul Silence Armor Trim", "정화된 영혼 고요 갑옷 장식");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_REDSTONE, "Soul Infused Redstone Material", "영혼이 주입된 레드스톤 소재");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_COPPER, "Soul Infused Copper Material", "영혼이 주입된 구리 소재");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_GOLD, "Soul Infused Gold Material", "영혼이 주입된 금 소재");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_EMERALD, "Soul Infused Emerald Material", "영혼이 주입된 에메랄드 소재");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_DIAMOND, "Soul Infused Diamond Material", "영혼이 주입된 다이아몬드 소재");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_LAPIS, "Soul Infused Lapis Material", "영혼이 주입된 청금석 소재");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_AMETHYST, "Soul Infused Amethyst Material", "영혼이 주입된 자수정 소재");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_NECTARIUM, "Soul Infused Nectarium Material", "영혼이 주입된 넥타륨 소재");
        addTrimMaterial(NarakaTrimMaterials.GOD_BLOOD, "God Blood Material", "신의 피 소재");

        addAdvancement(AdvancementNarakaComponents.ROOT,
                List.of("Naraka", "나라카"),
                List.of("Lasciate ogni speranza, voi ch'entrate", "여기 들어오는 자, 모든 희망을 버려라")
        );
        addAdvancement(AdvancementNarakaComponents.SANCTUARY_COMPASS,
                List.of("Way to him", "그에게로 가는 길"),
                List.of("Get Sanctuary Compass", "생츄어리 나침반을 얻으세요")
        );
        addAdvancement(AdvancementNarakaComponents.FIND_HEROBRINE_SANCTUARY,
                List.of("Herobrine Sanctuary", "폭군의 성역"),
                List.of("Too Big!", "뭐가 이렇게 커?")
        );
        addAdvancement(AdvancementNarakaComponents.SUMMON_HEROBRINE,
                List.of("Naraka Tyrant", "나락의 폭군"),
                List.of("Summon Herobrine", "나락의 군주를 목도하세요")
        );
        addAdvancement(AdvancementNarakaComponents.KILL_HEROBRINE,
                List.of("Purified Soul", "정화된 영혼"),
                List.of("Defeat the lord of Naraka", "나락의 군주를 쓰러트리세요")
        );
        addAdvancement(AdvancementNarakaComponents.PURIFIED_SOUL_METAL,
                List.of("Soap...?", "비누...?"),
                List.of("Get purified soul metal", "정화된 영혼 금속을 얻으세요")
        );
        addAdvancement(AdvancementNarakaComponents.PURIFIED_SOUL_SWORD,
                List.of("Pure vessel", "순수한 그릇"),
                List.of("It can be anything!", "무엇이든 될 수 있어!")
        );
        addAdvancement(AdvancementNarakaComponents.GOD_BLOOD,
                List.of("God's blood", "신의 피"),
                List.of("Amazing!", "굉장해 엄청나!")
        );
        addAdvancement(AdvancementNarakaComponents.SOUL_INFUSED_MATERIALS,
                List.of("Soul infused Minerals", "영혼이 깃든 광물"),
                List.of("Make one kind of soul infused minerals", "아무 종류의 영혼이 깃든 광물을 만드세요")
        );
        addAdvancement(AdvancementNarakaComponents.STABILIZER,
                List.of("Stabilizer", "안정기"),
                List.of("Make soul stabilizer that contains large amount of soul minerals", "대량의 영혼이 깃든 광물을 저장할 수 있는 영혼 안정기를 만드세요"))
        ;
        addAdvancement(AdvancementNarakaComponents.FILL_SOUL_STABILIZER,
                List.of("Fully charged", "충전 완료"),
                List.of("Fill soul stabilizer with one kind of soul infused minerals", "영혼 안정기를 아무 종류의 영혼이 깃든 광물로 가득 채우세요")
        );
        addAdvancement(AdvancementNarakaComponents.CHALLENGERS_BLESSING,
                List.of("Challenger", "도전자"),
                List.of("Get challenger's blessing with full armor of soul armor and sword", "아무 종류의 영혼이 깃든 갑옷과 검을 가지고 도전자의 축복을 받으세요")
        );
        addAdvancement(AdvancementNarakaComponents.SOUL_SWORDS,
                List.of("Rainbow!", "무지개!"),
                List.of("Get every color type of blessed soul sword", "모든 색의 축복받은 검을 수집하세요")
        );
        addAdvancement(AdvancementNarakaComponents.ULTIMATE_SWORD,
                List.of("Long live the new Naraka tyrant", "새 나락의 폭군"),
                List.of("Get Ultimate soul sword", "궁극의 검을 얻으세요")
        );

        addAdvancement(AdvancementExtraComponents.BUY_NECTARIUM_CORE,
                List.of("What is this?", "이게 뭐지?"),
                List.of("Buy nectarium core from wandering trader", "떠돌이 상인으로부터 넥타륨 코어를 구입하세요")
        );
        addAdvancement(AdvancementExtraComponents.ACTIVATE_NECTARIUM_CORE,
                List.of("A block flowing with tears and honey", "눈물과 꿀이 흐르는 블록"),
                List.of("Activate nectarium core", "넥타륨 코어에 꿀을 부어 활성화하세요")
        );
        addAdvancement(AdvancementExtraComponents.EAT_NECTARIUM,
                List.of("Yummy", "냠냠"),
                List.of("Eat nectarium", "자라는 넥타륨을 채굴해서 먹어보세요")
        );
        addAdvancement(AdvancementExtraComponents.CRAFT_SOUL_INFUSED_NECTARIUM,
                List.of("Not eatable", "먹을 수 없잖아?!"),
                List.of("Don't eat, give it to your soul", "먹지 마세요, 영혼에 양보하세요")
        );

        addJukeboxSound(NarakaJukeboxSongs.HEROBRINE_PHASE_1, "Herobrine Phase 1", "히로빈 1 페이즈");
        addJukeboxSound(NarakaJukeboxSongs.HEROBRINE_PHASE_2, "Herobrine Phase 2", "히로빈 2 페이즈");
        addJukeboxSound(NarakaJukeboxSongs.HEROBRINE_PHASE_3, "Herobrine Phase 3", "히로빈 3 페이즈");
        addJukeboxSound(NarakaJukeboxSongs.HEROBRINE_PHASE_4, "Herobrine Phase 4", "히로빈 4 페이즈");

        addItem(NarakaItems.STIGMA_ROD, "Stigma Rod", "낙인 막대기");
        addItem(NarakaItems.STARDUST_STAFF, "Stardust Staff");
        addItem(NarakaItems.NARAKA_FIREBALL, "Naraka Fireball", "나락 화염구");
        addItem(NarakaItems.RAINBOW_SWORD, "Rainbow Sword", "무지개 검");
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
        addItem(NarakaItems.HEROBRINE_SPAWN_EGG, "Herobrine Spawn Egg", "히로빈 생성 알");
        addItem(NarakaItems.SKILL_CONTROLLER, "Skill Controller", "스킬 컨트롤러");

        addBlock(NarakaBlocks.AMETHYST_ORE, "Amethyst Ore", "자수정 광석");
        addBlock(NarakaBlocks.DEEPSLATE_AMETHYST_ORE, "Deepslate Amethyst Ore", "심층암 자수정 광석");
        addBlock(NarakaBlocks.NECTARIUM_ORE, "Nectarium Ore", "넥타륨 광석");
        addBlock(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE, "Deepslate Nectarium Ore", "심층암 넥타륨 광석");
        addBlock(NarakaBlocks.NECTARIUM_BLOCK, "Block of Nectarium", "넥타륨 블록");
        addBlock(NarakaBlocks.TRANSPARENT_BLOCK, "Block of Transparent", "투명 블록");
        addBlock(NarakaBlocks.COMPRESSED_IRON_BLOCK, "Block of Compressed Iron", "압축된 철 블록");
        addBlock(NarakaBlocks.IMITATION_GOLD_BLOCK, "Block of Imitation Gold", "거짓된 금 블록");
        addBlock(NarakaBlocks.AMETHYST_SHARD_BLOCK, "Block of Amethyst Shard", "자수정 조각 블록");

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
        addBlock(NarakaBlocks.FORGING_BLOCK, "Forging Block", "단조 블록");
        addBlock(NarakaBlocks.NECTARIUM_CORE_BLOCK, "Nectarium Core", "넥타륨 코어");
        addBlock(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK, "Nectarium Crystal", "넥타륨 결정");
        addBlock(NarakaBlocks.SOUL_STABILIZER, "Soul Stabilizer", "영혼 안정기");
        addBlock(NarakaBlocks.SOUL_SMITHING_BLOCK, "Soul Smithing Block", "영혼 대장장이 블록");

        addTooltip(NarakaBlocks.NECTARIUM_CORE_BLOCK, "Honey is dripping", "꿀이 뚝뚝 떨어져");
        addTooltip(NarakaBlocks.FORGING_BLOCK, "Smash item with a hammer...?", "아이템을 망치로 부수기..?");
        addTooltip(NarakaBlocks.SOUL_SMITHING_BLOCK, "Smash item with a hammer...?", "아이템을 망치로 부수기..?");

        addEntityType(NarakaEntityTypes.HEROBRINE, "Naraka: Herobrine", "히로빈");
        addEntityType(NarakaEntityTypes.SHADOW_HEROBRINE, "Shadow Herobrine", "그림자 히로빈");
        addEntityType(NarakaEntityTypes.THROWN_SPEAR, "Spear", "창");
        addEntityType(NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR, "Mighty Holy Spear", "강력한 성스러운 창");
        addEntityType(NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS, "Spear of Longinus", "롱기누스의 창");
        addEntityType(NarakaEntityTypes.NARAKA_FIREBALL, "Naraka Fireball", "나락 화염구");
        addEntityType(NarakaEntityTypes.STARDUST, "Stardust");

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


    public void addItem(Supplier<? extends Item> item, String... translations) {
        add(item.get().getDescriptionId(), translations);
    }

    public void addBlock(Supplier<? extends Block> block, String... translations) {
        add(block.get().asItem().getDescriptionId(), translations);
    }

    public void addTooltip(Supplier<? extends Block> block, String... translations) {
        add(LanguageKey.tooltip(block.get()), translations);
    }

    public void addEntityType(Supplier<? extends EntityType<?>> entityType, String... translations) {
        add(entityType.get().getDescriptionId(), translations);
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

    public void addReinforcementEffect(Holder<ReinforcementEffect> effect, String... translations) {
        add(LanguageKey.reinforcementEffect(effect), translations);
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
