package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.util.Collection;
import java.util.Properties;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.GeneratorCastle;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.RandomCastleConfigOptions;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms.EnumRoomType;
import com.teamcqr.chocolatequestrepoured.util.CQRWeightedRandom;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.EnumMCWoodType;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.block.BlockState;
import net.minecraft.state.IProperty;
import net.minecraft.world.World;

/**
 * Copyright (c) 20.04.2020
 * Developed by KalgogSmash
 * GitHub: https://github.com/KalgogSmash
 */
public class DungeonCastle extends DungeonBase {

	private int maxSize;
	private int roomSize;
	private int floorHeight;
	private BlockState mainBlock;
	private BlockState fancyBlock;
	private BlockState slabBlock;
	private BlockState stairBlock;
	private BlockState roofBlock;
	private BlockState fenceBlock;
	private BlockState floorBlock;
	private BlockState woodStairBlock;
	private BlockState woodSlabBlock;
	private BlockState plankBlock;
	private BlockState doorBlock;

	private CQRWeightedRandom<RandomCastleConfigOptions.RoofType> roofTypeRandomizer;
	private CQRWeightedRandom<RandomCastleConfigOptions.WindowType> windowTypeRandomizer;
	private CQRWeightedRandom<EnumRoomType> roomRandomizer;

	private int minSpawnerRolls;
	private int maxSpawnerRolls;
	private int spawnerRollChance;

	public DungeonCastle(String name, Properties prop) {
		super(name, prop);

		this.maxSize = PropertyFileHelper.getIntProperty(prop, "maxSize", 60);
		this.roomSize = PropertyFileHelper.getIntProperty(prop, "roomSize", 10);
		this.floorHeight = PropertyFileHelper.getIntProperty(prop, "floorHeight", 8);

		EnumMCWoodType woodType = PropertyFileHelper.getWoodTypeProperty(prop, "woodType", EnumMCWoodType.OAK);
		this.mainBlock = PropertyFileHelper.getDefaultStateBlockProperty(prop, "mainBlock", Blocks.STONEBRICK.getDefaultState());
		this.stairBlock = PropertyFileHelper.getDefaultStateBlockProperty(prop, "stairBlock", Blocks.STONE_BRICK_STAIRS.getDefaultState());
		this.slabBlock = PropertyFileHelper.getDefaultStateBlockProperty(prop, "slabBlock", Blocks.STONE_SLAB.getDefaultState());
		this.fancyBlock = PropertyFileHelper.getDefaultStateBlockProperty(prop, "fancyBlock", Blocks.STONEBRICK.getDefaultState());
		this.floorBlock = PropertyFileHelper.getDefaultStateBlockProperty(prop, "floorBlock", woodType.getPlankBlockState());
		this.roofBlock = PropertyFileHelper.getDefaultStateBlockProperty(prop, "roofBlock", woodType.getStairBlockState());
		this.fenceBlock = PropertyFileHelper.getDefaultStateBlockProperty(prop, "fenceBlock", woodType.getFenceBlockState());
		this.woodStairBlock = PropertyFileHelper.getDefaultStateBlockProperty(prop, "woodStairBlock", woodType.getStairBlockState());
		this.woodSlabBlock = PropertyFileHelper.getDefaultStateBlockProperty(prop, "woodSlabBlock", woodType.getSlabBlockState());
		this.plankBlock = PropertyFileHelper.getDefaultStateBlockProperty(prop, "plankBlock", woodType.getPlankBlockState());
		this.doorBlock = PropertyFileHelper.getDefaultStateBlockProperty(prop, "doorBlock", woodType.getDoorBlockState());
		Collection<IProperty<? >> x = Blocks.SANDSTONE.getDefaultState().getPropertyKeys();

		this.roomRandomizer = new CQRWeightedRandom<>(this.random);
		int weight = PropertyFileHelper.getIntProperty(prop, "roomWeightAlchemyLab", 1);
		this.roomRandomizer.add(EnumRoomType.ALCHEMY_LAB, weight);
		weight = PropertyFileHelper.getIntProperty(prop, "roomWeightArmory", 1);
		this.roomRandomizer.add(EnumRoomType.ARMORY, weight);
		weight = PropertyFileHelper.getIntProperty(prop, "roomWeightBedroomBasic", 1);
		this.roomRandomizer.add(EnumRoomType.BEDROOM_BASIC, weight);
		weight = PropertyFileHelper.getIntProperty(prop, "roomWeightBedroomFancy", 1);
		this.roomRandomizer.add(EnumRoomType.BEDROOM_FANCY, weight);
		weight = PropertyFileHelper.getIntProperty(prop, "roomWeightKitchen", 1);
		this.roomRandomizer.add(EnumRoomType.KITCHEN, weight);
		weight = PropertyFileHelper.getIntProperty(prop, "roomWeightLibrary", 1);
		this.roomRandomizer.add(EnumRoomType.LIBRARY, weight);
		weight = PropertyFileHelper.getIntProperty(prop, "roomWeightPool", 1);
		this.roomRandomizer.add(EnumRoomType.POOL, weight);
		weight = PropertyFileHelper.getIntProperty(prop, "roomWeightPortal", 1);
		this.roomRandomizer.add(EnumRoomType.PORTAL, weight);

		this.roofTypeRandomizer = new CQRWeightedRandom<>(this.random);
		weight = PropertyFileHelper.getIntProperty(prop, "roofWeightTwoSided", 1);
		this.roofTypeRandomizer.add(RandomCastleConfigOptions.RoofType.TWO_SIDED, weight);
		weight = PropertyFileHelper.getIntProperty(prop, "roofWeightFourSided", 1);
		this.roofTypeRandomizer.add(RandomCastleConfigOptions.RoofType.FOUR_SIDED, weight);

		this.windowTypeRandomizer = new CQRWeightedRandom<>(this.random);
		weight = PropertyFileHelper.getIntProperty(prop, "windowWeightBasicGlass", 1);
		this.windowTypeRandomizer.add(RandomCastleConfigOptions.WindowType.BASIC_GLASS, weight);
		weight = PropertyFileHelper.getIntProperty(prop, "windowWeightCrossGlass", 1);
		this.windowTypeRandomizer.add(RandomCastleConfigOptions.WindowType.CROSS_GLASS, weight);
		weight = PropertyFileHelper.getIntProperty(prop, "windowWeightSquareBars", 1);
		this.windowTypeRandomizer.add(RandomCastleConfigOptions.WindowType.SQUARE_BARS, weight);
		weight = PropertyFileHelper.getIntProperty(prop, "windowWeightOpenSlit", 1);
		this.windowTypeRandomizer.add(RandomCastleConfigOptions.WindowType.OPEN_SLIT, weight);

		this.minSpawnerRolls = PropertyFileHelper.getIntProperty(prop, "minSpawnerRolls", 1);
		this.maxSpawnerRolls = PropertyFileHelper.getIntProperty(prop, "maxSpawnerRolls", 3);
		this.spawnerRollChance = PropertyFileHelper.getIntProperty(prop, "spawnerRollChance", 100);
	}

