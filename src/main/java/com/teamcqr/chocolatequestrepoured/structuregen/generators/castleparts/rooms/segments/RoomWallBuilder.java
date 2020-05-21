package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.segments;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.RandomCastleConfigOptions;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

/**
 * Copyright (c) 20.04.2020
 * Developed by KalgogSmash
 * GitHub: https://github.com/KalgogSmash
 */
public class RoomWallBuilder {
	protected BlockPos wallStart;
	protected WallOptions options;
	protected int doorStart = 0;
	protected int doorWidth = 0;
	protected int doorHeight = 0;
	protected int length;
	protected int height;
	protected Direction side;
	protected Random random;
	protected RandomCastleConfigOptions.WindowType windowType = RandomCastleConfigOptions.WindowType.BASIC_GLASS;

	public RoomWallBuilder(BlockPos wallStart, int height, int length, WallOptions options, Direction side) {
		this.height = height;
		this.length = length;
		this.options = options;
		this.side = side;

		this.wallStart = wallStart;

		if (options.hasDoor()) {
			this.doorStart = options.getDoor().getOffset();
			this.doorWidth = options.getDoor().getWidth();
			this.doorHeight = options.getDoor().getHeight();
		}
	}

	public void generate(BlockStateGenArray genArray, DungeonCastle dungeon) {
		BlockPos pos;
		BlockState blockToBuild;

		Direction iterDirection;
		this.windowType = dungeon.getRandomWindowType();

		if (this.side.getAxis() == Direction.Axis.X) {
			iterDirection = Direction.SOUTH;
		} else {
			iterDirection = Direction.EAST;
		}

		for (int i = 0; i < this.length; i++) {
			for (int y = 0; y < this.height; y++) {
				pos = this.wallStart.offset(iterDirection, i).offset(Direction.UP, y);
				blockToBuild = this.getBlockToBuild(pos, dungeon);
				genArray.forceAddBlockState(pos, blockToBuild, BlockStateGenArray.GenerationPhase.MAIN);
			}
		}
	}

	protected BlockState getBlockToBuild(BlockPos pos, DungeonCastle dungeon) {
		if (this.options.hasWindow()) {
			return this.getWindowBlock(pos, dungeon);
		} else if (this.options.hasDoor()) {
			return this.getDoorBlock(pos, dungeon);
		} else {
			return dungeon.getMainBlockState();
		}
	}

	protected BlockState getDoorBlock(BlockPos pos, DungeonCastle dungeon) {
		switch (this.options.getDoor().getType()) {
		case AIR:
			return this.getBlockDoorAir(pos, dungeon);

		case STANDARD:
			return this.getBlockDoorStandard(pos, dungeon);

		case FENCE_BORDER:
			return this.getBlockDoorFenceBorder(pos, dungeon);

		case STAIR_BORDER:
			return this.getBlockDoorStairBorder(pos, dungeon);

		case GRAND_ENTRY:
			return this.getBlockGrandEntry(pos, dungeon);

		default:
			return dungeon.getMainBlockState();
		}
	}

	private BlockState getBlockDoorAir(BlockPos pos, DungeonCastle dungeon) {
		BlockState blockToBuild = dungeon.getMainBlockState();
		int y = pos.getY() - this.wallStart.getY();
		int dist = this.getLengthPoint(pos);

		if (this.withinDoorWidth(dist)) {
			if (y == 0) {
				blockToBuild = dungeon.getMainBlockState();
			} else if (y < this.doorHeight) {
				blockToBuild = Blocks.AIR.getDefaultState();
			}
		}

		return blockToBuild;
	}

