package com.yummy.naraka.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.damagesource.NarakaDamageTypes;
import com.yummy.naraka.world.entity.NarakaEntities;
import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredBlock;

public abstract class NarakaLanguageProvider extends LanguageProvider {
    public static String createId(String parent, String name) {
        return "%s.%s.%s".formatted(parent, NarakaMod.MOD_ID, name);
    }

    public static String createId(String parent, ResourceLocation location) {
        return createId(parent, location.getPath());
    }

    public NarakaLanguageProvider(PackOutput output, String locale) {
        super(output, NarakaMod.MOD_ID, locale);
    }

    public void addDamageType(ResourceKey<DamageType> damageType, String directMessage, String indirectMessage) {
        String directKey = "death.attack." + damageType.location().getPath();
        String indirectKey = directKey + ".player";

        add(directKey, directMessage);
        add(indirectKey, indirectMessage);
    }

    public void addBlockWithActualId(DeferredBlock<? extends Block> block, String name) {
        String id = createId("block", block.getId());
        add(id, name);
    }

    public static class EN extends NarakaLanguageProvider {

        public EN(PackOutput output) {
            super(output, "en_us");
        }

        @Override
        protected void addTranslations() {
            add("itemGroup.naraka", "Naraka");
            add("container.soul_crafting", "Soul Crafter");

            addItem(NarakaItems.PURIFIED_SOUL_SHARD, "Purified Soul Shard");
            addItem(NarakaItems.NECTARIUM, "Nectarium");
            addItem(NarakaItems.GOD_BLOOD, "God Blood");
            addItem(NarakaItems.COMPRESSED_IRON_INGOT, "Compressed Iron Ingot");
            addItem(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, "Smithing Template");
            addItem(NarakaItems.PURIFIED_GEMS_UPGRADE_SMITHING_TEMPLATE, "Smithing Template");
            addItem(NarakaItems.TEST_ITEM, "Test Item");
            addItem(NarakaItems.SPEAR_ITEM, "Spear");
            addItem(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, "Mighty Holy Spear");
            addItem(NarakaItems.SPEAR_OF_LONGINUS_ITEM, "Spear of Longinus");

            addItem(NarakaItems.SOUL_INFUSED_REDSTONE, "Soul Infused Redstone");
            addItem(NarakaItems.SOUL_INFUSED_COPPER, "Soul Infused Copper");
            addItem(NarakaItems.SOUL_INFUSED_GOLD, "Soul Infused Gold");
            addItem(NarakaItems.SOUL_INFUSED_EMERALD, "Soul Infused Emerald");
            addItem(NarakaItems.SOUL_INFUSED_DIAMOND, "Soul Infused Diamond");
            addItem(NarakaItems.SOUL_INFUSED_LAPIS, "Soul Infused Lapis");
            addItem(NarakaItems.SOUL_INFUSED_AMETHYST, "Soul Infused Amethyst");
            addItem(NarakaItems.SOUL_INFUSED_NECTARIUM, "Soul Infused Nectarium");
            addItem(NarakaItems.PURIFIED_SOUL_METAL, "Purified Soul Metal");

            addBlock(NarakaBlocks.NECTARIUM_ORE, "Nectarium Ore");
            addBlock(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE, "Deepslate Nectarium Ore");
            addBlock(NarakaBlocks.NECTARIUM_BLOCK, "Nectarium Block");
            addBlock(NarakaBlocks.TRANSPARENT_BLOCK, "Transparent Block");
            addBlock(NarakaBlocks.COMPRESSED_IRON_BLOCK, "Compressed Iron Block");

            addBlock(NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK, "Soul Infused Redstone Block");
            addBlock(NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK, "Soul Infused Copper Block");
            addBlock(NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK, "Soul Infused Gold Block");
            addBlock(NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK, "Soul Infused Emerald Block");
            addBlock(NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK, "Soul Infused Diamond Block");
            addBlock(NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK, "Soul Infused Lapis Block");
            addBlock(NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK, "Soul Infused Amethyst Block");
            addBlock(NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK, "Soul Infused Nectarium Block");
            addBlock(NarakaBlocks.PURIFIED_SOUL_BLOCK, "Purified Soul Block");

            addBlock(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK, "Purified Soul Fire");

            addBlock(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK, "Purified Soul Metal Block");
            addBlock(NarakaBlocks.EBONY_LOG, "Ebony Log");
            addBlock(NarakaBlocks.STRIPPED_EBONY_LOG, "Stripped Ebony Log");
            addBlock(NarakaBlocks.EBONY_WOOD, "Ebony Wood");
            addBlock(NarakaBlocks.STRIPPED_EBONY_WOOD, "Stripped Ebony Wood");
            addBlock(NarakaBlocks.EBONY_LEAVES, "Ebony Leaves");
            addBlock(NarakaBlocks.EBONY_SAPLING, "Ebony Sapling");
            addBlock(NarakaBlocks.EBONY_PLANKS, "Ebony Planks");
            addBlock(NarakaBlocks.EBONY_SLAB, "Ebony Slab");
            addBlock(NarakaBlocks.EBONY_STAIRS, "Ebony Stairs");
            addBlock(NarakaBlocks.EBONY_SIGN, "Ebony Sign");
            addBlockWithActualId(NarakaBlocks.EBONY_WALL_SIGN, "Ebony Wall Sign");
            addBlock(NarakaBlocks.EBONY_HANGING_SIGN, "Ebony Hanging Sign");
            addBlockWithActualId(NarakaBlocks.EBONY_WALL_HANGING_SIGN, "Ebony Wall Hanging Sign");

            addEntityType(NarakaEntities.HEROBRINE, "Naraka: Herobrine");
            addEntityType(NarakaEntities.THROWN_SPEAR, "Spear");
            addEntityType(NarakaEntities.THROWN_MIGHTY_HOLY_SPEAR, "Mighty Holy Spear");
            addEntityType(NarakaEntities.THROWN_SPEAR_OF_LONGINUS, "Spear of Longinus");

            addDamageType(NarakaDamageTypes.SPEAR_OF_LONGINUS,
                    "%1$s's AT Field was torn by %2$s",
                    "%1$s's AT Field was torn by %2$s thrown %3$s");
        }
    }

