package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.GeneratorGuardedStructure;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;
import com.teamcqr.chocolatequestrepoured.util.PropertyFileHelper;

import net.minecraft.block.Block;
import net.minecraft.world.World;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class DungeonGuardedCastle extends DungeonBase {

	private File structureFolder;
	private File centerStructureFolder;

	private int minBuildings = 7;
	private int maxBuilding = 14;

	private int minDistance = 15;
	private int maxDistance = 30;

	private boolean buildPaths = true;
	private boolean placeInCircle = false;
	private Block pathBlock = Blocks.GRASS_PATH;

	public DungeonGuardedCastle(String name, Properties prop) {
		super(name, prop);

		this.structureFolder = PropertyFileHelper.getFileProperty(prop, "structurefolder", "village_buildings");

		this.centerStructureFolder = PropertyFileHelper.getFileProperty(prop, "centerstructurefolder", "village_centers");
		this.minBuildings = PropertyFileHelper.getIntProperty(prop, "minbuildings", 6);
		this.maxBuilding = PropertyFileHelper.getIntProperty(prop, "maxbuildings", 10);

		this.minDistance = PropertyFileHelper.getIntProperty(prop, "mindistance", 20);
		this.maxDistance = PropertyFileHelper.getIntProperty(prop, "maxdistance", 40);

		this.placeInCircle = PropertyFileHelper.getBooleanProperty(prop, "circle", false);

		this.buildPaths = PropertyFileHelper.getBooleanProperty(prop, "buildroads", true);

		this.pathBlock = PropertyFileHelper.getBlockProperty(prop, "pathblock", Blocks.GRASS_PATH);
	}

	@Override
	public void generate(World world, int x, int y, int z) {
		IDungeonGenerator generator = new GeneratorGuardedStructure(this);

		int buildings = DungeonGenUtils.getIntBetweenBorders(this.minBuildings, this.maxBuilding, this.random);
		((GeneratorGuardedStructure) generator).setCenterStructure(this.getStructureFileFromDirectory(this.centerStructureFolder));
		for (int i = 0; i < buildings; i++) {
			((GeneratorGuardedStructure) generator).addStructure(this.getStructureFileFromDirectory(this.structureFolder));
		}

		generator.generate(world, world.getChunkFromChunkCoords(x >> 4, z >> 4), x, y, z);
	}

	public int getMinDistance() {
		return this.minDistance;
	}

	public int getMaxDistance() {
		return this.maxDistance;
	}

	public boolean buildPaths() {
		return this.buildPaths;
	}

	public boolean placeInCircle() {
		return this.placeInCircle;
	}

	public Block getPathMaterial() {
		return this.pathBlock;
	}

}