	private BlockState getBlockDoorStairBorder(BlockPos pos, DungeonCastle dungeon) {
		BlockState blockToBuild = dungeon.getMainBlockState();
		final int y = pos.getY() - this.wallStart.getY();
		final int dist = this.getLengthPoint(pos);
		final int halfPoint = this.doorStart + (this.doorWidth / 2);

		if (this.withinDoorWidth(dist)) {
			if (y == 0) {
				blockToBuild = dungeon.getMainBlockState();
			} else if (dist == halfPoint || dist == halfPoint - 1) {
				if (y >= 1 && y <= 3) {
					blockToBuild = Blocks.AIR.getDefaultState();
				} else if (y == 4) {
					blockToBuild = dungeon.getSlabBlockState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
				}
			} else if (dist == halfPoint + 1 || dist == halfPoint - 2) {
				Direction stairFacing;

				if (this.side == Direction.WEST || this.side == Direction.SOUTH) {
					stairFacing = (dist == halfPoint - 2) ? this.side.rotateY() : this.side.rotateYCCW();
				} else {
					stairFacing = (dist == halfPoint - 2) ? this.side.rotateYCCW() : this.side.rotateY();
				}

				BlockState stairBase = dungeon.getStairBlockState().withProperty(BlockStairs.FACING, stairFacing);

				if (y == 1) {
					blockToBuild = stairBase;
				} else if (y == 2 || y == 3) {
					blockToBuild = Blocks.AIR.getDefaultState();
				} else if (y == 4) {
					blockToBuild = stairBase.withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP);
				}
			}
		}

