package com.yummy.naraka.datagen;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.block.NarakaBlocks;
import com.yummy.naraka.entity.NarakaEntities;
import com.yummy.naraka.item.NarakaItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public abstract class NarakaLanguageProvider extends LanguageProvider {

    public NarakaLanguageProvider(PackOutput output, String locale) {
        super(output, NarakaMod.MOD_ID, locale);
    }

    public static class EN extends NarakaLanguageProvider {

        public EN(PackOutput output) {
            super(output, "en_us");
        }

        @Override
        protected void addTranslations() {
            addItem(NarakaItems.TEST_ITEM, "Test Item");
            addItem(NarakaItems.SPEAR_ITEM, "Spear");
            addItem(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, "Mighty Holy Spear");

            addBlock(NarakaBlocks.TRANSPARENT_BLOCK, "Transparent Block");

            addEntityType(NarakaEntities.HEROBRINE, "Naraka: Herobrine");
            addEntityType(NarakaEntities.THROWN_SPEAR, "Spear");
            addEntityType(NarakaEntities.THROWN_MIGHTY_HOLY_SPEAR, "Mighty Holy Spear");
        }
    }

    public static class KR extends NarakaLanguageProvider {
        public KR(PackOutput output) {
            super(output, "ko_kr");
        }

        @Override
        protected void addTranslations() {
            addItem(NarakaItems.TEST_ITEM, "테스트 아이템");
            addItem(NarakaItems.SPEAR_ITEM, "창");
            addItem(NarakaItems.MIGHTY_HOLY_SPEAR_ITEM, "강력한 성스러운 창");

            addBlock(NarakaBlocks.TRANSPARENT_BLOCK, "투명 블록");

            addEntityType(NarakaEntities.HEROBRINE, "히로빈");
            addEntityType(NarakaEntities.THROWN_SPEAR, "창");
            addEntityType(NarakaEntities.THROWN_MIGHTY_HOLY_SPEAR, "강력한 성스러운 창");
        }

    }
}