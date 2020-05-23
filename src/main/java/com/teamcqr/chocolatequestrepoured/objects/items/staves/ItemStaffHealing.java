package com.teamcqr.chocolatequestrepoured.objects.items.staves;

import java.util.List;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.init.ModSounds;

import net.java.games.input.Keyboard;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStaffHealing extends Item {

	public static final float HEAL_AMOUNT_ENTITIES = 4.0F;

	public ItemStaffHealing() {
		this.setMaxDamage(128);
		this.setMaxStackSize(1);
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
		if (!player.world.isRemote && entity instanceof LivingEntity && !player.getCooldownTracker().hasCooldown(stack.getItem())) {
			((LivingEntity) entity).heal(HEAL_AMOUNT_ENTITIES);
			entity.setFire(0);
			((WorldServer) player.world).spawnParticle(EnumParticleTypes.HEART, entity.posX, entity.posY + entity.height * 0.5D, entity.posZ, 4, 0.25D, 0.25D, 0.25D, 0.0D);
			player.world.playSound(null, entity.posX, entity.posY + entity.height * 0.5D, entity.posZ, ModSounds.MAGIC, SoundCategory.MASTER, 0.6F, 0.6F + itemRand.nextFloat() * 0.2F);
			stack.damageItem(1, player);
			player.getCooldownTracker().setCooldown(stack.getItem(), 30);
		}
		return true;
	}

	@Override
	@OnlyIn(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.staff_healing.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

}
