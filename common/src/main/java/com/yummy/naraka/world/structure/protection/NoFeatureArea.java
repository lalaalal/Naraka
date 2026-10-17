package com.yummy.naraka.world.structure.protection;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.util.NarakaUtils;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.SavedDataStorage;

import java.util.List;
import java.util.function.BiPredicate;

public class NoFeatureArea extends SavedData {
    public static final Codec<NoFeatureArea> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Area.CODEC.listOf().fieldOf("area").forGetter(container -> container.areas)
            ).apply(instance, NoFeatureArea::new)
    );
    public static final SavedDataType<NoFeatureArea> TYPE = new SavedDataType<>(
            NarakaMod.identifier("no_feature_area"), NoFeatureArea::new, CODEC, DataFixTypes.LEVEL
    );

    private final List<Area> areas;

    public static boolean isProtectedArea(WorldGenLevel level, Vec3i position) {
        ServerLevel serverLevel = level.getLevel();
        SavedDataStorage storage = serverLevel.getDataStorage();
        for (Area area : storage.computeIfAbsent(TYPE).areas) {
            if (area.test(position))
                return true;
        }
        return false;
    }

    private NoFeatureArea(List<Area> areas) {
        this.areas = areas;
    }

    private NoFeatureArea() {
        this.areas = List.of();
    }

    public NoFeatureArea append(Type type, BoundingBox boundingBox) {
        return append(new Area(type, boundingBox));
    }

    private NoFeatureArea append(Area area) {
        return new NoFeatureArea(ImmutableList.<Area>builder()
                .addAll(areas)
                .add(area)
                .build()
        );
    }

    public enum Type implements BiPredicate<BoundingBox, Vec3i>, StringRepresentable {
        BOX(BoundingBox::isInside),
        CYLINDER((boundingBox, vec3i) -> NarakaUtils.isInCylinder(boundingBox, 0.8f, vec3i));

        public static final Codec<Type> CODEC = StringRepresentable.fromValues(Type::values);

        private final BiPredicate<BoundingBox, Vec3i> predicate;

        Type(BiPredicate<BoundingBox, Vec3i> predicate) {
            this.predicate = predicate;
        }

        @Override
        public boolean test(BoundingBox boundingBox, Vec3i position) {
            return predicate.test(boundingBox, position);
        }

        @Override
        public String getSerializedName() {
            return name();
        }
    }

    private record Area(Type type, BoundingBox box) {
        public static final Codec<Area> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        Type.CODEC.fieldOf("type").forGetter(Area::type),
                        BoundingBox.CODEC.fieldOf("box").forGetter(Area::box)
                ).apply(instance, Area::new)
        );

        public boolean test(Vec3i pos) {
            return box.isInside(pos) && type.test(box, pos);
        }
    }
}
