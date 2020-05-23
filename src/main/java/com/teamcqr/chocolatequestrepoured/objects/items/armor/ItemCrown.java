package com.teamcqr.chocolatequestrepoured.objects.items.armor;

import java.util.List;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.capability.armor.kingarmor.CapabilityDynamicCrown;
import com.teamcqr.chocolatequestrepoured.capability.armor.kingarmor.CapabilityDynamicCrownProvider;
import com.teamcqr.chocolatequestrepoured.client.init.ModArmorModels;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;

public class ItemCrown extends ItemArmor {

	public ItemCrown(ArmorMaterial materialIn, int renderIndexIn) {
		super(materialIn, renderIndexIn, EquipmentSlotType.HEAD);
	}
	
	@Override
	@OnlyIn(Side.CLIENT)
	public ModelBiped getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, ModelBiped _default) {
		return ModArmorModels.crown;
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		return CapabilityDynamicCrownProvider.createProvider();
	}
	
	@Nullable
	public Item getAttachedItem(ItemStack stack) {
		if(stack.hasCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null)) {
			return stack.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).getAttachedItem();
		}
		return null;
	}
	
	public void attachItem(ItemStack crown, Item toAttach) {
		if(crown.hasCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null)) {
			crown.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).attachItem(toAttach);
		}
	}
	
	public void attachItem(ItemStack crown, ItemStack toAttach) {
		if(crown.hasCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null)) {
			crown.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).attachItem(toAttach);
		}
	}
	
	public void attachItem(ItemStack crown, ResourceLocation toAttach) {
		if(crown.hasCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null)) {
			crown.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null).attachItem(toAttach);
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		CapabilityDynamicCrown capability = stack.getCapability(CapabilityDynamicCrownProvider.DYNAMIC_CROWN, null);
		if(capability.getAttachedItem() != null) {
			tooltip.add("Attached helmet: " + new ItemStack(capability.getAttachedItem(), 1).getDisplayName());
		} 
	}
	
	//TODO: Tooltip that shows the attachment
	//TODO: Stats are affected by attachment

}
