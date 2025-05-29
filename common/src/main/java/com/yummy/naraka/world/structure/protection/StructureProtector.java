package com.yummy.naraka.world.structure.protection;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.core.registries.NarakaRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.ArrayList;
import java.util.List;

public class StructureProtector {
    public static final Codec<StructureProtector> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    RegistryFixedCodec.create(NarakaRegistries.Keys.PROTECTION_PREDICATE)
                            .fieldOf("protection_predicate")
                            .forGetter(protector -> protector.predicate),
                    BoundingBox.CODEC
                            .fieldOf("box")
                            .forGetter(protector -> protector.box)
            ).apply(instance, StructureProtector::new)
    );

    private final Holder<ProtectionPredicate> predicate;
    private final BoundingBox box;

    public StructureProtector(Holder<ProtectionPredicate> predicate, BoundingBox box) {
        this.predicate = predicate;
        this.box = box;
    }

    public boolean isProtected(Vec3i pos) {
        return predicate.value().shouldProtect(box, pos);
    }

    public static void addProtector(Holder<ProtectionPredicate> predicate, BoundingBox box) {
        addProtector(new StructureProtector(predicate, box));
    }

    public static void addProtector(StructureProtector protector) {
        Container.instance.protectors.add(protector);
        Container.instance.setDirty();
    }

    public static boolean checkProtected(Vec3i pos) {
        for (StructureProtector protector : Container.instance.protectors) {
            if (protector.isProtected(pos))
                return true;
        }
        return false;
    }

    public static void initialize(ServerLevel level) {
        DimensionDataStorage storage = level.getDataStorage();
        Container.instance = storage.computeIfAbsent(Container.factory);
    }

    private static class Container extends SavedData {
        public static final Codec<Container> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        StructureProtector.CODEC.listOf().fieldOf("protectors").forGetter(container -> container.protectors)
                ).apply(instance, Container::new)
        );
        private static final SavedDataType<Container> factory = new SavedDataType<>(
                "structure_protectors", Container::new, CODEC, DataFixTypes.LEVEL
        );
        private static Container instance = new Container();

        private final List<StructureProtector> protectors;

        private Container() {
            protectors = new ArrayList<>();
        }

        private Container(List<StructureProtector> protectors) {
            this.protectors = new ArrayList<>(protectors);
        }
    }
}
