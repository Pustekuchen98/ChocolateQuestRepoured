package com.teamcqr.chocolatequestrepoured.objects.items;

import com.teamcqr.chocolatequestrepoured.init.ModBlocks;
import com.teamcqr.chocolatequestrepoured.objects.factories.SpawnerFactory;
import com.teamcqr.chocolatequestrepoured.tileentity.TileEntitySpawner;

import net.minecraft.block.state.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ItemMobToSpawner extends Item {

	public ItemMobToSpawner() {
		this.setMaxStackSize(1);
	}

	/*@Override
	public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, PlayerEntity player) {
		Block block = world.getBlockState(pos).getBlock();
		return block != ModBlocks.SPAWNER && block != Blocks.MOB_SPAWNER;
	}*/

	@Override
	public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
		if (player.isCreative()) {
			if (!player.world.isRemote && !(entity instanceof MultiPartEntityPart)) {
				SpawnerFactory.placeSpawner(new Entity[] { entity }, false, null, player.world, new BlockPos(entity));
				entity.setDead();
				for (Entity passenger : entity.getPassengers()) {
					passenger.setDead();
				}
				this.spawnAdditions(entity.world, entity.posX, entity.posY + entity.height * 0.5D, entity.posZ);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
		if(state.getBlock() == ModBlocks.SPAWNER && !worldIn.isRemote) {
			TileEntitySpawner spawner = (TileEntitySpawner) worldIn.getTileEntity(pos);
			spawner.forceTurnBackIntoEntity();
		}
		return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
	}

	private void spawnAdditions(World world, double x, double y, double z) {
		if (!world.isRemote) {
			((WorldServer) world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 4, 0.25D, 0.25D, 0.25D, 0.0D);
			((WorldServer) world).spawnParticle(EnumParticleTypes.LAVA, x, y, z, 8, 0.25D, 0.25D, 0.25D, 0.0D);
			world.playSound(null, x, y, z, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 0.8F, 0.6F + itemRand.nextFloat() * 0.2F);
		}
	}

}
