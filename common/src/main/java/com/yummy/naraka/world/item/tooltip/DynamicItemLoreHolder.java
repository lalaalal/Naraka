package com.yummy.naraka.world.item.tooltip;

public class DynamicItemLoreHolder {
    private final DynamicItemLore dynamicItemLore;

    public static DynamicItemLoreHolder of(ConditionalComponents... conditionalComponents) {
        return new DynamicItemLoreHolder(DynamicItemLore.of(conditionalComponents));
    }

    public static Single single(ConditionalComponents conditionalComponents) {
        return new Single(conditionalComponents);
    }

    public DynamicItemLoreHolder(DynamicItemLore dynamicItemLore) {
        this.dynamicItemLore = dynamicItemLore;
    }

    public DynamicItemLore tooltip() {
        return dynamicItemLore;
    }

    public static class Single extends DynamicItemLoreHolder {
        private final ConditionalComponents conditionalComponents;

        public Single(ConditionalComponents conditionalComponents) {
            super(DynamicItemLore.of(conditionalComponents));
            this.conditionalComponents = conditionalComponents;
        }

        public ConditionalComponents getConditionalComponents() {
            return conditionalComponents;
        }
    }
}
