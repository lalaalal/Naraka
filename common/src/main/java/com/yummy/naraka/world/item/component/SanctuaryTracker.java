package com.yummy.naraka.world.item.component;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.world.structure.NarakaStructures;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Optional;

public record SanctuaryTracker(Optional<GlobalPos> sanctuaryPos, boolean tracked) {
    public static final SanctuaryTracker UNTRACKED = new SanctuaryTracker(Optional.empty(), false);

    public static final Codec<SanctuaryTracker> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    GlobalPos.CODEC.optionalFieldOf("sanctuary_pos").forGetter(SanctuaryTracker::sanctuaryPos),
                    Codec.BOOL.fieldOf("tracked").forGetter(SanctuaryTracker::tracked)
            ).apply(instance, SanctuaryTracker::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SanctuaryTracker> STREAM_CODEC = StreamCodec.composite(
            GlobalPos.STREAM_CODEC.apply(ByteBufCodecs::optional),
            SanctuaryTracker::sanctuaryPos,
            ByteBufCodecs.BOOL,
            SanctuaryTracker::tracked,
            SanctuaryTracker::new
    );

    public SanctuaryTracker(GlobalPos sanctuaryPos, boolean tracked) {
        this(Optional.of(sanctuaryPos), tracked);
    }

    public SanctuaryTracker update(ServerLevel serverLevel, BlockPos userPos, boolean forceUpdate) {
        if (tracked && !forceUpdate)
            return this;
        Registry<Structure> registry = serverLevel.registryAccess().registryOrThrow(Registries.STRUCTURE);
        Holder<Structure> sanctuary = registry.getHolderOrThrow(NarakaStructures.HEROBRINE_SANCTUARY);
        Pair<BlockPos, Holder<Structure>> pair = serverLevel.getChunkSource().getGenerator().findNearestMapStructure(serverLevel, HolderSet.direct(sanctuary), userPos, 100, false);
        if (pair == null)
            return UNTRACKED;
        BlockPos sanctuaryPos = pair.getFirst();
        return new SanctuaryTracker(GlobalPos.of(Level.OVERWORLD, sanctuaryPos), true);
    }
}