    public static class KR extends NarakaLanguageProvider {
        public KR(PackOutput output) {
            super(output, "ko_kr");
        }

        @Override
        protected void addTranslations() {
            add("itemGroup.naraka", "Naraka");
            add("container.soul_crafting", "영혼 세공기");

            addItem(NarakaItems.PURIFIED_SOUL_SHARD, "정화된 영혼 조각");
            addItem(NarakaItems.NECTARIUM, "넥타리움");
            addItem(NarakaItems.GOD_BLOOD, "신의 피");
            addItem(NarakaItems.COMPRESSED_IRON_INGOT, "압축된 철 주괴");
            addItem(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, "대장장이 형판");
            addItem(NarakaItems.PURIFIED_GEMS_UPGRADE_SMITHING_TEMPLATE, "대장장이 형판");
            addItem(NarakaItems.TEST_ITEM, "테스트 아이템");
            addItem(NarakaItems.SPEAR_ITEM, "창");
            addItem(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, "강력한 성스러운 창");
            addItem(NarakaItems.SPEAR_OF_LONGINUS_ITEM, "롱기누스의 창");

            addItem(NarakaItems.SOUL_INFUSED_REDSTONE, "영혼이 주입된 레드스톤");
            addItem(NarakaItems.SOUL_INFUSED_COPPER, "영혼이 주입된 구리");
            addItem(NarakaItems.SOUL_INFUSED_GOLD, "영혼이 주입된 금");
            addItem(NarakaItems.SOUL_INFUSED_EMERALD, "영혼이 주입된 에메랄드");
            addItem(NarakaItems.SOUL_INFUSED_DIAMOND, "영혼이 주입된 다이아몬드");
            addItem(NarakaItems.SOUL_INFUSED_LAPIS, "영혼이 주입된 청금석");
            addItem(NarakaItems.SOUL_INFUSED_AMETHYST, "영혼이 주입된 자수정");
            addItem(NarakaItems.SOUL_INFUSED_NECTARIUM, "영혼이 주입된 넥타륨");
            addItem(NarakaItems.PURIFIED_SOUL_METAL, "정화된 영혼 금속");

            addBlock(NarakaBlocks.NECTARIUM_ORE, "넥타륨 광석");
            addBlock(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE, "심층암 넥타륨 광석");
            addBlock(NarakaBlocks.NECTARIUM_BLOCK, "넥타륨 블록");
            addBlock(NarakaBlocks.TRANSPARENT_BLOCK, "투명 블록");
            addBlock(NarakaBlocks.COMPRESSED_IRON_BLOCK, "압축된 철 블록");

            addBlock(NarakaBlocks.SOUL_INFUSED_REDSTONE_BLOCK, "영혼이 주입된 레드스톤 블록");
            addBlock(NarakaBlocks.SOUL_INFUSED_COPPER_BLOCK, "영혼이 주입된 구리 블록");
            addBlock(NarakaBlocks.SOUL_INFUSED_GOLD_BLOCK, "영혼이 주입된 금 블록");
            addBlock(NarakaBlocks.SOUL_INFUSED_EMERALD_BLOCK, "영혼이 주입된 에메랄드 블록");
            addBlock(NarakaBlocks.SOUL_INFUSED_DIAMOND_BLOCK, "영혼이 주입된 다이아몬드 블록");
            addBlock(NarakaBlocks.SOUL_INFUSED_LAPIS_BLOCK, "영혼이 주입된 청금석 블록");
            addBlock(NarakaBlocks.SOUL_INFUSED_AMETHYST_BLOCK, "영혼이 주입된 자수정 블록");
            addBlock(NarakaBlocks.SOUL_INFUSED_NECTARIUM_BLOCK, "영혼이 주입된 넥타륨 블록");
            addBlock(NarakaBlocks.PURIFIED_SOUL_BLOCK, "정화된 영혼 블록");

            addBlock(NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK, "정화된 영혼 불");

            addBlock(NarakaBlocks.PURIFIED_SOUL_METAL_BLOCK, "정화된 영혼 금속 블록");
            addBlock(NarakaBlocks.EBONY_LOG, "흑단나무 원목");
            addBlock(NarakaBlocks.STRIPPED_EBONY_LOG, "껍질 벗긴 흑단나무 원목");
            addBlock(NarakaBlocks.EBONY_WOOD, "흑단나무");
            addBlock(NarakaBlocks.EBONY_LEAVES, "흑단나무 나뭇잎");
            addBlock(NarakaBlocks.STRIPPED_EBONY_WOOD, "껍질 벗긴 흑단나무");
            addBlock(NarakaBlocks.EBONY_SAPLING, "흑단나무 묘목");
            addBlock(NarakaBlocks.EBONY_PLANKS, "흑단나무 판자");
            addBlock(NarakaBlocks.EBONY_SLAB, "흑단나무 반 블록");
            addBlock(NarakaBlocks.EBONY_STAIRS, "흑단나무 계단");
            addBlock(NarakaBlocks.EBONY_SIGN, "흑단나무 표지판");
            addBlockWithActualId(NarakaBlocks.EBONY_WALL_SIGN, "흑단나무 벽 표지판");
            addBlock(NarakaBlocks.EBONY_HANGING_SIGN, "흑단나무 매다는 표지판");
            addBlockWithActualId(NarakaBlocks.EBONY_WALL_HANGING_SIGN, "흑단나무 벽 매다는 표지판");

            addEntityType(NarakaEntities.HEROBRINE, "히로빈");
            addEntityType(NarakaEntities.THROWN_SPEAR, "창");
            addEntityType(NarakaEntities.THROWN_MIGHTY_HOLY_SPEAR, "강력한 성스러운 창");
            addEntityType(NarakaEntities.THROWN_SPEAR_OF_LONGINUS, "롱기누스의 창");

            addDamageType(NarakaDamageTypes.SPEAR_OF_LONGINUS,
                    "%1$s의 AT 필드가 %2$s에 찢어졌습니다.",
                    "%1$s의 AT 필드가 %3$s가 던진 %2$s에 찢어졌습니다.");
        }
    }
}