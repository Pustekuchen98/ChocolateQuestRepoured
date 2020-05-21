package com.teamcqr.chocolatequestrepoured.structuregen.dungeons;

import java.io.File;
import java.util.Properties;

import com.teamcqr.chocolatequestrepoured.structuregen.generators.GeneratorOceanFloor;
import com.teamcqr.chocolatequestrepoured.structuregen.generators.IDungeonGenerator;
import com.teamcqr.chocolatequestrepoured.structuregen.structurefile.CQStructure;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.template.PlacementSettings;

/**
 * Copyright (c) 29.04.2019
 * Developed by DerToaster98
 * GitHub: https://github.com/DerToaster98
 */
public class DungeonOceanFloor extends DungeonSurface {

	public DungeonOceanFloor(String name, Properties prop) {
		super(name, prop);
	}

	public void generate(World world, int x, int z) {
		Chunk chunk = world.getChunkFromChunkCoords(x >> 4, z >> 4);
		int y = 0;
		for (int ix = 0; ix < 16; ix++) {
			for (int iz = 0; iz < 16; iz++) {
				y += DungeonGenUtils.getYForPos(world, chunk.x * 16 + ix, chunk.z * 16 + iz, true);
			}
		}
		y /= 256;
		y -= this.getUnderGroundOffset();
		y += this.getYOffset();
		this.generate(world, x, y, z);
	}

	@Override
	public void generate(World world, int x, int y, int z) {
		File file = this.getStructureFileFromDirectory(this.structureFolderPath);

		if (file != null && file.exists() && file.isFile()) {
			CQStructure structure = new CQStructure(file);
			PlacementSettings settings = new PlacementSettings();

			if (this.rotateDungeon()) {
				settings.setRotation(Rotation.values()[this.random.nextInt(Rotation.values().length)]);
				settings.setMirror(Mirror.values()[this.random.nextInt(Mirror.values().length)]);
			}

			IDungeonGenerator generator = new GeneratorOceanFloor(this, structure, settings);
			generator.generate(world, world.getChunkFromChunkCoords(x >> 4, z >> 4), x, y, z);
		}
	}

}
