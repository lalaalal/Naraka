package com.yummy.naraka.data.lang;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.damagesource.NarakaDamageTypes;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.item.NarakaItems;
import com.yummy.naraka.world.item.armortrim.NarakaTrimMaterials;
import com.yummy.naraka.world.item.armortrim.NarakaTrimPatterns;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;

import java.util.concurrent.CompletableFuture;

public abstract class NarakaLanguageProvider extends FabricLanguageProvider {
    public static final String PURIFIED_SOUL_UPGRADE_KEY = Util.makeDescriptionId("upgrade", NarakaMod.location("purified_soul_upgrade"));
    public static final String PURIFIED_SOUL_UPGRADE_APPLIES_TO_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_soul_upgrade.applies_to"));
    public static final String PURIFIED_SOUL_UPGRADE_INGREDIENTS_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_soul_upgrade.ingredients"));
    public static final String PURIFIED_SOUL_UPGRADE_BASE_SLOT_DESCRIPTION_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_soul_upgrade.base_slot_description"));
    public static final String PURIFIED_SOUL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_KEY = Util.makeDescriptionId("item", NarakaMod.location("smithing_template.purified_soul_upgrade.additions_slot_description"));
    public static final String JADE_SOUL_CRAFTING_FUEL_KEY = "jade.naraka.soul_crafting.fuel";