	@Override
	public void generate(World world, int x, int y, int z) {
		IDungeonGenerator generator = new GeneratorCastle(this);
		generator.generate(world, world.getChunkAt(x >> 4, z >> 4), x, y, z);
	}

	public BlockState getMainBlockState() {
		return this.mainBlock;
	}

	public BlockState getFancyBlockState() {
		return fancyBlock;
	}

	public BlockState getSlabBlockState() {
		return slabBlock;
	}

	public BlockState getStairBlockState() {
		return stairBlock;
	}

	public BlockState getFloorBlockState() {
		return this.floorBlock;
	}

	public BlockState getRoofBlockState() {
		return this.roofBlock;
	}

	public BlockState getFenceBlockState() {
		return fenceBlock;
	}

	public BlockState getWoodStairBlockState() {
		return this.woodStairBlock;
	}

	public BlockState getWoodSlabBlockState() {
		return woodSlabBlock;
	}

	public BlockState getPlankBlockState() {
		return plankBlock;
	}

	public BlockState getDoorBlockState() {
		return doorBlock;
	}

	public int getMaxSize() {
		return this.maxSize;
	}

	public int getRoomSize() {
		return this.roomSize;
	}

	public int getFloorHeight() {
		return this.floorHeight;
	}

	public Random getRandom() {
		return this.random;
	}

	public EnumRoomType getRandomRoom() {
		return this.roomRandomizer.next();
	}

	public RandomCastleConfigOptions.RoofType getRandomRoofType() {
		return this.roofTypeRandomizer.next();
	}

	public RandomCastleConfigOptions.WindowType getRandomWindowType() {
		return this.windowTypeRandomizer.next();
	}

	public int randomizeRoomSpawnerCount() {
		int numRolls;
		int result = 0;
		if (this.minSpawnerRolls >= this.maxSpawnerRolls) {
			numRolls = this.minSpawnerRolls;
		} else {
			numRolls = DungeonGenUtils.getIntBetweenBorders(this.minSpawnerRolls, this.maxSpawnerRolls, this.random);
		}

		for (int i = 0; i < numRolls; i++) {
			if (this.spawnerRollChance > 0) {
				if (DungeonGenUtils.PercentageRandom(this.spawnerRollChance, this.random)) {
					result++;
				}
			}
		}

		return result;
	}
}
