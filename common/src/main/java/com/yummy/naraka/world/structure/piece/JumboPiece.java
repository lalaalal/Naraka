package com.yummy.naraka.world.structure.piece;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class JumboPiece extends TemplateStructurePiece {
    public JumboPiece(StructureTemplateManager structureTemplateManager, Identifier location, BlockPos templatePosition) {
        super(NarakaStructurePieceTypes.JUMBO_PIECE.get(), 0, structureTemplateManager, location, location.toString(), makeSettings(location), templatePosition);
    }

    public JumboPiece(StructurePieceSerializationContext context, CompoundTag tag) {
        super(NarakaStructurePieceTypes.JUMBO_PIECE.get(), tag, context.structureTemplateManager(), JumboPiece::makeSettings);
    }

    private static StructurePlaceSettings makeSettings(Identifier location) {
        return new StructurePlaceSettings()
                .setMirror(Mirror.NONE)
                .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK)
                .addProcessor(BlockIgnoreProcessor.AIR)
                .setLiquidSettings(LiquidSettings.IGNORE_WATERLOGGING);
    }

    @Override
    protected void handleDataMarker(String pName, BlockPos pPos, ServerLevelAccessor pLevel, RandomSource pRandom, BoundingBox pBox) {

    }
}