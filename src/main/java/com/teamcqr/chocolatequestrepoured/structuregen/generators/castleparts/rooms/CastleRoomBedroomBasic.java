package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.RoomDecorTypes;

import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;

public class CastleRoomBedroomBasic extends CastleRoomGenericBase {
	private DyeColor carpetColor;

	public CastleRoomBedroomBasic(BlockPos startOffset, int sideLength, int height, int floor) {
		super(startOffset, sideLength, height, floor);
		this.roomType = EnumRoomType.BEDROOM_BASIC;
		this.maxSlotsUsed = 2;
		this.defaultCeiling = true;
		this.defaultFloor = true;

		this.decoSelector.registerEdgeDecor(RoomDecorTypes.NONE, 10);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.SHELF, 2);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.PLANKS, 2);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.TABLE_1x1, 2);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.BED, 3);
	}

	@Override
	protected BlockState getFloorBlock(DungeonCastle dungeon) {
		return dungeon.getMainBlockState();
	}
}
