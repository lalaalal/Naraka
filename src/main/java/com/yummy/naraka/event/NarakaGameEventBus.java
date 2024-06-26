package com.yummy.naraka.event;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.attachment.AttachmentSyncHelper;
import com.yummy.naraka.attachment.DeathCountHelper;
import com.yummy.naraka.attachment.StigmaHelper;
import com.yummy.naraka.tags.NarakaEntityTypeTags;
import com.yummy.naraka.util.ComponentStyles;
import com.yummy.naraka.util.NarakaUtil;
import com.yummy.naraka.world.damagesource.NarakaDamageSources;
import com.yummy.naraka.world.entity.DeathCountingEntity;
import com.yummy.naraka.world.item.enchantment.NarakaEnchantments;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = NarakaMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class NarakaGameEventBus {
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        MinecraftServer server = event.getServer();
        RegistryAccess registryAccess = server.registryAccess();
        NarakaDamageSources.initialize(registryAccess);
        NarakaEnchantments.initialize(registryAccess);
        NarakaUtil.initialize(server);
    }

    @SubscribeEvent
    public static void clientTick(ClientTickEvent.Pre event) {
        ComponentStyles.tick();
    }

    @SubscribeEvent
    public static void syncAttachmentsOn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        AttachmentSyncHelper.syncAttachments(player);
    }

    @SubscribeEvent
    public static void handleSulliedEntityOn(EntityTickEvent.Pre event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity livingEntity)
            StigmaHelper.handleSulliedEntity(livingEntity);
    }

    /**
     * If damage is going to kill entity, make health full and reduce death count<br>
     *
     * @param event LivingDamageEvent
     * @see DeathCountHelper#reduceDeathCount(LivingEntity, Entity)
     */
    @SubscribeEvent
    public static void handleDeathCountOn(LivingDamageEvent.Pre event) {
        DamageContainer container = event.getContainer();
        DamageSource source = container.getSource();
        LivingEntity livingEntity = event.getEntity();
        if (!DeathCountHelper.isDeathCounted(livingEntity))
            return;

        if (livingEntity.getHealth() - container.getNewDamage() > 0)
            return;
        if (livingEntity.getType().is(NarakaEntityTypeTags.APPLY_DEATH_COUNT)) {
            DeathCountHelper.reduceDeathCount(livingEntity, source.getEntity());
            postDeathCountReduced(event.getEntity(), container);
        }
    }

    private static void postDeathCountReduced(LivingEntity livingEntity, DamageContainer container) {
        Entity source = container.getSource().getEntity();

        if (DeathCountHelper.getDeathCount(livingEntity) < 1) {
            if (source instanceof DeathCountingEntity deathCountingEntity)
                deathCountingEntity.onDeathCountZero(livingEntity);
            return;
        }
        container.setNewDamage(0);
        livingEntity.setHealth(livingEntity.getMaxHealth());
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (DeathCountHelper.getDeathCount(player) < 1)
            DeathCountHelper.restoreDeathCount(player);
        AttachmentSyncHelper.syncAttachments(player);
    }
}
