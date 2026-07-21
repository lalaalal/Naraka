package com.yummy.naraka.advancements;

import com.yummy.naraka.advancements.criterion.EquipmentSetTrigger;
import com.yummy.naraka.advancements.criterion.FillSoulStabilizerTrigger;
import com.yummy.naraka.advancements.criterion.SimpleTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public class NarakaCriteriaTriggers {
    public static final FillSoulStabilizerTrigger FILL_SOUL_STABILIZER = CriteriaTriggers.register(new FillSoulStabilizerTrigger());
    public static final SimpleTrigger SIMPLE_TRIGGER = CriteriaTriggers.register(new SimpleTrigger());
    public static final EquipmentSetTrigger EQUIPMENT_SET = CriteriaTriggers.register(new EquipmentSetTrigger());

    public static void initialize() {

    }
}
