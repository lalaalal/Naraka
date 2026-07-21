package com.yummy.naraka.fabric.data.lang;

import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.data.lang.AdvancementExtraComponents;
import com.yummy.naraka.data.lang.AdvancementNarakaComponents;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.data.lang.NarakaJadeProviderComponents;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.damagesource.NarakaDamageTypes;
import com.yummy.naraka.world.effect.NarakaMobEffects;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.entity.ai.skill.herobrine.*;
import com.yummy.naraka.world.entity.ai.skill.origin_herobrine.ChargingSkill;
import com.yummy.naraka.world.entity.ai.skill.origin_herobrine.SwordSwingSkill;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationLocations;
import com.yummy.naraka.world.item.NarakaItemTooltip;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.NarakaJukeboxSongs;
import com.yummy.naraka.world.item.SoulType;
import com.yummy.naraka.world.item.alchemy.NarakaPotions;
import com.yummy.naraka.world.item.equipment.trim.NarakaTrimMaterials;
import com.yummy.naraka.world.item.equipmentset.EquipmentSetHelper;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.world.item.Items;

import java.util.List;

public class DefaultLanguageProvider extends NarakaLanguageProviders {
    public DefaultLanguageProvider() {
        super("en_us", "ko_kr");
    }

    public static void add(FabricDataGenerator.Pack pack) {
        new DefaultLanguageProvider().addProvidersTo(pack::addProvider);
    }

