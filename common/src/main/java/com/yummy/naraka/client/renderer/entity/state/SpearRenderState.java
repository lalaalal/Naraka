package com.yummy.naraka.client.renderer.entity.state;

import com.mojang.math.Axis;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import com.yummy.naraka.world.entity.Spear;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.EntityType;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class SpearRenderState extends EntityRenderState {
    public Quaternionf yRotation = Axis.YP.rotation(0);
    public Quaternionf xRotation = Axis.XP.rotation(0);
    public boolean hasFoil = false;
    public EntityType<?> type = NarakaEntityTypes.THROWN_SPEAR.get();
    public boolean isLonginus = false;

    public void setRotation(Spear spear, float partialTicks) {
        yRotation = Axis.YP.rotationDegrees(spear.getYRot(partialTicks) - 90.0F);
        xRotation = Axis.ZP.rotationDegrees(spear.getXRot(partialTicks) + 90.0F);
    }

    public void setType(Spear spear) {
        this.type = spear.getType();
        this.isLonginus = (type == NarakaEntityTypes.THROWN_SPEAR_OF_LONGINUS.get());
    }
}
