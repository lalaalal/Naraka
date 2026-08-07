package com.yummy.naraka.client.event;

import com.mojang.serialization.Codec;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.renderer.WhiteFogRenderHelper;
import com.yummy.naraka.config.Configuration;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.data.lang.LanguageKey;
import com.yummy.naraka.event.ItemEvents;
import com.yummy.naraka.util.ComponentStyles;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.item.equipmentset.EquipmentSetGroup;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import com.yummy.naraka.world.item.tooltip.DynamicItemLore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.level.BlockGetter;

import java.util.function.Consumer;

public class NarakaClientEvents {
    public static void initialize() {
        ClientEvents.TICK_PRE.register(NarakaClientEvents::onClientTick);
        ClientEvents.TICK_POST.register(NarakaClientEvents::updateMusicVolume);
        ClientEvents.CLIENT_STOPPING.register(NarakaClientEvents::onClientStopping);
        ClientEvents.CAMERA_SETUP.register(NarakaClientEvents::shakeCamera);
        ClientEvents.LOGIN.register(NarakaClientEvents::onClientLogin);

        ItemEvents.ITEM_TOOLTIP_TOP.register(NarakaClientEvents::addItemTooltipsTop);
        ItemEvents.ITEM_TOOLTIP_MIDDLE.register(NarakaClientEvents::addItemTooltipsMiddle);
        ItemEvents.ITEM_TOOLTIP_BOTTOM.register(NarakaClientEvents::addItemTooltipsBottom);
    }

    private static void shakeCamera(ClientEvents.CameraSetup.Context context, BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick) {
        int cameraShakeTick = NarakaClientContext.CAMERA_SHAKE_TICK.getValue();
        if (cameraShakeTick > 0) {
            float ageInTicks = entity.tickCount + partialTick;
            float dy = Mth.sin(ageInTicks * NarakaConfig.CLIENT.cameraShakingSpeed.getValue()) * cameraShakeTick * NarakaConfig.CLIENT.cameraShakingStrength.getValue();
            context.move(0, dy, 0);
        }
    }

    private static void onClientLogin() {
        NarakaClientContext.initialize();
    }

    private static void onClientTick(Minecraft minecraft) {
        ComponentStyles.RAINBOW_COLOR.updateColor();
        WhiteFogRenderHelper.tick();
        contextTick(NarakaClientContext.CAMERA_SHAKE_TICK);
        contextTick(NarakaClientContext.POST_EFFECT_TICK);
        contextTick(NarakaClientContext.MUTE_MUSIC_TICK);
    }

    private static void updateMusicVolume(Minecraft minecraft) {
        float originalVolume = NarakaClientContext.MUSIC_VOLUME.getValue();
        SoundManager soundManager = minecraft.getSoundManager();
        if (NarakaClientContext.MUTE_MUSIC_TICK.getValue() > 0) {
            float newVolume = Math.max(originalVolume - 0.1f, 0.01f);
            soundManager.updateSourceVolume(SoundSource.MUSIC, newVolume);
            NarakaClientContext.MUSIC_VOLUME.set(newVolume);
        } else if (originalVolume < 1) {
            float newVolume = Math.min(originalVolume + 0.02f, 1);
            soundManager.updateSourceVolume(SoundSource.MUSIC, newVolume);
            NarakaClientContext.MUSIC_VOLUME.set(newVolume);
        }
    }

    private static void contextTick(Configuration.ConfigValue<Integer> context) {
        int tickCount = context.getValue();
        if (tickCount > 0)
            context.set(tickCount - 1);
    }

    private static void onClientStopping(Minecraft minecraft) {
        NarakaConfig.stopWatching();
    }

    private static void addItemTooltipsTop(ItemStack itemStack, Player player, TooltipFlag tooltipFlag, boolean shiftKeyPressed, Consumer<Component> builder) {
        RegistryAccess registryAccess = player.level().registryAccess();
        EquipmentSetGroup equipmentSetGroup = NarakaItemUtils.readNbtDataOrDefault(itemStack, NarakaItemUtils.TAG_EQUIPMENT_SET_GROUP, EquipmentSetGroup.CODEC, registryAccess, EquipmentSetGroup.EMPTY);
        equipmentSetGroup.addToTooltip(itemStack, player, tooltipFlag, shiftKeyPressed, builder);
        Reinforcement reinforcement = Reinforcement.get(itemStack, registryAccess);
        boolean reinforcementHasTooltip = reinforcement.hasTooltip();
        boolean hasDynamicItemLore = !NarakaItemUtils.readNbtDataOrDefault(itemStack, NarakaItemUtils.TAG_DYNAMIC_ITEM_LORE, DynamicItemLore.CODEC, registryAccess, DynamicItemLore.EMPTY)
                .isEmpty();
        if (!equipmentSetGroup.isEmpty() && !reinforcementHasTooltip && !hasDynamicItemLore)
            builder.accept(Component.empty());
        if (reinforcementHasTooltip) {
            reinforcement.addToTooltip(builder);
            if (ArmorTrim.getTrim(registryAccess, itemStack).isPresent() && !hasDynamicItemLore)
                builder.accept(Component.empty());
        }
    }

    private static void addItemTooltipsMiddle(ItemStack itemStack, Player player, TooltipFlag tooltipFlag, boolean shiftKeyPressed, Consumer<Component> builder) {
        RegistryAccess registryAccess = player.level().registryAccess();
        DynamicItemLore dynamicItemLore = NarakaItemUtils.readNbtDataOrDefault(itemStack, NarakaItemUtils.TAG_DYNAMIC_ITEM_LORE, DynamicItemLore.CODEC, registryAccess, DynamicItemLore.EMPTY);
        if (dynamicItemLore.isEmpty())
            return;

        builder.accept(Component.empty());
        dynamicItemLore.addToTooltip(itemStack, player, tooltipFlag, shiftKeyPressed, builder);
        if (ArmorTrim.getTrim(registryAccess, itemStack).isPresent() || !hasAttributeModifiers(itemStack))
            builder.accept(Component.empty());
    }

    private static boolean hasAttributeModifiers(ItemStack itemStack) {
        if (NarakaItemUtils.hasNbtData(itemStack, NarakaItemUtils.TAG_ATTRIBUTE_MODIFIERS))
            return true;
        return hasDefaultAttributeModifiers(itemStack.getItem());
    }

    private static boolean hasDefaultAttributeModifiers(Item item) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!item.getDefaultAttributeModifiers(slot).isEmpty())
                return true;
        }
        return false;
    }

    private static void addItemTooltipsBottom(ItemStack itemStack, Player player, TooltipFlag tooltipFlag, boolean shiftKeyPressed, Consumer<Component> builder) {
        if (NarakaItemUtils.hasNbtData(itemStack, NarakaItemUtils.TAG_BLESSED))
            builder.accept(Component.translatable(LanguageKey.BLESSED_KEY).withStyle(ComponentStyles.RAINBOW_COLOR));
        if (NarakaItemUtils.readNbtDataOrDefault(itemStack, NarakaItemUtils.TAG_HEROBRINE_SCARF, Codec.BOOL, false))
            builder.accept(Component.translatable(LanguageKey.HEROBRINE_SCARF_KEY).withStyle(ComponentStyles.RAINBOW_COLOR));
    }
}
