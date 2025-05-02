package com.yummy.naraka.world.item;

import com.yummy.naraka.world.entity.NarakaFireball;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class NarakaFireballItem extends Item {
    public NarakaFireballItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        Vec3 viewVector = player.getViewVector(0);
        NarakaFireball fireball = new NarakaFireball(player, player, Vec3.ZERO, level);
        fireball.setPos(player.position().add(viewVector.x, 0, viewVector.z));
        level.addFreshEntity(fireball);
        fireball.shoot(viewVector.x, viewVector.y, viewVector.z, 4, 0);
        return super.use(level, player, usedHand);
    }
}
