package com.teamcqr.chocolatequestrepoured.crafting;

import com.teamcqr.chocolatequestrepoured.objects.items.armor.ItemArmorDyable;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeArmorDyableBreathing extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

	public RecipeArmorDyableBreathing() {
		this.setRegistryName(Reference.MODID, "breathing");
	}

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		ItemStack itemstack = ItemStack.EMPTY;
		int glowStoneDustCount = 0;

		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack itemstack1 = inv.getStackInSlot(i);

			if (itemstack1.getItem() instanceof ItemArmorDyable) {
				itemstack = itemstack1;
			} else if (itemstack1.getItem() == Items.GLOWSTONE_DUST) {
				glowStoneDustCount++;
			}
		}

		return !itemstack.isEmpty() && glowStoneDustCount != 0;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack itemstack = ItemStack.EMPTY;
		int glowStoneDustCount = 0;

		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack itemstack1 = inv.getStackInSlot(i);

			if (itemstack1.getItem() instanceof ItemArmorDyable) {
				itemstack = itemstack1;
			} else if (itemstack1.getItem() == Items.GLOWSTONE_DUST) {
				glowStoneDustCount++;
			}
		}

		ItemStack copy = itemstack.copy();
		ItemArmorDyable item = (ItemArmorDyable) itemstack.getItem();
		item.setColor(copy, (glowStoneDustCount << 24) | (0x00FFFFFF & item.getPersistentColor(itemstack)));
		return copy;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}

}
