package com.yummy.naraka.world.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.world.structure.generation.StructureGenerationPointProvider;
import com.yummy.naraka.world.structure.piece.JumboPiece;
import com.yummy.naraka.world.structure.piece.StructurePieceFactory;
import com.yummy.naraka.world.structure.protection.ProtectionPredicate;
import com.yummy.naraka.world.structure.protection.StructureProtector;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class JumboStructure extends Structure {
    public static final MapCodec<JumboStructure> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    settingsCodec(instance),
                    Codec.STRING.fieldOf("name").forGetter(structure -> structure.name),
                    RegistryFixedCodec.create(NarakaRegistries.Keys.PROTECTION_PREDICATE)
                            .fieldOf("protection_predicate")
                            .forGetter(structure -> structure.protectionPredicate),
                    RegistryFixedCodec.create(NarakaRegistries.Keys.STRUCTURE_GENERATION_POINT_PROVIDER)
                            .fieldOf("generation_point_provider")
                            .forGetter(structure -> structure.generationPointProvider),
                    JumboPart.CODEC.codec().listOf().fieldOf("parts").forGetter(structure -> structure.parts),
                    StructurePieceFactory.CODEC.listOf()
                            .fieldOf("custom_pieces")
                            .forGetter(structure -> structure.customPieces),
                    BlockPos.CODEC.fieldOf("structure_offset").forGetter(structure -> structure.structureOffset)
            ).apply(instance, JumboStructure::new)
    );

    protected final String name;
    protected final Holder<ProtectionPredicate> protectionPredicate;
    protected final Holder<StructureGenerationPointProvider> generationPointProvider;
    protected final List<JumboPart> parts;
    protected final List<Holder<StructurePieceFactory>> customPieces;
    protected final BlockPos structureOffset;

    public JumboStructure(StructureSettings settings, String name, Holder<ProtectionPredicate> protectionPredicate, Holder<StructureGenerationPointProvider> generationPointProvider, List<JumboPart> parts, List<Holder<StructurePieceFactory>> customPieces, BlockPos structureOffset) {
        super(settings);
        this.name = name;
        this.protectionPredicate = protectionPredicate;
        this.generationPointProvider = generationPointProvider;
        this.parts = parts;
        this.customPieces = customPieces;
        this.structureOffset = structureOffset;
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        BlockPos base = new BlockPos(chunkPos.getMinBlockX(), 0, chunkPos.getMinBlockZ())
                .offset(structureOffset);

        Optional<BlockPos> optionalPoint = generationPointProvider.value().getPoint(context, base);
        return optionalPoint.flatMap(blockPos -> addPieces(context, blockPos));
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
            StructureProtector.addProtector(protectionPredicate, builder.getBoundingBox());
        }));
    }

    private void addPart(StructureTemplateManager templateManager, StructurePiecesBuilder builder, JumboPart part, BlockPos basePos) {
        for (Vec3i piecePosition : part.getPositions()) {
            Identifier location = part.getPieceLocation(name, piecePosition);
            BlockPos actualPosition = basePos.offset(part.getPiecePosition(piecePosition));
            builder.addPiece(new JumboPiece(templateManager, location, actualPosition));
        }
    }

    @Override
    public @NotNull StructureType<?> type() {
        return NarakaStructureTypes.JUMBO.get();
    }
}
