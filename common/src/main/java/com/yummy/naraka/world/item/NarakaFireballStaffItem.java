package com.yummy.naraka.world.item;

import com.yummy.naraka.world.entity.NarakaFireball;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NarakaFireballStaffItem extends Item {
    public NarakaFireballStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        Vec3 viewVector = player.getViewVector(0);
        NarakaFireball fireball = new NarakaFireball(player, player, Vec3.ZERO, level);
        fireball.setPos(player.position().add(viewVector.x, 0, viewVector.z));
        level.addFreshEntity(fireball);
        fireball.shoot(viewVector.x, viewVector.y, viewVector.z, 1, 0);
        return super.use(level, player, usedHand);
    }
}
