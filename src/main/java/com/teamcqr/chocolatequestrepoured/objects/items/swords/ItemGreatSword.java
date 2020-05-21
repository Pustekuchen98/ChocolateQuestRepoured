package com.teamcqr.chocolatequestrepoured.objects.items.swords;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import net.java.games.input.Keyboard;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGreatSword extends ItemSword {

	private float damage;
	private int cooldown;
	private float attackSpeed;

	public ItemGreatSword(ToolMaterial material, float damage, int cooldown, float attackSpeed) {
		super(material);

		this.damage = damage;
		this.cooldown = cooldown;
		this.attackSpeed = attackSpeed;
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
		Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
		this.replaceModifier(modifiers, SharedMonsterAttributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER, this.attackSpeed);
		return modifiers;
	}

	protected void replaceModifier(Multimap<String, AttributeModifier> modifierMultimap, IAttribute attribute, UUID id, double value) {
		Collection<AttributeModifier> modifiers = modifierMultimap.get(attribute.getName());
		Optional<AttributeModifier> modifierOptional = modifiers.stream().filter(attributeModifier -> attributeModifier.getID().equals(id)).findFirst();

		if (modifierOptional.isPresent()) {
			AttributeModifier modifier = modifierOptional.get();
			modifiers.remove(modifier);
			modifiers.add(new AttributeModifier(modifier.getID(), modifier.getName(), modifier.getAmount() + value, modifier.getOperation()));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.great_sword.name"));
		} else {
			tooltip.add(TextFormatting.BLUE + I18n.format("description.click_shift.name"));
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		float range = 3F;
		double mx = entityLiving.posX - range;
		double my = entityLiving.posY - range;
		double mz = entityLiving.posZ - range;
		double max = entityLiving.posX + range;
		double may = entityLiving.posY + range;
		double maz = entityLiving.posZ + range;

		AxisAlignedBB bb = new AxisAlignedBB(mx, my, mz, max, may, maz);

		List<EntityLiving> entitiesInAABB = worldIn.getEntitiesWithinAABB(EntityLiving.class, bb);

		for (int i = 0; i < entitiesInAABB.size(); i++) {
			EntityLiving entityInAABB = entitiesInAABB.get(i);

			if (this.getMaxItemUseDuration(stack) - timeLeft <= 30) {
				entityInAABB.attackEntityFrom(DamageSource.causeExplosionDamage(entityLiving), this.damage);
			}

			if (this.getMaxItemUseDuration(stack) - timeLeft > 30 && this.getMaxItemUseDuration(stack) - timeLeft <= 60) {
				entityInAABB.attackEntityFrom(DamageSource.causeExplosionDamage(entityLiving), this.damage * 3);
			}

			if (this.getMaxItemUseDuration(stack) - timeLeft > 60) {
				entityInAABB.attackEntityFrom(DamageSource.causeExplosionDamage(entityLiving), this.damage * 4);
			}
		}

		if (entityLiving instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entityLiving;

			float x = (float) -Math.sin(Math.toRadians(player.rotationYaw));
			float z = (float) Math.cos(Math.toRadians(player.rotationYaw));
			float y = (float) -Math.sin(Math.toRadians(player.rotationPitch));
			x *= (1.0F - Math.abs(y));
			z *= (1.0F - Math.abs(y));

			if (player.onGround && this.getMaxItemUseDuration(stack) - timeLeft > 40) {
				player.posY += 0.1D;
				player.motionY += 0.35D;
			}

			player.getCooldownTracker().setCooldown(stack.getItem(), this.cooldown);
			player.swingArm(Hand.MAIN_HAND);
			worldIn.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT, 1.0F, 1.0F, false);
			worldIn.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, player.posX + x, player.posY + y + 1.5D, player.posZ + z, 0D, 0D, 0D);
			stack.damageItem(1, player);
		}
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getHeldItem(handIn);
		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!worldIn.isRemote && entityIn instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entityIn;

			if (player.getHeldItemMainhand() == stack && !player.getHeldItemOffhand().isEmpty()) {
				ItemStack stack1 = player.getHeldItemOffhand();
				player.setHeldItem(Hand.OFF_HAND, ItemStack.EMPTY);

				if (!player.inventory.addItemStackToInventory(stack1)) {
					player.entityDropItem(stack1, 0.0F);
				}
			}
		}
	}

}
