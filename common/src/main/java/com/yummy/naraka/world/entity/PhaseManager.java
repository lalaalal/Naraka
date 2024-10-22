package com.yummy.naraka.world.entity;

import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.function.Consumer;

public class PhaseManager {
    protected final LivingEntity entity;
    private final float[] phaseHealthArray;
    private float prevHealth;
    private int prevPhase;
    private final BossEvent.BossBarColor[] bossBarColors;
    private final ArrayList<PhaseChangeListener> phaseChangeListeners = new ArrayList<>();
    private final ArrayList<HealthChangeListener> healthChangeListeners = new ArrayList<>();
    private Runnable onAbsorptionDisappear = () -> {
    };
    private float maxAbsorption = 0;
    private float prevAbsorption = 0;

    public PhaseManager(float[] phaseHealthArray, BossEvent.BossBarColor[] bossBarColors, LivingEntity entity) {
        this.entity = entity;
        this.phaseHealthArray = phaseHealthArray;
        this.bossBarColors = bossBarColors;
        this.prevHealth = entity.getHealth();
        this.prevPhase = getCurrentPhase();
    }

    public void setAbsorptionDisappearAction(Runnable runnable) {
        this.onAbsorptionDisappear = runnable;
    }

    public void setMaxAbsorption(float maxAbsorption) {
        this.maxAbsorption = maxAbsorption;
    }

    public void updatePhaseValueOnly(BossEvent bossEvent) {
        prevPhase = getCurrentPhase();
        bossEvent.setColor(getPhaseColor(prevPhase));
    }

    public int getMaxPhase() {
        return phaseHealthArray.length;
    }

    public int getCurrentPhase() {
        float leftHealth = entity.getMaxHealth();
        for (int phase = 0; phase < phaseHealthArray.length; phase++) {
            float phaseMinHealth = (leftHealth -= phaseHealthArray[phase]);
            if (entity.getHealth() > phaseMinHealth)
                return phase + 1;
        }
        return phaseHealthArray.length;
    }

    public float getPhaseMaxHealth(int phase) {
        return phaseHealthArray[phase - 1];
    }

    public float getCurrentPhaseHealth() {
        int phase = getCurrentPhase();
        float health = entity.getHealth();
        for (int i = 0; i < phaseHealthArray.length - phase; i++) {
            int index = phaseHealthArray.length - i - 1;
            health -= phaseHealthArray[index];
        }

        return health;
    }

    public float getActualPhaseMaxHealth(int phase) {
        float health = 0;
        for (int i = 0; i < phaseHealthArray.length - phase + 1; i++) {
            int index = phaseHealthArray.length - i - 1;
            health += phaseHealthArray[index];
        }

        return health;
    }

    public float getActualCurrentPhaseMaxHealth() {
        return getActualPhaseMaxHealth(getCurrentPhase());
    }

    private BossEvent.BossBarColor getPhaseColor(int phase) {
        return bossBarColors[phase - 1];
    }

    public boolean isPhaseChanged() {
        return prevPhase != getCurrentPhase();
    }

    public void updatePhase(BossEvent bossEvent) {
        int phase = getCurrentPhase();
        updateProgressBar(bossEvent, phase);

        if (prevPhase != phase) {
            for (PhaseChangeListener listener : phaseChangeListeners)
                listener.onPhaseChange(prevPhase, phase);
            prevPhase = phase;
        }

        float currentHealth = entity.getHealth();
        if (prevHealth != currentHealth) {
            for (HealthChangeListener listener : healthChangeListeners)
                listener.onHealthChange(prevHealth, currentHealth);
            prevHealth = currentHealth;
        }
    }

    private void updateProgressBar(BossEvent bossEvent, int phase) {
        if (prevPhase != phase) {
            BossEvent.BossBarColor color = getPhaseColor(phase);
            bossEvent.setColor(color);
        }

        if (entity.getAbsorptionAmount() > 0) {
            if (maxAbsorption == 0)
                maxAbsorption = entity.getAbsorptionAmount();
            bossEvent.setColor(BossEvent.BossBarColor.WHITE);
            bossEvent.setProgress(entity.getAbsorptionAmount() / maxAbsorption);
            prevAbsorption = entity.getAbsorptionAmount();
        } else {
            if (prevAbsorption > 0) {
                BossEvent.BossBarColor color = getPhaseColor(phase);
                bossEvent.setColor(color);
                onAbsorptionDisappear.run();
                maxAbsorption = 0;
                prevAbsorption = 0;
            }
            bossEvent.setProgress(calculateProgress(phase));
        }
    }

    public void updateBossBarOnly(BossEvent bossEvent) {
        int phase = getCurrentPhase();
        updateProgressBar(bossEvent, phase);
        this.prevPhase = phase;
    }

    private float calculateProgress(int phase) {
        float maxPhaseHealth = phaseHealthArray[phase - 1];
        float currentPhaseHealth = getCurrentPhaseHealth();

        return currentPhaseHealth / maxPhaseHealth;
    }

    public void addPhaseChangeListener(PhaseChangeListener listener) {
        phaseChangeListeners.add(listener);
    }

    public void addPhaseChangeListener(Consumer<Integer> consumer, int phase) {
        phaseChangeListeners.add((from, to) -> {
            if (from < to && phase == to)
                consumer.accept(from);
        });
    }

    public void addHealthChangeListener(HealthChangeListener listener) {
        healthChangeListeners.add(listener);
    }

    public void addHealthUnderListener(Runnable runnable, final float pivotHealth) {
        healthChangeListeners.add((prevHealth, currentHealth) -> {
            if (currentHealth <= pivotHealth && pivotHealth < prevHealth)
                runnable.run();
        });
    }

    @FunctionalInterface
    public interface PhaseChangeListener {
        void onPhaseChange(int from, int to);
    }

    @FunctionalInterface
    public interface HealthChangeListener {
        void onHealthChange(float prevHealth, float currentHealth);
    }
}