    protected NarakaLanguageProvider(FabricDataOutput dataOutput, String languageCode, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, languageCode, registryLookup);
    }

    public void addTrimPattern(TranslationBuilder builder, ResourceKey<TrimPattern> trimPattern, String value) {
        String key = trimPattern.location().toLanguageKey("trim_pattern");
        builder.add(key, value);
    }

    public void addTrimMaterial(TranslationBuilder builder, ResourceKey<TrimMaterial> trimMaterial, String value) {
        String key = trimMaterial.location().toLanguageKey("trim_material");
        builder.add(key, value);
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
            builder.add(PURIFIED_SOUL_UPGRADE_APPLIES_TO_KEY, "Ebony Tools, Purified Soul Weapons");
            builder.add(PURIFIED_SOUL_UPGRADE_INGREDIENTS_KEY, "Purified Soul Metal, Soul Infused Materials");
            builder.add(PURIFIED_SOUL_UPGRADE_BASE_SLOT_DESCRIPTION_KEY, "Add Ebony Sword, Soul Weapon");
            builder.add(PURIFIED_SOUL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_KEY, "Add Purified Soul Metal, Soul Infused Materials");

            builder.add(JADE_SOUL_CRAFTING_FUEL_KEY, "Fuel: %d");
            builder.add(NarakaJadeProviders.SOUL_CRAFTING_BLOCK.translationKey, "Soul Crafting Block");

            addTrimPattern(builder, NarakaTrimPatterns.PURIFIED_SOUL_SILENCE, "Purified Soul Silence Armor Trim");
            addTrimMaterial(builder, NarakaTrimMaterials.SOUL_INFUSED_REDSTONE, "Soul Infused Redstone Material");
            addTrimMaterial(builder, NarakaTrimMaterials.SOUL_INFUSED_COPPER, "Soul Infused Copper Material");
            addTrimMaterial(builder, NarakaTrimMaterials.SOUL_INFUSED_GOLD, "Soul Infused Gold Material");
            addTrimMaterial(builder, NarakaTrimMaterials.SOUL_INFUSED_EMERALD, "Soul Infused Emerald Material");
            addTrimMaterial(builder, NarakaTrimMaterials.SOUL_INFUSED_DIAMOND, "Soul Infused Diamond Material");
            addTrimMaterial(builder, NarakaTrimMaterials.SOUL_INFUSED_LAPIS, "Soul Infused Lapis Material");
            addTrimMaterial(builder, NarakaTrimMaterials.SOUL_INFUSED_AMETHYST, "Soul Infused Amethyst Material");
            addTrimMaterial(builder, NarakaTrimMaterials.SOUL_INFUSED_NECTARIUM, "Soul Infused Nectarium Material");

            addAdvancement(builder, AdvancementNarakaComponents.ROOT, "Naraka!", "Welcome to Naraka!");
            addAdvancement(builder, AdvancementNarakaComponents.HEROBRINE_SANCTUARY, "Herobrine Sanctuary", "Too Big!");
            addAdvancement(builder, AdvancementNarakaComponents.SUMMON_HEROBRINE, "Naraka Tyrant", "Summon Herobrine");
            addAdvancement(builder, AdvancementNarakaComponents.KILL_HEROBRINE, "Purified Soul", "Defeat the lord of Naraka");
            addAdvancement(builder, AdvancementNarakaComponents.GOD_BLOOD, "Blood of God", "Very AWESOME!");

            builder.add(NarakaItems.PURIFIED_SOUL_SHARD, "Purified Soul Shard");
            builder.add(NarakaItems.NECTARIUM, "Nectarium");
            builder.add(NarakaItems.GOD_BLOOD, "§lGod Blood");
            builder.add(NarakaItems.COMPRESSED_IRON_INGOT, "Compressed Iron Ingot");
            builder.add(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, "Smithing Template");
            builder.add(NarakaItems.PURIFIED_SOUL_SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, "Smithing Template");
            builder.add(NarakaItems.SPEAR_ITEM, "Spear");
            builder.add(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, "Mighty Holy Spear");
            builder.add(NarakaItems.SPEAR_OF_LONGINUS_ITEM, "Spear of Longinus");

            builder.add(NarakaItems.EBONY_METAL_HELMET, "Ebony Metal Helmet");
            builder.add(NarakaItems.EBONY_METAL_CHESTPLATE, "Ebony Metal Chestplate");
            builder.add(NarakaItems.EBONY_METAL_LEGGINGS, "Ebony Metal Leggings");
            builder.add(NarakaItems.EBONY_METAL_BOOTS, "Ebony Metal Boots");
            builder.add(NarakaItems.PURIFIED_SOUL_HELMET, "Purified Soul Helmet");
            builder.add(NarakaItems.PURIFIED_SOUL_CHESTPLATE, "Purified Soul Chestplate");
            builder.add(NarakaItems.PURIFIED_SOUL_LEGGINGS, "Purified Soul Leggings");
            builder.add(NarakaItems.PURIFIED_SOUL_BOOTS, "Purified Soul Boots");

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
            builder.add(NarakaItems.EBONY_ROOTS_SCRAP, "Ebony Roots Scrap");
            builder.add(NarakaItems.EBONY_METAL_INGOT, "Ebony Metal Ingot");
            builder.add(NarakaItems.SANCTUARY_COMPASS, "Sanctuary Compass");

            builder.add(NarakaBlocks.NECTARIUM_ORE, "Nectarium Ore");
            builder.add(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE, "Deepslate Nectarium Ore");
            builder.add(NarakaBlocks.NECTARIUM_BLOCK, "Block of Nectarium");
            builder.add(NarakaBlocks.TRANSPARENT_BLOCK, "Block of Transparent");
            builder.add(NarakaBlocks.COMPRESSED_IRON_BLOCK, "Block of Compressed Iron");
            builder.add(NarakaBlocks.FAKE_GOLD_BLOCK, "Block of Fake Gold");
            builder.add(NarakaBlocks.AMETHYST_SHARD_BLOCK, "Block of Amethyst Shard");
            builder.add(NarakaBlocks.SOUL_CRAFTING_BLOCK, "Block of Soul Crafting");

            builder.add(NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK, "Block of Soul Infused Redstone");
            builder.add(NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK, "Block of Soul Infused Copper");
            builder.add(NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK, "Block of Soul Infused Gold");
            builder.add(NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK, "Block of Soul Infused Emerald");
            builder.add(NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK, "Block of Soul Infused Diamond");
            builder.add(NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK, "Block of Soul Infused Lapis");
            builder.add(NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK, "Block of Soul Infused Amethyst");
            builder.add(NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK, "Block of Soul Infused Nectarium");
            builder.add(NarakaBlocks.PURIFIED_SOUL_BLOCK, "Block of Purified Soul");

            builder.add(NarakaBlocks.HEROBRINE_TOTEM, "Herobrine Totem");
            builder.add(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK, "Purified Soul Fire");
            builder.add(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK, "Block of Purified Soul Metal");
            builder.add(NarakaBlocks.EBONY_LOG, "Ebony Log");
            builder.add(NarakaBlocks.STRIPPED_EBONY_LOG, "Stripped Ebony Log");
            builder.add(NarakaBlocks.EBONY_WOOD, "Ebony Wood");
            builder.add(NarakaBlocks.STRIPPED_EBONY_WOOD, "Stripped Ebony Wood");
            builder.add(NarakaBlocks.EBONY_LEAVES, "Ebony Leaves");
            builder.add(NarakaBlocks.EBONY_SAPLING, "Ebony Sapling");
            builder.add(NarakaBlocks.POTTED_EBONY_SAPLING, "Potted Ebony Sapling");
            builder.add(NarakaBlocks.HARD_EBONY_PLANKS, "Hard Ebony Planks");
            builder.add(NarakaBlocks.EBONY_ROOTS, "Ebony Roots");
            builder.add(NarakaBlocks.EBONY_METAL_BLOCK, "Block of Ebony Metal");

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
            builder.add(PURIFIED_SOUL_UPGRADE_APPLIES_TO_KEY, "흑단나무 검, 정화된 영혼 무기");
            builder.add(PURIFIED_SOUL_UPGRADE_INGREDIENTS_KEY, "정화된 영혼 금속, 영혼이 주입된 재료");
            builder.add(PURIFIED_SOUL_UPGRADE_BASE_SLOT_DESCRIPTION_KEY, "흑단나무 무기, 정화된 영혼 검 또는 창를 놓으세요");
            builder.add(PURIFIED_SOUL_UPGRADE_ADDITIONS_SLOT_DESCRIPTION_KEY, "정화된 영혼 금속, 영혼이 주입된 재료 또는 신의 피를 놓으세요");

            builder.add(JADE_SOUL_CRAFTING_FUEL_KEY, "연로: %d");
            builder.add(NarakaJadeProviders.SOUL_CRAFTING_BLOCK.translationKey, "영혼 세공기");

            addTrimPattern(builder, NarakaTrimPatterns.PURIFIED_SOUL_SILENCE, "정화된 영혼 고요 갑옷 장식");
            addTrimMaterial(builder, NarakaTrimMaterials.SOUL_INFUSED_REDSTONE, "영혼이 주입된 레드스톤 소재");
            addTrimMaterial(builder, NarakaTrimMaterials.SOUL_INFUSED_COPPER, "영혼이 주입된 구리 소재");
            addTrimMaterial(builder, NarakaTrimMaterials.SOUL_INFUSED_GOLD, "영혼이 주입된 금 소재");
            addTrimMaterial(builder, NarakaTrimMaterials.SOUL_INFUSED_EMERALD, "영혼이 주입된 에메랄드 소재");
            addTrimMaterial(builder, NarakaTrimMaterials.SOUL_INFUSED_DIAMOND, "영혼이 주입된 다이아몬드 소재");
            addTrimMaterial(builder, NarakaTrimMaterials.SOUL_INFUSED_LAPIS, "영혼이 주입된 청금석 소재");
            addTrimMaterial(builder, NarakaTrimMaterials.SOUL_INFUSED_AMETHYST, "영혼이 주입된 자수정 소재");
            addTrimMaterial(builder, NarakaTrimMaterials.SOUL_INFUSED_NECTARIUM, "영혼이 주입된 넥타륨 소재");

            builder.add(NarakaItems.PURIFIED_SOUL_SHARD, "정화된 영혼 조각");
            builder.add(NarakaItems.NECTARIUM, "넥타륨");
            builder.add(NarakaItems.GOD_BLOOD, "§l신의 피");
            builder.add(NarakaItems.COMPRESSED_IRON_INGOT, "압축된 철 주괴");
            builder.add(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, "대장장이 형판");
            builder.add(NarakaItems.PURIFIED_SOUL_SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, "대장장이 형판");

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
            builder.add(NarakaItems.EBONY_ROOTS_SCRAP, "흑단나무 뿌리 파편");
            builder.add(NarakaItems.EBONY_METAL_INGOT, "흑단나무 금속 주괴");
            builder.add(NarakaItems.SANCTUARY_COMPASS, "생츄어리 나침반");

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
            builder.add(NarakaBlocks.HARD_EBONY_PLANKS, "단단한 흑단나무 판자");
            builder.add(NarakaBlocks.EBONY_ROOTS, "흑단나무 뿌리");
            builder.add(NarakaBlocks.EBONY_METAL_BLOCK, "흑단나무 금속 블록");

            builder.add(NarakaEntityTypes.HEROBRINE, "히로빈");
            builder.add(NarakaEntityTypes.THROWN_SPEAR, "창");
            builder.add(NarakaEntityTypes.THROWN_MIGHTY_HOLY_SPEAR, "강력한 성스러운 창");
            builder.add(NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS, "롱기누스의 창");

            builder.add(NarakaItems.EBONY_METAL_HELMET, "흑단나무 금속 투구");
            builder.add(NarakaItems.EBONY_METAL_CHESTPLATE, "흑단나무 금속 흉갑");
            builder.add(NarakaItems.EBONY_METAL_LEGGINGS, "흑단나무 금속 레깅스");
            builder.add(NarakaItems.EBONY_METAL_BOOTS, "흑단나무 금속 부츠");
            builder.add(NarakaItems.PURIFIED_SOUL_HELMET, "정화된 영혼 투구");
            builder.add(NarakaItems.PURIFIED_SOUL_CHESTPLATE, "정화된 영혼 흉갑");
            builder.add(NarakaItems.PURIFIED_SOUL_LEGGINGS, "정화된 영혼 레깅스");
            builder.add(NarakaItems.PURIFIED_SOUL_BOOTS, "정화된 영혼 부츠");

            addDamageType(builder, NarakaDamageTypes.SPEAR_OF_LONGINUS,
                    "%1$s의 AT 필드가 %2$s에 찢어졌습니다.",
                    "%1$s의 AT 필드가 %3$s가 던진 %2$s에 찢어졌습니다.");
        }
    }
}