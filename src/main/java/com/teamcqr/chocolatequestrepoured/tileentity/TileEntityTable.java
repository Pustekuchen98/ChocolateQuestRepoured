package com.teamcqr.chocolatequestrepoured.tileentity;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityTable extends TileEntitySyncClient {
	private float rotation = 0F;
	public ItemStackHandler inventory = new ItemStackHandler(1) {
		@Override
		protected void onContentsChanged(int slot) {
			if (!TileEntityTable.this.world.isRemote) {
				TileEntityTable.this.markDirty();
			}
		}

		@Override
		public int getSlotLimit(int slot) {
			return 1;
		}
	};

	public void setRotation(PlayerEntity playerIn) {
		int direction = MathHelper.floor((playerIn.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		this.rotation = direction * 90F;
	}

	public float getRotation() {
		return this.rotation;
	}

	@Override
	public ITextComponent getDisplayName() {
		Style style = new Style();
		ITextComponent itemName = new TextComponentString(this.inventory.getStackInSlot(0).getDisplayName());

		if (this.inventory.getStackInSlot(0).hasDisplayName()) {
			return itemName.setStyle(style);
		} else {
			return null;
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newSate) {
		return (oldState.getBlock() != newSate.getBlock());
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return new AxisAlignedBB(this.getPos(), this.getPos().add(1, 2, 1));
	}

	@Override
	public CompoundNBT writeToNBT(CompoundNBT compound) {
		compound.setTag("inventory", this.inventory.serializeNBT());
		compound.setFloat("rotation", this.rotation);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(CompoundNBT compound) {
		this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		this.rotation = compound.getFloat("rotation");
		super.readFromNBT(compound);
	}

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
}