		return blockToBuild;
	}

	private BlockState getBlockDoorFenceBorder(BlockPos pos, DungeonCastle dungeon) {
		BlockState blockToBuild = dungeon.getMainBlockState();
		final int y = pos.getY() - this.wallStart.getY();
		final int dist = this.getLengthPoint(pos);
		final int halfPoint = this.doorStart + (this.doorWidth / 2);

		if (this.withinDoorWidth(dist)) {
			if (y == 0) {
				blockToBuild = dungeon.getMainBlockState();
			} else if (dist == halfPoint || dist == halfPoint - 1) {
				if (y == 1 || y == 2) {
					blockToBuild = Blocks.AIR.getDefaultState();
				} else if (y == 3) {
					blockToBuild = dungeon.getFenceBlockState();
				}
			} else if (((dist == halfPoint + 1) || (dist == halfPoint - 2)) && (y < this.doorHeight)) {
				blockToBuild = dungeon.getFenceBlockState();
			}
		}

		return blockToBuild;
	}

	private BlockState getBlockDoorStandard(BlockPos pos, DungeonCastle dungeon) {
		BlockState blockToBuild = dungeon.getMainBlockState();
		final int y = pos.getY() - this.wallStart.getY();
		final int dist = this.getLengthPoint(pos);
		final int halfPoint = this.doorStart + (this.doorWidth / 2);

		if (this.withinDoorWidth(dist)) {
			if (y == 0) {
				blockToBuild = dungeon.getFloorBlockState();
			} else if ((dist == halfPoint || dist == halfPoint - 1)) {
				if (y == 1 || y == 2) {
					BlockDoor.EnumDoorHalf half = (y == 1) ? BlockDoor.EnumDoorHalf.LOWER : BlockDoor.EnumDoorHalf.UPPER;
					BlockDoor.EnumHingePosition hinge;

					if (this.side == Direction.WEST || this.side == Direction.SOUTH) {
						hinge = (dist == halfPoint) ? BlockDoor.EnumHingePosition.LEFT : BlockDoor.EnumHingePosition.RIGHT;
					} else {
						hinge = (dist == halfPoint) ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT;
					}

					blockToBuild = dungeon.getDoorBlockState().withProperty(BlockDoor.HALF, half).withProperty(BlockDoor.FACING, this.side).withProperty(BlockDoor.HINGE, hinge);
				} else if (y == 3) {
					blockToBuild = dungeon.getPlankBlockState();
				}

			} else if (((dist == halfPoint + 1) || (dist == halfPoint - 2)) && (y < this.doorHeight)) {
				blockToBuild = dungeon.getPlankBlockState();
			}
		}

		return blockToBuild;
	}

	private BlockState getBlockGrandEntry(BlockPos pos, DungeonCastle dungeon) {
		BlockState blockToBuild = dungeon.getMainBlockState();

		final int y = pos.getY() - this.wallStart.getY();
		final int dist = this.getLengthPoint(pos);
		final int halfPoint = this.doorStart + (this.doorWidth / 2);
		final int distFromHalf = Math.abs(dist - halfPoint);

		final BlockState outlineBlock = dungeon.getFancyBlockState();

		if (this.withinDoorWidth(dist)) {
			if (y == 0) {
				blockToBuild = dungeon.getMainBlockState();
			} else if (distFromHalf == 0) {
				if (y <= 3) {
					return Blocks.AIR.getDefaultState();
				} else if (y == 4) {
					return dungeon.getFenceBlockState();
				} else if (y == 5) {
					return outlineBlock;
				}
			} else if (distFromHalf == 1) {
				if (y <= 2) {
					return Blocks.AIR.getDefaultState();
				} else if (y == 3 || y == 4) {
					return dungeon.getFenceBlockState();
				} else if (y == 5) {
					return outlineBlock;
				}
			} else if (Math.abs(dist - halfPoint) == 2) {
				if (y <= 3) {
					return dungeon.getFenceBlockState();
				} else if (y == 4 || y == 5) {
					return outlineBlock;
				}
			} else if (Math.abs(dist - halfPoint) == 3) {
				if (y <= 4) {
					return outlineBlock;
				}
			}
		}

		return blockToBuild;
	}

	protected BlockState getWindowBlock(BlockPos pos, DungeonCastle dungeon) {
		switch (this.windowType) {
			case BASIC_GLASS:
				return getBlockWindowBasicGlass(pos, dungeon);
			case CROSS_GLASS:
				return getBlockWindowCrossGlass(pos, dungeon);
			case SQUARE_BARS:
				return getBlockWindowSquareBars(pos, dungeon);
			case OPEN_SLIT:
			default:
				return getBlockWindowOpenSlit(pos, dungeon);
		}
	}

	private BlockState getBlockWindowBasicGlass(BlockPos pos, DungeonCastle dungeon) {
		int y = pos.getY() - this.wallStart.getY();
		int dist = this.getLengthPoint(pos);

		if ((y == 2 || y == 3) && (dist == this.length / 2)) {
			return Blocks.GLASS_PANE.getDefaultState();
		} else {
			return dungeon.getMainBlockState();
		}
	}

	private BlockState getBlockWindowCrossGlass(BlockPos pos, DungeonCastle dungeon) {
		int y = pos.getY() - this.wallStart.getY();
		int dist = this.getLengthPoint(pos);
		int halfDist = this.length / 2;

		if ((dist == halfDist - 1 && y == 3) ||
				(dist == halfDist && y >= 2 && y <= 4) ||
				(dist == halfDist + 1 && y == 3)){
			return Blocks.GLASS_PANE.getDefaultState();
		} else {
			return dungeon.getMainBlockState();
		}
	}

	private BlockState getBlockWindowSquareBars(BlockPos pos, DungeonCastle dungeon) {
		int y = pos.getY() - this.wallStart.getY();
		int dist = this.getLengthPoint(pos);
		int halfDist = length / 2;

		if (((y == 2) || (y == 3)) &&
				((dist == halfDist) || (dist == halfDist + 1)))  {
			return Blocks.IRON_BARS.getDefaultState();
		} else {
			return dungeon.getMainBlockState();
		}
	}

	private BlockState getBlockWindowOpenSlit(BlockPos pos, DungeonCastle dungeon) {
		int y = pos.getY() - this.wallStart.getY();
		int dist = this.getLengthPoint(pos);
		int halfDist = length / 2;

		if ((y == 2) && (dist >= halfDist - 1) && (dist <= halfDist + 1))  {
			return Blocks.AIR.getDefaultState();
		} else {
			return dungeon.getMainBlockState();
		}
	}

	/*
	 * Whether to build a door or window is usually determined by how far along the wall we are.
	 * This function gets the relevant length along the wall based on if we are a horizontal
	 * wall or a vertical wall.
	 */
	protected int getLengthPoint(BlockPos pos) {
		if (this.side.getAxis() == Direction.Axis.X) {
			return pos.getZ() - this.wallStart.getZ();
		} else {
			return pos.getX() - this.wallStart.getX();
		}
	}

	protected boolean withinDoorWidth(int value) {
		int relativeToDoor = value - this.doorStart;
		return (relativeToDoor >= 0 && relativeToDoor < this.doorWidth);
	}
}
