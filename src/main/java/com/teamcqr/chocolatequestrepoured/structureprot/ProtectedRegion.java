package com.teamcqr.chocolatequestrepoured.structureprot;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.teamcqr.chocolatequestrepoured.CQRMain;
import com.teamcqr.chocolatequestrepoured.network.packets.toClient.SPacketSyncProtectedRegions;
import com.teamcqr.chocolatequestrepoured.util.DungeonGenUtils;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class ProtectedRegion {

	private final World world;
	private UUID uuid = MathHelper.getRandomUUID();
	private BlockPos startPos;
	private BlockPos endPos;
	private boolean preventBlockBreaking = false;
	private boolean preventBlockPlacing = false;
	private boolean preventExplosionsTNT = false;
	private boolean preventExplosionsOther = false;
	private boolean preventFireSpreading = false;
	private boolean preventEntitySpawning = false;
	private boolean ignoreNoBossOrNexus = false;
	private boolean isGenerating = true;
	private final Set<UUID> entityDependencies = new HashSet<>();
	private final Set<BlockPos> blockDependencies = new HashSet<>();

	public ProtectedRegion(World world, BlockPos startPos, BlockPos endPos) {
		this.world = world;
		this.startPos = DungeonGenUtils.getValidMinPos(startPos, endPos);
		this.endPos = DungeonGenUtils.getValidMaxPos(startPos, endPos);
	}

	public ProtectedRegion(World world, CompoundNBT compound) {
		this.world = world;
		this.readFromNBT(compound);
	}

	public CompoundNBT writeToNBT() {
		CompoundNBT compound = new CompoundNBT();
		compound.setTag("uuid", NBTUtil.createUUIDTag(this.uuid));
		compound.setTag("startPos", NBTUtil.createPosTag(this.startPos));
		compound.setTag("endPos", NBTUtil.createPosTag(this.endPos));
		compound.setBoolean("preventBlockBreaking", this.preventBlockBreaking);
		compound.setBoolean("preventBlockPlacing", this.preventBlockPlacing);
		compound.setBoolean("preventExplosionsTNT", this.preventExplosionsTNT);
		compound.setBoolean("preventExplosionsOther", this.preventExplosionsOther);
		compound.setBoolean("preventFireSpreading", this.preventFireSpreading);
		compound.setBoolean("preventEntitySpawning", this.preventEntitySpawning);
		compound.setBoolean("ignoreNoBossOrNexus", this.ignoreNoBossOrNexus);
		compound.setBoolean("isGenerating", this.isGenerating);
		ListNBT nbtTagList1 = new ListNBT();
		for (UUID entityUuid : this.entityDependencies) {
			nbtTagList1.appendTag(NBTUtil.createUUIDTag(entityUuid));
		}
		compound.setTag("entityDependencies", nbtTagList1);
		ListNBT nbtTagList2 = new ListNBT();
		for (BlockPos pos : this.blockDependencies) {
			nbtTagList1.appendTag(NBTUtil.createPosTag(pos));
		}
		compound.setTag("blockDependencies", nbtTagList2);
		return compound;
	}

	public void readFromNBT(CompoundNBT compound) {
		this.uuid = NBTUtil.getUUIDFromTag(compound.getCompoundTag("uuid"));
		this.startPos = NBTUtil.getPosFromTag(compound.getCompoundTag("startPos"));
		this.endPos = NBTUtil.getPosFromTag(compound.getCompoundTag("endPos"));
		this.preventBlockBreaking = compound.getBoolean("preventBlockBreaking");
		this.preventBlockPlacing = compound.getBoolean("preventBlockPlacing");
		this.preventExplosionsTNT = compound.getBoolean("preventExplosionsTNT");
		this.preventExplosionsOther = compound.getBoolean("preventExplosionsOther");
		this.preventFireSpreading = compound.getBoolean("preventFireSpreading");
		this.preventEntitySpawning = compound.getBoolean("preventEntitySpawning");
		this.ignoreNoBossOrNexus = compound.getBoolean("ignoreNoBossOrNexus");
		this.isGenerating = compound.getBoolean("isGenerating");
		this.entityDependencies.clear();
		ListNBT nbtTagList1 = compound.getTagList("entityDependencies", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < nbtTagList1.tagCount(); i++) {
			this.entityDependencies.add(NBTUtil.getUUIDFromTag(nbtTagList1.getCompoundTagAt(i)));
		}
		this.blockDependencies.clear();
		ListNBT nbtTagList2 = compound.getTagList("blockDependencies", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < nbtTagList2.tagCount(); i++) {
			this.blockDependencies.add(NBTUtil.getPosFromTag(nbtTagList2.getCompoundTagAt(i)));
		}
	}

	public boolean isInsideProtectedRegion(BlockPos pos) {
		if (pos.getX() < this.startPos.getX()) {
			return false;
		}
		if (pos.getY() < this.startPos.getY()) {
			return false;
		}
		if (pos.getZ() < this.startPos.getZ()) {
			return false;
		}
		if (pos.getX() > this.endPos.getX()) {
			return false;
		}
		if (pos.getY() > this.endPos.getY()) {
			return false;
		}
		if (pos.getZ() > this.endPos.getZ()) {
			return false;
		}
		return true;
	}

	public boolean isValid() {
		return this.isGenerating || !this.entityDependencies.isEmpty() || !this.blockDependencies.isEmpty() || this.ignoreNoBossOrNexus;
	}

	public void setup(boolean preventBlockBreaking, boolean preventBlockPlacing, boolean preventExplosionsTNT, boolean preventExplosionsOther, boolean preventFireSpreading, boolean preventEntitySpawning, boolean ignoreNoBossOrNexus) {
		this.preventBlockBreaking = preventBlockBreaking;
		this.preventBlockPlacing = preventBlockPlacing;
		this.preventExplosionsTNT = preventExplosionsTNT;
		this.preventExplosionsOther = preventExplosionsOther;
		this.preventFireSpreading = preventFireSpreading;
		this.preventEntitySpawning = preventEntitySpawning;
		this.ignoreNoBossOrNexus = ignoreNoBossOrNexus;
	}

	public World getWorld() {
		return this.world;
	}

	public UUID getUuid() {
		return this.uuid;
	}

	public BlockPos getStartPos() {
		return this.startPos;
	}

	public BlockPos getEndPos() {
		return this.endPos;
	}

	public void setPreventBlockBreaking(boolean preventBlockBreaking) {
		this.preventBlockBreaking = preventBlockBreaking;
	}

	public boolean preventBlockBreaking() {
		return this.preventBlockBreaking;
	}

	public void setPreventBlockPlacing(boolean preventBlockPlacing) {
		this.preventBlockPlacing = preventBlockPlacing;
	}

	public boolean preventBlockPlacing() {
		return this.preventBlockPlacing;
	}

	public void setPreventExplosionsTNT(boolean preventExplosionsTNT) {
		this.preventExplosionsTNT = preventExplosionsTNT;
	}

	public boolean preventExplosionsTNT() {
		return this.preventExplosionsTNT;
	}

	public void setPreventExplosionsOther(boolean preventExplosionsOther) {
		this.preventExplosionsOther = preventExplosionsOther;
	}

	public boolean preventExplosionsOther() {
		return this.preventExplosionsOther;
	}

	public void setPreventFireSpreading(boolean preventFireSpreading) {
		this.preventFireSpreading = preventFireSpreading;
	}

	public boolean preventFireSpreading() {
		return this.preventFireSpreading;
	}

	public void setPreventEntitySpawning(boolean preventEntitySpawning) {
		this.preventEntitySpawning = preventEntitySpawning;
	}

	public boolean preventEntitySpawning() {
		return this.preventEntitySpawning;
	}

	public void setIgnoreNoBossOrNexus(boolean ignoreNoBossOrNexus) {
		this.ignoreNoBossOrNexus = ignoreNoBossOrNexus;
	}

	public boolean ignoreNoBossOrNexus() {
		return this.ignoreNoBossOrNexus;
	}

	public void addEntityDependency(UUID uuid) {
		this.entityDependencies.add(uuid);
	}

	public void removeEntityDependency(UUID uuid) {
		this.entityDependencies.remove(uuid);

		if (this.world != null && !this.world.isRemote) {
			// TODO Only send changes to clients
			ProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(this.world);
			List<ProtectedRegion> protectedRegions = protectedRegionManager != null ? protectedRegionManager.getProtectedRegions() : Collections.emptyList();
			CQRMain.NETWORK.sendToDimension(new SPacketSyncProtectedRegions(protectedRegions), this.world.provider.getDimension());
		}

		if (!this.isValid()) {
			ProtectedRegionManager.getInstance(this.world).removeProtectedRegion(this);
		}
	}

	public boolean isEntityDependency(UUID uuid) {
		return this.entityDependencies.contains(uuid);
	}

	public void addBlockDependency(BlockPos pos) {
		this.blockDependencies.add(pos);
	}

	public void removeBlockDependency(BlockPos pos) {
		this.blockDependencies.remove(pos);

		if (this.world != null && !this.world.isRemote) {
			// TODO Only send changes to clients
			ProtectedRegionManager protectedRegionManager = ProtectedRegionManager.getInstance(this.world);
			List<ProtectedRegion> protectedRegions = protectedRegionManager != null ? protectedRegionManager.getProtectedRegions() : Collections.emptyList();
			CQRMain.NETWORK.sendToDimension(new SPacketSyncProtectedRegions(protectedRegions), this.world.provider.getDimension());
		}

		if (!this.isValid()) {
			ProtectedRegionManager.getInstance(this.world).removeProtectedRegion(this);
		}
	}

	public boolean isBlockDependency(BlockPos pos) {
		return this.blockDependencies.contains(pos);
	}

	public void setGenerating(boolean isGenerating) {
		this.isGenerating = isGenerating;
	}

	public boolean isGenerating() {
		return this.isGenerating;
	}

}
