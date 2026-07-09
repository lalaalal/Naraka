package com.yummy.naraka.world.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public class NarakaTiers implements Tier {
    public static final NarakaTiers LONGINUS = new NarakaTiers(4, Integer.MAX_VALUE, 12, 65, 22, () -> Ingredient.EMPTY);
    public static final NarakaTiers PURIFIED_SOUL = new NarakaTiers(2, 250, 6.0F, 2.0F, 14, () -> Ingredient.EMPTY);

    private final int tierLevel;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final Supplier<Ingredient> repairIngredient;

    private NarakaTiers(int tierLevel, int uses, float speed, float damage, int enchantmentValue, Supplier<Ingredient> repairIngredient) {
        this.tierLevel = tierLevel;
        this.uses = uses;
        this.speed = speed;
        this.damage = damage;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = repairIngredient;
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    @Override
    public int getLevel() {
        return tierLevel;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
