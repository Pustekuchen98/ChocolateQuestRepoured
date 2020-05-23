package com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.structuregen.PlateauBuilder;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonStrongholdOpen;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.ExtendedBlockStatePart;
import com.teamcqr.chocolatequestrepoured.structuregen.generation.IStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.stronghold.open.StrongholdFloorOpen;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.EPosType;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.data.FileIOUtil;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.template.PlacementSettings;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class GeneratorStrongholdOpen implements IDungeonGenerator {

	private DungeonStrongholdOpen dungeon;

	private List<String> blacklistedRooms = new ArrayList<String>();
	private Tuple<Integer, Integer> structureBounds;

	private PlacementSettings settings = new PlacementSettings();

	private StrongholdFloorOpen[] floors;

	private int dunX;
	private int dunZ;
	
	private int entranceSizeX = 0;
	private int entranceSizeZ = 0;
	
	public GeneratorStrongholdOpen(DungeonStrongholdOpen dungeon) {
		super();
		this.dungeon = dungeon;
		this.structureBounds = new Tuple<Integer, Integer>(dungeon.getRoomSizeX(), dungeon.getRoomSizeZ());

		this.settings.setMirror(Mirror.NONE);
		this.settings.setRotation(Rotation.NONE);
		//this.settings.setReplacedBlock(Blocks.STRUCTURE_VOID);
		//this.settings.setIntegrity(1.0F);

		this.floors = new StrongholdFloorOpen[dungeon.getRandomFloorCount()];
		this.searchStructureBounds();
		this.computeNotFittingStructures();
	}

	private void computeNotFittingStructures() {
		for (File f : this.dungeon.getRoomFolder().listFiles(FileIOUtil.getNBTFileFilter())) {
			CQStructure struct = new CQStructure(f);
			if (struct != null && (struct.getSize().getX() != this.structureBounds.getA() || struct.getSize().getZ() != this.structureBounds.getB())) {
				this.blacklistedRooms.add(f.getParent() + "/" + f.getName());
			}
		}
	}

	public DungeonStrongholdOpen getDungeon() {
		return this.dungeon;
	}

	private void searchStructureBounds() {

	}

	@Override
	public void preProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		this.dunX = x;
		this.dunZ = z;
		BlockPos initPos = new BlockPos(x, y, z);
		// initPos = initPos.subtract(new Vec3i(0,dungeon.getYOffset(),0));
		// initPos = initPos.subtract(new Vec3i(0,dungeon.getUnderGroundOffset(),0));
		
		int rgd = getDungeon().getRandomRoomCountForFloor();
		if (rgd < 2) {
			rgd = 2;
		}
		if (rgd % 2 != 0) {
			rgd++;
		}
		rgd = (new Double(Math.ceil(Math.sqrt(rgd)))).intValue();
		if(rgd % 2 == 0) {
			rgd++;
		}
		
		StrongholdFloorOpen prevFloor = null;
		for (int i = 0; i < this.floors.length; i++) {
			boolean isFirst = i == 0;
			StrongholdFloorOpen floor = null;
			if(isFirst) {
				floor = new StrongholdFloorOpen(this, rgd, ((Double)Math.floor(rgd /2)).intValue(), ((Double)Math.floor(rgd /2)).intValue());
			} else {
				floor = new StrongholdFloorOpen(this, rgd, prevFloor.getExitStairIndexes().getA(), prevFloor.getExitStairIndexes().getB());
			}
			File stair = null;
			if (isFirst) {
				stair = this.dungeon.getEntranceStair();
				if (stair == null) {
					CQRMain.logger.error("No entrance stair rooms for Stronghold Open Dungeon: " + this.getDungeon().getDungeonName());
					return;
				}
			} else {
				stair = this.dungeon.getStairRoom();
				if (stair == null) {
					CQRMain.logger.error("No stair rooms for Stronghold Open Dungeon: " + this.getDungeon().getDungeonName());
					return;
				}
			}
			floor.setIsFirstFloor(isFirst);
			int dY = initPos.getY() - new CQStructure(stair).getSize().getY();
			if (dY <= (this.dungeon.getRoomSizeY() + 2)) {
				this.floors[i - 1].setExitIsBossRoom(true);
			} else {
				initPos = initPos.subtract(new Vec3i(0, new CQStructure(stair).getSize().getY(), 0));
				if (!isFirst) {
					initPos = initPos.add(0, this.dungeon.getRoomSizeY(), 0);
				}
				if ((i + 1) == this.floors.length) {
					floor.setExitIsBossRoom(true);
				}
				
				if(isFirst) {
					floor.setEntranceStairPosition(stair, initPos.getX(), initPos.getY(), initPos.getZ());
				} else {
					floor.setEntranceStairPosition(stair, prevFloor.getExitCoordinates().getA(), initPos.getY(), prevFloor.getExitCoordinates().getB());
				}
				
				floor.calculatePositions();
				initPos = new BlockPos(floor.getExitCoordinates().getA(), initPos.getY(), floor.getExitCoordinates().getB());
			}
			prevFloor = floor;
			this.floors[i] = floor;
		}
	}

	@Override
	public void buildStructure(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		File building = this.dungeon.getEntranceBuilding();
		EDungeonMobType mobType = dungeon.getDungeonMob();
		if (mobType == EDungeonMobType.DEFAULT) {
			mobType = EDungeonMobType.getMobTypeDependingOnDistance(world, x, z);
		}
		if (building == null || this.dungeon.getEntranceBuildingFolder().listFiles(FileIOUtil.getNBTFileFilter()).length <= 0) {
			CQRMain.logger.error("No entrance buildings for Open Stronghold dungeon: " + this.getDungeon().getDungeonName());
			return;
		}
		CQStructure structure = new CQStructure(building);
		structure.setDungeonMob(mobType);
		if (this.dungeon.doBuildSupportPlatform()) {
			PlateauBuilder supportBuilder = new PlateauBuilder();
			supportBuilder.load(this.dungeon.getSupportBlock(), this.dungeon.getSupportTopBlock());
			lists.add(supportBuilder.createSupportHillList(new Random(), world, new BlockPos(x, y + this.dungeon.getUnderGroundOffset(), z), structure.getSize().getX(), structure.getSize().getZ(), EPosType.CENTER_XZ_LAYER));
		}
		entranceSizeX = structure.getSize().getX();
		entranceSizeZ = structure.getSize().getX();
		//structure.addBlocksToWorld(world, new BlockPos(x, y, z), this.settings, EPosType.CENTER_XZ_LAYER, this.dungeon, chunk.x, chunk.z);
		for (List<? extends IStructure> list : structure.addBlocksToWorld(world, new BlockPos(x, y, z), this.settings, EPosType.CENTER_XZ_LAYER, this.dungeon, chunk.getPos().x, chunk.getPos().z)) {
			lists.add(list);
		}
		/*
		 * CQStructure stairs = new CQStructure(dungeon.getStairRoom(), dungeon, chunk.x, chunk.z, dungeon.isProtectedFromModifications());
		 * BlockPos pastePosForStair = new BlockPos(x, y - stairs.getSizeY(), z);
		 * stairs.placeBlocksInWorld(world, pastePosForStair, settings, EPosType.CENTER_XZ_LAYER);
		 */
		// Will generate the structure
		// Algorithm: while(genRooms < rooms && genFloors < maxFloors) do {
		// while(genRoomsOnFloor < roomsPerFloor) {
		// choose structure, calculate next pos and chose next structure (System: structures in different folders named to where they may attach
		// build Staircase at next position
		// genRoomsOnFloor++
		// genFloors++
		// build staircase to bossroom at next position, then build boss room

		// Structure gen information: stored in map with location and structure file
		for (StrongholdFloorOpen floor : this.floors) {
			floor.generateRooms(world, lists, mobType);
		}
	}

	@Override
	public void postProcess(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// build all the structures in the map
		for (StrongholdFloorOpen floor : this.floors) {
			if (floor == null) {
				CQRMain.logger.error("Floor is null! Not generating it!");
			} else {
				try {
					floor.buildWalls(world, lists);
				} catch (NullPointerException ex) {
					CQRMain.logger.error("Error whilst trying to construct wall in open stronghold at: X " + x + "  Y " + y + "  Z " + z);
				}
			}
		}
	}

	@Override
	public void fillChests(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Unused
	}

	@Override
	public void placeSpawners(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		// Unused
	}

	@Override
	public void placeCoverBlocks(World world, Chunk chunk, int x, int y, int z, List<List<? extends IStructure>> lists) {
		if (this.dungeon.isCoverBlockEnabled()) {
			Map<BlockPos, ExtendedBlockStatePart.ExtendedBlockState> stateMap = new HashMap<>();
			
			int startX = x - entranceSizeX / 3 - CQRConfig.general.supportHillWallSize / 2;
			int startZ = z - entranceSizeZ / 3 - CQRConfig.general.supportHillWallSize / 2;

			int endX = x + entranceSizeX + entranceSizeX / 3 + CQRConfig.general.supportHillWallSize / 2;
			int endZ = z + entranceSizeZ + entranceSizeZ / 3 + CQRConfig.general.supportHillWallSize / 2;

			for (int iX = startX; iX <= endX; iX++) {
				for (int iZ = startZ; iZ <= endZ; iZ++) {
					BlockPos pos = new BlockPos(iX, world.getTopSolidOrLiquidBlock(new BlockPos(iX, 0, iZ)).getY(), iZ);
					if (world.getBlockState(pos.subtract(new Vec3i(0, 1, 0))).getBlock() != this.dungeon.getCoverBlock()) {
						stateMap.put(pos, new ExtendedBlockStatePart.ExtendedBlockState(this.dungeon.getCoverBlock().getDefaultState(), null));
					}
				}
			}
			lists.add(ExtendedBlockStatePart.splitExtendedBlockStateMap(stateMap));
		}
	}

	public int getDunX() {
		return this.dunX;
	}

	public int getDunZ() {
		return this.dunZ;
	}

	public PlacementSettings getPlacementSettings() {
		return this.settings;
	}

}
