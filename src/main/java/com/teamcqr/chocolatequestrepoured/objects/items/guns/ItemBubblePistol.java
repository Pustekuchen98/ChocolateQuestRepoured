package com.teamcqr.chocolatequestrepoured.objects.items.guns;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.init.ModSounds;
import com.teamcqr.chocolatequestrepoured.objects.entity.projectiles.ProjectileBubble;
import com.teamcqr.chocolatequestrepoured.util.IRangedWeapon;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemBubblePistol extends Item implements IRangedWeapon {
	
	private final Random rng = new Random();

	public ItemBubblePistol() {
		super();
		setMaxDamage(getMaxUses());
		setMaxStackSize(1);
	}
	
	public int getMaxUses() {
		return 200;
	}
	
	public int getCooldown() {
		return 30;
	}
	
	public double getInaccurary() {
		return 0.5D;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 10;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if(entityLiving instanceof PlayerEntity) {
			((PlayerEntity) entityLiving).getCooldownTracker().setCooldown(this, getCooldown());
		}
		stack.damageItem(1, entityLiving);
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
		stack.damageItem(1, entityLiving);
		if(entityLiving instanceof PlayerEntity) {
			((PlayerEntity) entityLiving).getCooldownTracker().setCooldown(this, getCooldown());
		}
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(entityIn instanceof LivingEntity && ((LivingEntity) entityIn).isHandActive() && ((LivingEntity) entityIn).getActiveItemStack() == stack) {
			shootBubbles((LivingEntity) entityIn);
		}
	}
	
	private void shootBubbles(LivingEntity entity) {
		double x = -Math.sin(Math.toRadians(entity.rotationYaw));
		double z = Math.cos(Math.toRadians(entity.rotationYaw));
		double y = -Math.sin(Math.toRadians(entity.rotationPitch));
		shootBubbles(new Vec3d(x,y,z), entity);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
	
	private void shootBubbles(Vec3d velocity, LivingEntity shooter) {
		Vec3d v = new Vec3d( -getInaccurary() + velocity.x + (2* getInaccurary() * rng.nextDouble()), -getInaccurary() + velocity.y + (2* getInaccurary() * rng.nextDouble()), -getInaccurary() + velocity.z + (2* getInaccurary() * rng.nextDouble()));
		v = v.normalize();
		v = v.scale(1.4);
		
		shooter.playSound(ModSounds.BUBBLE_BUBBLE, 1, 0.75F + (0.5F* shooter.getRNG().nextFloat()));
		
		ProjectileBubble bubble = new ProjectileBubble(shooter.world, shooter);
		bubble.motionX = v.x;
		bubble.motionY = v.y;
		bubble.motionZ = v.z;
		bubble.velocityChanged = true;
		shooter.world.spawnEntity(bubble);
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public SoundEvent getShootSound() {
		//TODO: return bubble sound
		return SoundEvents.ENTITY_BOBBER_THROW;
	}

	@Override
	public void shoot(World world, LivingEntity shooter, Entity target, Hand hand) {
		shootBubbles(shooter);
	}

}
