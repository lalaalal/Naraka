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

public class ChineseLanguageProvider extends NarakaLanguageProviders {
    public ChineseLanguageProvider() {
        super("zh_cn");
    }

    public static void add(FabricDataGenerator.Pack pack) {
        new ChineseLanguageProvider().addProvidersTo(pack::addProvider);
    }

    @Override
    protected void generate() {
        add(LanguageKey.ITEM_GROUP_NARAKA, "Naraka");
        add(LanguageKey.ITEM_GROUP_SOUL_MATERIALS, "魂浸矿物");
        add(LanguageKey.ITEM_GROUP_TEST, "调试工具");

        add(LanguageKey.KEY_CATEGORIES_NARAKA, "Naraka");
        add(LanguageKey.KEY_TOGGLE_ORE_SEE_THROUGH, "开关矿物透视");

        add(LanguageKey.toggleOreSeeThroughMessage(true), "矿物透视已启用");
        add(LanguageKey.toggleOreSeeThroughMessage(false), "矿物透视已关闭");

        add(LanguageKey.REINFORCEMENT_KEY, "强化： %d");
        add(LanguageKey.BLESSED_KEY, "受祝福的");
        add(LanguageKey.HEROBRINE_SCARF_KEY, "附加披风");

        add(LanguageKey.JADE_SOUL_STABILIZER_KEY, "%d");
        add(NarakaJadeProviderComponents.SOUL_STABILIZER.translationKey, "灵魂稳定器");
        add(LanguageKey.JADE_STIGMA_KEY, ": %d");
        add(LanguageKey.JADE_LOCKED_HEALTH_KEY, ": %d");
        add(LanguageKey.JADE_DEATH_COUNT_KEY, "Death Count: %d");
        add(LanguageKey.JADE_NECTARIUM_CORE_ACTIVATED_KEY, "已激活");
        add(LanguageKey.JADE_NECTARIUM_CORE_INACTIVATED_KEY, "未激活");
        add(LanguageKey.JADE_NECTARIUM_CORE_HONEY_KEY, "（剩余 %d）");
        add(NarakaJadeProviderComponents.SOUL_SMITHING_BLOCK.translationKey, "灵魂锻造台");
        add(NarakaJadeProviderComponents.NECTARIUM_CORE.translationKey, "蜜银核心");
        add(NarakaJadeProviderComponents.ENTITY_DATA.translationKey, "耻辱印记");

        add(SoulType.REDSTONE.translationKey(), "红石");
        add(SoulType.COPPER.translationKey(), "铜");
        add(SoulType.GOLD.translationKey(), "金锭");
        add(SoulType.EMERALD.translationKey(), "绿宝石");
        add(SoulType.DIAMOND.translationKey(), "钻石");
        add(SoulType.LAPIS.translationKey(), "青金石");
        add(SoulType.AMETHYST.translationKey(), "紫水晶");
        add(SoulType.NECTARIUM.translationKey(), "蜜银");
        add(SoulType.GOD_BLOOD.translationKey(), "神之血");

        addReinforcementEffect(NarakaReinforcementEffects.INCREASE_ATTACK_DAMAGE, "攻击增幅");
        addReinforcementEffect(NarakaReinforcementEffects.INCREASE_ARMOR, "护甲值提升");
        addReinforcementEffect(NarakaReinforcementEffects.INCREASE_ARMOR_TOUGHNESS, "韧性增强");
        addReinforcementEffect(NarakaReinforcementEffects.KNOCKBACK_RESISTANCE, "击退抗性");
        addReinforcementEffect(NarakaReinforcementEffects.FASTER_LIQUID_SWIMMING, "流体疾泳");
        addReinforcementEffect(NarakaReinforcementEffects.IGNORE_LIQUID_PUSHING, "无视流体推动");
        addReinforcementEffect(NarakaReinforcementEffects.FLYING, "启用飞行");
        addReinforcementEffect(NarakaReinforcementEffects.ORE_SEE_THROUGH, "矿物透视");
        addReinforcementEffect(NarakaReinforcementEffects.LAVA_VISION, "熔岩明视");
        addReinforcementEffect(NarakaReinforcementEffects.FIRE_RESISTANCE, "火焰抗性");
        addReinforcementEffect(NarakaReinforcementEffects.EFFICIENT_MINING_IN_WATER, "水下速掘");
        addReinforcementEffect(NarakaReinforcementEffects.EFFICIENT_MINING_IN_AIR, "空中速掘");
        addReinforcementEffect(NarakaReinforcementEffects.WATER_BREATHING, "水下呼吸");

        add(LanguageKey.equipmentSet(EquipmentSetHelper.ID_BLESSED), "祝福套装");
        add(LanguageKey.equipmentSet(EquipmentSetHelper.ID_CHALLENGER), "挑战者套装");

        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_REDSTONE, "魂浸红石材质");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_COPPER, "魂浸铜材质");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_GOLD, "魂浸金材质");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_EMERALD, "魂浸绿宝石材质");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_DIAMOND, "魂浸钻石材质");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_LAPIS, "魂浸青金石材质");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_AMETHYST, "魂浸紫水晶材质");
        addTrimMaterial(NarakaTrimMaterials.SOUL_INFUSED_NECTARIUM, "魂浸蜜银材质");
        addTrimMaterial(NarakaTrimMaterials.GOD_BLOOD, "神之血材质");

        addAdvancement(AdvancementNarakaComponents.ROOT,
                List.of("Naraka"),
                List.of("入此门者，当放弃一切希望。《神曲》地狱篇")
        );
        addAdvancement(AdvancementNarakaComponents.SANCTUARY_COMPASS,
                List.of("众里寻他千百度"),
                List.of("得到圣所指南针")
        );
        addAdvancement(AdvancementNarakaComponents.FIND_HEROBRINE_SANCTUARY,
                List.of("Herobrine 圣所"),
                List.of("如此宏伟...")
        );
        addAdvancement(AdvancementNarakaComponents.SUMMON_HEROBRINE,
                List.of("炼狱暴君"),
                List.of("召唤Herobrine")
        );
        addAdvancement(AdvancementNarakaComponents.KILL_HEROBRINE,
                List.of("纯粹灵魂"),
                List.of("击败Herobrine")
        );
        addAdvancement(AdvancementNarakaComponents.KILL_ORIGIN_HEROBRINE,
                List.of("我来，我见，我征服"),
                List.of("他曾经光彩照人，如今却丑陋不堪。他竟竖起眼眉，与造物主针锋相对。《神曲》")
        );
        addAdvancement(AdvancementNarakaComponents.PURIFIED_SOUL_METAL,
                List.of("看起来像肥皂...？"),
                List.of("得到纯净灵魂金属")
        );
        addAdvancement(AdvancementNarakaComponents.PURIFIED_SOUL_SWORD,
                List.of("纯粹容器"),
                List.of("它可以变成任何形态!")
        );
        addAdvancement(AdvancementNarakaComponents.GOD_BLOOD,
                List.of("他的血..."),
                List.of("完美绝佳!")
        );
        addAdvancement(AdvancementNarakaComponents.SOUL_INFUSED_MATERIALS,
                List.of("灵魂灌注"),
                List.of("制作任意一种魂浸矿物")
        );
        addAdvancement(AdvancementNarakaComponents.STABILIZER,
                List.of("稳定装置"),
                List.of("制作可容纳魂浸矿物的容器"))
        ;
        addAdvancement(AdvancementNarakaComponents.FILL_SOUL_STABILIZER,
                List.of("⚡电⚡量⚡满⚡格⚡"),
                List.of("用一种魂浸矿物填满灵魂稳定器")
        );
        addAdvancement(AdvancementNarakaComponents.CHALLENGERS_BLESSING,
                List.of("挑战者"),
                List.of("穿戴全套纯粹灵魂盔甲，并手持魂浸剑以获得挑战者的祝福")
        );
        addAdvancement(AdvancementNarakaComponents.SOUL_SWORDS,
                List.of("看啊，彩虹!"),
                List.of("为每一把魂浸矿石剑附上祝福")
        );
        addAdvancement(AdvancementNarakaComponents.ULTIMATE_SPEAR,
                List.of("旧王已死，新王当立!"),
                List.of("我把头俯下去看，但是活人的眼光却及不到黑暗的底部。《神曲》地狱篇")
        );

        addAdvancement(AdvancementExtraComponents.BUY_NECTARIUM_CORE,
                List.of("这是什么?"),
                List.of("从流浪商人处购买蜜银核心")
        );
        addAdvancement(AdvancementExtraComponents.ACTIVATE_NECTARIUM_CORE,
                List.of("流淌着泪水与蜂蜜的方块"),
                List.of("激活蜜银核心")
        );
        addAdvancement(AdvancementExtraComponents.EAT_NECTARIUM,
                List.of("好吃到爆"),
                List.of("食用一个蜜银")
        );
        addAdvancement(AdvancementExtraComponents.CRAFT_SOUL_INFUSED_NECTARIUM,
                List.of("把它交给你的灵魂"),
                List.of("把它交给你的灵魂")
        );

        addPotion(Items.POTION, NarakaPotions.CHALLENGER, "挑战者药水");
        addPotion(Items.SPLASH_POTION, NarakaPotions.CHALLENGER, "喷溅型挑战者药水");
        addPotion(Items.LINGERING_POTION, NarakaPotions.CHALLENGER, "滞留型挑战者药水");
        addPotion(Items.POTION, NarakaPotions.BLESS, "祝福药水");
        addPotion(Items.SPLASH_POTION, NarakaPotions.BLESS, "喷溅型祝福药水");
        addPotion(Items.LINGERING_POTION, NarakaPotions.BLESS, "滞留型祝福药水");

        addItem(NarakaItems.STIGMA_ROD, "耻辱印记调试棒");
        addItem(NarakaItems.NARAKA_FIREBALL_STAFF, "狱炎球法杖");
        addItem(NarakaItems.RAINBOW_SWORD, "虹彩剑");
        addItem(NarakaItems.PURIFIED_SOUL_SHARD, "纯净灵魂碎片");
        addItem(NarakaItems.NECTARIUM, "蜜银");
        addItem(NarakaItems.GOD_BLOOD, "§l神之血");
        addItem(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, "灵魂锻造模板");

        addItem(NarakaItems.SPEAR_ITEM, "矛");
        addItem(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, "神圣之矛");
        addItem(NarakaItems.SPEAR_OF_LONGINUS_ITEM, "朗基努斯之枪");

        addItem(NarakaItems.PURIFIED_SOUL_HELMET, "纯净灵魂头盔");
        addItem(NarakaItems.PURIFIED_SOUL_CHESTPLATE, "纯净灵魂胸甲");
        addItem(NarakaItems.PURIFIED_SOUL_LEGGINGS, "纯净灵魂护腿");
        addItem(NarakaItems.PURIFIED_SOUL_BOOTS, "纯净灵魂靴子");

        addItem(NarakaItems.SOUL_INFUSED_REDSTONE, "魂浸红石");
        addItem(NarakaItems.SOUL_INFUSED_COPPER, "魂浸铜锭");
        addItem(NarakaItems.SOUL_INFUSED_GOLD, "魂浸金锭");
        addItem(NarakaItems.SOUL_INFUSED_EMERALD, "魂浸绿宝石");
        addItem(NarakaItems.SOUL_INFUSED_DIAMOND, "魂浸钻石");
        addItem(NarakaItems.SOUL_INFUSED_LAPIS, "魂浸青金石");
        addItem(NarakaItems.SOUL_INFUSED_AMETHYST, "魂浸紫水晶");
        addItem(NarakaItems.SOUL_INFUSED_NECTARIUM, "魂浸蜜银");
        addItem(NarakaItems.PURIFIED_SOUL_METAL, "纯净灵魂金属");

        addItem(NarakaItems.SOUL_INFUSED_REDSTONE_SWORD, "魂浸红石剑");
        addItem(NarakaItems.SOUL_INFUSED_COPPER_SWORD, "魂浸铜剑");
        addItem(NarakaItems.SOUL_INFUSED_GOLD_SWORD, "魂浸金剑");
        addItem(NarakaItems.SOUL_INFUSED_EMERALD_SWORD, "魂浸绿宝石剑");
        addItem(NarakaItems.SOUL_INFUSED_DIAMOND_SWORD, "魂浸钻石剑");
        addItem(NarakaItems.SOUL_INFUSED_LAPIS_SWORD, "魂浸青金石剑");
        addItem(NarakaItems.SOUL_INFUSED_AMETHYST_SWORD, "魂浸紫水晶剑");
        addItem(NarakaItems.SOUL_INFUSED_NECTARIUM_SWORD, "魂浸蜜银剑");
        addItem(NarakaItems.PURIFIED_SOUL_SWORD, "纯净灵魂剑");

        addItem(NarakaItems.SANCTUARY_COMPASS, "圣所指南针");

        addItem(NarakaItems.HEROBRINE_PHASE_1_DISC, "Herobrine 第一阶段唱片");
        addItem(NarakaItems.HEROBRINE_PHASE_2_DISC, "Herobrine 第二阶段唱片");
        addItem(NarakaItems.HEROBRINE_PHASE_3_DISC, "Herobrine 第三阶段唱片");
        addItem(NarakaItems.HEROBRINE_PHASE_4_DISC, "Herobrine 第四阶段唱片");
        addItem(NarakaItems.SKILL_CONTROLLER, "技能控制器");
        addItem(NarakaItems.ANIMATION_CONTROLLER, "动画控制器");
        addItem(NarakaItems.HEROBRINE_SCARF, "Herobrine的披风");
        addItem(NarakaItems.NARAKA_PICKAXE, "白瞳者之镰");

        addItem(NarakaItems.LOCKED_HEALTH, "被锁定的生命值");
        addItem(NarakaItems.HEROBRINE_SPAWN_EGG, "Herobrine刷怪蛋");
        addItem(NarakaItems.DIAMOND_GOLEM_SPAWN_EGG, "钻石傀儡刷怪蛋");

        addBlock(NarakaBlocks.AMETHYST_ORE, "紫水晶矿石");
        addBlock(NarakaBlocks.DEEPSLATE_AMETHYST_ORE, "深层紫水晶矿石");
        addBlock(NarakaBlocks.NECTARIUM_ORE, "蜜银矿石");
        addBlock(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE, "深层蜜金矿石");
        addBlock(NarakaBlocks.NECTARIUM_BLOCK, "蜜银块");
        addBlock(NarakaBlocks.TRANSPARENT_BLOCK, "透明方块");
        addBlock(NarakaBlocks.IMITATION_GOLD_BLOCK, "仿金块");
        addBlock(NarakaBlocks.AMETHYST_SHARD_BLOCK, "紫水晶碎片块");

        addBlock(NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK, "魂浸红石块");
        addBlock(NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK, "魂浸铜块");
        addBlock(NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK, "魂浸金块");
        addBlock(NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK, "魂浸绿宝石块");
        addBlock(NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK, "魂浸钻石块");
        addBlock(NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK, "魂浸青金石块");
        addBlock(NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK, "魂浸紫水晶块");
        addBlock(NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK, "魂浸蜜银块");

        addBlock(NarakaBlocks.HEROBRINE_TOTEM, "Herobrine图腾");
        addBlock(NarakaBlocks.PURIFIED_SOUL_LANTERN, "纯净灵魂明灯");
        addBlock(NarakaBlocks.PURIFIED_SOUL_LAMP, "纯净灵魂灯");
        addBlock(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK, "纯净灵魂火");
        addBlock(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK, "纯净灵魂金属块");
        addBlock(NarakaBlocks.NECTARIUM_CORE_BLOCK, "蜜银核心");
        addBlock(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK, "蜜银晶簇");
        addBlock(NarakaBlocks.SOUL_STABILIZER, "灵魂稳定器");
        addBlock(NarakaBlocks.SOUL_SMITHING_BLOCK, "灵魂锻造台");
        addBlock(NarakaBlocks.NARAKA_PORTAL, "炼狱传送门");

        add(LanguageKey.HIDDEN_TOOLTIP, "按住 Shift 键显示提示");

        addTooltip(NarakaItemTooltip.HEROBRINE_SCARF, List.of(
                List.of("可装备于胸甲栏位，或通过‘灵魂锻造台’将其融合至装备上。"),
                List.of("沿着披风的内侧，其曾经存在的「炼狱」之影在微微晃动。"),
                List.of("然而这仅为映照，并不能通过它穿梭于「炼狱」之间。")
        ));
        addTooltip(NarakaItemTooltip.NARAKA_PICKAXE, List.of(
                List.of("可用作镐或斧。"),
                List.of("为寻找「色彩」而漂泊于异界，所到之处皆遭践踏与吞噬。"),
                List.of("总是从炼狱中现身，在其入侵的世界里只留下虚无便悄然离去。"),
                List.of("因此在某些世界它被称为‘虚无之神’，而在另一些世界则被称为‘炼狱暴君’。")
        ));
        addTooltip(NarakaItemTooltip.GOD_BLOOD, List.of(
                List.of("用作强力装备的合成材料。"),
                List.of("此外，还可充能至‘灵魂稳定器’中，用于打造强力装备。"),
                List.of("饱含无数恐惧与敬畏之名的本质，不过是为了填补匮乏的暴食。"),
                List.of("未能获得色彩，便渴求色彩，甚至妄图剥夺色彩以充实自身。"),
                List.of("然而无论再添些什么，黑色终究只是黑色。即便是残留的遗骸，也不过是一片漆黑的虚无。")
        ));
        addTooltip(NarakaItemTooltip.SPEAR_OF_LONGINUS, List.of(
                List.of("可以投掷，且会自动回到主人手中。"),
                List.of("无论攻击方式为何，命中敌人时皆可将其即死。"),
                List.of("八次安息，八次证明。"),
                List.of("这件兵器是历经终局所获得的最高纯度之力，亦是汝蜕变为「炼狱」之君的证明。")
        ));
        addTooltip(NarakaItemTooltip.SANCTUARY_COMPASS, List.of(
                List.of("指向当前世界中最接近的‘Herobrine圣所’建筑所在的方向。"),
                List.of("世界为了自身的延续，倾向于去调整因果。"),
                List.of("此物便作为其中一环，将始终指向侵入该世界的存在之圣所。"),
                List.of("静待着能够将其击败并让世界得以延续的英雄现身。")
        ));
        addTooltip(NarakaItemTooltip.IMITATION_GOLD, List.of(
                List.of("放置时，会将相邻的铁块转化为仿金块。"),
                List.of("此外，还用于复制‘Herobrine图腾’。"),
                List.of("金蕴含光彩，而光彩将其唤来。"),
                List.of("此金乃微光暗淡之虚妄产物，逊于真金。"),
                List.of("然而仅凭那缕微光，亦足以将它的脚步引向此处。")
        ));
        addTooltip(NarakaItemTooltip.HEROBRINE_TOTEM, List.of(
                List.of("由下至上依次堆叠 2 个仿金块、1 个 Herobrine图腾和 1 个地狱岩并点燃，即可召唤 Herobrine。"),
                List.of("Herobrine 仅可在‘Herobrine圣所’建筑内被召唤。"),
                List.of("刻有其面容的石碑。"),
                List.of("当仿金吸引了它的目光并引导其脚步时，此物便会将它的肉身召唤并锚定于此方世界。")
        ));
        addTooltip(NarakaItemTooltip.SOUL_INFUSED_MATERIALS, List.of(
                List.of("用于给‘灵魂稳定器’充能。"),
                List.of("将渴望色彩的灵魂碎片融入能发散出最烈色彩的矿物中所成之物。"),
                List.of("渴望色彩之魂的执念，因与最烈之彩合而为一而得到了满足。")
        ));
        addTooltip(NarakaItemTooltip.SOUL_STABILIZER, List.of(
                List.of("放置后，右键使用魂浸矿物或神之血即可充能。"),
                List.of("充能过程必须仅使用同一种物品完成。")
        ));
        addTooltip(NarakaItemTooltip.SOUL_SMITHING_BLOCK, List.of(
                List.of("放置后，装备已充能 provide 的灵魂稳定器与锻造模板，即可打造特定装备。"),
                List.of("打造时，会消耗灵魂稳定器中所充能的一定数量矿物或神之血。")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_METAL, List.of(
                List.of("用作稀有装备 generalize 的合成材料。"),
                List.of("虽称为金属，但更接近于其消散的灵魂凝聚成型后的固化物。")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_SWORD, List.of(
                List.of("右键可附着黑火。黑火仅能通过使用该物品左键予以熄灭。"),
                List.of("可在灵魂锻造台上使用矿物或神之血进行打造。"),
                List.of("最纯净的容器。"),
                List.of("若欲将其用于仪式，需先通过魂浸矿物进行调谐。")
        ));
        addTooltip(NarakaItemTooltip.SOUL_INFUSED_SWORDS_DEFAULT, List.of(
                List.of("手持此剑，并装备 4 件与此剑矿物材质相同的纯净灵魂防具时，将激活‘挑战者’增益。"),
                List.of("在带有‘挑战者’增益的状态下击杀 Herobrine 时，剑将获得祝福，且装备的所有防具将被摧毁。"),
                List.of("挑战者须身披因欲望得以满足而稳定下来的灵魂，并将其作为武器握于手中，让未能摆脱执念的它重新归于安息。"),
                List.of("唯有如此，它的灵魂才能与已稳定的灵魂产生共鸣并寻得宁静，而挑战者手中的武器亦将赐予其祝福。")
        ));
        addTooltip(NarakaItemTooltip.SOUL_INFUSED_SWORDS_BLESSED, List.of(
                List.of("用作"),
                List.of("「朗基努斯之枪」"),
                List.of("的合成材料。"),
                List.of("获得祝福的兵器会彼此共鸣并互相吸引，"),
                List.of("当以其血液为媒介相连时，将展现出最纯粹的力量。")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_ARMORS_DEFAULT, List.of(
                List.of("可在灵魂锻造台上使用矿物或神之血进行打造。"),
                List.of("最纯净的容器。"),
                List.of("若欲将其用于仪式，需先通过魂浸矿物进行调谐。"),
                List.of("抑或是，使用它的血液——")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_ARMORS_SOUL, List.of(
                List.of("装备 4 件与此防具矿物材质相同的纯净灵魂防具，并手持与此防具矿物材质相同的剑时，将激活‘挑战者’增益。"),
                List.of("在带有‘挑战者’增益的状态下击杀 Herobrine 时，剑将获得祝福，且装备的所有防具将被摧毁。"),
                List.of("挑战者须身披因欲望得以满足而稳定下来的灵魂，并将其作为武器握于手中，让未能摆脱执念的它重新归于安息。"),
                List.of("唯有如此，它的灵魂才能与已稳定的灵魂产生共鸣并寻得宁静，而挑战者手中的武器亦将赐予其祝福。")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_ARMOR_BLESSED, List.of(
                List.of("暴食与暴食共鸣，激发出最为强大力量的铠甲。"),
                List.of("未得满足而相互触及的二位一体之魂彼此吞噬，在无尽的苦痛中哀嚎。")
        ));
        addTooltip(NarakaItemTooltip.NECTARIUM, List.of(
                List.of("可食用。食用后饱食度、额外饱食度与生命值将恢复至最大值。"),
                List.of("据说在某个异次元，未遵守契约之人将被施以「吞石之刑」。")
        ));
        addTooltip(NarakaItemTooltip.NECTARIUM_CORE, List.of(
                List.of("使用蜂蜜瓶可将其激活，激活后会生成蜜银晶簇。"),
                List.of("无论是矿物还是作物，本质上都是‘物’，因而并无二致。"),
                List.of("因此，这是旨在拯救 22 世纪人类的新概念作物。")
        ));

        addEntityType(NarakaEntityTypes.HEROBRINE, "炼狱暴君 Herobrine");
        addEntityType(NarakaEntityTypes.ORIGIN_HEROBRINE, "炼狱之源 Herobrine");
        addEntityType(NarakaEntityTypes.SHADOW_HEROBRINE, "Herobrine之影");
        addEntityType(NarakaEntityTypes.THROWN_SPEAR, "矛");
        addEntityType(NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR, "神圣之矛");
        addEntityType(NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS, "朗基努斯之枪");
        addEntityType(NarakaEntityTypes.NARAKA_FIREBALL, "炼狱火球");
        addEntityType(NarakaEntityTypes.STARDUST, "星尘");
        addEntityType(NarakaEntityTypes.PICKAXE_SLASH, "巨镰剑气");
        addEntityType(NarakaEntityTypes.DIAMOND_GOLEM, "钻石傀儡");
        addEntityType(NarakaEntityTypes.MAGIC_CIRCLE, "法阵");
        addEntityType(NarakaEntityTypes.NARAKA_PICKAXE, "白瞳者之镰");
        addEntityType(NarakaEntityTypes.COLORED_LIGHTNING_BOLT, "彩色闪电");
        addEntityType(NarakaEntityTypes.MASSIVE_LIGHTNING, "巨雷");
        addEntityType(NarakaEntityTypes.CORRUPTED_STAR, "堕落之星");
        addEntityType(NarakaEntityTypes.SHINY_EFFECT, "闪光");
        addEntityType(NarakaEntityTypes.AREA_EFFECT, "效果云");
        addEntityType(NarakaEntityTypes.NARAKA_PORTAL, "Naraka 传送门");
        addEntityType(NarakaEntityTypes.LIGHTNING_CIRCLE, "雷环");

        addDamageType(NarakaDamageTypes.SPEAR_OF_LONGINUS, "%1$s失去了属于自己的颜色，从世界上消失了。");
        addDamageType(NarakaDamageTypes.STIGMA, "%1$s被%2$s施加的耻辱印记夺去了生命。");
        addDamageType(NarakaDamageTypes.STIGMA_CONSUME, "%2$s消耗了%1$s的印记。");
        addDamageType(NarakaDamageTypes.PICKAXE_SLASH, "%1$s被%2$s劈死了。");
        addDamageType(NarakaDamageTypes.NARAKA_FIREBALL, "%1$s被%2$s投出的炼狱火球炸烂了。");
        addDamageType(NarakaDamageTypes.PURIFIED_SOUL_FIRE, "%1$s在黑焰中化为灰烬。");
        addDamageType(NarakaDamageTypes.SOUL_ATTACK, "%2$s处决了%1$s。");

        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_AMETHYST), "挑战者的祝福");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_COPPER), "挑战者的祝福");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_DIAMOND), "挑战者的祝福");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_EMERALD), "挑战者的祝福");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_GOLD), "挑战者的祝福");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_LAPIS), "挑战者的祝福");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_NECTARIUM), "挑战者的祝福");
        add(LanguageKey.mobEffect(NarakaMobEffects.CHALLENGERS_BLESSING_REDSTONE), "挑战者的祝福");
        add(LanguageKey.mobEffect(NarakaMobEffects.GOD_BLESS), "神佑");

        add(LanguageKey.STIGMA_COMMAND_GET_KEY, "实体 %s 的耻辱印记值为 %d");
        add(LanguageKey.STIGMA_COMMAND_SET_KEY, "已将 %d 个实体的耻辱印记值设为 %d");
        add(LanguageKey.STIGMA_COMMAND_INCREASE_KEY, "已增加 %1$d 个实体的耻辱印记");
        add(LanguageKey.STIGMA_COMMAND_REMOVE_KEY, "已移除 %1$d 个实体的耻辱印记");
        add(LanguageKey.STIGMA_COMMAND_CONSUME_KEY, "%2$s 消耗了 %1$d 个实体的印记");
        add(LanguageKey.STIGMA_COMMAND_DISABLE_KEY, "已禁用耻辱印记");
        add(LanguageKey.STIGMA_COMMAND_ENABLE_KEY, "已启用耻辱印记");

        add(LanguageKey.LOCK_HEALTH_COMMAND_LOCK_KEY, "已将 %2$s 的生命值锁定为 %1$d");
        add(LanguageKey.LOCK_HEALTH_COMMAND_REMOVE_KEY, "已恢复 %1$s 被锁定的生命值");
    }
}
