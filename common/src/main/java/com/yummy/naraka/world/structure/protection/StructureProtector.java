package com.yummy.naraka.world.structure.protection;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.util.NarakaNbtUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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
        Container.instance().ifPresent(instance -> {
            instance.protectors.add(protector);
            instance.setDirty();
        });
    }

    public static boolean checkProtected(Vec3i pos) {
        List<StructureProtector> protectors = Container.instance()
                .map(instance -> instance.protectors)
                .orElse(List.of());
        for (StructureProtector protector : protectors) {
            if (protector.isProtected(pos))
                return true;
        }
        return false;
    }

    public static void initialize(ServerLevel level) {
        DimensionDataStorage storage = level.getDataStorage();
        Container.instance = storage.computeIfAbsent(Container.factory(level.registryAccess()), () -> new Container(level.registryAccess()), "structure_protectors");
    }

    private static class Container extends SavedData {
        @Nullable
        private static Container instance;

        public static Optional<Container> instance() {
            return Optional.ofNullable(instance);
        }

        private static Container create(CompoundTag tag, HolderLookup.Provider registries) {
            return NarakaNbtUtils.read(tag, "structure_protectors", StructureProtector.CODEC.listOf(), RegistryOps.create(NbtOps.INSTANCE, registries))
                    .map(protectors -> new Container(registries, protectors))
                    .orElse(new Container(registries));
        }

        public static Function<CompoundTag, Container> factory(HolderLookup.Provider registries) {
            return tag -> create(tag, registries);
        }

        private final HolderLookup.Provider registries;
        private final List<StructureProtector> protectors;

        private Container(HolderLookup.Provider registries) {
            this.registries = registries;
            protectors = new ArrayList<>();
        }

        private Container(HolderLookup.Provider registries, List<StructureProtector> protectors) {
            this.registries = registries;
            this.protectors = new ArrayList<>(protectors);
        }

        @Override
        public CompoundTag save(CompoundTag tag) {
            NarakaNbtUtils.store(tag, "structure_protectors", StructureProtector.CODEC.listOf(), RegistryOps.create(NbtOps.INSTANCE, registries), protectors);
            return tag;
        }
    }
}