    @Override
    protected void generate() {
        add(LanguageKey.ITEM_GROUP_NARAKA, "Naraka", "Naraka");
        add(LanguageKey.ITEM_GROUP_SOUL_MATERIALS, "Soul Materials", "영혼 재료");
        add(LanguageKey.ITEM_GROUP_TEST, "Naraka Test", "나락! 테스트");

        add(LanguageKey.KEY_CATEGORIES_NARAKA, "Naraka", "Naraka");
        add(LanguageKey.KEY_TOGGLE_ORE_SEE_THROUGH, "Toggle Ore See Through", "광물 투시 켜기/끄기");

        add(LanguageKey.toggleOreSeeThroughMessage(true), "Ore See Through is now enabled", "광물 투시 활성화됨");
        add(LanguageKey.toggleOreSeeThroughMessage(false), "Ore See Through is now disabled", "광물 투시 비활성화됨");

        add("container.soul_crafting", "Soul Crafter", "영혼 세공기");
        add(LanguageKey.REINFORCEMENT_KEY, "Reinforcement: %d", "강화: %d");
        add(LanguageKey.BLESSED_KEY, "Blessed", "축복받음");
        add(LanguageKey.HEROBRINE_SCARF_KEY, "Scarf Attached", "스카프 장착됨");
        add(LanguageKey.PURIFIED_SOUL_UPGRADE_KEY, "Purified Soul Upgrade", "정화된 영혼 강화");
        add(LanguageKey.PURIFIED_SOUL_UPGRADE_APPLIES_TO_KEY, "Ebony Tools, Purified Soul Weapons", "흑단나무 검, 정화된 영혼 무기");
        add(LanguageKey.PURIFIED_SOUL_UPGRADE_INGREDIENTS_KEY, "Purified Soul Metal, Soul Infused Materials", "정화된 영혼 금속, 영혼이 주입된 재료");
        add(LanguageKey.PURIFIED_SOUL_UPGRADE_BASE_SLOT_DESCRIPTION_KEY, "Add Ebony Sword, Soul Weapon", "흑단나무 무기, 정화된 영혼 검 또는 창를 놓으세요");
        add(LanguageKey.PURIFIED_SOUL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_KEY, "Add Purified Soul Metal, Soul Infused Materials", "정화된 영혼 금속, 영혼이 주입된 재료 또는 신의 피를 놓으세요");

        add(LanguageKey.CONFIG_TITLE, "Naraka Config");
        add(LanguageKey.CONFIG_CATEGORY_COMMON, "Naraka Common Config");
        addConfig(NarakaConfig.COMMON.showTestCreativeModeTab,
                List.of("Show Test Creative Mode Tab", "테스트 크리에이티브 모드 탭 표시"),
                List.of(
                        List.of("Restart required", "재시작 필요")
                )
        );
        addConfig(NarakaConfig.COMMON.enableStigma, "Enable Stigma", "낙인 활성화");
        addConfig(NarakaConfig.COMMON.stigmaStunDuration,
                List.of("Stigma Stun Duration", "낙인 스턴 지속시간"),
                List.of(
                        List.of("Stun duration in ticks", "낙인 스턴 지속 시간 (틱)")
                )
        );
        addConfig(NarakaConfig.COMMON.lockHealthRatio, "Lock Health Ratio", "체력 잠금 비율");

        add(LanguageKey.CONFIG_CATEGORY_CLIENT, "Naraka Client Config");
        addConfig(NarakaConfig.CLIENT.playHerobrineBossMusic, "Play Herobrine Boss Music", "히로빈 음악 재생");
        addConfig(NarakaConfig.CLIENT.enableOreSeeThrough, "Enable Ore See Through", "광물 투시 활성화");
        addConfig(NarakaConfig.CLIENT.oreSeeThroughRange, "Ore See Through Range", "광물 투시 거리");
        addConfig(NarakaConfig.CLIENT.cameraShakingSpeed, "Camera Shaking Speed", "카메라 흔들림 속도");
        addConfig(NarakaConfig.CLIENT.cameraShakingStrength, "Camera Shaking Strength", "카메라 흔들림 강도");
        add(LanguageKey.CONFIG_ORE_COLOR, "Ore Outline Color", "광물 외곽선 색");
        add(LanguageKey.CONFIG_ORE_COLOR_WRONG, "Wrong Format!", "잘못된 형식입니다!");

        add(LanguageKey.JADE_SOUL_CRAFTING_FUEL_KEY, "Fuel: %d", "연료: %d");
        add(NarakaJadeProviderComponents.SOUL_CRAFTING_BLOCK.translationKey, "Soul Crafting Block", "영혼 세공기");
        add(LanguageKey.JADE_SOUL_STABILIZER_KEY, "%d");
        add(NarakaJadeProviderComponents.SOUL_STABILIZER.translationKey, "Soul Stabilizer", "영혼 안정기");
        add(LanguageKey.JADE_STIGMA_KEY, ": %d", ": %d");
        add(LanguageKey.JADE_LOCKED_HEALTH_KEY, ": %d", ": %d");
        add(LanguageKey.JADE_DEATH_COUNT_KEY, "Death Count: %d", "데스카운트: %d");
        add(LanguageKey.JADE_NECTARIUM_CORE_ACTIVATED_KEY, "Activated", "활성화됨");
        add(LanguageKey.JADE_NECTARIUM_CORE_INACTIVATED_KEY, "Inactivated", "비활성화됨");
        add(LanguageKey.JADE_NECTARIUM_CORE_HONEY_KEY, "(%d left)", "(%d 남음)");
        add(NarakaJadeProviderComponents.SOUL_SMITHING_BLOCK.translationKey, "Soul Smithing Block", "영혼 대장장이 블록");
        add(NarakaJadeProviderComponents.NECTARIUM_CORE.translationKey, "Nectarium Core", "넥타륨 코어");
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

        add(LanguageKey.equipmentSet(EquipmentSetHelper.ID_BLESSED), "Blessed Set", "축복 세트");
        add(LanguageKey.equipmentSet(EquipmentSetHelper.ID_CHALLENGER), "Challenger Set", "도전자 세트");

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
        addAdvancement(AdvancementNarakaComponents.KILL_ORIGIN_HEROBRINE,
                List.of("Naraka Conqueror", "나락의 정복자"),
                List.of("Defeat the origin of Naraka", "나락의 근원을 쓰러트리세요")
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
                List.of("Craft any type of soul-infused mineral", "아무 종류의 영혼이 깃든 광물을 만드세요")
        );
        addAdvancement(AdvancementNarakaComponents.STABILIZER,
                List.of("Stabilizer", "안정기"),
                List.of("Craft a soul stabilizer that can store a large amount of soul minerals", "대량의 영혼이 깃든 광물을 저장할 수 있는 영혼 안정기를 만드세요"))
        ;
        addAdvancement(AdvancementNarakaComponents.FILL_SOUL_STABILIZER,
                List.of("Fully charged", "충전 완료"),
                List.of("Completely fill a soul stabilizer with one type of soul-infused mineral", "영혼 안정기를 아무 종류의 영혼이 깃든 광물로 가득 채우세요")
        );
        addAdvancement(AdvancementNarakaComponents.CHALLENGERS_BLESSING,
                List.of("Challenger", "도전자"),
                List.of("Receive the Challenger's Blessing while wearing a full set of Soul Armor and holding its matching sword", "아무 종류의 영혼이 깃든 갑옷과 검을 가지고 도전자의 축복을 받으세요")
        );
        addAdvancement(AdvancementNarakaComponents.SOUL_SWORDS,
                List.of("Rainbow!", "무지개!"),
                List.of("Collect every color variant of the blessed soul swords", "모든 색의 축복받은 검을 수집하세요")
        );
        addAdvancement(AdvancementNarakaComponents.ULTIMATE_SPEAR,
                List.of("Long live the new Naraka tyrant", "새 나락의 폭군"),
                List.of("Get Ultimate soul spear", "궁극의 창을 얻으세요")
        );

        addAdvancement(AdvancementExtraComponents.BUY_NECTARIUM_CORE,
                List.of("What is this?", "이게 뭐지?"),
                List.of("Purchase a Nectarium Core from a Wandering Trader", "떠돌이 상인으로부터 넥타륨 코어를 구입하세요")
        );
        addAdvancement(AdvancementExtraComponents.ACTIVATE_NECTARIUM_CORE,
                List.of("A block flowing with tears and honey", "눈물과 꿀이 흐르는 블록"),
                List.of("Activate nectarium core", "넥타륨 코어에 꿀을 부어 활성화하세요")
        );
        addAdvancement(AdvancementExtraComponents.EAT_NECTARIUM,
                List.of("Yummy", "냠냠"),
                List.of("Mine and eat Nectarium", "자라는 넥타륨을 채굴해서 먹어보세요")
        );
        addAdvancement(AdvancementExtraComponents.CRAFT_SOUL_INFUSED_NECTARIUM,
                List.of("Inedible!", "먹을 수 없잖아?!"),
                List.of("Don't eat, give it to your soul", "먹지 마세요, 영혼에 양보하세요")
        );

        addJukeboxSound(NarakaJukeboxSongs.HEROBRINE_PHASE_1, "Herobrine Phase 1", "히로빈 1 페이즈");
        addJukeboxSound(NarakaJukeboxSongs.HEROBRINE_PHASE_2, "Herobrine Phase 2", "히로빈 2 페이즈");
        addJukeboxSound(NarakaJukeboxSongs.HEROBRINE_PHASE_3, "Herobrine Phase 3", "히로빈 3 페이즈");
        addJukeboxSound(NarakaJukeboxSongs.HEROBRINE_PHASE_4, "Herobrine Phase 4", "히로빈 4 페이즈");

        addPotion(Items.POTION, NarakaPotions.CHALLENGER, "Challenger's Potion", "도전자의 물약");
        addPotion(Items.SPLASH_POTION, NarakaPotions.CHALLENGER, "Challenger's Splash Potion", "도전자의 물약");
        addPotion(Items.LINGERING_POTION, NarakaPotions.CHALLENGER, "Challenger's Lingering Potion", "도전자의 물약");
        addPotion(Items.POTION, NarakaPotions.BLESS, "Potion of Bless", "축복의 물약");
        addPotion(Items.SPLASH_POTION, NarakaPotions.BLESS, "Splash Potion of Bless", "투척용 축복의 물약");
        addPotion(Items.LINGERING_POTION, NarakaPotions.BLESS, "Lingering Potion of Bless", "잔류형 축복의 물약");

        addItem(NarakaItems.STIGMA_ROD, "Stigma Rod", "낙인 막대기");
        addItem(NarakaItems.STARDUST_STAFF, "Stardust Staff");
        addItem(NarakaItems.NARAKA_FIREBALL_STAFF, "Naraka Fireball Staff");
        addItem(NarakaItems.RAINBOW_SWORD, "Rainbow Sword", "무지개 검");
        addItem(NarakaItems.PURIFIED_SOUL_SHARD, "Purified Soul Shard", "정화된 영혼 조각");
        addItem(NarakaItems.NECTARIUM, "Nectarium", "넥타륨");
        addItem(NarakaItems.GOD_BLOOD, "§lGod Blood", "§l신의 피");
        addItem(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, "Soul Smithing Template", "영혼 대장장이 형판");

        addItem(NarakaItems.SPEAR_ITEM, "Spear", "창");
        addItem(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, "Mighty Holy Spear", "강력한 성스러운 창");
        addItem(NarakaItems.SPEAR_OF_LONGINUS_ITEM, "Spear of Longinus", "롱기누스의 창");

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

        addItem(NarakaItems.SANCTUARY_COMPASS, "Sanctuary Compass", "생츄어리 나침반");

        addItem(NarakaItems.HEROBRINE_PHASE_1_DISC, "Herobrine Phase 1 Disc", "히로빈 1 페이즈 음반");
        addItem(NarakaItems.HEROBRINE_PHASE_2_DISC, "Herobrine Phase 2 Disc", "히로빈 2 페이즈 음반");
        addItem(NarakaItems.HEROBRINE_PHASE_3_DISC, "Herobrine Phase 3 Disc", "히로빈 3 페이즈 음반");
        addItem(NarakaItems.HEROBRINE_PHASE_4_DISC, "Herobrine Phase 4 Disc", "히로빈 4 페이즈 음반");
        addItem(NarakaItems.HEROBRINE_SPAWN_EGG, "Herobrine Spawn Egg", "히로빈 생성 알");
        addItem(NarakaItems.DIAMOND_GOLEM_SPAWN_EGG, "Diamond Golem Spawn Egg", "다이아몬드 골렘 생성 알");
        addItem(NarakaItems.SKILL_CONTROLLER, "Skill Controller", "스킬 컨트롤러");
        addItem(NarakaItems.ANIMATION_CONTROLLER, "Animation Controller", "애니메이션 컨트롤러");
        addItem(NarakaItems.HEROBRINE_SCARF, "Herobrine Scarf", "히로빈 스카프");
        addItem(NarakaItems.NARAKA_PICKAXE, "Naraka Pickaxe", "파멸의 낫");

        addItem(NarakaItems.LOCKED_HEALTH, "Locked Health");

        addBlock(NarakaBlocks.AMETHYST_ORE, "Amethyst Ore", "자수정 광석");
        addBlock(NarakaBlocks.DEEPSLATE_AMETHYST_ORE, "Deepslate Amethyst Ore", "심층암 자수정 광석");
        addBlock(NarakaBlocks.NECTARIUM_ORE, "Nectarium Ore", "넥타륨 광석");
        addBlock(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE, "Deepslate Nectarium Ore", "심층암 넥타륨 광석");
        addBlock(NarakaBlocks.NECTARIUM_BLOCK, "Block of Nectarium", "넥타륨 블록");
        addBlock(NarakaBlocks.TRANSPARENT_BLOCK, "Block of Transparent", "투명 블록");
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

        addBlock(NarakaBlocks.HEROBRINE_TOTEM, "Herobrine Totem", "히로빈 토템");
        addBlock(NarakaBlocks.PURIFIED_SOUL_LANTERN, "Purified Soul Lantern", "정화된 영혼 랜턴");
        addBlock(NarakaBlocks.PURIFIED_SOUL_LAMP, "Purified Soul Lamp", "정화된 영혼 조명");
        addBlock(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK, "Purified Soul Fire", "정화된 영혼 불");
        addBlock(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK, "Block of Purified Soul Metal", "정화된 영혼 금속 블록");
        addBlock(NarakaBlocks.NECTARIUM_CORE_BLOCK, "Nectarium Core", "넥타륨 코어");
        addBlock(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK, "Nectarium Crystal", "넥타륨 결정");
        addBlock(NarakaBlocks.SOUL_STABILIZER, "Soul Stabilizer", "영혼 안정기");
        addBlock(NarakaBlocks.SOUL_SMITHING_BLOCK, "Soul Smithing Block", "영혼 대장장이 블록");
        addBlock(NarakaBlocks.NARAKA_PORTAL, "Naraka Portal", "나락 포탈");

        addTooltip(NarakaItemTooltip.HEROBRINE_SCARF, List.of(
                List.of("Can be equipped in the chestplate slot or combined with armor using an anvil.", "흉갑 슬롯에 착용하거나, 모루를 통해 갑옷에 합성할 수 있다."),
                List.of("Along the inner side of the cloak, the shape of the [Naraka] where it once existed shimmers.", "망토의 내면을 따라, 그것이 존재했던 [나락]의 형상이 일렁거린다."),
                List.of("However, this merely reflects it; one cannot enter or leave [Naraka] through this.", "다만 이것은 그저 비출 뿐, 이것을 통해 [나락]을 드나들 수는 없다.")
        ));
        addTooltip(NarakaItemTooltip.NARAKA_PICKAXE, List.of(
                List.of("Can be used as a pickaxe.", "곡괭이로 사용할 수 있다."),
                List.of("Wandering through other realms in search of [Attributes], trampling and absorbing everything in its path.", "[속성]을 찾아 이계를 떠돌며, 발길이 닿는 대로 짓밟아 흡수했다."),
                List.of("An entity that always appears through Naraka, leaving only emptiness in the worlds it invades before vanishing.", "항상 나락을 통해 나타나, 침입한 세계에 공허만을 남기고 사라지는 존재."),
                List.of("Thus, in some worlds it is called the 'God of the Void', and in others, the 'Tyrant of Naraka'.", "그렇기에 어느 세계에서는 '공허의 신', 어느 세계에서는 '나락의 폭군'이라고 불린다.")
        ));
        addTooltip(NarakaItemTooltip.GOD_BLOOD, List.of(
                List.of("Used as a material for powerful equipment.", "강력한 장비의 재료로 사용된다."),
                List.of("It can also be charged into a 'Soul Stabilizer' to forge powerful equipment.", "또한, '영혼 안정기'에 충전하여 강력한 장비 단조에 사용할 수도 있다."),
                List.of("The essence of a name filled with all kinds of terror and awe is gluttony to fill its void.", "온갖 공포와 경외를 담은 이름의 본질은 결핍을 채우기 위한 탐식."),
                List.of("Unable to obtain color, longing for color, it sought to strip color away and add it to itself.", "색을 얻지 못하고, 색을 갈망하며, 색을 빼앗아 자신에게 더하려 했다."),
                List.of("But no matter what is added, black is merely black. Even the remaining essence is nothing but pitch-black emptiness.", "하나 무엇을 더한들 검은색은 그저 검은색. 남은 유해마저도 새까만 허무일 뿐.")
        ));
        addTooltip(NarakaItemTooltip.SPEAR_OF_LONGINUS, List.of(
                List.of("Can be thrown, and automatically returns to its owner.", "투척할 수 있으며, 주인에게 자동으로 되돌아온다."),
                List.of("Instantly kills enemies hit, regardless of the attack method.", "공격 방식에 관계 없이 적중한 적을 즉사시킨다."),
                List.of("Color is individuality; in that sense, color is no different from the independence of every living being.", "색은 곧 개성, 그 점에 있어서 색은 생명체 개개인의 독립성과도 다르지 않다."),
                List.of("This spear is a weapon crafted from the blood of that entity, which lusted for color until the moment of its demise.", "이 창은, 소멸하는 순간까지도 색을 갈망했던 그것의 피를 사용하여 만든 무기."),
                List.of("Thus, it devours the color—and even the independence—of whatever it pierces, erasing it from this world.", "그렇기에 찔러 넣은 대상의 색, 더 나아가 독립성까지도 포식해 이 세상에서 지워낸다.")
        ));
        addTooltip(NarakaItemTooltip.SANCTUARY_COMPASS, List.of(
                List.of("Points in the direction of the nearest 'Herobrine Sanctuary' structure in the current world.", "현재 월드에 있는 가장 가까운 '히로빈 생츄어리' 건축물이 있는 방향을 가리킨다."),
                List.of("The world tends to manipulate cause and effect for its own survival.", "세계는 그 자신의 존속을 위해 인과를 조정하는 경향을 보인다."),
                List.of("As part of that effort, this will always point toward the sanctuary of the entity that invaded the world.", "이것은 그 일환으로서, 언제나 세계를 침범한 그것의 성역을 가리킬 것이다."),
                List.of("Waiting for a hero to appear who can defeat it and preserve the world's existence.", "그것을 쓰러뜨리고 세계의 존속을 이어나갈 수 있는, 영웅이 나타나기를 기다리며.")
        ));
        addTooltip(NarakaItemTooltip.IMITATION_GOLD, List.of(
                List.of("When placed, converts Iron Blocks within a 3x3x3 area around itself into Imitation Gold Blocks.", "설치되어 있으면, 자신 중심 3*3*3 공간 내의 철 블록을 거짓된 금 블럭으로 변화시킨다."),
                List.of("Also used to duplicate 'Herobrine Totem'.", "또한 '히로빈 토템'을 복사하는 데에도 사용된다."),
                List.of("Gold holds radiance, and that radiance summons it.", "금은 광채를 품고, 광채는 그것을 불러낸다."),
                List.of("This gold is a false creation, holding a radiance weaker than true gold.", "이 금은 원래의 금보다 미약한 광채를 품은, 거짓된 것."),
                List.of("Yet, that faint radiance alone is more than enough to guide its steps.", "하나 그 광채만으로도, 그것의 발을 이끄는 데에는 부족함이 없다.")
        ));
        addTooltip(NarakaItemTooltip.HEROBRINE_TOTEM, List.of(
                List.of("Can summon Herobrine by stacking 2 Imitation Gold Blocks, a Herobrine Totem, and 1 Netherrack from bottom to top, then lighting it on fire.", "거짓된 금 블록 2개, 히로빈 토템, 네더랙 1개를 아래에서부터 순서대로 쌓아 올리고 불을 붙여 히로빈을 불러낼 수 있다."),
                List.of("Herobrine can only be summoned inside a 'Herobrine Sanctuary' structure.", "히로빈은 '히로빈 생츄어리' 건축물 내에서만 소환할 수 있다."),
                List.of("A stone tablet carved with its face.", "그것의 얼굴이 새겨진 비석."),
                List.of("When the false gold catches its gaze and leads its path, this stone summons and binds its physical form to this world.", "거짓된 금이 그것의 눈길을 사로잡고 발걸음을 이끌면, 이것이 그것의 육신을 이 세계에 불러들이고, 고정시킨다.")
        ));
        addTooltip(NarakaItemTooltip.SOUL_INFUSED_MATERIALS, List.of(
                List.of("Used to charge the 'Soul Stabilizer'.", "'영혼 안정기'를 충전하는 데 사용한다."),
                List.of("An item infused with fragments of its color-craving soul into minerals emitting the most vibrant colors.", "색을 갈망하는 그것의 영혼 편린을, 가장 강렬한 색채를 발하는 광물에 스미게 한 물건.")
        ));
        addTooltip(NarakaItemTooltip.SOUL_STABILIZER, List.of(
                List.of("After placing, right-click with Soul-Infused Minerals or God Blood to charge it.", "설치 후, 영혼이 주입된 광물이나 신의 피를 우클릭하여 충전할 수 있다."),
                List.of("Charging must be done using only one type of item.", "충전은 한 종류의 아이템으로만 이루어져야 한다.")
        ));
        addTooltip(NarakaItemTooltip.SOUL_SMITHING_BLOCK, List.of(
                List.of("After placing, equip a charged Soul Stabilizer and a Smithing Template to forge specific equipment.", "설치 후, 충전된 영혼 안정기와 대장장이 형판을 장착해 특정 장비를 단조할 수 있다."),
                List.of("Forging consumes a set amount of minerals or God Blood charged in the Soul Stabilizer.", "단조 시, 영혼 안정기에 충전된 광물이나 신의 피를 일정량 소모한다.")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_METAL, List.of(
                List.of("Used as a material for rare equipment.", "희귀한 장비의 재료로 사용된다."),
                List.of("Though called a metal, it is closer to its scattered soul taking form and hardening.", "금속이라고 칭하지만, 흩어진 그것의 영혼이 형체를 이루어 굳은 것에 가깝다.")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_SWORD, List.of(
                List.of("Right-click to ignite black fire. The black fire can only be extinguished by left-clicking with this item.", "우클릭으로 검은 불을 붙일 수 있다. 검은 불은 해당 아이템의 좌클릭으로만 제거할 수 있다."),
                List.of("Can be forged at the Soul Smithing Block using minerals or God Blood.", "영혼 대장장이 작업대에서 광물이나 신의 피를 사용해 단조할 수 있다."),
                List.of("The purest vessel.", "가장 순수한 그릇."),
                List.of("Imbuing it with the color of a mineral turns it into a necessary sacrifice for the ritual.", "광물이 지닌 색을 깃들게 하면, 의식에 필요한 제물로 변화한다.")
        ));
        addTooltip(NarakaItemTooltip.SOUL_INFUSED_SWORDS, List.of(
                List.of("Wearing 4 pieces of Soul Armor forged from the same mineral as this sword activates the 'Challenger' buff.", "해당 검의 광물과 동일한 광물로 단조된 영혼 갑옷 4개를 착용할 시, '도전자' 버프가 활성화된다."),
                List.of("Defeating Herobrine with the 'Challenger' buff bestows a blessing upon the equipment while completely destroying all armor.", "'도전자' 버프를 가진 상태로 히로빈을 처치할 경우, 해당 장비에 축복이 내려지며 갑옷이 전부 파괴된다.")
        ));
        addTooltip(NarakaItemTooltip.SOUL_INFUSED_SWORDS_BLESSED, List.of(
                List.of("Used as a material for the 'Spear of Longinus'.", "'롱기누스의 창'의 재료로 사용된다.")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_ARMORS, List.of(
                List.of("Can be forged at the Soul Smithing Block using minerals or God Blood.", "영혼 대장장이 작업대에서 광물이나 신의 피를 사용해 단조할 수 있다.")
        ));
        addTooltip(NarakaItemTooltip.NECTARIUM, List.of(
                List.of("Edible; consuming it fully restores saturation, hunger, and health to maximum.", "먹을 수 있으며, 먹을 시 포만도와 허기, 체력이 최대치로 회복된다.")
        ));
        addTooltip(NarakaItemTooltip.NECTARIUM_CORE, List.of(
                List.of("Can be activated using a Honey Bottle, which generates Nectarium Icicles when active.", "꿀병을 사용해 활성화시킬 수 있으며, 활성화시킬 경우 넥타륨 고드름을 만들어낸다.")
        ));

        addEntityType(NarakaEntityTypes.HEROBRINE, "Naraka Tyrant: Herobrine", "나락의 폭군: 히로빈");
        addEntityType(NarakaEntityTypes.ORIGIN_HEROBRINE, "The Origin of Naraka: Herobrine", "나락의 근원: 히로빈");
        addEntityType(NarakaEntityTypes.SHADOW_HEROBRINE, "Shadow Herobrine", "그림자 히로빈");
        addEntityType(NarakaEntityTypes.THROWN_SPEAR, "Spear", "창");
        addEntityType(NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR, "Mighty Holy Spear", "강력한 성스러운 창");
        addEntityType(NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS, "Spear of Longinus", "롱기누스의 창");
        addEntityType(NarakaEntityTypes.NARAKA_FIREBALL, "Naraka Fireball", "나락 화염구");
        addEntityType(NarakaEntityTypes.STARDUST, "Stardust", "부패의 별");
        addEntityType(NarakaEntityTypes.PICKAXE_SLASH, "Pickaxe Slash");
        addEntityType(NarakaEntityTypes.DIAMOND_GOLEM, "Diamond Golem", "다이아몬드 골렘");
        addEntityType(NarakaEntityTypes.MAGIC_CIRCLE, "Magic Circle", "마법진");
        addEntityType(NarakaEntityTypes.NARAKA_PICKAXE, "Naraka Pickaxe", "파멸의 낫");
        addEntityType(NarakaEntityTypes.COLORED_LIGHTNING_BOLT, "Colored Lightning Bolt");
        addEntityType(NarakaEntityTypes.MASSIVE_LIGHTNING, "Massive Lightning");
        addEntityType(NarakaEntityTypes.CORRUPTED_STAR, "Corrupted Star", "부패의 별");
        addEntityType(NarakaEntityTypes.SHINY_EFFECT, "Shiny Effect");
        addEntityType(NarakaEntityTypes.AREA_EFFECT, "Area Effect");
        addEntityType(NarakaEntityTypes.NARAKA_PORTAL, "Naraka Portal");
        addEntityType(NarakaEntityTypes.LIGHTNING_CIRCLE, "Lightning Circle");

        addDamageType(NarakaDamageTypes.SPEAR_OF_LONGINUS, "%1$s's AT Field was torn by %2$s", "%1$s의 AT 필드가 %2$s에 찢어졌습니다");
        addDamageType(NarakaDamageTypes.STIGMA, "%1$s's has been deprived of life due to %2$s's stigma stacking", "%1$s이(가) %2$s의 낙인이 중첩돼 생명을 빼앗겼습니다");
        addDamageType(NarakaDamageTypes.STIGMA_CONSUME, "%2$s has been consumed %1$s's stigma", "%2$s이(가) %1$s의 낙인을 소모해 생명을 앗아갔습니다");
        addDamageType(NarakaDamageTypes.PICKAXE_SLASH, "%1$s was slain by %2$s", "%1$s이(가) %2$s에 썰렸습니다");
        addDamageType(NarakaDamageTypes.NARAKA_FIREBALL, "%1$s was blown by Naraka Fireball thrown by %2$s", "%1$s이(가) %2$s이(가) 던진 나락 화염구에 폭발했습니다");
        addDamageType(NarakaDamageTypes.STARDUST, "%1$s was exploded by %2$s", "%1$s이(가) %2$s에 폭사했습니다");
        addDamageType(NarakaDamageTypes.PURIFIED_SOUL_FIRE, "%1$s went up in black flames", "%1$s이(가) 검은 불 속에서 타 죽었습니다");
        addDamageType(NarakaDamageTypes.SOUL_ATTACK, "%2$s executed %1$s");

        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_AMETHYST), "Challenger's Blessing", "도전자의 축복");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_COPPER), "Challenger's Blessing", "도전자의 축복");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_DIAMOND), "Challenger's Blessing", "도전자의 축복");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_EMERALD), "Challenger's Blessing", "도전자의 축복");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_GOLD), "Challenger's Blessing", "도전자의 축복");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_LAPIS), "Challenger's Blessing", "도전자의 축복");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_NECTARIUM), "Challenger's Blessing", "도전자의 축복");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_REDSTONE), "Challenger's Blessing", "도전자의 축복");
        add(LanguageKey.mobEffect(NarakaMobEffects.GOD_BLESS), "God Bless", "신의 축복");

        add(LanguageKey.animation(HerobrineAnimationLocations.COMBO_ATTACK_1), "P1 Combo Attack 1", "연계 공격 1");
        add(LanguageKey.animation(HerobrineAnimationLocations.COMBO_ATTACK_2), "P1Combo Attack 2", "연계 공격 2");
        add(LanguageKey.animation(HerobrineAnimationLocations.COMBO_ATTACK_3), "P1 Combo Attack 3", "연계 공격 3");
        add(LanguageKey.animation(HerobrineAnimationLocations.COMBO_ATTACK_4), "P1 Combo Attack 4", "연계 공격 4");
        add(LanguageKey.animation(HerobrineAnimationLocations.COMBO_ATTACK_5), "P1 Combo Attack 5", "연계 공격 5");
        add(LanguageKey.animation(HerobrineAnimationLocations.IDLE), "P1 Idle", "대기");
        add(LanguageKey.animation(HerobrineAnimationLocations.WALKING), "P1 Walking", "걷기");
        add(LanguageKey.animation(HerobrineAnimationLocations.PHASE_3_IDLE), "P3 Idle", "3페 걷기");
        add(LanguageKey.animation(HerobrineAnimationLocations.ENTER_PHASE_2), "P1 Enter Phase 2", "2페 진입");
        add(LanguageKey.animation(HerobrineAnimationLocations.PREPARE_PHASE_3), "P2 Prepare Phase 3", "3페 준비");
        add(LanguageKey.animation(HerobrineAnimationLocations.ENTER_PHASE_3), "P3 Enter Phase 3", "3페 진입");
        add(LanguageKey.animation(HerobrineAnimationLocations.STAGGERING), "P1 Staggering", "허약");
        add(LanguageKey.animation(HerobrineAnimationLocations.STAGGERING_PHASE_2), "P1 Staggering Enter Phase 2", "허약 2페 진입");
        add(LanguageKey.animation(HerobrineAnimationLocations.BLOCKING), "P1 Blocking");
        add(LanguageKey.animation(HerobrineAnimationLocations.RUSH), "P1 Rush", "돌진");
        add(LanguageKey.animation(HerobrineAnimationLocations.RUSH_SUCCEED), "P1 Rush Succeed", "돌진 성공");
        add(LanguageKey.animation(HerobrineAnimationLocations.RUSH_FAILED), "P1 Rush Failed", "돌진 실패");
        add(LanguageKey.animation(HerobrineAnimationLocations.STIGMATIZE_ENTITIES), "P2 Gimmick", "2 페이즈 기믹");
        add(LanguageKey.animation(HerobrineAnimationLocations.STIGMATIZE_ENTITIES_START), "P2 Gimmick Start", "2 페이즈 기믹 시작");
        add(LanguageKey.animation(HerobrineAnimationLocations.STIGMATIZE_ENTITIES_END), "P2 Gimmick End", "2 페이즈 기믹 종료");
        add(LanguageKey.animation(HerobrineAnimationLocations.THROW_NARAKA_FIREBALL), "P1 Throw Naraka Fireball", "화염구 투척");

        add(LanguageKey.animation(HerobrineAnimationLocations.FINAL_COMBO_ATTACK_1), "P3 Combo Attack 1");
        add(LanguageKey.animation(HerobrineAnimationLocations.FINAL_COMBO_ATTACK_2), "P3 Combo Attack 2");
        add(LanguageKey.animation(HerobrineAnimationLocations.FINAL_COMBO_ATTACK_3), "P3 Combo Attack 3");
        add(LanguageKey.animation(HerobrineAnimationLocations.FINAL_COMBO_ATTACK_1_RETURN), "P3 Combo Attack 1 Return");
        add(LanguageKey.animation(HerobrineAnimationLocations.FINAL_COMBO_ATTACK_2_RETURN), "P3 Combo Attack 2 Return");
        add(LanguageKey.animation(HerobrineAnimationLocations.CHZZK), "Chzzk");
        add(LanguageKey.animation(HerobrineAnimationLocations.HIDDEN_CHZZK), "Hidden Chzzk");
        add(LanguageKey.animation(HerobrineAnimationLocations.CARPET_BOMBING), "P3 Carpet Bombing");
        add(LanguageKey.animation(HerobrineAnimationLocations.STAR_SHOOTING_1), "P3 Star Shooting 1");
        add(LanguageKey.animation(HerobrineAnimationLocations.STAR_SHOOTING_2), "P3 Star Shooting 2");
        add(LanguageKey.animation(HerobrineAnimationLocations.STAR_SHOOTING_3), "P3 Star Shooting 3");
        add(LanguageKey.animation(HerobrineAnimationLocations.EXPLOSION), "P3 Explosion");
        add(LanguageKey.animation(HerobrineAnimationLocations.DYING), "Dying");
        add(LanguageKey.animation(HerobrineAnimationLocations.PARRYING), "P3 Parrying", "패링");
        add(LanguageKey.animation(HerobrineAnimationLocations.PARRYING_SUCCEED), "P3 Parrying Succeed", "패링 성공");
        add(LanguageKey.animation(HerobrineAnimationLocations.PARRYING_FAILED), "P3 Parrying Failed", "패링 실패");
        add(LanguageKey.animation(HerobrineAnimationLocations.RYOIKI_TENKAI), "P3 Ryoiki Tenkai", "영역 전개");
        add(LanguageKey.animation(HerobrineAnimationLocations.PICKAXE_SLASH_TRIPLE), "P3 Triple Pickaxe Slash");
        add(LanguageKey.animation(HerobrineAnimationLocations.PICKAXE_SLASH_SINGLE), "P3 Single Pickaxe Slash");
        add(LanguageKey.animation(HerobrineAnimationLocations.STORM), "P3 Storm", "폭풍");
        add(LanguageKey.animation(HerobrineAnimationLocations.EARTH_SHOCK), "P3 Earth Shock");

        add(LanguageKey.animation(HerobrineAnimationLocations.CHARGING), "Charging", "충전");
        add(LanguageKey.animation(HerobrineAnimationLocations.SWORD_ATTACK), "Sword Attack");
        add(LanguageKey.animation(HerobrineAnimationLocations.SWORD_ATTACK_SPIN), "Sword Attack Spin");

        add(LanguageKey.skill(RushSkill.LOCATION), "P1 Rush");
        add(LanguageKey.skill(DashSkill.LOCATION), "P1 Dash");
        add(LanguageKey.skill(DashAroundSkill.LOCATION), "P1 Dash Around");
        add(LanguageKey.skill(PunchSkill.LOCATION), "P1 Combo Attack 1");
        add(LanguageKey.skill(UppercutSkill.LOCATION), "P1 Combo Attack 2");
        add(LanguageKey.skill(SpinningSkill.LOCATION), "P1 Combo Attack 3");
        add(LanguageKey.skill(SuperHitSkill.LOCATION), "P1 Combo Attack 4");
        add(LanguageKey.skill(LandingSkill.LOCATION), "P1 Combo Attack 5");
        add(LanguageKey.skill(ThrowFireballSkill.LOCATION), "P1 Throw Fireball");
        add(LanguageKey.skill(StigmatizeEntitiesSkill.LOCATION), "P2 Gimmick");
        add(LanguageKey.skill(WalkAroundTargetSkill.LOCATION), "P1 Walk Around");
        add(LanguageKey.skill(FlickerSkill.LOCATION), "P1 Flicker");
        add(LanguageKey.skill(StormSkill.LOCATION), "P3 #1 REDSTONE [Storm]");
        add(LanguageKey.skill(StarShootingSkill.LOCATION), "P3 #2 COPPER [Star Shooting]");
        add(LanguageKey.skill(ExplosionSkill.LOCATION), "P3 #3 GOLD [Explosion]");
        add(LanguageKey.skill(RyoikiTenkaiSkill.LOCATION), "P3 #4 EMERALD [Ryoiki Tenkai]");
        add(LanguageKey.skill(SplitAttackSkill.LOCATION), "P3 #5 DIAMOND [Combo Attack 1]");
        add(LanguageKey.skill(SpinUpSkill.LOCATION), "P3 #5 DIAMOND [Combo Attack 2]");
        add(LanguageKey.skill(StrikeDownSkill.LOCATION), "P3 #5 DIAMOND [Combo Attack 3]");
        add(LanguageKey.skill(PickaxeSlashSkill.SINGLE), "P3 #6 LAPIS [Pickaxe Slash Single]");
        add(LanguageKey.skill(PickaxeSlashSkill.TRIPLE), "P3 #6 LAPIS [Pickaxe Slash Triple]");
        add(LanguageKey.skill(EarthShockSkill.LOCATION), "P3 #7 AMETHYST [Earth Shock]");
        add(LanguageKey.skill(ParryingSkill.LOCATION), "P3 #8 NECTARIUM [Parrying]");
        add(LanguageKey.skill(DestroyStructureSkill.LOCATION), "P3 Destroy Structure (Enter Phase 3)");
        add(LanguageKey.skill(SummonShadowSkill.LOCATION), "P1 Summon Shadow");
        add(LanguageKey.skill(CarpetBombingSkill.LOCATION), "P3 Carpet Bombing");
        add(LanguageKey.skill(SpawnPickaxeSkill.LOCATION), "P3 Pickaxe Strike");
        add(LanguageKey.skill(BlockingSkill.LOCATION), "P1 Blocking");
        add(LanguageKey.skill(ChargingSkill.LOCATION), "Charging");
        add(LanguageKey.skill(SwordSwingSkill.LOCATION), "Sword Swing");

        add(LanguageKey.STIGMA_COMMAND_GET_KEY, "Stigma for entity %s is %d");
        add(LanguageKey.STIGMA_COMMAND_SET_KEY, "Stigma for %d entities set to %d", "%d개의 엔티티의 낙인을 %d로 설정하였습니다");
        add(LanguageKey.STIGMA_COMMAND_INCREASE_KEY, "Increased stigma for %1$d entities", "%1$d개의 엔티티의 낙인을 증가시켰습니다");
        add(LanguageKey.STIGMA_COMMAND_REMOVE_KEY, "Removed stigma for %1$d entities", "%1$d개의 엔티티의 낙인을 제거했습니다");
        add(LanguageKey.STIGMA_COMMAND_CONSUME_KEY, "%2$s consumed %1$d entities' stigma", "%2$s(이)가 %1$d개의 엔티티의 낙인을 소모했습니다");
        add(LanguageKey.STIGMA_COMMAND_DISABLE_KEY, "Disabled stigma", "낙인이 비활성화되었습니다");
        add(LanguageKey.STIGMA_COMMAND_ENABLE_KEY, "Enabled stigma", "낙인이 활성화되었습니다");

        add(LanguageKey.LOCK_HEALTH_COMMAND_LOCK_KEY, "Locked %2$s health %1$d", "%2$s의 체력을 %1$d 잠궜습니다");
        add(LanguageKey.LOCK_HEALTH_COMMAND_REMOVE_KEY, "Removed %1$s locked healths", "%1$s의 잠긴 체력을 해제했습니다");
    }
}
