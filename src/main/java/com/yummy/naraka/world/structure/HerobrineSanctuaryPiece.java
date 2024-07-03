package com.yummy.naraka.world.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class HerobrineSanctuaryPiece extends TemplateStructurePiece {
    public HerobrineSanctuaryPiece(StructureTemplateManager structureTemplateManager, ResourceLocation location, BlockPos templatePosition) {
        super(NarakaStructureTypes.HEROBRINE_SANCTUARY_PIECE.get(), 0, structureTemplateManager, location, location.toString(), makeSettings(location), templatePosition);
    }

    public HerobrineSanctuaryPiece(StructurePieceSerializationContext context, CompoundTag tag) {
        super(NarakaStructureTypes.HEROBRINE_SANCTUARY_PIECE.get(), tag, context.structureTemplateManager(), HerobrineSanctuaryPiece::makeSettings);
    }

    private static StructurePlaceSettings makeSettings(ResourceLocation location) {
        return new StructurePlaceSettings()
                .setMirror(Mirror.NONE)
                .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
    }

    @Override
    protected void handleDataMarker(String pName, BlockPos pPos, ServerLevelAccessor pLevel, RandomSource pRandom, BoundingBox pBox) {

    }
}