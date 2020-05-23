package com.teamcqr.chocolatequestrepoured.tileentity;

import java.util.Random;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.objects.entity.bases.AbstractEntityCQR;
import com.teamcqr.chocolatequestrepoured.structuregen.EDungeonMobType;
import com.teamcqr.chocolatequestrepoured.util.CQRConfig;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Difficulty;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntitySpawner extends TileEntitySyncClient implements ITickable {

	public ItemStackHandler inventory = new ItemStackHandler(9);
	private boolean spawnedInDungeon = false;
	private EDungeonMobType mobOverride = null;
	private int dungeonChunkX = 0;
	private int dungeonChunkZ = 0;
	private Mirror mirror = Mirror.NONE;
	private Rotation rot = Rotation.NONE;

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable Direction facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable Direction facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) this.inventory : super.getCapability(capability, facing);
	}

	@Override
	public void readFromNBT(CompoundNBT compound) {
		super.readFromNBT(compound);
		this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		if (compound.hasKey("isDungeonSpawner")) {
			this.spawnedInDungeon = compound.getBoolean("isDungeonSpawner");
		}
		if (compound.hasKey("overrideMob")) {
			this.mobOverride = EDungeonMobType.byString(compound.getString("overrideMob"));
		}
		if (compound.hasKey("dungeonChunkX") && compound.hasKey("dungeonChunkZ")) {
			this.dungeonChunkX = compound.getInteger("dungeonChunkX");
			this.dungeonChunkZ = compound.getInteger("dungeonChunkZ");
		}
		this.mirror = Mirror.values()[compound.getInteger("mirror")];
		this.rot = Rotation.values()[compound.getInteger("rot")];
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT compound) {
		compound = super.writeToNBT(compound);
		compound.setTag("inventory", this.inventory.serializeNBT());
		if (this.spawnedInDungeon) {
			compound.setBoolean("isDungeonSpawner", true);
		}
		if (this.mobOverride != null) {
			compound.setString("overrideMob", this.mobOverride.name());
		}
		if (this.dungeonChunkX != 0 && this.dungeonChunkZ != 0) {
			compound.setInteger("dungeonChunkX", this.dungeonChunkX);
			compound.setInteger("dungeonChunkZ", this.dungeonChunkZ);
		}
		compound.setInteger("mirror", this.mirror.ordinal());
		compound.setInteger("rot", this.rot.ordinal());
		return compound;
	}

	@Nullable
	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(I18n.format("tile.spawner.name"));
	}

	@Override
	public void update() {
		if (!this.world.isRemote && this.world.getDifficulty() != Difficulty.PEACEFUL && this.isNonCreativePlayerInRange(CQRConfig.general.spawnerActivationDistance)) {
			this.turnBackIntoEntity();
		}
	}
	
	public void forceTurnBackIntoEntity() {
		if (!this.world.isRemote && this.world.getDifficulty() != Difficulty.PEACEFUL) {
			this.turnBackIntoEntity();
		}
	}

	public void setInDungeon(int dunChunkX, int dunChunkZ, EDungeonMobType mobOverride) {
		this.spawnedInDungeon = true;
		this.mobOverride = mobOverride;
		this.dungeonChunkX = dunChunkX;
		this.dungeonChunkZ = dunChunkZ;

		this.markDirty();
	}

	protected void turnBackIntoEntity() {
		if (!this.world.isRemote) {
			for (int i = 0; i < this.inventory.getSlots(); i++) {
				ItemStack stack = this.inventory.getStackInSlot(i);

				if (!stack.isEmpty() && stack.getTagCompound() != null) {
					CompoundNBT nbt = stack.getTagCompound().getCompoundTag("EntityIn");

					while (!stack.isEmpty()) {
						this.spawnEntityFromNBT(nbt);
						stack.shrink(1);
					}
				}
			}

			this.world.setBlockToAir(this.pos);
		}
	}

	protected Entity spawnEntityFromNBT(CompoundNBT nbt) {
		{
			// needed because in earlier versions the uuid and pos were not removed when using a soul bottle/mob to spawner on an entity
			nbt.removeTag("UUIDLeast");
			nbt.removeTag("UUIDMost");
			nbt.removeTag("Pos");
			ListNBT passengers = nbt.getTagList("Passengers", 10);
			for (NBTBase passenger : passengers) {
				((CompoundNBT) passenger).removeTag("UUIDLeast");
				((CompoundNBT) passenger).removeTag("UUIDMost");
				((CompoundNBT) passenger).removeTag("Pos");
			}
		}

		if (this.mobOverride != null && nbt.getString("id").equals(Reference.MODID + ":dummy")) {
			if (this.mobOverride == EDungeonMobType.DEFAULT) {
				EDungeonMobType mobType = EDungeonMobType.getMobTypeDependingOnDistance(this.world, this.pos.getX(), this.pos.getZ());
				nbt.setString("id", mobType.getEntityResourceLocation().toString());
			} else {
				nbt.setString("id", this.mobOverride.getEntityResourceLocation().toString());
			}
		}

		Entity entity = EntityList.createEntityFromNBT(nbt, this.world);

		if (entity != null) {
			Random rand = new Random();
			Vec3d pos = new Vec3d(this.pos.getX() + 0.5D, this.pos.getY(), this.pos.getZ() + 0.5D);
			double offset = entity.width < 0.96F ? 0.5D - entity.width * 0.5D : 0.02D;
			pos = pos.addVector(rand.nextDouble() * offset * 2.0D - offset, 0.0D, rand.nextDouble() * offset * 2.0D - offset);
			entity.setPosition(pos.x, pos.y, pos.z);

			if (entity instanceof EntityLiving) {
				if (CQRConfig.general.mobsFromCQSpawnerDontDespawn) {
					((EntityLiving) entity).enablePersistence();
				}

				if (this.spawnedInDungeon && entity instanceof AbstractEntityCQR) {
					((AbstractEntityCQR) entity).onSpawnFromCQRSpawnerInDungeon(new PlacementSettings().setMirror(this.mirror).setRotation(this.rot), this.mobOverride);
				}
			}

			this.world.spawnEntity(entity);

			ListNBT list = nbt.getTagList("Passengers", 10);
			if (!list.hasNoTags()) {
				Entity rider = this.spawnEntityFromNBT(list.getCompoundTagAt(0));
				rider.startRiding(entity);
			}
		}

		return entity;
	}

	protected boolean isNonCreativePlayerInRange(double range) {
		if (range > 0.0D) {
			double d = range * range;
			for (PlayerEntity player : this.world.playerEntities) {
				if (!player.isCreative() && !player.isSpectator() && player.getDistanceSqToCenter(this.pos) < d) {
					return true;
				}
			}
		}
		return false;
	}

	public void setDungeonSpawner() {
		this.spawnedInDungeon = true;
	}

	public void setMirrorAndRotation(Mirror mirror, Rotation rotation) {
		this.mirror = mirror;
		this.rot = rotation;
	}

}
