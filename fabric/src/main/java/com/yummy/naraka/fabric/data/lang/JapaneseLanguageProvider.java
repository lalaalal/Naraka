package com.yummy.naraka.fabric.data.lang;

import com.yummy.naraka.data.lang.AdvancementExtraComponents;
import com.yummy.naraka.data.lang.AdvancementNarakaComponents;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.data.lang.NarakaJadeProviderComponents;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.damagesource.NarakaDamageTypes;
import com.yummy.naraka.world.effect.NarakaMobEffects;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.item.NarakaItemTooltip;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.SoulType;
import com.yummy.naraka.world.item.alchemy.NarakaPotions;
import com.yummy.naraka.world.item.equipment.trim.NarakaTrimMaterials;
import com.yummy.naraka.world.item.equipmentset.EquipmentSetHelper;
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
        add(LanguageKey.ITEM_GROUP_SOUL_MATERIALS, "魂の宿ったアイテム");
        add(LanguageKey.ITEM_GROUP_TEST, "Naraka テストアイテム");

        add(LanguageKey.KEY_CATEGORIES_NARAKA, "Naraka");
        add(LanguageKey.KEY_TOGGLE_ORE_SEE_THROUGH, "鉱石の透過の表示/非表示を切り替える");

        add(LanguageKey.toggleOreSeeThroughMessage(true), "鉱石の透過を表示にしました");
        add(LanguageKey.toggleOreSeeThroughMessage(false), "鉱石の透過を非表示にしました");

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

        add(LanguageKey.HIDDEN_TOOLTIP, "Shiftキーを押して詳細を表示");

        addTooltip(NarakaItemTooltip.HEROBRINE_SCARF, List.of(
                List.of("チェストプレートスロットに装備するか、「魂の鍛冶台」を使用して防具に合成できる。"),
                List.of("マントの内側に沿って、かつて存在した「奈落」の光景がゆらめいている。"),
                List.of("ただし、これは映し出しているに過ぎず、これを通じて「奈落」を行き来することはできない。")
        ));
        addTooltip(NarakaItemTooltip.NARAKA_PICKAXE, List.of(
                List.of("ツルハシや斧として hover 使用できる。"),
                List.of("「色彩」を求めて異界をさまよい、足の向くままに踏みにじり吸収した。"),
                List.of("常に奈落を通じて現れ、侵入した世界に虚無だけを残して消え去る存在。"),
                List.of("それゆえ、ある世界では『虚無の神』、ある世界では『奈落の暴君』と呼ばれている。")
        ));
        addTooltip(NarakaItemTooltip.GOD_BLOOD, List.of(
                List.of("強力な装備の材料として使用される。"),
                List.of("また、「ソウルスタビライザー」に充填して強力な装備の鍛造に使用することもできる。"),
                List.of("あらゆる恐怖と畏怖を込めた名の本質は、欠落を埋めるための貪食。"),
                List.of("色を得られず、色を渇望し、色を奪い取って自分に付け加えようとした。"),
                List.of("しかし何を加えようと、黒はただの黒。残された遺骸すらも漆黒の虚無に過ぎない。")
        ));
        addTooltip(NarakaItemTooltip.SPEAR_OF_LONGINUS, List.of(
                List.of("投擲可能で、自動的に持ち主のもとへ戻ってくる。"),
                List.of("攻撃方法に関わらず、命中した敵を即死させる。"),
                List.of("八度の安らぎ、八度の証明。"),
                List.of("この武具はその果てに得た最も純粋な力であり、あなたが「奈落」の君主として生まれ変わった証である。")
        ));
        addTooltip(NarakaItemTooltip.SANCTUARY_COMPASS, List.of(
                List.of("現在のワールドで最も近い「へロブラインの聖域」構造体がある方向を指し示す。"),
                List.of("世界はその存続のために因果を調整する傾向がある。"),
                List.of("これはその一環であり、常に世界を侵したものの聖域を指し示す。"),
                List.of("それを打ち倒し、世界の存続を繋ぎ止めることができる英雄が現れるのを待ちながら。")
        ));
        addTooltip(NarakaItemTooltip.IMITATION_GOLD, List.of(
                List.of("設置すると、隣接する鉄ブロックを古の金ブロックに変化させる。"),
                List.of("また、「へロブライントーテム」を複製する際にも使用される。"),
                List.of("金は輝きを宿し、輝きはそれを呼び寄せる。"),
                List.of("この金は元の金よりも微かな輝きを宿した、偽りのもの。"),
                List.of("しかし、その輝きだけでも、その足を導くには十分である。")
        ));
        addTooltip(NarakaItemTooltip.HEROBRINE_TOTEM, List.of(
                List.of("古の金ブロック2個、へロブライントーテム、ネザラック1個を下から順に積み上げ、火をつけることでへロブラインを呼び出すことができる。"),
                List.of("へロブラインは「へロブラインの聖域」構造体の中でのみ召喚できる。"),
                List.of("その顔が刻まれた碑石。"),
                List.of("古の金がその視線を捕らえ歩みを導く時、これがその肉体をこの世界に呼び寄せ、固定する。")
        ));
        addTooltip(NarakaItemTooltip.SOUL_INFUSED_MATERIALS, List.of(
                List.of("「ソウルスタビライザー」の充填に使用する。"),
                List.of("色を渇望するその魂の欠片を、最も強烈な色彩を放つ鉱物に染み込ませたもの。"),
                List.of("色を渇望していた魂の渇きが、最も強烈な色彩と直接一つになることで満たされている。")
        ));
        addTooltip(NarakaItemTooltip.SOUL_STABILIZER, List.of(
                List.of("設置後、魂の宿った鉱物や神の血を右クリックすることで充填できる。"),
                List.of("充填は一種類のアイテムのみで行う必要がある。")
        ));
        addTooltip(NarakaItemTooltip.SOUL_SMITHING_BLOCK, List.of(
                List.of("設置後、充填されたソウルスタビライザーと鍛冶型を装着して特定の装備を鍛造できる。"),
                List.of("鍛造時、ソウルスタビライザーに充填された鉱物や神の血を一定量消費する。")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_METAL, List.of(
                List.of("希少な装備の材料として使用される。"),
                List.of("金属と称されているが、散らばったその魂が形を成して固まったものに近い。")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_SWORD, List.of(
                List.of("右クリックで黒い炎をつけることができる。黒い炎はこのアイテムの左クリックでのみ消去できる。"),
                List.of("魂の鍛冶台で鉱物や神の血を使用して鍛조できる。"),
                List.of("最も純粋な器。"),
                List.of("儀式に使用するためには、まず魂の宿った鉱物を通じて同調させる必要がある。")
        ));
        addTooltip(NarakaItemTooltip.SOUL_INFUSED_SWORDS_DEFAULT, List.of(
                List.of("この剣を持ち、この剣の鉱物と同じ鉱物で鍛造された純粋な魂の防具4部位を装備すると、「挑戦者」バフが有効化される。"),
                List.of("「挑戦者」バフを持った状態でへロブラインを討伐すると、剣に祝福が授けられ、装備していた防具がすべて破壊される。"),
                List.of("挑戦者は渇望の充足により安定した魂を纏い、武器として握ったまま、渇望を捨てきれなかったそれを再び眠らせなければならない。"),
                List.of("そうすることで、その魂は安定した魂と共鳴して安らぎを見出し、挑戦者の握る武器にはその祝福が授けられるだろう。")
        ));
        addTooltip(NarakaItemTooltip.SOUL_INFUSED_SWORDS_BLESSED, List.of(
                List.of(""),
                List.of("「ロンギヌスの槍」"),
                List.of("の材料として使用される。"),
                List.of("祝福が授けられた武具は互いに共鳴して引き合い、"),
                List.of("その血を媒介として繋がる時、最も純粋な力を現すだろう。")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_ARMORS_DEFAULT, List.of(
                List.of("魂の鍛冶台で鉱物や神の血を使用して鍛造できる。"),
                List.of("最も純粋な器。"),
                List.of("儀式に使用するためには、まず魂の宿った鉱物を通じて同調させる必要がある。"),
                List.of("あるいは、その血を使用して――")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_ARMORS_SOUL, List.of(
                List.of("この防具の鉱物と同じ鉱物で鍛造された純粋な魂의 防具4部位を装備し、この防具の鉱物と同じ鉱物で鍛造された剣を持つと、「挑戦者」バフが有効化される。"),
                List.of("「挑戦者」バフを持った状態でへロブラインを討伐すると、剣に祝福が授けられ、装備していた防具がすべて破壊される。"),
                List.of("挑戦者は渇望の充足により安定した魂を纏い、武器として握ったまま、渇望を捨てきれなかったそれを再び眠らせなければならない。"),
                List.of("そうすることで、その魂は安定した魂と共鳴して安らぎを見出し、挑戦者の握る武器にはその祝福가 授けられるだろう。")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_ARMOR_BLESSED, List.of(
                List.of("貪食と貪食が共鳴し、最も強力な力を生み出す甲冑。"),
                List.of("満たされないまま触れ合った二つにして一つの魂は互いを貪食し、絶え間ない苦痛の中で泣き叫ぶ。")
        ));
        addTooltip(NarakaItemTooltip.NECTARIUM, List.of(
                List.of("食用可能。食べると満腹度、隠し満腹度、体力が最大値まで回復する。"),
                List.of("ある異次元では、契約を守らなかった者に「石を食べる罰」を与えるという。")
        ));
        addTooltip(NarakaItemTooltip.NECTARIUM_CORE, List.of(
                List.of("ハチミツ入り瓶を使用して活性化でき、活性化させるとネクタリウムの結晶を作り出す。"),
                List.of("鉱物も作物も、どちらも「物」であるため大差ない。"),
                List.of("したがって、これは22世紀の人類を救う新概念の作物である。")
        ));

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

        addDamageType(NarakaDamageTypes.SPEAR_OF_LONGINUS, "%1$sは自身の色を奪われ、世界から消し去滅した。");
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
