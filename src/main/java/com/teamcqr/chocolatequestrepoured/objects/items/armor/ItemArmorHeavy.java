package com.teamcqr.chocolatequestrepoured.objects.items.armor;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;
import com.teamcqr.chocolatequestrepoured.client.init.ModArmorModels;
import com.teamcqr.chocolatequestrepoured.init.ModMaterials;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorHeavy extends ItemArmor {

	private AttributeModifier movementSpeed;
	private AttributeModifier knockbackResistance;

	public ItemArmorHeavy(ArmorMaterial materialIn, int renderIndexIn, EquipmentSlotType equipmentSlotIn) {
		super(materialIn, renderIndexIn, equipmentSlotIn);

		this.movementSpeed = new AttributeModifier("HeavySpeedModifier", -0.06D, 2);
		this.knockbackResistance = new AttributeModifier("HeavyKnockbackModifier", 0.25D, 0);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

		if (slot == EntityLiving.getSlotForItemStack(stack)) {
			multimap.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), this.knockbackResistance);
			multimap.put(SharedMonsterAttributes.MOVEMENT_SPEED.getName(), this.movementSpeed);
		}

		return multimap;
	}

	@Override
	public void onArmorTick(World world, PlayerEntity player, ItemStack itemStack) {
		player.jumpMovementFactor *= 0.94F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Nullable
	public ModelBiped getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, ModelBiped _default) {
		ArmorMaterial armorMaterial = ((ItemArmor) itemStack.getItem()).getArmorMaterial();

		if (armorMaterial == ModMaterials.ArmorMaterials.ARMOR_HEAVY_DIAMOND) {
			return armorSlot == EquipmentSlotType.LEGS ? ModArmorModels.heavyDiamondArmorLegs : ModArmorModels.heavyDiamondArmor;
		} else if (armorMaterial == ModMaterials.ArmorMaterials.ARMOR_HEAVY_IRON) {
			return armorSlot == EquipmentSlotType.LEGS ? ModArmorModels.heavyIronArmorLegs : ModArmorModels.heavyIronArmor;
		}

		return null;

	}

}
