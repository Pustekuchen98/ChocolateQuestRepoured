package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.objects;

import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.block.BlockAnvil;
import net.minecraft.util.Direction;

public class RoomDecorAnvil extends RoomDecorBlocksBase {
	public RoomDecorAnvil() {
		super();
	}

	@Override
	protected void makeSchematic() {
		this.schematic.add(new DecoBlockRotating(0, 0, 0, Blocks.ANVIL.getDefaultState(), BlockAnvil.FACING, Direction.WEST, BlockStateGenArray.GenerationPhase.MAIN));
	}
}
