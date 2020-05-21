package com.teamcqr.chocolatequestrepoured.objects.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ItemGoldenFeather extends Item {
	public ItemGoldenFeather() {
		this.setMaxStackSize(1);
		this.setMaxDamage(385);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (isSelected && entityIn instanceof LivingEntity) {
			if (entityIn.fallDistance > 1.5F) {
				stack.damageItem(1, (LivingEntity) entityIn);
				entityIn.fallDistance = 0.0F;

				for (int i = 0; i < 3; i++) {
					worldIn.spawnParticle(EnumParticleTypes.CLOUD, entityIn.posX, entityIn.posY, entityIn.posZ, (itemRand.nextFloat() - 0.5F) / 2.0F, -0.5D, (itemRand.nextFloat() - 0.5F) / 2.0F);
				}
			}
		}
	}
}
