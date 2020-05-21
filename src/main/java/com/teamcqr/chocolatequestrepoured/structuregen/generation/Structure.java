package com.teamcqr.chocolatequestrepoured.structuregen.generation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegionManager;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class Structure {

	private UUID uuid = MathHelper.getRandomUUID();
	private final World world;
	private final List<List<? extends IStructure>> list = new LinkedList<>();
	private int tick;
	private BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
	private BlockPos.MutableBlockPos minPos = new BlockPos.MutableBlockPos();
	private BlockPos.MutableBlockPos maxPos = new BlockPos.MutableBlockPos();
	private ProtectedRegion protectedRegion;

	public Structure(World world, CompoundNBT compound) {
		this.world = world;
		this.readFromNBT(compound);
	}

	public Structure(World world, BlockPos pos) {
		this.world = world;
		this.pos.setPos(pos);
		this.minPos.setPos(pos);
		this.maxPos.setPos(pos);
	}

	public void addList(List<? extends IStructure> list) {
		if (list != null && !list.isEmpty()) {
			this.list.add(list);
			for (IStructure istructure : list) {
				BlockPos partMinPos = istructure.getPos();
				BlockPos partMaxPos = partMinPos.add(istructure.getSize());
				this.minPos.setPos(DungeonGenUtils.getValidMinPos(this.minPos, partMinPos));
				this.maxPos.setPos(DungeonGenUtils.getValidMaxPos(this.maxPos, partMaxPos));
			}
		} else {
			CQRMain.logger.warn("Tried to add null or an empty list to structure.");
		}
	}

	private IStructure createFromNBT(CompoundNBT compound) {
		String name = compound.getString("id");

		switch (name) {
		case "structurePart":
			return new StructurePart(compound);
		case "extendedBlockStatePart":
			return new ExtendedBlockStatePart(compound);
		case "supportHillPart":
			return new SupportHillPart(compound);
		case "randomBlobPart":
			return new RandomBlobPart(compound);
		case "lightPart":
			return new LightPart(compound);
		case "coverBlockPart":
			return new CoverBlockPart(compound);
		default:
			return null;
		}
	}

	public CompoundNBT writeToNBT() {
		CompoundNBT compound = new CompoundNBT();

		compound.setTag("uuid", NBTUtil.createUUIDTag(this.uuid));
		NBTTagList nbtTagList1 = new NBTTagList();
		for (List<? extends IStructure> partList : this.list) {
			NBTTagList nbtTagList2 = new NBTTagList();
			for (IStructure istructure : partList) {
				nbtTagList2.appendTag(istructure.writeToNBT());
			}
			nbtTagList1.appendTag(nbtTagList2);
		}
		compound.setTag("list", nbtTagList1);
		compound.setTag("protectedRegion", this.protectedRegion != null ? NBTUtil.createUUIDTag(this.protectedRegion.getUuid()) : new CompoundNBT());
		compound.setTag("pos", NBTUtil.createPosTag(this.pos));
		compound.setTag("minPos", NBTUtil.createPosTag(this.minPos));
		compound.setTag("maxPos", NBTUtil.createPosTag(this.maxPos));

		return compound;
	}

	public void readFromNBT(CompoundNBT compound) {
		this.uuid = NBTUtil.getUUIDFromTag(compound.getCompoundTag("uuid"));
		this.list.clear();
		NBTTagList nbtTagList1 = compound.getTagList("list", 9);
		for (int i = 0; i < nbtTagList1.tagCount(); i++) {
			NBTTagList nbtTagList2 = (NBTTagList) nbtTagList1.get(i);
			List<IStructure> partList = new ArrayList<>(nbtTagList2.tagCount());
			for (int j = 0; j < nbtTagList2.tagCount(); j++) {
				IStructure part = this.createFromNBT(nbtTagList2.getCompoundTagAt(j));
				if (part != null) {
					partList.add(part);
				}
			}
			this.list.add(partList);
		}
		this.protectedRegion = ProtectedRegionManager.getInstance(this.world).getProtectedRegion(NBTUtil.getUUIDFromTag(compound.getCompoundTag("protectedRegion")));
		this.pos = new BlockPos.MutableBlockPos(NBTUtil.getPosFromTag(compound.getCompoundTag("pos")));
		this.minPos = new BlockPos.MutableBlockPos(NBTUtil.getPosFromTag(compound.getCompoundTag("minPos")));
		this.maxPos = new BlockPos.MutableBlockPos(NBTUtil.getPosFromTag(compound.getCompoundTag("maxPos")));
	}

	public void tick(World world) {
		this.tick++;

		if (!this.list.isEmpty()) {
			List<? extends IStructure> partList = this.list.get(0);
			List<Integer> toRemove = new ArrayList<>();

			if (this.tick % CQRConfig.advanced.dungeonGenerationFrequencyInLoaded == 0) {
				for (int i = 0; i < partList.size(); i++) {
					IStructure istructure = partList.get(i);

					if (istructure.canGenerate(world)) {
						istructure.generate(world, this.protectedRegion);
						toRemove.add(i);
						if (toRemove.size() == CQRConfig.advanced.dungeonGenerationCountInLoaded) {
							break;
						}
					}
				}
			}

			if (toRemove.isEmpty() && this.tick % CQRConfig.advanced.dungeonGenerationFrequencyInUnloaded == 0) {
				for (int i = 0; i < partList.size(); i++) {
					IStructure istructure = partList.get(i);

					istructure.generate(world, this.protectedRegion);
					toRemove.add(i);
					if (toRemove.size() == CQRConfig.advanced.dungeonGenerationCountInUnloaded) {
						break;
					}
				}
			}

			for (int i = 0; i < toRemove.size(); i++) {
				partList.remove(toRemove.get(i) - i);
			}

			if (partList.isEmpty()) {
				this.list.remove(0);

				if (this.list.size() == 1) {
					for (int x = this.minPos.getX() >> 4; x <= this.maxPos.getX() >> 4; x++) {
						for (int z = this.minPos.getZ() >> 4; z <= this.maxPos.getZ() >> 4; z++) {
							world.getChunkFromChunkCoords(x, z).generateSkylightMap();
						}
					}
				}
			}
		}
	}

	public boolean isGenerated() {
		if (this.list.isEmpty()) {
			if (this.protectedRegion != null) {
				this.protectedRegion.setGenerating(false);
			}
			this.updateLiquids();
			return true;
		}
		return false;
	}

	public void addLightParts() {
		List<LightPart> lightParts = new ArrayList<>();
		int partSize = 24;
		int startX = this.minPos.getX();
		int startY = this.minPos.getY();
		int startZ = this.minPos.getZ();
		int endX = this.maxPos.getX();
		int endY = this.maxPos.getY();
		int endZ = this.maxPos.getZ();
		for (int y = startY; y <= endY; y += partSize) {
			int partSizeY = y + partSize > endY ? endY - y + 1 : partSize;
			for (int x = startX; x <= endX; x += partSize) {
				int partSizeX = x + partSize > endX ? endX - x + 1 : partSize;
				for (int z = startZ; z <= endZ; z += partSize) {
					int partSizeZ = z + partSize > endZ ? endZ - z + 1 : partSize;
					BlockPos startPos = new BlockPos(x, y, z);
					BlockPos endPos = startPos.add(partSizeX - 1, partSizeY - 1, partSizeZ - 1);
					lightParts.add(new LightPart(startPos, endPos));
				}
			}
		}
		this.list.add(lightParts);
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public void setupProtectedRegion(boolean preventBlockBreaking, boolean preventBlockPlacing, boolean preventExplosionsTNT, boolean preventExplosionsOther, boolean preventFireSpreading, boolean preventEntitySpawning, boolean ignoreNoBossOrNexus) {
		this.protectedRegion = new ProtectedRegion(this.world, this.minPos.toImmutable(), this.maxPos.toImmutable());
		this.protectedRegion.setup(preventBlockBreaking, preventBlockPlacing, preventExplosionsTNT, preventExplosionsOther, preventFireSpreading, preventEntitySpawning, ignoreNoBossOrNexus);
		ProtectedRegionManager.getInstance(this.world).addProtectedRegion(this.protectedRegion);
	}

	private void updateLiquids() {
		if (this.world.getWorldType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
			return;
		}
		int startX = this.minPos.getX();
		int startY = this.minPos.getY();
		int startZ = this.minPos.getZ();
		int endX = this.maxPos.getX();
		int endY = this.maxPos.getY();
		int endZ = this.maxPos.getZ();
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		int oldChunkX = startX >> 4;
		int oldChunkY = startY >> 4;
		int oldChunkZ = startZ >> 4;
		Chunk chunk = this.world.getChunkFromChunkCoords(oldChunkX, oldChunkZ);
		ExtendedBlockStorage extendedBlockStorage = chunk.getBlockStorageArray()[oldChunkY];
		for (int x = startX; x <= endX; x++) {
			int chunkX = x >> 4;

			if (chunkX != oldChunkX) {
				oldChunkX = chunkX;
				oldChunkY = startY >> 4;
				oldChunkZ = startZ >> 4;
				chunk = this.world.getChunkFromChunkCoords(chunkX, startZ >> 4);
				extendedBlockStorage = chunk.getBlockStorageArray()[startY >> 4];
			}

			for (int z = startZ; z <= endZ; z++) {
				int chunkZ = z >> 4;

				if (chunkZ != oldChunkZ) {
					oldChunkX = chunkX;
					oldChunkY = startY >> 4;
					oldChunkZ = chunkZ;
					chunk = this.world.getChunkFromChunkCoords(chunkX, chunkZ);
					extendedBlockStorage = chunk.getBlockStorageArray()[startY >> 4];
				}

				BlockPos.MutableBlockPos oldPos = new BlockPos.MutableBlockPos(x, startY == 0 ? 1 : startY - 1, z);
				IBlockState oldState = chunk.getBlockState(oldPos);
				for (int y = startY; y <= endY; y++) {
					int chunkY = y >> 4;

					if (chunkY != oldChunkY) {
						oldChunkY = chunkY;
						extendedBlockStorage = chunk.getBlockStorageArray()[chunkY];
					}

					if (extendedBlockStorage != Chunk.NULL_BLOCK_STORAGE) {
						IBlockState state = extendedBlockStorage.get(x & 15, y & 15, z & 15);

						if (state.getBlock() instanceof BlockLiquid) {
							state.neighborChanged(this.world, pos.setPos(x, y, z), oldState.getBlock(), oldPos);
						}

						oldState = state;
						oldPos.setPos(x, y, z);
					} else {
						y += 15 - (y & 15);
						oldState = Blocks.AIR.getDefaultState();
						oldPos.setPos(x, y, z);
					}
				}
			}
		}
	}

}
