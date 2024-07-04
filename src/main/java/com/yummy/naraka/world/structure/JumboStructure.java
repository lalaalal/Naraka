package com.yummy.naraka.world.structure;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.List;
import java.util.Optional;

public class JumboStructure extends Structure {
    public static final MapCodec<JumboStructure> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    settingsCodec(instance),
                    Codec.STRING.fieldOf("name").forGetter(structure -> structure.name),
                    Heightmap.Types.CODEC.fieldOf("heightmap_type").forGetter(structure -> structure.heightmapType),
                    Codec.INT.fieldOf("x_count").forGetter(structure -> structure.xCount),
                    Codec.INT.fieldOf("y_count").forGetter(structure -> structure.yCount),
                    Codec.INT.fieldOf("z_count").forGetter(structure -> structure.zCount),
                    Codec.mapPair(PiecePlacement.CODEC.fieldOf("placement"), BlockPos.CODEC.fieldOf("offset"))
                            .codec()
                            .listOf()
                            .fieldOf("custom_placements")
                            .forGetter(structure -> structure.customPiecePlacements)
            ).apply(instance, JumboStructure::new)
    );

    private final String name;
    private final Heightmap.Types heightmapType;
    private final int xCount, yCount, zCount;
    private final List<Pair<Holder<PiecePlacement>, BlockPos>> customPiecePlacements;

    public JumboStructure(StructureSettings settings, String name, Heightmap.Types heightmapType, int xCount, int yCount, int zCount, List<Pair<Holder<PiecePlacement>, BlockPos>> customPiecePlacements) {
        super(settings);
        this.name = name;
        this.heightmapType = heightmapType;
        this.xCount = xCount;
        this.yCount = yCount;
        this.zCount = zCount;
        this.customPiecePlacements = customPiecePlacements;
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int height = context.chunkGenerator().getFirstFreeHeight(
                chunkPos.x, chunkPos.z,
                heightmapType,
                context.heightAccessor(),
                context.randomState()
        );
        BlockPos base = new BlockPos(chunkPos.getMinBlockX(), height, chunkPos.getMinBlockZ());
        return JumboPlacement.addPieces(
                context,
                new JumboPlacement.SimplePiecePositionProvider(name),
                JumboPlacement.simple(xCount, yCount, zCount),
                customPiecePlacements,
                base
        );
    }

    @Override
    public StructureType<?> type() {
        return NarakaStructureTypes.JUMBO.get();
    }
}
