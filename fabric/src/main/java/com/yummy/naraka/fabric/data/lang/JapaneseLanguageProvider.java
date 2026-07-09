package com.yummy.naraka.fabric.data.lang;

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
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.SoulType;
import com.yummy.naraka.world.item.alchemy.NarakaPotions;
import com.yummy.naraka.world.item.equipment.trim.NarakaTrimMaterials;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.world.item.Items;

import java.util.List;

public class JapaneseLanguageProvider extends NarakaLanguageProviders {
    public JapaneseLanguageProvider() {
        super("ja_jp");
    }

    public static void add(FabricDataGenerator.Pack pack) {
        new JapaneseLanguageProvider().addProvidersTo(pack::addProvider);
    }

    @Override
    protected void generate() {
        add(LanguageKey.ITEM_GROUP_NARAKA, "Naraka");
        add(LanguageKey.ITEM_GROUP_SOUL_MATERIALS, "魂の宿った物");
        add(LanguageKey.ITEM_GROUP_TEST, "Naraka テストアイテム");

        add(LanguageKey.KEY_CATEGORIES_NARAKA, "Naraka");
        add(LanguageKey.KEY_TOGGLE_ORE_SEE_THROUGH, "鉱石の透過の表示/非表示を切り替える");

        add(LanguageKey.toggleOreSeeThroughMessage(false), "鉱石の透過を表示にしました");
        add(LanguageKey.toggleOreSeeThroughMessage(true), "鉱石の透過を非表示にしました");

        add(LanguageKey.REINFORCEMENT_KEY, "アップグレード: %d");
        add(LanguageKey.BLESSED_KEY, "奈落の祝福");
        add(LanguageKey.HEROBRINE_SCARF_KEY, "スカーフ付き");

        add(LanguageKey.JADE_SOUL_STABILIZER_KEY, "%d");
        add(NarakaJadeProviderComponents.SOUL_STABILIZER.translationKey, "ソウルスタビライザー");
        add(LanguageKey.JADE_STIGMA_KEY, ": %d");
        add(LanguageKey.JADE_LOCKED_HEALTH_KEY, ": %d");
        add(LanguageKey.JADE_DEATH_COUNT_KEY, "Death Count: %d");
        add(LanguageKey.JADE_NECTARIUM_CORE_ACTIVATED_KEY, "活性化");
        add(LanguageKey.JADE_NECTARIUM_CORE_INACTIVATED_KEY, "不活性化");
        add(LanguageKey.JADE_NECTARIUM_CORE_HONEY_KEY, "(%d)");
        add(NarakaJadeProviderComponents.SOUL_SMITHING_BLOCK.translationKey, "魂の鍛冶台");
        add(NarakaJadeProviderComponents.NECTARIUM_CORE.translationKey, "ネクタリウムコア");
        add(NarakaJadeProviderComponents.ENTITY_DATA.translationKey, "スティグマ");

        add(SoulType.REDSTONE.translationKey(), "レッドストーン");
        add(SoulType.COPPER.translationKey(), "銅");
        add(SoulType.GOLD.translationKey(), "金");
        add(SoulType.EMERALD.translationKey(), "エメラルド");
        add(SoulType.DIAMOND.translationKey(), "ダイヤモンド");
        add(SoulType.LAPIS.translationKey(), "ラピスラズリ");
        add(SoulType.AMETHYST.translationKey(), "アメジスト");
        add(SoulType.NECTARIUM.translationKey(), "ネクタリウム");
        add(SoulType.GOD_BLOOD.translationKey(), "神の血");

        addReinforcementEffect(NarakaReinforcementEffects.INCREASE_ATTACK_DAMAGE, "攻撃力上昇");
        addReinforcementEffect(NarakaReinforcementEffects.INCREASE_ARMOR, "防御力上昇");
        addReinforcementEffect(NarakaReinforcementEffects.INCREASE_ARMOR_TOUGHNESS, "防具強度上昇");
        addReinforcementEffect(NarakaReinforcementEffects.KNOCKBACK_RESISTANCE, "ノックバック耐性の追加");
        addReinforcementEffect(NarakaReinforcementEffects.FASTER_LIQUID_SWIMMING, "水中での遊泳速度上昇");
        addReinforcementEffect(NarakaReinforcementEffects.IGNORE_LIQUID_PUSHING, "液体による押し出しを無視する");
        addReinforcementEffect(NarakaReinforcementEffects.FLYING, "クリエイティブ飛行能力の追加");
        addReinforcementEffect(NarakaReinforcementEffects.ORE_SEE_THROUGH, "鉱石の透過");
        addReinforcementEffect(NarakaReinforcementEffects.LAVA_VISION, "溶岩内の視界の暗視化");
        addReinforcementEffect(NarakaReinforcementEffects.FIRE_RESISTANCE, "火炎耐性の永続付与");
        addReinforcementEffect(NarakaReinforcementEffects.EFFICIENT_MINING_IN_WATER, "水中の採掘速度上昇");
        addReinforcementEffect(NarakaReinforcementEffects.EFFICIENT_MINING_IN_AIR, "空中/泳ぎ状態での採掘速度上昇");
        addReinforcementEffect(NarakaReinforcementEffects.WATER_BREATHING, "酸素の減りの無効化");

        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_REDSTONE, "魂の宿ったレッドストーン素材");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_COPPER, "魂の宿った銅素材");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_GOLD, "魂の宿った金素材");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_EMERALD, "魂の宿ったエメラルド素材");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_DIAMOND, "魂の宿ったダイヤモンド素材");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_LAPIS, "魂の宿ったラピスラズリ素材");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_AMETHYST, "魂の宿ったアメジスト素材");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_NECTARIUM, "魂の宿ったネクタリウム素材");
        addTrimMaterial(NarakaTrimMaterials.GOD_BLOOD, "神の血素材");

        addAdvancement(AdvancementNarakaComponents.ROOT,
                List.of("Naraka"),
                List.of("ここに入る者よ、あらゆる希望を捨てよ")
        );
        addAdvancement(AdvancementNarakaComponents.SANCTUARY_COMPASS,
                List.of("彼への道"),
                List.of("聖域のコンパスを手に入れる")
        );
        addAdvancement(AdvancementNarakaComponents.FIND_HEROBRINE_SANCTUARY,
                List.of("へロブラインの聖域"),
                List.of("大きすぎる!")
        );
        addAdvancement(AdvancementNarakaComponents.SUMMON_HEROBRINE,
                List.of("奈落の暴君"),
                List.of("へロブラインを召喚する")
        );
        addAdvancement(AdvancementNarakaComponents.KILL_HEROBRINE,
                List.of("純粋なる魂"),
                List.of("奈落の王を倒す")
        );
        addAdvancement(AdvancementNarakaComponents.KILL_ORIGIN_HEROBRINE,
                List.of("奈落のコレクター"),
                List.of("奈落の始祖を倒す")
        );
        addAdvancement(AdvancementNarakaComponents.PURIFIED_SOUL_METAL,
                List.of("石鹸...?"),
                List.of("純粋な魂の合金を手に入れる")
        );
        addAdvancement(AdvancementNarakaComponents.PURIFIED_SOUL_SWORD,
                List.of("純粋なる器"),
                List.of("何でもあり")
        );
        addAdvancement(AdvancementNarakaComponents.GOD_BLOOD,
                List.of("神々の血"),
                List.of("すごい！")
        );
        addAdvancement(AdvancementNarakaComponents.SOUL_INFUSED_MATERIALS,
                List.of("魂の宿った物質"),
                List.of("何らかの魂の宿った鉱物を作る")
        );
        addAdvancement(AdvancementNarakaComponents.STABILIZER,
                List.of("スタビライザー"),
                List.of("ソウルスタビライザーを作る"))
        ;
        addAdvancement(AdvancementNarakaComponents.FILL_SOUL_STABILIZER,
                List.of("フルチャージ"),
                List.of("ソウルスタビライザーに魂の宿った1種類の鉱物で満タンにする")
        );
        addAdvancement(AdvancementNarakaComponents.CHALLENGERS_BLESSING,
                List.of("奈落の挑戦者"),
                List.of("魂の鍛冶台で、1種類の魂の宿った素材の武器防具を作り、それぞれの挑戦者の祝福を享ける")
        );
        addAdvancement(AdvancementNarakaComponents.SOUL_SWORDS,
                List.of("レインボー!"),
                List.of("全ての魂の宿った剣を手に入れる")
        );
        addAdvancement(AdvancementNarakaComponents.ULTIMATE_SPEAR,
                List.of("新たなる奈落の王よ、万歳"),
                List.of("究極の魂の槍を作る")
        );

        addAdvancement(AdvancementExtraComponents.BUY_NECTARIUM_CORE,
                List.of("これは何だ?蜂の巣?"),
                List.of("行商人からネクタリウムコアを取引する")
        );
        addAdvancement(AdvancementExtraComponents.ACTIVATE_NECTARIUM_CORE,
                List.of("涙と蜜が溢れ出るブロック"),
                List.of("ネクタリウムコアを起動する")
        );
        addAdvancement(AdvancementExtraComponents.EAT_NECTARIUM,
                List.of("ヤミー！"),
                List.of("ネクタリウムを食べる")
        );
        addAdvancement(AdvancementExtraComponents.CRAFT_SOUL_INFUSED_NECTARIUM,
                List.of("※食べられません"),
                List.of("食べないで、それをあなたの魂に捧げなさい...")
        );

        addPotion(Items.POTION, NarakaPotions.CHALLENGER, "挑戦者のポーション");
        addPotion(Items.SPLASH_POTION, NarakaPotions.CHALLENGER, "挑戦者のスプラッシュポーション");
        addPotion(Items.LINGERING_POTION, NarakaPotions.CHALLENGER, "挑戦者の残留ポーション");
        addPotion(Items.POTION, NarakaPotions.BLESS, "祝福のポーション");
        addPotion(Items.SPLASH_POTION, NarakaPotions.BLESS, "祝福のスプラッシュポーション");
        addPotion(Items.LINGERING_POTION, NarakaPotions.BLESS, "祝福の残留ポーション");

        addItem(NarakaItems.STIGMA_ROD, "スティグマの棒");
        addItem(NarakaItems.NARAKA_FIREBALL_STAFF, "奈落の火球の杖");
        addItem(NarakaItems.RAINBOW_SWORD, "虹色の剣");
        addItem(NarakaItems.PURIFIED_SOUL_SHARD, "純粋な魂の欠片");
        addItem(NarakaItems.NECTARIUM, "ネクタリウム");
        addItem(NarakaItems.GOD_BLOOD, "§l神の血");
        addItem(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, "魂の鍛冶型");

        addItem(NarakaItems.SPEAR_ITEM, "槍");
        addItem(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, "強大なる聖槍");
        addItem(NarakaItems.SPEAR_OF_LONGINUS_ITEM, "ロンギヌスの槍");

        addItem(NarakaItems.PURIFIED_SOUL_HELMET, "純粋な魂の合金のヘルメット");
        addItem(NarakaItems.PURIFIED_SOUL_CHESTPLATE, "純粋な魂の合金のチェストプレート");
        addItem(NarakaItems.PURIFIED_SOUL_LEGGINGS, "純粋な魂の合金のレギンス");
        addItem(NarakaItems.PURIFIED_SOUL_BOOTS, "純粋な魂の合金のブーツ");

        addItem(NarakaItems.SOUL_INFUSED_REDSTONE, "魂の宿ったレッドストーン");
        addItem(NarakaItems.SOUL_INFUSED_COPPER, "魂の宿った銅");
        addItem(NarakaItems.SOUL_INFUSED_GOLD, "魂の宿った金");
        addItem(NarakaItems.SOUL_INFUSED_EMERALD, "魂の宿ったエメラルド");
        addItem(NarakaItems.SOUL_INFUSED_DIAMOND, "魂の宿ったダイヤモンド");
        addItem(NarakaItems.SOUL_INFUSED_LAPIS, "魂の宿ったラピスラズリ");
        addItem(NarakaItems.SOUL_INFUSED_AMETHYST, "魂の宿ったアメジスト");
        addItem(NarakaItems.SOUL_INFUSED_NECTARIUM, "魂の宿ったネクタリウム");
        addItem(NarakaItems.PURIFIED_SOUL_METAL, "純粋な魂の合金");

        addItem(NarakaItems.SOUL_INFUSED_REDSTONE_SWORD, "魂の宿ったレッドストーンの剣");
        addItem(NarakaItems.SOUL_INFUSED_COPPER_SWORD, "魂の宿った銅の剣");
        addItem(NarakaItems.SOUL_INFUSED_GOLD_SWORD, "魂の宿った金の剣");
        addItem(NarakaItems.SOUL_INFUSED_EMERALD_SWORD, "魂の宿ったエメラルドの剣");
        addItem(NarakaItems.SOUL_INFUSED_DIAMOND_SWORD, "魂の宿ったダイヤモンドの剣");
        addItem(NarakaItems.SOUL_INFUSED_LAPIS_SWORD, "魂の宿ったラピスラズリの剣");
        addItem(NarakaItems.SOUL_INFUSED_AMETHYST_SWORD, "魂の宿ったアメジストの剣");
        addItem(NarakaItems.SOUL_INFUSED_NECTARIUM_SWORD, "魂の宿ったネクタリウムの剣");
        addItem(NarakaItems.PURIFIED_SOUL_SWORD, "純粋な魂の剣");

        addItem(NarakaItems.SANCTUARY_COMPASS, "聖域のコンパス");

        addItem(NarakaItems.HEROBRINE_PHASE_1_DISC, "へロブラインフェーズ1のレコード");
        addItem(NarakaItems.HEROBRINE_PHASE_2_DISC, "へロブラインフェーズ2のレコード");
        addItem(NarakaItems.HEROBRINE_PHASE_3_DISC, "へロブラインフェーズ3のレコード");
        addItem(NarakaItems.HEROBRINE_PHASE_4_DISC, "へロブラインフェーズ4のレコード");
        addItem(NarakaItems.SKILL_CONTROLLER, "スキルコントローラー");
        addItem(NarakaItems.ANIMATION_CONTROLLER, "アニメーションコントローラー");
        addItem(NarakaItems.HEROBRINE_SCARF, "へロブラインのスカーフ");
        addItem(NarakaItems.NARAKA_PICKAXE, "奈落の大鎌");

        addItem(NarakaItems.LOCKED_HEALTH, "ロックヘルス");
        addItem(NarakaItems.HEROBRINE_SPAWN_EGG, "へロブラインのスポーンエッグ");

        addBlock(NarakaBlocks.AMETHYST_ORE, "アメジスト鉱石");
        addBlock(NarakaBlocks.DEEPSLATE_AMETHYST_ORE, "深層アメジスト鉱石");
        addBlock(NarakaBlocks.NECTARIUM_ORE, "ネクタリウム鉱石");
        addBlock(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE, "深層ネクタリウム鉱石");
        addBlock(NarakaBlocks.NECTARIUM_BLOCK, "ネクタリウムブロック");
        addBlock(NarakaBlocks.TRANSPARENT_BLOCK, "透明ブロック");
        addBlock(NarakaBlocks.IMITATION_GOLD_BLOCK, "古の金ブロック");
        addBlock(NarakaBlocks.AMETHYST_SHARD_BLOCK, "アメジストの欠片ブロック");

        addBlock(NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK, "魂の宿ったレッドストーンブロック");
        addBlock(NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK, "魂の宿った銅ブロック");
        addBlock(NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK, "魂の宿った金ブロック");
        addBlock(NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK, "魂の宿ったエメラルドブロック");
        addBlock(NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK, "魂の宿ったダイヤモンドブロック");
        addBlock(NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK, "魂の宿ったラピスラズリブロック");
        addBlock(NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK, "魂の宿ったアメジストブロック");
        addBlock(NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK, "魂の宿ったネクタリウムブロック");

        addBlock(NarakaBlocks.HEROBRINE_TOTEM, "へロブライントーテム");
        addBlock(NarakaBlocks.PURIFIED_SOUL_LANTERN, "純粋な魂のランタン");
        addBlock(NarakaBlocks.PURIFIED_SOUL_LAMP, "純粋な魂のレッドストーンランプ");
        addBlock(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK, "純粋な魂の炎");
        addBlock(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK, "純粋な魂の合金ブロック");
        addBlock(NarakaBlocks.NECTARIUM_CORE_BLOCK, "ネクタリウムコア");
        addBlock(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK, "ネクタリウムの結晶");
        addBlock(NarakaBlocks.SOUL_STABILIZER, "ソウルスタビライザー");
        addBlock(NarakaBlocks.SOUL_SMITHING_BLOCK, "魂の鍛冶台");
        addBlock(NarakaBlocks.NARAKA_PORTAL, "奈落へのポータル");

        addTooltip(NarakaBlocks.NECTARIUM_CORE_BLOCK, "はちみつが滴り落ちている。");
        addTooltip(NarakaBlocks.SOUL_SMITHING_BLOCK, "ハンマー/メイスでアイテムを加工すると…？");
        addTooltip(NarakaBlocks.HEROBRINE_TOTEM, "どうやら、神聖な場所でのみ使えるようだ...");

        addEntityType(NarakaEntityTypes.HEROBRINE, "奈落の暴君: へロブライン");
        addEntityType(NarakaEntityTypes.ORIGIN_HEROBRINE, "始祖の奈落: へロブライン");
        addEntityType(NarakaEntityTypes.SHADOW_HEROBRINE, "へロブラインの影");
        addEntityType(NarakaEntityTypes.THROWN_SPEAR, "槍");
        addEntityType(NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR, "強大なる聖槍");
        addEntityType(NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS, "ロンギヌスの槍");
        addEntityType(NarakaEntityTypes.NARAKA_FIREBALL, "奈落の火球");
        addEntityType(NarakaEntityTypes.STARDUST, "星屑");
        addEntityType(NarakaEntityTypes.PICKAXE_SLASH, "奈落の大鎌の斬撃");
        addEntityType(NarakaEntityTypes.DIAMOND_GOLEM, "ダイヤモンドゴーレム");
        addEntityType(NarakaEntityTypes.MAGIC_CIRCLE, "魔法のサークル");
        addEntityType(NarakaEntityTypes.NARAKA_PICKAXE, "奈落の大鎌");
        addEntityType(NarakaEntityTypes.COLORED_LIGHTNING_BOLT, "色付きの雷");
        addEntityType(NarakaEntityTypes.MASSIVE_LIGHTNING, "巨大な雷");
        addEntityType(NarakaEntityTypes.CORRUPTED_STAR, "堕ちた星");
        addEntityType(NarakaEntityTypes.SHINY_EFFECT, "光沢効果");
        addEntityType(NarakaEntityTypes.AREA_EFFECT, "エリアエフェクト");
        addEntityType(NarakaEntityTypes.NARAKA_PORTAL, "奈落のポータル");
        addEntityType(NarakaEntityTypes.LIGHTNING_CIRCLE, "雷のサークル");

        addDamageType(NarakaDamageTypes.SPEAR_OF_LONGINUS, "%1$sのATフィールドは、%2$sによって引き裂かれた");
        addDamageType(NarakaDamageTypes.STIGMA, "%1$sは%2$sのスティグマが悪化して死んだ");
        addDamageType(NarakaDamageTypes.STIGMA_CONSUME, "%2$sは%1$sのスティグマを消費した");
        addDamageType(NarakaDamageTypes.PICKAXE_SLASH, "%1$sは%2$sに切り刻まれた");
        addDamageType(NarakaDamageTypes.NARAKA_FIREBALL, "%1$sは%2$sが放った奈落の火球に吹き飛ばされた");
        addDamageType(NarakaDamageTypes.PURIFIED_SOUL_FIRE, "%1$sは純粋な魂の炎に焼き尽くされた");
        addDamageType(NarakaDamageTypes.SOUL_ATTACK, "%2$sが%1$sを超越した");

        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_AMETHYST), "挑戦者の祝福");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_COPPER), "挑戦者の祝福");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_DIAMOND), "挑戦者の祝福");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_EMERALD), "挑戦者の祝福");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_GOLD), "挑戦者の祝福");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_LAPIS), "挑戦者の祝福");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_NECTARIUM), "挑戦者の祝福");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_REDSTONE), "挑戦者の祝福");
        add(LanguageKey.mobEffect(NarakaMobEffects.GOD_BLESS), "神々の祝福");

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

        add(LanguageKey.STIGMA_COMMAND_GET_KEY, "%sのスティグマは%dです");
        add(LanguageKey.STIGMA_COMMAND_SET_KEY, "%d体のエンティティのスティグマを%dに設定しました");
        add(LanguageKey.STIGMA_COMMAND_INCREASE_KEY, "スティグマの最大値を%1$dに設定しました");
        add(LanguageKey.STIGMA_COMMAND_REMOVE_KEY, "%1$d体のエンティティのスティグマを解除しました");
        add(LanguageKey.STIGMA_COMMAND_CONSUME_KEY, "%2$sが%1$dのスティグマを消費しました");
        add(LanguageKey.STIGMA_COMMAND_DISABLE_KEY, "スティグマを無効化しました");
        add(LanguageKey.STIGMA_COMMAND_ENABLE_KEY, "スティグマを有効化しました");

        add(LanguageKey.LOCK_HEALTH_COMMAND_LOCK_KEY, "%2$sの体力を%1$dにロックしました");
        add(LanguageKey.LOCK_HEALTH_COMMAND_REMOVE_KEY, "ロックされた体力を%1$sに減らしました");
    }
}
