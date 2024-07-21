package com.yummy.naraka.data.lang;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.damagesource.NarakaDamageTypes;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;

public abstract class NarakaLanguageProvider extends FabricLanguageProvider {
    public static final String PURIFIED_SOUL_UPGRADE_KEY = Util.makeDescriptionId("upgrade", NarakaMod.location("purified_soul_upgrade"));
    public static final String PURIFIED_SOUL_UPGRADE_APPLIES_TO_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_soul_upgrade.applies_to"));
    public static final String PURIFIED_SOUL_UPGRADE_INGREDIENTS_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_soul_upgrade.ingredients"));
    public static final String PURIFIED_SOUL_UPGRADE_BASE_SLOT_DESCRIPTION_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_soul_upgrade.base_slot_description"));
    public static final String PURIFIED_SOUL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_soul_upgrade.additions_slot_description"));

    public static final String PURIFIED_GEM_UPGRADE_KEY = Util.makeDescriptionId("upgrade", NarakaMod.location("purified_gem_upgrade"));
    public static final String PURIFIED_GEM_UPGRADE_APPLIES_TO_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_gem_upgrade.applies_to"));
    public static final String PURIFIED_GEM_UPGRADE_INGREDIENTS_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_gem_upgrade.ingredients"));
    public static final String PURIFIED_GEM_UPGRADE_BASE_SLOT_DESCRIPTION_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_gem_upgrade.base_slot_description"));
    public static final String PURIFIED_GEM_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_gem_upgrade.additions_slot_description"));

