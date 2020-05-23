package com.teamcqr.chocolatequestrepoured.objects.items.staves;

import java.util.List;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.init.ModSounds;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileCannonBall;
import com.teamcqr.chocolatequestrepoured.util.IRangedWeapon;

import net.java.games.input.Keyboard;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.relauncher.Side;

public class ItemStaffGun extends Item implements IRangedWeapon {

	public ItemStaffGun() {
		this.setMaxDamage(2048);
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		this.shootStaff(worldIn, playerIn, stack, handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	public void shootStaff(World worldIn, PlayerEntity player, ItemStack stack, Hand handIn) {
		worldIn.playSound(player.posX, player.posY, player.posZ, ModSounds.GUN_SHOOT, SoundCategory.MASTER, 4.0F, (1.0F + (itemRand.nextFloat() - itemRand.nextFloat()) * 0.2F) * 0.7F, false);

		if (!worldIn.isRemote) {
			ProjectileCannonBall ball = new ProjectileCannonBall(worldIn, player);
			ball.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 3.5F, 0F);
			worldIn.spawnEntity(ball);
			stack.damageItem(1, player);
			player.getCooldownTracker().setCooldown(stack.getItem(), 20);
		}
	}

	@Override
	@OnlyIn(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.staff_gun.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

	@Override
	public void shoot(World worldIn, LivingEntity shooter, Entity target, Hand handIn) {
		if (!worldIn.isRemote) {
			ProjectileCannonBall ball = new ProjectileCannonBall(worldIn, shooter);
			Vec3d v = target.getPositionVector().subtract(shooter.getPositionVector());
			v = v.normalize();
			v = v.scale(3.5D);
			//ball.setVelocity(v.x, v.y, v.z);
			ball.motionX = v.x;
			ball.motionY = v.y;
			ball.motionZ = v.z;
			ball.velocityChanged = true;
			worldIn.spawnEntity(ball);
		}
	}

	@Override
	public SoundEvent getShootSound() {
		return ModSounds.GUN_SHOOT;
	}

}
