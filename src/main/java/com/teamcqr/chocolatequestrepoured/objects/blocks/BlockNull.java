package com.teamcqr.chocolatequestrepoured.objects.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.IProperty;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockNull extends Block {

	private final boolean ignoreSimilarity;
	public static final PropertyBool PASSABLE = PropertyBool.create("passable");

	public BlockNull(boolean ignoreSimilarityIn) {
		super(Material.GLASS);

		this.setSoundType(SoundType.GLASS);
		this.setHardness(2.0F);
		this.setResistance(30.0F);
		this.setHarvestLevel("hand", 0);
		this.setDefaultState(this.blockState.getBaseState().withProperty(PASSABLE, false));
		this.ignoreSimilarity = ignoreSimilarityIn;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, PlayerEntity playerIn, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
		if (playerIn.capabilities.isCreativeMode && playerIn.getHeldItem(Hand.MAIN_HAND).isEmpty()) {
			if (state.getValue(PASSABLE)) {
				worldIn.setBlockState(pos, state.withProperty(PASSABLE, false), 3);
			} else {
				worldIn.setBlockState(pos, state.withProperty(PASSABLE, true), 3);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return blockState.getValue(PASSABLE) ? null : blockState.getBoundingBox(worldIn, pos);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(PASSABLE, (meta & 1) != 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(PASSABLE) ? 1 : 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { PASSABLE });
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@SuppressWarnings("deprecation")
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, Direction side) {
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();

		if (blockState != iblockstate) {
			return true;
		}

		if (block == this) {
			return false;
		}

		return !this.ignoreSimilarity && block == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}

}