    protected NarakaLanguageProvider(FabricDataOutput dataOutput, String languageCode, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, languageCode, registryLookup);
    }

    public static String createId(String parent, String name) {
        return "%s.%s.%s".formatted(parent, NarakaMod.MOD_ID, name);
    }

    public static String createId(String parent, ResourceLocation location) {
        return createId(parent, location.getPath());
    }

    public void addAdvancement(TranslationBuilder builder, AdvancementComponent advancementComponent, String title, String description) {
        builder.add(advancementComponent.titleKey(), title);
        builder.add(advancementComponent.descriptionKey(), description);
    }

    public void addDamageType(TranslationBuilder builder, ResourceKey<DamageType> damageType, String directMessage, String indirectMessage) {
        String directKey = "death.attack." + damageType.location().getPath();
        String indirectKey = directKey + ".player";

        builder.add(directKey, directMessage);
        builder.add(indirectKey, indirectMessage);
    }

    public static class EN extends NarakaLanguageProvider {
        public EN(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(dataOutput, "en_us", registryLookup);
        }

        @Override
        public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder builder) {
            builder.add("itemGroup.naraka", "Naraka");
            builder.add("container.soul_crafting", "Soul Crafter");
            builder.add(PURIFIED_SOUL_UPGRADE_KEY, "Purified Soul Upgrade");
            builder.add(PURIFIED_SOUL_UPGRADE_APPLIES_TO_KEY, "Ebony Tools");
            builder.add(PURIFIED_SOUL_UPGRADE_INGREDIENTS_KEY, "Purified Soul Metal");
            builder.add(PURIFIED_SOUL_UPGRADE_BASE_SLOT_DESCRIPTION_KEY, "Add ebony weapon");
            builder.add(PURIFIED_SOUL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_KEY, "Add Purified Soul Metal");
            builder.add(PURIFIED_GEM_UPGRADE_KEY, "Purified Gem Upgrade");
            builder.add(PURIFIED_GEM_UPGRADE_APPLIES_TO_KEY, "Purified Soul Weapons");
            builder.add(PURIFIED_GEM_UPGRADE_INGREDIENTS_KEY, "Soul Infused Materials");
            builder.add(PURIFIED_GEM_UPGRADE_BASE_SLOT_DESCRIPTION_KEY, "Add soul weapon");
            builder.add(PURIFIED_GEM_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_KEY, "Add soul infused materials");

            addAdvancement(builder, AdvancementNarakaComponents.ROOT, "Naraka!", "Welcome to Naraka!");
            addAdvancement(builder, AdvancementNarakaComponents.HEROBRINE_SANCTUARY, "Herobrine Sanctuary", "Too Big!");
            addAdvancement(builder, AdvancementNarakaComponents.SUMMON_HEROBRINE, "Naraka Tyrant", "Summon Herobrine");
            addAdvancement(builder, AdvancementNarakaComponents.KILL_HEROBRINE, "Purified Soul", "Defeat the lord of Naraka");
            addAdvancement(builder, AdvancementNarakaComponents.GOD_BLOOD, "Blood of God", "Very AWESOME!");

            builder.add(NarakaItems.PURIFIED_SOUL_SHARD, "Purified Soul Shard");
            builder.add(NarakaItems.NECTARIUM, "Nectarium");
            builder.add(NarakaItems.GOD_BLOOD, "God Blood");
            builder.add(NarakaItems.COMPRESSED_IRON_INGOT, "Compressed Iron Ingot");
            builder.add(NarakaItems.FAKE_GOLD_INGOT, "Fake Gold Ingot");
            builder.add(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, "Smithing Template");
            builder.add(NarakaItems.PURIFIED_GEMS_UPGRADE_SMITHING_TEMPLATE, "Smithing Template");
            builder.add(NarakaItems.SPEAR_ITEM, "Spear");
            builder.add(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, "Mighty Holy Spear");
            builder.add(NarakaItems.SPEAR_OF_LONGINUS_ITEM, "Spear of Longinus");

            builder.add(NarakaItems.SOUL_INFUSED_REDSTONE, "Soul Infused Redstone");
            builder.add(NarakaItems.SOUL_INFUSED_COPPER, "Soul Infused Copper");
            builder.add(NarakaItems.SOUL_INFUSED_GOLD, "Soul Infused Gold");
            builder.add(NarakaItems.SOUL_INFUSED_EMERALD, "Soul Infused Emerald");
            builder.add(NarakaItems.SOUL_INFUSED_DIAMOND, "Soul Infused Diamond");
            builder.add(NarakaItems.SOUL_INFUSED_LAPIS, "Soul Infused Lapis");
            builder.add(NarakaItems.SOUL_INFUSED_AMETHYST, "Soul Infused Amethyst");
            builder.add(NarakaItems.SOUL_INFUSED_NECTARIUM, "Soul Infused Nectarium");
            builder.add(NarakaItems.PURIFIED_SOUL_METAL, "Purified Soul Metal");

            builder.add(NarakaItems.SOUL_INFUSED_REDSTONE_SWORD, "Soul Infused Redstone Sword");
            builder.add(NarakaItems.SOUL_INFUSED_COPPER_SWORD, "Soul Infused Copper Sword");
            builder.add(NarakaItems.SOUL_INFUSED_GOLD_SWORD, "Soul Infused Gold Sword");
            builder.add(NarakaItems.SOUL_INFUSED_EMERALD_SWORD, "Soul Infused Emerald Sword");
            builder.add(NarakaItems.SOUL_INFUSED_DIAMOND_SWORD, "Soul Infused Diamond Sword");
            builder.add(NarakaItems.SOUL_INFUSED_LAPIS_SWORD, "Soul Infused Lapis Sword");
            builder.add(NarakaItems.SOUL_INFUSED_AMETHYST_SWORD, "Soul Infused Amethyst Sword");
            builder.add(NarakaItems.SOUL_INFUSED_NECTARIUM_SWORD, "Soul Infused Nectarium Sword");
            builder.add(NarakaItems.PURIFIED_SOUL_SWORD, "Purified Soul Sword");

            builder.add(NarakaItems.EBONY_SWORD, "Ebony Sword");

            builder.add(NarakaBlocks.NECTARIUM_ORE, "Nectarium Ore");
            builder.add(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE, "Deepslate Nectarium Ore");
            builder.add(NarakaBlocks.NECTARIUM_BLOCK, "Nectarium Block");
            builder.add(NarakaBlocks.TRANSPARENT_BLOCK, "Transparent Block");
            builder.add(NarakaBlocks.COMPRESSED_IRON_BLOCK, "Compressed Iron Block");
            builder.add(NarakaBlocks.FAKE_GOLD_BLOCK, "Fake Gold Block");
            builder.add(NarakaBlocks.AMETHYST_SHARD_BLOCK, "Amethyst Shard Block");
            builder.add(NarakaBlocks.SOUL_CRAFTING_BLOCK, "Soul Crafting Block");

            builder.add(NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK, "Soul Infused Redstone Block");
            builder.add(NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK, "Soul Infused Copper Block");
            builder.add(NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK, "Soul Infused Gold Block");
            builder.add(NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK, "Soul Infused Emerald Block");
            builder.add(NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK, "Soul Infused Diamond Block");
            builder.add(NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK, "Soul Infused Lapis Block");
            builder.add(NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK, "Soul Infused Amethyst Block");
            builder.add(NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK, "Soul Infused Nectarium Block");
            builder.add(NarakaBlocks.PURIFIED_SOUL_BLOCK, "Purified Soul Block");

            builder.add(NarakaBlocks.HEROBRINE_TOTEM, "Herobrine Totem");
            builder.add(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK, "Purified Soul Fire");
            builder.add(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK, "Purified Soul Metal Block");
            builder.add(NarakaBlocks.EBONY_LOG, "Ebony Log");
            builder.add(NarakaBlocks.STRIPPED_EBONY_LOG, "Stripped Ebony Log");
            builder.add(NarakaBlocks.EBONY_WOOD, "Ebony Wood");
            builder.add(NarakaBlocks.STRIPPED_EBONY_WOOD, "Stripped Ebony Wood");
            builder.add(NarakaBlocks.EBONY_LEAVES, "Ebony Leaves");
            builder.add(NarakaBlocks.EBONY_SAPLING, "Ebony Sapling");
            builder.add(NarakaBlocks.EBONY_PLANKS, "Ebony Planks");
            builder.add(NarakaBlocks.HARD_EBONY_PLANKS, "Hard Ebony Planks");

            builder.add(NarakaEntityTypes.HEROBRINE, "Naraka: Herobrine");
            builder.add(NarakaEntityTypes.THROWN_SPEAR, "Spear");
            builder.add(NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR, "Mighty Holy Spear");
            builder.add(NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS, "Spear of Longinus");

            addDamageType(builder, NarakaDamageTypes.SPEAR_OF_LONGINUS,
                    "%1$s's AT Field was torn by %2$s",
                    "%1$s's AT Field was torn by %2$s thrown %3$s");
        }
    }

    public static class KR extends NarakaLanguageProvider {
        public KR(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(dataOutput, "ko_kr", registryLookup);
        }

        @Override
        public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder builder) {
            builder.add("itemGroup.naraka", "Naraka");
            builder.add("container.soul_crafting", "영혼 세공기");
            builder.add(PURIFIED_SOUL_UPGRADE_KEY, "정화된 영혼 강화");
            builder.add(PURIFIED_SOUL_UPGRADE_APPLIES_TO_KEY, "흑단나무 검");
            builder.add(PURIFIED_SOUL_UPGRADE_INGREDIENTS_KEY, "정화된 영혼 금속");
            builder.add(PURIFIED_SOUL_UPGRADE_BASE_SLOT_DESCRIPTION_KEY, "흑단나무 무기를 놓으세요");
            builder.add(PURIFIED_SOUL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_KEY, "정화된 영혼 금속을 놓으세요");
            builder.add(PURIFIED_GEM_UPGRADE_KEY, "정화된 영혼 강화");
            builder.add(PURIFIED_GEM_UPGRADE_APPLIES_TO_KEY, "정화된 영혼 무기");
            builder.add(PURIFIED_GEM_UPGRADE_INGREDIENTS_KEY, "영혼이 주입된 재료");
            builder.add(PURIFIED_GEM_UPGRADE_BASE_SLOT_DESCRIPTION_KEY, "정화된 영혼 검 또는 창을 놓으세요");
            builder.add(PURIFIED_GEM_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_KEY, "영혼이 주입된 재료 또는 신의 피를 놓으세요");

            builder.add(NarakaItems.PURIFIED_SOUL_SHARD, "정화된 영혼 조각");
            builder.add(NarakaItems.NECTARIUM, "넥타륨");
            builder.add(NarakaItems.GOD_BLOOD, "신의 피");
            builder.add(NarakaItems.COMPRESSED_IRON_INGOT, "압축된 철 주괴");
            builder.add(NarakaItems.FAKE_GOLD_INGOT, "거짓된 금 주괴");
            builder.add(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, "대장장이 형판");
            builder.add(NarakaItems.PURIFIED_GEMS_UPGRADE_SMITHING_TEMPLATE, "대장장이 형판");
            builder.add(NarakaItems.SPEAR_ITEM, "창");
            builder.add(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, "강력한 성스러운 창");
            builder.add(NarakaItems.SPEAR_OF_LONGINUS_ITEM, "롱기누스의 창");

            builder.add(NarakaItems.SOUL_INFUSED_REDSTONE, "영혼이 주입된 레드스톤");
            builder.add(NarakaItems.SOUL_INFUSED_COPPER, "영혼이 주입된 구리");
            builder.add(NarakaItems.SOUL_INFUSED_GOLD, "영혼이 주입된 금");
            builder.add(NarakaItems.SOUL_INFUSED_EMERALD, "영혼이 주입된 에메랄드");
            builder.add(NarakaItems.SOUL_INFUSED_DIAMOND, "영혼이 주입된 다이아몬드");
            builder.add(NarakaItems.SOUL_INFUSED_LAPIS, "영혼이 주입된 청금석");
            builder.add(NarakaItems.SOUL_INFUSED_AMETHYST, "영혼이 주입된 자수정");
            builder.add(NarakaItems.SOUL_INFUSED_NECTARIUM, "영혼이 주입된 넥타륨");
            builder.add(NarakaItems.PURIFIED_SOUL_METAL, "정화된 영혼 금속");

            builder.add(NarakaItems.SOUL_INFUSED_REDSTONE_SWORD, "영혼이 주입된 레드스톤 검");
            builder.add(NarakaItems.SOUL_INFUSED_COPPER_SWORD, "영혼이 주입된 구리 검");
            builder.add(NarakaItems.SOUL_INFUSED_GOLD_SWORD, "영혼이 주입된 금 검");
            builder.add(NarakaItems.SOUL_INFUSED_EMERALD_SWORD, "영혼이 주입된 에메랄드 검");
            builder.add(NarakaItems.SOUL_INFUSED_DIAMOND_SWORD, "영혼이 주입된 다이아몬드 검");
            builder.add(NarakaItems.SOUL_INFUSED_LAPIS_SWORD, "영혼이 주입된 청금석 검");
            builder.add(NarakaItems.SOUL_INFUSED_AMETHYST_SWORD, "영혼이 주입된 자수정 검");
            builder.add(NarakaItems.SOUL_INFUSED_NECTARIUM_SWORD, "영혼이 주입된 넥타륨 검");
            builder.add(NarakaItems.PURIFIED_SOUL_SWORD, "정화된 영혼 검");

            builder.add(NarakaItems.EBONY_SWORD, "흑단나무 검");

            builder.add(NarakaBlocks.NECTARIUM_ORE, "넥타륨 광석");
            builder.add(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE, "심층암 넥타륨 광석");
            builder.add(NarakaBlocks.NECTARIUM_BLOCK, "넥타륨 블록");
            builder.add(NarakaBlocks.TRANSPARENT_BLOCK, "투명 블록");
            builder.add(NarakaBlocks.COMPRESSED_IRON_BLOCK, "압축된 철 블록");
            builder.add(NarakaBlocks.FAKE_GOLD_BLOCK, "거짓된 금 블록");
            builder.add(NarakaBlocks.AMETHYST_SHARD_BLOCK, "자수정 조각 블록");
            builder.add(NarakaBlocks.SOUL_CRAFTING_BLOCK, "영혼 세공기");

            builder.add(NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK, "영혼이 주입된 레드스톤 블록");
            builder.add(NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK, "영혼이 주입된 구리 블록");
            builder.add(NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK, "영혼이 주입된 금 블록");
            builder.add(NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK, "영혼이 주입된 에메랄드 블록");
            builder.add(NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK, "영혼이 주입된 다이아몬드 블록");
            builder.add(NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK, "영혼이 주입된 청금석 블록");
            builder.add(NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK, "영혼이 주입된 자수정 블록");
            builder.add(NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK, "영혼이 주입된 넥타륨 블록");
            builder.add(NarakaBlocks.PURIFIED_SOUL_BLOCK, "정화된 영혼 블록");

            builder.add(NarakaBlocks.HEROBRINE_TOTEM, "히로빈 토템");
            builder.add(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK, "정화된 영혼 불");
            builder.add(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK, "정화된 영혼 금속 블록");
            builder.add(NarakaBlocks.EBONY_LOG, "흑단나무 원목");
            builder.add(NarakaBlocks.STRIPPED_EBONY_LOG, "껍질 벗긴 흑단나무 원목");
            builder.add(NarakaBlocks.EBONY_WOOD, "흑단나무");
            builder.add(NarakaBlocks.EBONY_LEAVES, "흑단나무 나뭇잎");
            builder.add(NarakaBlocks.STRIPPED_EBONY_WOOD, "껍질 벗긴 흑단나무");
            builder.add(NarakaBlocks.EBONY_SAPLING, "흑단나무 묘목");
            builder.add(NarakaBlocks.EBONY_PLANKS, "흑단나무 판자");
            builder.add(NarakaBlocks.HARD_EBONY_PLANKS, "단단한 흑단나무 판자");

            builder.add(NarakaEntityTypes.HEROBRINE, "히로빈");
            builder.add(NarakaEntityTypes.THROWN_SPEAR, "창");
            builder.add(NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR, "강력한 성스러운 창");
            builder.add(NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS, "롱기누스의 창");

            addDamageType(builder, NarakaDamageTypes.SPEAR_OF_LONGINUS,
                    "%1$s의 AT 필드가 %2$s에 찢어졌습니다.",
                    "%1$s의 AT 필드가 %3$s가 던진 %2$s에 찢어졌습니다.");
        }
    }
}