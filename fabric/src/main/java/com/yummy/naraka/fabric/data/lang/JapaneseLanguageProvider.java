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
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.SoulType;
import com.yummy.naraka.world.item.alchemy.NarakaPotions;
import com.yummy.naraka.world.item.equipment.trim.NarakaTrimMaterials;
import com.yummy.naraka.world.item.equipmentset.EquipmentSetHelper;
import com.yummy.naraka.world.item.reinforcement.NarakaReinforcementEffects;
import com.yummy.naraka.world.item.tooltip.NarakaItemTooltip;
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
        add(LanguageKey.ITEM_GROUP_SOUL_MATERIALS, "魂の宿ったアイテム");
        add(LanguageKey.ITEM_GROUP_TEST, "Naraka テストアイテム");

        add(LanguageKey.KEY_CATEGORIES_NARAKA, "Naraka");
        add(LanguageKey.KEY_TOGGLE_ORE_SEE_THROUGH, "鉱石の透過の表示/非表示を切り替える");

        add(LanguageKey.toggleOreSeeThroughMessage(true), "鉱石の透過を表示にしました");
        add(LanguageKey.toggleOreSeeThroughMessage(false), "鉱石の透過を非表示にしました");

        add(LanguageKey.REINFORCEMENT_KEY, "アップグレード: %d");
        add(LanguageKey.BLESSED_KEY, "奈落の祝福");
        add(LanguageKey.HEROBRINE_SCARF_KEY, "スカーフ付き");

        add(LanguageKey.CONFIG_TITLE, "Naraka Config");
        add(LanguageKey.CONFIG_CATEGORY_COMMON, "Naraka Common Config");
        addConfig(NarakaConfig.COMMON.showTestCreativeModeTab,
                List.of("テストクリエイティブモードタブを表示"),
                List.of(
                        List.of("再起動が必要です")
                )
        );
        addConfig(NarakaConfig.COMMON.enableStigma, "スティグマを有効化");
        addConfig(NarakaConfig.COMMON.stigmaStunDuration,
                List.of("スティグマのスタン持続時間"),
                List.of(
                        List.of("スタン持続時間（ティック）")
                )
        );
        addConfig(NarakaConfig.COMMON.lockHealthRatio, "体力固定比率");

        add(LanguageKey.CONFIG_CATEGORY_CLIENT, "Naraka Client Config");
        addConfig(NarakaConfig.CLIENT.playHerobrineBossMusic, "へロブラインボスのBGMを再生");
        addConfig(NarakaConfig.CLIENT.enableOreSeeThrough, "鉱石の透過を有効化");
        addConfig(NarakaConfig.CLIENT.oreSeeThroughRange, "鉱石の透過の範囲");
        addConfig(NarakaConfig.CLIENT.cameraShakingSpeed, "カメラの揺れ速度");
        addConfig(NarakaConfig.CLIENT.cameraShakingStrength, "カメラの揺れ強度");
        add(LanguageKey.CONFIG_ORE_COLOR, "鉱石の輪郭の色");
        add(LanguageKey.CONFIG_ORE_COLOR_WRONG, "不正な形式です！");

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

        add(LanguageKey.equipmentSet(EquipmentSetHelper.ID_BLESSED), "祝福セット");
        add(LanguageKey.equipmentSet(EquipmentSetHelper.ID_CHALLENGER), "挑戦者セット");

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
                List.of("ヘロブラインの聖域"),
                List.of("大きすぎる!")
        );
        addAdvancement(AdvancementNarakaComponents.SUMMON_HEROBRINE,
                List.of("奈落の暴君"),
                List.of("ヘロブラインを召喚する")
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

        addEffectItem(Items.POTION, NarakaPotions.CHALLENGER, "挑戦者のポーション");
        addEffectItem(Items.SPLASH_POTION, NarakaPotions.CHALLENGER, "挑戦者のスプラッシュポーション");
        addEffectItem(Items.LINGERING_POTION, NarakaPotions.CHALLENGER, "挑戦者の残留ポーション");
        addEffectItem(Items.TIPPED_ARROW, NarakaPotions.CHALLENGER, "挑戦者の矢");
        addEffectItem(Items.POTION, NarakaPotions.BLESS, "祝福のポーション");
        addEffectItem(Items.SPLASH_POTION, NarakaPotions.BLESS, "祝福のスプラッシュポーション");
        addEffectItem(Items.LINGERING_POTION, NarakaPotions.BLESS, "祝福の残留ポーション");
        addEffectItem(Items.TIPPED_ARROW, NarakaPotions.BLESS, "祝福の矢");

        addItem(NarakaItems.STIGMA_ROD, "スティグマの棒");
        addItem(NarakaItems.NARAKA_FIREBALL_STAFF, "奈落の火球の杖");
        addItem(NarakaItems.RAINBOW_SWORD, "虹色の剣");
        addItem(NarakaItems.PURIFIED_SOUL_SHARD, "純粋な魂の欠片");
        addItem(NarakaItems.NECTARIUM, "ネクタリウム");
        addItem(NarakaItems.GOD_BLOOD, "§l神の血");
        addItem(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, "魂の鍛冶型");

        addItem(NarakaItems.NETHERITE_HAMMER, "ネザライトのハンマー");
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

        addItem(NarakaItems.HEROBRINE_PHASE_1_DISC, "ヘロブラインフェーズ1のレコード");
        addItem(NarakaItems.HEROBRINE_PHASE_2_DISC, "ヘロブラインフェーズ2のレコード");
        addItem(NarakaItems.HEROBRINE_PHASE_3_DISC, "ヘロブラインフェーズ3のレコード");
        addItem(NarakaItems.HEROBRINE_PHASE_4_DISC, "ヘロブラインフェーズ4のレコード");
        addItem(NarakaItems.SKILL_CONTROLLER, "スキルコントローラー");
        addItem(NarakaItems.ANIMATION_CONTROLLER, "アニメーションコントローラー");
        addItem(NarakaItems.HEROBRINE_SCARF, "ヘロブラインのスカーフ");
        addItem(NarakaItems.NARAKA_PICKAXE, "奈落の大鎌");

        addItem(NarakaItems.LOCKED_HEALTH, "ロックヘルス");
        addItem(NarakaItems.HEROBRINE_SPAWN_EGG, "ヘロブラインのスポーンエッグ");
        addItem(NarakaItems.DIAMOND_GOLEM_SPAWN_EGG, "ダイヤモンドゴーレムのスポーンエッグ");

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

        addBlock(NarakaBlocks.HEROBRINE_TOTEM, "ヘロブライントーテム");
        addBlock(NarakaBlocks.PURIFIED_SOUL_LANTERN, "純粋な魂のランタン");
        addBlock(NarakaBlocks.PURIFIED_SOUL_LAMP, "純粋な魂のレッドストーンランプ");
        addBlock(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK, "純粋な魂の炎");
        addBlock(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK, "純粋な魂の合金ブロック");
        addBlock(NarakaBlocks.NECTARIUM_CORE_BLOCK, "ネクタリウムコア");
        addBlock(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK, "ネクタリウムの結晶");
        addBlock(NarakaBlocks.SOUL_STABILIZER, "ソウルスタビライザー");
        addBlock(NarakaBlocks.SOUL_SMITHING_BLOCK, "魂の鍛冶台");
        addBlock(NarakaBlocks.NARAKA_PORTAL, "奈落へのポータル");

        add(LanguageKey.HIDDEN_TOOLTIP, "Shift長押しで詳細を表示");

        addTooltip(NarakaItemTooltip.HEROBRINE_SCARF, List.of(
                List.of("胸当てとして装備するか、【魂の鍛冶台】で【純粋な魂の合金】の防具に合成できる。"),
                List.of("布の内側に沿って、かつての深淵、奈落の姿が揺らめきを放ち、浮かび上がる。"),
                List.of("ただ、それは単に遠隔での投影に過ぎず、この布で奈落に移動することは出来ない。")
        ));
        addTooltip(NarakaItemTooltip.NARAKA_PICKAXE, List.of(
                List.of("ツルハシや斧として使用可能。"),
                List.of("【色彩】を求め異界の領域を彷徨いながら、それは道中のあらゆる物を踏みにじり、吸収した。"),
                List.of("奈落を通じて常に顕現する存在であり、侵略した世界に虚無のみを残し姿を消す存在。"),
                List.of("それらは、ある世界では【虚空の神】であり、また別の世界では【奈落の暴君】と呼ばれるという。")
        ));
        addTooltip(NarakaItemTooltip.GOD_BLOOD, List.of(
                List.of("神聖なる装備の素材として使われる。"),
                List.of("また、【ソウルスタビライザー】に充填して、【魂の鍛冶型】で奈落の力を宿した装備を鍛造出来る。"),
                List.of("恐怖と畏怖の念に包まれた名の背後に有る本質は、虚空の如き感覚を埋めようとする貪欲さである。"),
                List.of("それは色を得ることが出来ず、色彩を渇望し、万物を狩り、自らの虚に貼り付けた。"),
                List.of("しかし、何を加えようとも、黒は漆黒のままである。その残された灰でさえ、ただ深淵の虚無に過ぎない。")
        ));
        addTooltip(NarakaItemTooltip.SPEAR, List.of(
                List.of("【神の血】と【魂の鍛冶型】で鍛冶台でアップグレードすると、より強力な槍となる。"),
                List.of("純粋な魂を刃として変形させ造られた武器。"),
                List.of("単に魂を刃の形を加えただけでは、その力は微々たるもので、とても弱い(;ㅿ;)"),
                List.of("しかし、強大な力を宿す器として、これほどふさわしき物は無いだろう。")
        ));
        addTooltip(NarakaItemTooltip.MIGHTY_HOLY_SPEAR, List.of(
                List.of("八つの異なる魂が宿る鉱物の剣に祝福を授け、それらを素材とし組み合わせれば、神すらも殺す槍を作ることが出来るかもしれない..."),
                List.of("暴君は堕ちた星々となったが、深淵の虚無、漆黒の奈落は依然として支配者を求めている。"),
                List.of("虚無狩りよ、八つの試練を乗り越えて、王座に就くにふさわしき力を手に入れろ。")
        ));
        addTooltip(NarakaItemTooltip.SPEAR_OF_LONGINUS, List.of(
                List.of("投げることができ、忠誠エンチャントの様に手元に戻る。"),
                List.of("この武器で攻撃されたエンティティは例外なく即死する。"),
                List.of("八度の安息、八度の試練"),
                List.of("挑戦者はあらゆる苦難を経て、自身を超越した...  虚無狩りよ、汝は世界に証明し、今や奈落の王座は汝の物だ。"),
                List.of("故にこの虚無は、その旅の果てに得た、神の力であり、世界の力であり、色彩の力である、そして、汝が新たなる奈落の王と生まれ変わったのだ...")
        ));
        addTooltip(NarakaItemTooltip.SANCTUARY_COMPASS, List.of(
                List.of("現地点で最も近い【へロブラインの聖域】を指し示す。"),
                List.of("この世界は自らの存続のために、因果の調節をする事がある。"),
                List.of("これこそがその産物であり、この世界を侵略する【虚無】が巣食う深淵の聖域を指し示している。"),
                List.of("かの侵略者を退け、世界の英雄となる者、虚無狩りの【挑戦者】を待ち続けている。")
        ));
        addTooltip(NarakaItemTooltip.IMITATION_GOLD, List.of(
                List.of("設置すると、隣接する鉄ブロックを古の金ブロックに変色させる。"),
                List.of("【へロブライントーテム】を複製するためにも使用される。"),
                List.of("黄金は純粋な輝きを宿し、輝きは黄金を呼び寄せる。"),
                List.of("この金属は古の時代に存在し、今は輝きが失われている。"),
                List.of("しかし、これの元が純粋な古の輝きであるからこそ、彼への道を顕す純粋なる光となるのだ。")
        ));
        addTooltip(NarakaItemTooltip.HEROBRINE_TOTEM, List.of(
                List.of("古の金二つ、へロブライントーテム一つ、ネザーラック一つを下から順に積み重ね、ネザーラックに着火するとへロブラインを召喚する。"),
                List.of("へロブラインは【へロブラインの聖域】でのみ召喚できる。"),
                List.of("かの者の顔が刻まれた墓石。"),
                List.of("古の金がその目に留まり、彼への道を導く時、この純粋なる器は、自らを世界に呼び出し、色彩を手に入れるだろう。")
        ));
        addTooltip(NarakaItemTooltip.SOUL_INFUSED_MATERIALS, List.of(
                List.of("純粋な魂を宿す鉱物、【ソウルスタビライザー】を充填することが出来る"),
                List.of("色彩を渇望する魂、その断片を、最も鮮やかな色彩の輝きを放つ鉱物に注ぎ込み生まれた物。"),
                List.of("虚無は色彩を求め、あらゆる物を飲み込んだ、しかし、虚無の願いはたった一つの宝石に注ぎ込まれる事で叶えられ、流れ星の如く輝く。")
        ));
        addTooltip(NarakaItemTooltip.SOUL_STABILIZER, List.of(
                List.of("【何らかの魂の宿った鉱物】か【神の血】で右クリックすれば【ソウルスタビライザー】にチャージすることが可能。"),
                List.of("ソウルスタビライザーへのチャージは1種類のアイテムのみ使用する必要がある。")
        ));
        addTooltip(NarakaItemTooltip.SOUL_SMITHING_BLOCK, List.of(
                List.of("何らかの【魂の宿った鉱物】か【神の血】でフルチャージされた、【ソウルスタビライザー】と鍛冶型を使い、メイス/ハンマーで純粋な魂の武具を叩けば調律が出来る。"),
                List.of("調律や鍛造を行うと、【ソウルスタビライザー】にチャージされた【魂の宿った鉱物】または【神の血】が一定量消費される。")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_METAL, List.of(
                List.of("純粋な魂を宿す合金。純粋な器の武具の素材"),
                List.of("【合金】とは呼ばれているが、それはむしろ、浄化された魂たちが形作り、固まっている物に近い。")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_SWORD, List.of(
                List.of("地面に左クリックでその場に黒い炎を設置する。"),
                List.of("【魂の宿った鉱物】か【神の血】を素材として、【魂の鍛冶台】で鍛造することが出来る。"),
                List.of("最も純粋な器。"),
                List.of("挑戦者の儀式でこの鎧を使用する場合、まず【魂の宿った鉱物】を用いて器と魂を調律する必要がある。")
        ));
        addTooltip(NarakaItemTooltip.SOUL_INFUSED_SWORDS_DEFAULT, List.of(
                List.of("この剣の魂の宿った鉱物と同じ素材で鍛造した【純粋な魂の鎧】を全て装備し、更にこの剣をメインハンドに持つと【挑戦者の祝福】を得る。"),
                List.of("【挑戦者の祝福】が付与された状態でへロブラインを倒すと、装備した全ての防具が壊れる代わりに、この剣に【祝福】が授けられる。"),
                List.of("挑戦者達は、満たされた渇望によって虹色の輝きを放つ魂を身にまとい、それを武器とし、色彩に飢えた者を、虚無狩りを為し得なければならない。"),
                List.of("さすれば、かの漆黒の器は純粋なる魂と強く共鳴し、溶け、混ざり合い。挑戦者達への祝福となるだろう...")
        ));
        addTooltip(NarakaItemTooltip.SOUL_INFUSED_SWORDS_BLESSED, List.of(
                List.of(""),
                List.of("【ロンギヌスの槍】"),
                List.of("の材料として使用される。"),
                List.of("祝福された武具が互いを共鳴し、混ざり、祝福となった。"),
                List.of("さすれば、かの者の血を通じて結びつけば、最も純粋な器に虚無の力が宿るだろう。")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_ARMORS_DEFAULT, List.of(
                List.of("【魂の宿った鉱物】か【神の血】を素材として、【魂の鍛冶台】で鍛造することが出来る。"),
                List.of("最も純粋な器。"),
                List.of("【挑戦者の儀式】でこの鎧を使用する場合、まず【魂の宿った鉱物】を用いて器と魂を調律する必要がある。"),
                List.of("しかし【神の血】を使えば———")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_ARMORS_SOUL, List.of(
                List.of("同じ魂の宿った鉱物で鍛造した【純粋な魂の鎧】を全て装備し、更に同じ魂の宿った鉱物で鍛造した【純粋な魂の剣】をメインハンドに持つと【挑戦者の祝福】を得る。"),
                List.of("挑戦者の祝福が付与された状態で【へロブライン】を倒すと、装備した全ての防具が壊れる代わりに、貴方の純粋な魂の剣に【祝福】が授けられる。"),
                List.of("挑戦者達は、満たされた渇望によって虹色の輝きを放つ魂を身にまとい、それを武器とし、色彩に飢えた者を、、虚無狩りを為し得なければならない。"),
                List.of("さすれば、かの漆黒の器は純粋なる魂と強く共鳴し、溶け、混ざり合い。挑戦者達への祝福となるだろう...")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_ARMOR_BLESSED, List.of(
                List.of("互いの貪欲さが共鳴することでこの純粋な魂の器は、至高の力を発揮する。"),
                List.of("二つの魂———それでありながら一つ——満たされることなど無く触れ合い、結合し、互いを喰らい尽くし、果てしない苦痛の中で嘆きの声を歌う。")
        ));
        addTooltip(NarakaItemTooltip.NECTARIUM, List.of(
                List.of("食用の宝石(!?)、これを食べると満腹度、空腹度、体力が完全に回復する。"),
                List.of("とある異界では、契約を守らない者は【石を喰らう罰】を与えるという...")
        ));
        addTooltip(NarakaItemTooltip.NECTARIUM_CORE, List.of(
                List.of("ハチミツ入りの瓶を使用して活性化状態にすることができ、活性化状態の場合近くのネクタリウムの結晶が生えてくる。"),
                List.of("植物も鉱物も、、、どちらも、大地から【採取】するものだから、同じような物だよね？"),
                List.of("つまり、これは22世紀の人類を救うための画期的な作物なのだ。")
        ));

        addEntityType(NarakaEntityTypes.HEROBRINE, "奈落の暴君: ヘロブライン");
        addEntityType(NarakaEntityTypes.ORIGIN_HEROBRINE, "始祖の奈落: ヘロブライン");
        addEntityType(NarakaEntityTypes.SHADOW_HEROBRINE, "ヘロブラインの影");
        addEntityType(NarakaEntityTypes.THROWN_SPEAR, "槍");
        addEntityType(NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR, "強大なる聖槍");
        addEntityType(NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS, "ロンギヌスの槍");
        addEntityType(NarakaEntityTypes.NARAKA_FIREBALL, "奈落の火球");
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

        addDamageType(NarakaDamageTypes.SPEAR_OF_LONGINUS, "%1$sは自身の色を奪われ、世界から消し去滅した。");
        addDamageType(NarakaDamageTypes.STIGMA, "%1$sは%2$sのスティグマが悪化して死んだ");
        addDamageType(NarakaDamageTypes.STIGMA_CONSUME, "%2$sは%1$sのスティグマを消費した");
        addDamageType(NarakaDamageTypes.PICKAXE_SLASH, "%1$sは%2$sに切り刻まれた");
        addDamageType(NarakaDamageTypes.NARAKA_FIREBALL, "%1$sは%2$sが放った奈落の火球に吹き飛ばされた");
        addDamageType(NarakaDamageTypes.CORRUPTED_STAR, "%1$sは%2$sに星にされた");
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

        add(LanguageKey.STIGMA_COMMAND_GET_KEY, "%sのスティグマは%dです");
        add(LanguageKey.STIGMA_COMMAND_SET_KEY, "%d体のエンティティのスティグマを%dに設定しました");
        add(LanguageKey.STIGMA_COMMAND_INCREASE_KEY, "スティグマを%1$dに設定しました");
        add(LanguageKey.STIGMA_COMMAND_REMOVE_KEY, "%1$d体のエンティティのスティグマを解除しました");
        add(LanguageKey.STIGMA_COMMAND_CONSUME_KEY, "%2$sが%1$dのスティグマを消費しました");
        add(LanguageKey.STIGMA_COMMAND_DISABLE_KEY, "スティグマを無効化しました");
        add(LanguageKey.STIGMA_COMMAND_ENABLE_KEY, "スティグマを有効化しました");

        add(LanguageKey.LOCK_HEALTH_COMMAND_LOCK_KEY, "%2$sの体力を%1$dにロックしました");
        add(LanguageKey.LOCK_HEALTH_COMMAND_REMOVE_KEY, "ロックされた体力を%1$sに減らしました");
    }
}
