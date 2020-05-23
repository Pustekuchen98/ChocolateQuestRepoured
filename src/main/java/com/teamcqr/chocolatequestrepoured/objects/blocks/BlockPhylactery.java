package com.teamcqr.chocolatequestrepoured.objects.blocks;

import java.util.List;
import java.util.Random;

import com.teamcqr.chocolatequestrepoured.objects.entity.boss.EntityCQRLich;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPhylactery extends Block {
	
	protected final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);

	public BlockPhylactery(Material materialIn) {
		super(materialIn);
		this.setHardness(0.5F);
		this.setLightOpacity(8);
		this.setLightLevel(1.5F);
		this.setSoundType(SoundType.GLASS);
		this.setTickRandomly(true);
	}

	public BlockPhylactery(Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
		this.setHardness(0.5F);
		this.setLightOpacity(8);
		this.setLightLevel(1.5F);
		this.setSoundType(SoundType.GLASS);
		this.setTickRandomly(true);
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, BlockState state) {
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
		worldIn.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 1.5F, true);
	}

	@Override
	public boolean isFullCube(BlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(BlockState state) {
		return false;
	}

	@Override
	public Item getItemDropped(BlockState state, Random rand, int fortune) {
		return Items.AIR;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
	}

	@Override
	public EnumBlockRenderType getRenderType(BlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@OnlyIn(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public void randomTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
		super.randomTick(worldIn, pos, state, rand);
		AxisAlignedBB aabb = new AxisAlignedBB(pos.add(3,2,3), pos.add(-3,-2,-3));
		List<EntityCQRLich> lichesInRange = worldIn.getEntitiesWithinAABB(EntityCQRLich.class, aabb);
		if(!lichesInRange.isEmpty()) {
			int i = 0;
			while(i < lichesInRange.size()) {
				EntityCQRLich lich = lichesInRange.get(i);
				if(lich != null && !lich.isDead) {
					if(!lich.hasPhylactery()) {
						lich.setCurrentPhylacteryBlock(pos);
						i = lichesInRange.size();
					} else {
						i++;
					}
				}
			}
		}
	}

}
