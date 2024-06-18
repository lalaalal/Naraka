package com.yummy.naraka.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.block.NarakaBlocks;
import com.yummy.naraka.damagesource.NarakaDamageTypes;
import com.yummy.naraka.entity.NarakaEntities;
import com.yummy.naraka.item.NarakaItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.LanguageProvider;

public abstract class NarakaLanguageProvider extends LanguageProvider {

    public NarakaLanguageProvider(PackOutput output, String locale) {
        super(output, NarakaMod.MOD_ID, locale);
    }

    public void addDamageType(ResourceKey<DamageType> damageType, String directMessage, String indirectMessage) {
        String directKey = "death.attack." + damageType.location().getPath();
        String indirectKey = directKey + ".player";

        add(directKey, directMessage);
        add(indirectKey, indirectMessage);
    }

    public static class EN extends NarakaLanguageProvider {

        public EN(PackOutput output) {
            super(output, "en_us");
        }

        @Override
        protected void addTranslations() {
            add("itemGroup.naraka", "Naraka");
            addItem(NarakaItems.PURIFIED_SOUL_METAL, "Purified Soul Metal");
            addItem(NarakaItems.PURIFIED_SOUL_SHARD, "Purified Soul Shard");
            addItem(NarakaItems.NECTARIUM, "Nectarium");
            addItem(NarakaItems.GOD_BLOOD, "God Blood");
            addItem(NarakaItems.PURIFIED_SOUL_UPGRADE_SMITHING_TEMPLATE, "Smithing Template");
            addItem(NarakaItems.TEST_ITEM, "Test Item");
            addItem(NarakaItems.SPEAR_ITEM, "Spear");
            addItem(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, "Mighty Holy Spear");
            addItem(NarakaItems.SPEAR_OF_LONGINUS_ITEM, "Spear of Longinus");

            addBlock(NarakaBlocks.NECTARIUM_ORE, "Nectarium Ore");
            addBlock(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE, "Deepslate Nectarium Ore");
            addBlock(NarakaBlocks.NECTARIUM_BLOCK, "Nectarium Block");
            addBlock(NarakaBlocks.TRANSPARENT_BLOCK, "Transparent Block");

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
            addItem(NarakaItems.TEST_ITEM, "테스트 아이템");
            addItem(NarakaItems.NECTARIUM, "넥타리움");
            addItem(NarakaItems.SPEAR_ITEM, "창");
            addItem(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, "강력한 성스러운 창");
            addItem(NarakaItems.SPEAR_OF_LONGINUS_ITEM, "롱기누스의 창");

            addBlock(NarakaBlocks.NECTARIUM_ORE, "넥타리움 광석");
            addBlock(NarakaBlocks.DEEPSLATE_NECTARIUM_ORE, "심층암 넥타리움 광석");
            addBlock(NarakaBlocks.NECTARIUM_BLOCK, "넥타리움 블록");
            addBlock(NarakaBlocks.TRANSPARENT_BLOCK, "투명 블록");

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