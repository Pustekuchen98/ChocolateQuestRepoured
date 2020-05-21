package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.Direction;

public class RoomDecorWaterBasin extends RoomDecorBlocksBase {
    public RoomDecorWaterBasin() {
        super();
    }

    @Override
    protected void makeSchematic() {
        final BlockState chiseledStone = Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED);
        final BlockState stairs = Blocks.STONE_BRICK_STAIRS.getDefaultState();

        this.schematic.add(new DecoBlockBase(0, 0, 0, chiseledStone, BlockStateGenArray.GenerationPhase.MAIN));
        this.schematic.add(new DecoBlockRotating(1, 0, 0, stairs, BlockStairs.FACING, Direction.SOUTH, BlockStateGenArray.GenerationPhase.MAIN));
        this.schematic.add(new DecoBlockBase(2, 0, 0, chiseledStone, BlockStateGenArray.GenerationPhase.MAIN));

        this.schematic.add(new DecoBlockRotating(0, 0, 1, stairs, BlockStairs.FACING, Direction.EAST, BlockStateGenArray.GenerationPhase.MAIN));
        this.schematic.add(new DecoBlockBase(1, 0, 1, Blocks.WATER.getDefaultState(), BlockStateGenArray.GenerationPhase.POST));
        this.schematic.add(new DecoBlockRotating(2, 0, 1, stairs, BlockStairs.FACING, Direction.WEST, BlockStateGenArray.GenerationPhase.MAIN));

        this.schematic.add(new DecoBlockBase(0, 0, 2, chiseledStone, BlockStateGenArray.GenerationPhase.MAIN));
        this.schematic.add(new DecoBlockRotating(1, 0, 2, stairs, BlockStairs.FACING, Direction.NORTH, BlockStateGenArray.GenerationPhase.MAIN));
        this.schematic.add(new DecoBlockBase(2, 0, 2, chiseledStone, BlockStateGenArray.GenerationPhase.MAIN));
    }
}
