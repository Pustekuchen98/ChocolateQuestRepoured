package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.init.ModLoottables;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.decoration.RoomDecorTypes;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class CastleRoomArmory extends CastleRoomGenericBase {
	public CastleRoomArmory(BlockPos startOffset, int sideLength, int height, int floor) {
		super(startOffset, sideLength, height, floor);
		this.roomType = EnumRoomType.ARMORY;
		this.maxSlotsUsed = 2;
		this.defaultCeiling = true;
		this.defaultFloor = true;

		this.decoSelector.registerEdgeDecor(RoomDecorTypes.NONE, 5);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.SHELF, 3);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.ANVIL, 1);
		this.decoSelector.registerEdgeDecor(RoomDecorTypes.ARMOR_STAND, 1);

		this.decoSelector.registerMidDecor(RoomDecorTypes.NONE, 20);
		this.decoSelector.registerMidDecor(RoomDecorTypes.TABLE_2x2, 1);
	}

	@Override
	public void generateRoom(BlockStateGenArray genArray, DungeonCastle dungeon) {
		super.generateRoom(genArray, dungeon);
	}

	@Override
	public ResourceLocation[] getChestIDs() {
		return new ResourceLocation[] { ModLoottables.CHESTS_EQUIPMENT };
	}
}
