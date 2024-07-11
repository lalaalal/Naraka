package com.yummy.naraka.world.structure;

import com.yummy.naraka.NarakaMod;
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

public class JumboPiece extends TemplateStructurePiece {
    private final boolean protect;

    public JumboPiece(StructureTemplateManager structureTemplateManager, ResourceLocation location, BlockPos templatePosition) {
        this(structureTemplateManager, location, templatePosition, false);
    }

    public JumboPiece(StructureTemplateManager structureTemplateManager, ResourceLocation location, BlockPos templatePosition, boolean protect) {
        super(NarakaStructureTypes.JUMBO_PIECE.get(), 0, structureTemplateManager, location, location.toString(), makeSettings(location), templatePosition);
        this.protect = protect;
        if (protect)
            NarakaMod.context().addProtectedArea(getBoundingBox());
    }

    public JumboPiece(StructurePieceSerializationContext context, CompoundTag tag) {
        super(NarakaStructureTypes.JUMBO_PIECE.get(), tag, context.structureTemplateManager(), JumboPiece::makeSettings);
        this.protect = tag.getBoolean("Protect");
        if (protect)
            NarakaMod.context().addProtectedArea(getBoundingBox());
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        super.addAdditionalSaveData(context, tag);
        tag.putBoolean("Protect", protect);
    }

    private static StructurePlaceSettings makeSettings(ResourceLocation location) {
        return new StructurePlaceSettings()
                .setMirror(Mirror.NONE)
                .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK)
                .addProcessor(BlockIgnoreProcessor.AIR);
    }

    @Override
    protected void handleDataMarker(String pName, BlockPos pPos, ServerLevelAccessor pLevel, RandomSource pRandom, BoundingBox pBox) {

    }
}