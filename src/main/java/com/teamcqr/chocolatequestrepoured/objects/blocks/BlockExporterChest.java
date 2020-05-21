package com.teamcqr.chocolatequestrepoured.objects.blocks;

import java.util.HashSet;
import java.util.Set;

import com.teamcqr.chocolatequestrepoured.tileentity.TileEntityExporterChest;
import com.teamcqr.chocolatequestrepoured.util.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

public class BlockExporterChest extends BlockHorizontal {

	public static final Set<BlockExporterChest> EXPORTER_CHESTS = new HashSet<BlockExporterChest>();

	public final ResourceLocation lootTable;
	public final ResourceLocation itemTexture;

	protected static final AxisAlignedBB NORTH_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0D, 0.9375D, 0.875D, 0.9375D);
	protected static final AxisAlignedBB SOUTH_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 1.0D);
	protected static final AxisAlignedBB WEST_CHEST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
	protected static final AxisAlignedBB EAST_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 1.0D, 0.875D, 0.9375D);
	protected static final AxisAlignedBB NOT_CONNECTED_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);

	public BlockExporterChest(ResourceLocation lootTable, String resourceName) {
		this(lootTable, new ResourceLocation(resourceName));
	}

	public BlockExporterChest(ResourceLocation lootTable, String resourceDomain, String resourcePath) {
		this(lootTable, new ResourceLocation(resourceDomain, resourcePath));
	}

	public BlockExporterChest(ResourceLocation lootTable, ResourceLocation itemTexture) {
		super(Material.WOOD);
		this.lootTable = lootTable;
		this.itemTexture = itemTexture;
		EXPORTER_CHESTS.add(this);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, Direction.getHorizontal(meta));
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, Direction face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (BlockExporterChest.isChest(source.getBlockState(pos.north()).getBlock())) {
			return NORTH_CHEST_AABB;
		}

		if (BlockExporterChest.isChest(source.getBlockState(pos.south()).getBlock())) {
			return SOUTH_CHEST_AABB;
		}

		if (BlockExporterChest.isChest(source.getBlockState(pos.west()).getBlock())) {
			return WEST_CHEST_AABB;
		}

		if (BlockExporterChest.isChest(source.getBlockState(pos.east()).getBlock())) {
			return EAST_CHEST_AABB;
		}

		return NOT_CONNECTED_AABB;
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		Direction connectedChestDirection = null;
		for (Direction facing : Direction.HORIZONTALS) {
			if (BlockExporterChest.isChest(worldIn.getBlockState(pos.offset(facing)).getBlock())) {
				connectedChestDirection = facing;
				break;
			}
		}

		if (connectedChestDirection != null) {
			IBlockState connectedChestState = worldIn.getBlockState(pos.offset(connectedChestDirection));
			Direction facing = state.getValue(FACING);
			Direction otherFacing = connectedChestState.getValue(FACING);

			if (facing != otherFacing || facing == connectedChestDirection || facing.getOpposite() == connectedChestDirection) {
				if (facing.rotateYCCW() == connectedChestDirection || facing.rotateY() == connectedChestDirection) {
					worldIn.setBlockState(pos.offset(connectedChestDirection), connectedChestState.withProperty(FACING, facing), 3);
				} else if (otherFacing.rotateYCCW() == connectedChestDirection.getOpposite() || otherFacing.rotateY() == connectedChestDirection.getOpposite()) {
					worldIn.setBlockState(pos, state.withProperty(FACING, otherFacing), 3);
				} else {
					worldIn.setBlockState(pos.offset(connectedChestDirection), state.withProperty(FACING, facing.rotateY()), 3);
					worldIn.setBlockState(pos, state.withProperty(FACING, facing.rotateY()), 3);
				}
			}
		}
	};

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return canPlaceChestAt(worldIn, pos);
	}

	private static boolean canPlaceChestAt(World worldIn, BlockPos pos) {
		int i = 0;

		for (Direction facing : Direction.HORIZONTALS) {
			if (isChest(worldIn.getBlockState(pos.offset(facing)).getBlock())) {
				if (isDoubleChest(worldIn, pos, facing)) {
					return false;
				}

				++i;
			}
		}

		return i <= 1;
	}

	private static boolean isChest(Block block) {
		return block instanceof BlockExporterChest || block == Blocks.CHEST;
	}

	private static boolean isDoubleChest(World worldIn, BlockPos pos, Direction facing) {
		if (isChest(worldIn.getBlockState(pos.offset(facing)).getBlock())) {
			BlockPos blockpos = pos.offset(facing).offset(facing.rotateYCCW());
			BlockPos blockpos1 = pos.offset(facing).offset(facing);
			BlockPos blockpos2 = pos.offset(facing).offset(facing.rotateY());

			if (isChest(worldIn.getBlockState(blockpos).getBlock())) {
				return true;
			}

			if (isChest(worldIn.getBlockState(blockpos1).getBlock())) {
				return true;
			}

			if (isChest(worldIn.getBlockState(blockpos2).getBlock())) {
				return true;
			}
		}

		return false;
	}

	@EventBusSubscriber(modid = Reference.MODID)
	private static class EventHandler {
		@SubscribeEvent
		public static void onPlaceBlockEvent(BlockEvent.EntityPlaceEvent event) {
			if (event.getPlacedBlock().getBlock() == Blocks.CHEST && !canPlaceChestAt(event.getWorld(), event.getPos())) {
				event.setCanceled(true);
			}
		}
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityExporterChest();
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

}
