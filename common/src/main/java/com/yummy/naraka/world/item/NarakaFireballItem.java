package com.yummy.naraka.world.item;

import com.yummy.naraka.world.entity.NarakaFireball;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NarakaFireballItem extends Item {
    public NarakaFireballItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        NarakaFireball fireball = new NarakaFireball(player, player, Vec3.ZERO, level);
        level.addFreshEntity(fireball);
        fireball.shoot(1, 0, 0, 5, 0);
        return super.use(level, player, usedHand);
    }
}
