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

        add(LanguageKey.CONFIG_TITLE, "Naraka Config");
        add(LanguageKey.CONFIG_CATEGORY_COMMON, "Naraka Common Config");
        addConfig(NarakaConfig.COMMON.showTestCreativeModeTab,
                List.of("显示创造测试物品标签页"),
                List.of(
                        List.of("需要重启")
                )
        );
        addConfig(NarakaConfig.COMMON.enableStigma, "启用耻辱印记");
        addConfig(NarakaConfig.COMMON.stigmaStunDuration,
                List.of("耻辱印记眩晕持续时间"),
                List.of(
                        List.of("眩晕持续时间（tick）")
                )
        );
        addConfig(NarakaConfig.COMMON.lockHealthRatio, "锁定生命值比例");

        add(LanguageKey.CONFIG_CATEGORY_CLIENT, "Naraka Client Config");
        addConfig(NarakaConfig.CLIENT.playHerobrineBossMusic, "播放 Herobrine Boss 音乐");
        addConfig(NarakaConfig.CLIENT.enableOreSeeThrough, "启用矿物透视");
        addConfig(NarakaConfig.CLIENT.oreSeeThroughRange, "矿物透视范围");
        addConfig(NarakaConfig.CLIENT.cameraShakingSpeed, "视角摇晃速度");
        addConfig(NarakaConfig.CLIENT.cameraShakingStrength, "视角摇晃强度");
        add(LanguageKey.CONFIG_ORE_COLOR, "矿物轮廓颜色");
        add(LanguageKey.CONFIG_ORE_COLOR_WRONG, "格式错误！");

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

        addItem(NarakaItems.NETHERITE_HAMMER, "下界合金重锤");
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
                List.of("可装备于胸甲槽位，或通过“灵魂锻造台”合成为防具的一部分"),
                List.of("沿着披风的内衬，那曾经存在的“炼狱”若隐若现"),
                List.of("然而这只是幻像，无法借此往返于炼狱之中")
        ));
        addTooltip(NarakaItemTooltip.NARAKA_PICKAXE, List.of(
                List.of("可作为镐或斧使用"),
                List.of("为追寻「色彩」而游荡于异界，沿途的一切都被祂践踏吸收"),
                List.of("祂总是自炼狱而现，将一切吞噬后便悄然消失，只留下无尽的虚空"),
                List.of("因此，在某些世界祂被称为“虚无之神”，而在另一些世界则被称为“炼狱暴君”")
        ));
        addTooltip(NarakaItemTooltip.GOD_BLOOD, List.of(
                List.of("用于制作强大的装备。"),
                List.of("此外，也可充能至“灵魂稳定器”中，用于锻造强大的装备。"),
                List.of("藏匿在恐惧与敬畏之名下的本质，是为了填补空虚的贪欲"),
                List.of("无法拥有，因而渴望，于是祂试图剥离那些色彩并占为己有"),
                List.of("然而无论添加什么，黑色终究只是黑色，连留下的遗骸也不过是一片漆黑的虚无")
        ));
        addTooltip(NarakaItemTooltip.SPEAR, List.of(
                List.of("可在灵魂锻造台上使用魂浸矿物或神之血进行锻造"),
                List.of("以祂之灵魂锻造的武器"),
                List.of("仅仅是赋予了其实体，它的力量实在太过微弱"),
                List.of("但作为一件祭品，它确实再合适不过了")
        ));
        addTooltip(NarakaItemTooltip.MIGHTY_HOLY_SPEAR, List.of(
                List.of("为八把不同材质的魂浸剑附上祝福，并将它们作为材料来合成那来自神话中的武器..."),
                List.of("暴君已然逝去，但「炼狱」仍需一位王者"),
                List.of("弑君者啊，完成八次充满苦难的试炼，证明你配得上那王座")
        ));
        addTooltip(NarakaItemTooltip.SPEAR_OF_LONGINUS, List.of(
                List.of("可以投掷，并会自动回到主人手中"),
                List.of("无论何种方式，只要命中便可将敌人秒杀"),
                List.of("八次安息，八次证明"),
                List.of("你已证明了自己，弑君者。「炼狱」的王座如今属于你"),
                List.of("这把武器便是那段旅程终点获得的至纯之力，也是你蜕变为「炼狱」主宰的证明")
        ));
        addTooltip(NarakaItemTooltip.SANCTUARY_COMPASS, List.of(
                List.of("它指向距离你最近的「Herobrine圣所」"),
                List.of("世界为了自身的存续，往往倾向于调控因果"),
                List.of("作为其中的一环，它将始终指向那个入侵世界的「圣所」"),
                List.of("静候一位能将其击败并延续世界存续的英雄降临")
        ));
        addTooltip(NarakaItemTooltip.IMITATION_GOLD, List.of(
                List.of("放置后，会将相邻的铁块转化为仿金块"),
                List.of("此外，也用于复制“Herobrine图腾”"),
                List.of("黄金蕴含着耀眼的光芒，而光芒将召唤祂"),
                List.of("它所蕴的光芒远远弱于真金，不过是虚妄之物"),
                List.of("但即便是这道微弱的光芒，也足以指引祂的方向")
        ));
        addTooltip(NarakaItemTooltip.HEROBRINE_TOTEM, List.of(
                List.of("自下而上依次堆叠 2 个仿金块、1 个 Herobrine 图腾和 1 个下界岩并点燃，即可召唤 Herobrine"),
                List.of("Herobrine 仅可在“Herobrine 圣所”结构中召唤"),
                List.of("雕刻着祂面容的石碑"),
                List.of("当仿金吸引到祂的目光并为之引路时，此物便会将祂的肉身召唤至这个世界并予以锚定")
        ));
        addTooltip(NarakaItemTooltip.SOUL_INFUSED_MATERIALS, List.of(
                List.of("用于为“灵魂稳定器”充能。"),
                List.of("将祂渴望色彩的灵魂碎片注入到散发着最鲜艳色彩的矿物中制成的物品"),
                List.of("那份贪婪，在将其与最为灿烂的光芒融为一体时终于得到了满足")
        ));
        addTooltip(NarakaItemTooltip.SOUL_STABILIZER, List.of(
                List.of("放置后，对其右键使用魂浸矿物或 §l神之血§r 即可充能"),
                List.of("充能必须使用同种物品完成")
        ));
        addTooltip(NarakaItemTooltip.SOUL_SMITHING_BLOCK, List.of(
                List.of("放置后，装配充能完毕的灵魂稳定器与灵魂锻造模板，即可锻造特定装备"),
                List.of("锻造时将消耗灵魂稳定器中一定数量的魂浸矿物或神之血")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_METAL, List.of(
                List.of("用于制作稀有装备。"),
                List.of("虽被冠以金属之名，但却更接近于祂散落的灵魂凝聚成型")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_SWORD, List.of(
                List.of("右键可点燃黑焰，黑焰仅可通过此物品左键熄灭"),
                List.of("可在灵魂锻造台上使用魂浸矿物或神之血进行锻造"),
                List.of("最为纯净的容器"),
                List.of("若要用于仪式，需通过魂浸矿物对其进行调谐")
        ));
        addTooltip(NarakaItemTooltip.SOUL_INFUSED_SWORDS_DEFAULT, List.of(
                List.of("手持此剑并装备 4 件使用相同魂浸矿物锻造的纯净灵魂盔甲时，将激活“挑战者”效果"),
                List.of("在持有“挑战者”效果的状态下击杀 Herobrine，将为剑施加祝福，同时防具将全部损毁"),
                List.of("挑战者必须将满足渴望后而稳定的灵魂作为武器使用，才能让那未能脱离执妄的祂得以安息"),
                List.of("如此，祂的灵魂将与稳定的灵魂共鸣，而挑战者所持之剑将降下赐福")
        ));
        addTooltip(NarakaItemTooltip.SOUL_INFUSED_SWORDS_BLESSED, List.of(
                List.of("用作"),
                List.of("「朗基努斯之枪」"),
                List.of("的合成材料"),
                List.of("受祝福的装备将彼此吸引，互相共鸣，"),
                List.of("当以祂的血为媒介时，将会展现它最为纯粹的力量")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_ARMORS_DEFAULT, List.of(
                List.of("可在灵魂锻造台上使用魂浸矿物或神之血进行锻造"),
                List.of("最为纯净的容器。"),
                List.of("若要用于仪式，需先通过魂浸矿物对其进行调谐"),
                List.of("或者，借由“祂”的血——")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_ARMORS_SOUL, List.of(
                List.of("装备 4 件相同材质的纯净灵魂盔甲，并手持相同矿物锻造的魂浸剑时，将激活“挑战者”效果"),
                List.of("在持有“挑战者”效果的状态下击杀 Herobrine，将为剑施加祝福，同时防具将全部损毁"),
                List.of("挑战者必须将满足渴望后而稳定的灵魂作为武器使用，才能让那未能脱离执妄的祂得以安息"),
                List.of("如此，祂的灵魂将与稳定的灵魂共鸣，而挑战者所持之剑将降下赐福")
        ));
        addTooltip(NarakaItemTooltip.PURIFIED_SOUL_ARMOR_BLESSED, List.of(
                List.of("通过二者贪婪的共鸣而激发至高力量的盔甲"),
                List.of("两个灵魂——实则为一，在无法被满足的贪婪中彼此触碰、吞噬，在永无止境的苦痛中哀嚎")
        ));
        addTooltip(NarakaItemTooltip.NECTARIUM, List.of(
                List.of("可食用，食用后饱和度、饥饿值与生命值将恢复至上限"),
                List.of("据说在某个异次元中，未履约者将会受到「食岩之罚」")
        ));
        addTooltip(NarakaItemTooltip.NECTARIUM_CORE, List.of(
                List.of("使用蜂蜜瓶可将其激活，激活后将生成蜜银晶簇"),
                List.of("无论是矿物还是作物，本质上皆为“物”，并无实质区别"),
                List.of("因此，这是将拯救22世纪人类的新概念作物")
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
        addDamageType(NarakaDamageTypes.CORRUPTED_STAR, "%1$s被%2$s炸飞了。");
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
