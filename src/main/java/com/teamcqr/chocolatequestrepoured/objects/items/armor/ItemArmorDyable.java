package com.teamcqr.chocolatequestrepoured.objects.items.armor;

import java.awt.Color;

import com.teamcqr.chocolatequestrepoured.init.ModMaterials;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;

public class ItemArmorDyable extends ArmorItem {

	public ItemArmorDyable(ArmorMaterial materialIn, int renderIndexIn, EquipmentSlotType equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);
	}

	@Override
	public boolean hasOverlay(ItemStack stack) {
		return true;
	}

	/**
	 * Return whether the specified armor ItemStack has a color.
	 */
	@Override
	public boolean hasColor(ItemStack stack) {
		if (this.getArmorMaterial() == ModMaterials.ArmorMaterials.DIAMOND_DYABLE || this.getArmorMaterial() == ModMaterials.ArmorMaterials.IRON_DYABLE) {
			CompoundNBT nbttagcompound = stack.getTagCompound();
			return nbttagcompound != null && nbttagcompound.hasKey("display", Constants.NBT.TAG_COMPOUND) ? nbttagcompound.getCompoundTag("display").hasKey("color", Constants.NBT.TAG_INT) : false;
		} else {
			return super.hasColor(stack);
		}
	}

	/**
	 * Return the color for the specified armor ItemStack.
	 */
	@Override
	public int getColor(ItemStack stack) {
		if (this.getArmorMaterial() == ModMaterials.ArmorMaterials.DIAMOND_DYABLE || this.getArmorMaterial() == ModMaterials.ArmorMaterials.IRON_DYABLE) {
			CompoundNBT nbttagcompound = stack.getTagCompound();

			if (nbttagcompound != null) {
				CompoundNBT nbttagcompound1 = nbttagcompound.getCompoundTag("display");

				if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", Constants.NBT.TAG_INT)) {
					int color = nbttagcompound1.getInteger("color");
					Minecraft mc = Minecraft.getMinecraft();
					if (mc.world != null) {
						if ((color >> 28 & 1) == 1) {
							float j = 1530.0F / (color >> 16 & 255);
							float s = (color >> 8 & 255) / 255.0F;
							float b = (color & 255) / 255.0F;
							return Color.HSBtoRGB((mc.world.getTotalWorldTime() + mc.getRenderPartialTicks()) % j / j, s, b) & 0x00FFFFFF | (color & 0xFF000000);
						} else if ((color >> 24 & 15) > 0) {
							float f = 0.5F + 0.5F * MathHelper.sin((mc.world.getTotalWorldTime() + mc.getRenderPartialTicks()) / 15.0F * (color >> 25 & 15));
							int r = Math.round((color >> 16 & 255) * f);
							int g = Math.round((color >> 8 & 255) * f);
							int b = Math.round((color & 255) * f);
							return r << 16 | g << 8 | b | (color & 0xFF000000);
						}
					}
					return color;
				}
			}

			if (this.getArmorMaterial() == ModMaterials.ArmorMaterials.DIAMOND_DYABLE) {
				return 65535;
			} else {
				return 13421772;
			}
		} else {
			return super.getColor(stack);
		}
	}

	/**
	 * Remove the color from the specified armor ItemStack.
	 */
	@Override
	public void removeColor(ItemStack stack) {
		if (this.getArmorMaterial() == ModMaterials.ArmorMaterials.DIAMOND_DYABLE || this.getArmorMaterial() == ModMaterials.ArmorMaterials.IRON_DYABLE) {
			CompoundNBT nbttagcompound = stack.getTagCompound();

			if (nbttagcompound != null) {
				CompoundNBT nbttagcompound1 = nbttagcompound.getCompoundTag("display");

				if (nbttagcompound1.hasKey("color")) {
					nbttagcompound1.removeTag("color");
				}
			}
		}
	}

	/**
	 * Sets the color of the specified armor ItemStack
	 */
	@Override
	public void setColor(ItemStack stack, int color) {
		if (this.getArmorMaterial() == ModMaterials.ArmorMaterials.DIAMOND_DYABLE || this.getArmorMaterial() == ModMaterials.ArmorMaterials.IRON_DYABLE) {
			CompoundNBT nbttagcompound = stack.getTagCompound();

			if (nbttagcompound == null) {
				nbttagcompound = new CompoundNBT();
				stack.setTagCompound(nbttagcompound);
			}

			CompoundNBT nbttagcompound1 = nbttagcompound.getCompoundTag("display");

			if (!nbttagcompound.hasKey("display", Constants.NBT.TAG_COMPOUND)) {
				nbttagcompound.setTag("display", nbttagcompound1);
			}

			nbttagcompound1.setInteger("color", color);
		} else {
			super.setColor(stack, color);
		}
	}

	public int getPersistentColor(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			return 0;
		}

		return stack.getTagCompound().getCompoundTag("display").getInteger("color");
	}

}
