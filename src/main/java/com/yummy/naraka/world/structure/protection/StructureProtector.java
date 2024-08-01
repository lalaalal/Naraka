package com.yummy.naraka.world.structure.protection;

import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.util.NarakaNbtUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class StructureProtector {
    private final Holder<ProtectionPredicate> predicate;
    private final BoundingBox box;

    public StructureProtector(Holder<ProtectionPredicate> predicate, BoundingBox box) {
        this.predicate = predicate;
        this.box = box;
    }

    public StructureProtector(CompoundTag tag, HolderLookup.Provider provider) {
        String keyName = tag.getString("predicate");
        ResourceKey<ProtectionPredicate> key = ResourceKey.create(NarakaRegistries.PROTECTION_PREDICATE.key(), ResourceLocation.parse(keyName));
        HolderLookup.RegistryLookup<ProtectionPredicate> predicates = provider.lookupOrThrow(NarakaRegistries.PROTECTION_PREDICATE.key());
        this.predicate = predicates.getOrThrow(key);
        Optional<BoundingBox> box = NarakaNbtUtils.readBoundingBox(tag, "box");
        if (box.isEmpty())
            throw new IllegalStateException();
        this.box = box.get();
    }

    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        Optional<ResourceKey<ProtectionPredicate>> key = predicate.unwrapKey();
        key.ifPresent(resourceKey -> tag.putString("predicate", resourceKey.location().toString()));
        Tag boxTag = NarakaNbtUtils.writeBoundingBox(box);
        tag.put("box", boxTag);

        return tag;
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

    public static void initialize(MinecraftServer server) {
        ServerLevel overworld = server.overworld();
        DimensionDataStorage storage = overworld.getDataStorage();
        Container.instance = storage.computeIfAbsent(Container.factory, "structure_protectors");
    }

    private static class Container extends SavedData {
        private static final Factory<Container> factory = new Factory<>(
                Container::new, Container::create, DataFixTypes.LEVEL
        );
        private static Container instance = new Container();

        private final Set<StructureProtector> protectors;

        private Container() {
            protectors = new HashSet<>();
        }

        private Container(Set<StructureProtector> protectors) {
            this.protectors = protectors;
        }

        private static Container create(CompoundTag compoundTag, HolderLookup.Provider provider) {
            try {
                return new Container(NarakaNbtUtils.readCollection(compoundTag, "structure_protectors", HashSet::new, StructureProtector::new, provider));
            } catch (RuntimeException exception) {
                return new Container();
            }
        }

        @Override
        public @NotNull CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
            NarakaNbtUtils.writeCollection(compoundTag, "structure_protectors", protectors, StructureProtector::save, provider);
            return compoundTag;
        }
    }
}
