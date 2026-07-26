package com.yummy.naraka.world.item;

import com.yummy.naraka.world.entity.NarakaFireball;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.AttackRange;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class NarakaFireballStaffItem extends Item {
    public static final AttackRange DEFAULT_ATTACK_RANGE = new AttackRange(3, 32, 0, 32, 2, 1);

    public NarakaFireballStaffItem(Properties properties) {
        super(properties);
    }

    private static boolean isLivingEntity(Entity entity) {
        return entity instanceof LivingEntity;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        AttackRange attackRange = itemStack.getOrDefault(DataComponents.ATTACK_RANGE, DEFAULT_ATTACK_RANGE);
        float interactionReach = attackRange.effectiveMaxRange(player);

        Vec3 viewVector = player.getLookAngle();
        Vec3 from = player.getEyePosition();
        Vec3 to = from.add(viewVector.scale(interactionReach));
        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                level, player, from, to,
                player.getBoundingBox().inflate(interactionReach),
                entity -> !entity.is(player) && isLivingEntity(entity),
                attackRange.hitboxMargin()
        );
        NarakaFireball fireball = new NarakaFireball(player, null, Vec3.ZERO, level);
        fireball.setPos(from);
        if (entityHitResult != null)
            fireball.setTarget(entityHitResult.getEntity());
        level.addFreshEntity(fireball);
        fireball.shoot(viewVector.x, viewVector.y, viewVector.z, 1, 0);

        return InteractionResult.SUCCESS;
    }
}
