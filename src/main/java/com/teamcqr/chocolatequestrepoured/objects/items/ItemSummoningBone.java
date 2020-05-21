package com.teamcqr.chocolatequestrepoured.objects.items;

import java.util.List;

import javax.annotation.Nullable;

import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntitySummoningCircle;
import com.teamcqr.chocolatequestrepoured.objects.entity.misc.EntitySummoningCircle.ECircleTexture;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntityMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemSummoningBone extends Item {

	public ItemSummoningBone() {
		setMaxDamage(3);
		setMaxStackSize(1);
		setNoRepair();
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 40;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		if(!worldIn.isRemote && spawnEntity((PlayerEntity) entityLiving, worldIn, stack)) {
			stack.damageItem(1, entityLiving);
		}
		if(entityLiving instanceof PlayerEntityMP) {
			((PlayerEntityMP) entityLiving).getCooldownTracker().setCooldown(this, 20);
		}
		if(stack.getItemDamage() >= stack.getMaxDamage()) {
			return ItemStack.EMPTY;
		}
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}
	
	public boolean spawnEntity(PlayerEntity player, World worldIn, ItemStack item) {
		Vec3d start = player.getPositionEyes(1.0F);
		Vec3d end = start.add(player.getLookVec().scale(5.0D));
		RayTraceResult result = worldIn.rayTraceBlocks(start, end);

		if (result != null) {
			if(worldIn.isAirBlock(result.getBlockPos().offset(EnumFacing.UP, 1)) && worldIn.isAirBlock(new BlockPos(result.hitVec).offset(EnumFacing.UP, 2))) {
				//DONE: Spawn circle
				ResourceLocation resLoc = new ResourceLocation(Reference.MODID, "skeleton");
				//Get entity id
				if(item.hasTagCompound() && item.getTagCompound().hasKey("entity_to_summon")) {
					try {
						CompoundNBT tag = item.getTagCompound();//.getCompoundTag("tag");
						resLoc = new ResourceLocation(tag.getString("entity_to_summon"));
						if(!EntityList.isRegistered(resLoc)) {
							resLoc = new ResourceLocation(Reference.MODID, "skeleton");
						}
					} catch(Exception ex) {
						resLoc = new ResourceLocation(Reference.MODID, "skeleton");
					}
				}
				EntitySummoningCircle circle = new EntitySummoningCircle(worldIn, resLoc, 1F, ECircleTexture.METEOR, null, player);
				circle.setSummon(resLoc);
				circle.setNoGravity(false);
				circle.setPosition(result.hitVec.x, result.hitVec.y +1, result.hitVec.z);
				worldIn.spawnEntity(circle);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		super.onPlayerStoppedUsing(stack, worldIn, entityLiving, timeLeft);
		if(entityLiving instanceof PlayerEntity) {
			((PlayerEntity) entityLiving).getCooldownTracker().setCooldown(this, 20);
		}
		//stack.damageItem(1, entityLiving);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if(stack.hasTagCompound() && stack.getTagCompound().hasKey("entity_to_summon")) {
			try {
				CompoundNBT tag = stack.getTagCompound();//.getCompoundTag("tag");
				tooltip.add(TextFormatting.BLUE + I18n.format("description.cursed_bone.name") + " " + this.getEntityName(tag.getString("entity_to_summon")));
			} catch(Exception ex) {
				tooltip.add(TextFormatting.BLUE + I18n.format("description.cursed_bone.name") + "missingNo");
			}
			return;
		}
		tooltip.add(TextFormatting.BLUE + I18n.format("description.cursed_bone.name") + " " + this.getEntityName(Reference.MODID + ":skeleton"));
	}
	private String getEntityName(String registryName) {
		EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(registryName));
		if (entityEntry != null) {
			return I18n.format("entity." + ForgeRegistries.ENTITIES.getValue(new ResourceLocation(registryName)).getName() + ".name");
		}
		return "missingNO";
	}
	
}
