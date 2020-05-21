package com.teamcqr.chocolatequestrepoured.inventory;

import com.teamcqr.chocolatequestrepoured.objects.items.ItemSoulBottle;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSpawner extends Container {

	private final int numRows = 3;
	private final int numColumns = 3;

	public ContainerSpawner(InventoryPlayer playerInv, TileEntitySpawner tileentity) {
		IItemHandler inventory = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; k++) {
			this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
		}

		for (int l = 0; l < this.numRows; l++) {
			for (int m = 0; m < this.numColumns; m++) {
				this.addSlotToContainer(new SlotItemHandler(inventory, m + l * 3, 62 + m * 18, 17 + l * 18) {
					@Override
					public void onSlotChanged() {
						tileentity.markDirty();
					}

					@Override
					public boolean isItemValid(ItemStack stack) {
						return stack.getItem() instanceof ItemSoulBottle && stack.hasTagCompound() && stack.getTagCompound().hasKey("EntityIn");
					}
				});
			}
		}
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return playerIn.isCreative();
	}

	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			ItemStack itemstack = itemstack1.copy();

			if (index > 35) {
				if (this.mergeItemStack(itemstack1, 0, 36, false)) {
					return itemstack;
				}
			} else {
				if (this.mergeItemStack(itemstack1, 36, this.inventorySlots.size(), false)) {
					return itemstack;
				}
			}
		}

		return ItemStack.EMPTY;
	}

}
