package com.yummy.naraka.world.structure;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.util.HeightProvider;
import com.yummy.naraka.util.NarakaProtectionPredicates;
import com.yummy.naraka.util.StructureProtector;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.List;
import java.util.Optional;

public class JumboStructure extends Structure {
    public static final MapCodec<JumboStructure> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    settingsCodec(instance),
                    Codec.STRING.fieldOf("name").forGetter(structure -> structure.name),
                    Codec.BOOL.fieldOf("protect").forGetter(structure -> structure.protect),
                    HeightProvider.CODEC.fieldOf("height_provider").forGetter(structure -> structure.heightProvider),
                    JumboPart.CODEC.codec().listOf().fieldOf("parts").forGetter(structure -> structure.parts),
                    StructurePieceFactory.CODEC.listOf()
                            .fieldOf("custom_pieces")
                            .forGetter(structure -> structure.customPieces),
                    BlockPos.CODEC.fieldOf("structure_offset").forGetter(structure -> structure.structureOffset)
            ).apply(instance, JumboStructure::new)
    );

    private final String name;
    private final boolean protect;
    private final HeightProvider heightProvider;
    private final List<JumboPart> parts;
    private final List<Holder<StructurePieceFactory>> customPieces;
    private final BlockPos structureOffset;

    public JumboStructure(StructureSettings settings, String name, boolean protect, HeightProvider heightProvider, List<JumboPart> parts, List<Holder<StructurePieceFactory>> customPieces, BlockPos structureOffset) {
        super(settings);
        this.name = name;
        this.protect = protect;
        this.heightProvider = heightProvider;
        this.parts = parts;
        this.customPieces = customPieces;
        this.structureOffset = structureOffset;
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        ChunkGenerator generator = context.chunkGenerator();
        int height = heightProvider.getHeight(context);
        BlockPos base = new BlockPos(chunkPos.getMinBlockX(), Math.max(height, generator.getSeaLevel()), chunkPos.getMinBlockZ());
        return addPieces(context, base.offset(structureOffset));
    }

    protected Optional<GenerationStub> addPieces(GenerationContext context, BlockPos basePos) {
        StructureTemplateManager templateManager = context.structureTemplateManager();
        return Optional.of(new GenerationStub(basePos, builder -> {
            for (Holder<StructurePieceFactory> holder : customPieces) {
                StructurePieceFactory structurePieceFactory = holder.value();
                StructurePiece structurePiece = structurePieceFactory.create(basePos);
                builder.addPiece(structurePiece);
            }
            for (JumboPart part : parts)
                addPart(templateManager, builder, part, basePos);
            StructureProtector.addProtector(NarakaProtectionPredicates.SPHERE, builder.getBoundingBox());
        }));
    }

    private void addPart(StructureTemplateManager templateManager, StructurePiecesBuilder builder, JumboPart part, BlockPos basePos) {
        for (Vec3i piecePosition : part.getPositions()) {
            ResourceLocation location = part.getPieceLocation(name, piecePosition);
            LogUtils.getLogger().debug("Add {}", location);
            BlockPos actualPosition = basePos.offset(part.getPiecePosition(piecePosition));
            builder.addPiece(new JumboPiece(templateManager, location, actualPosition));
        }
    }

    @Override
    public StructureType<?> type() {
        return NarakaStructureTypes.JUMBO.get();
    }
}
