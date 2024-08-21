package com.yummy.naraka.world.entity;

import com.yummy.naraka.NarakaMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class NarakaEntityTypes {
    public static final EntityType<Herobrine> HEROBRINE = register(
            "herobrine",
            FabricEntityType.Builder.<Herobrine>createMob(
                            Herobrine::new,
                            MobCategory.MONSTER,
                            attribute(Herobrine::getAttributeSupplier)
                    )
                    .fireImmune()
                    .sized(0.6f, 2.0f)
    );

    public static final EntityType<Spear> THROWN_SPEAR = register(
            "spear",
            EntityType.Builder.<Spear>of(Spear::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
    );

    public static final EntityType<Spear> THROWN_MIGHTY_HOLY_SPEAR = register(
            "mighty_holy_spear",
            EntityType.Builder.<Spear>of(Spear::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
    );

    public static final EntityType<SpearOfLonginus> THROWN_SPEAR_OF_LONGINUS = register(
            "spear_of_longinus",
            EntityType.Builder.<SpearOfLonginus>of(SpearOfLonginus::new, MobCategory.MISC)
                    .sized(0.5f, 1.0f)
                    .fireImmune()
                    .immuneTo(Blocks.CACTUS)
    );

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        EntityType<T> type = builder.build(name);
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, NarakaMod.location(name), type);
    }

    private static <T extends Mob> UnaryOperator<FabricEntityType.Builder.Mob<T>> attribute(Supplier<AttributeSupplier.Builder> supplier) {
        return mob -> mob.defaultAttributes(supplier);
    }

    public static void initialize() {

    }
}
