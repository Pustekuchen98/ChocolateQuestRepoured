package com.teamcqr.chocolatequestrepoured.objects.blocks;

import java.util.Random;

import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegion;
import com.teamcqr.chocolatequestrepoured.structureprot.ProtectedRegionManager;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.FireBlock;
import net.minecraft.block.SoundType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockFireCQR extends FireBlock {

	public BlockFireCQR() {
		this.setRegistryName("fire");
		this.setHardness(0.0F);
		this.setLightLevel(1.0F);
		this.setSoundType(SoundType.CLOTH);
		this.setUnlocalizedName("fire");
		this.disableStats();
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
		if (worldIn.getGameRules().getBoolean("doFireTick")) {
			if (!worldIn.isAreaLoaded(pos, 2)) {
				return; // Forge: prevent loading unloaded chunks when spreading fire
			}
			if (!this.canPlaceBlockAt(worldIn, pos)) {
				worldIn.setBlockToAir(pos);
			}

			Block block = worldIn.getBlockState(pos.down()).getBlock();
			boolean flag = block.isFireSource(worldIn, pos.down(), Direction.UP);

			int i = ((Integer) state.getValue(AGE)).intValue();

			if (!flag && worldIn.isRaining() && this.canDie(worldIn, pos) && rand.nextFloat() < 0.2F + (float) i * 0.03F) {
				worldIn.setBlockToAir(pos);
			} else {
				if (i < 15) {
					state = state.withProperty(AGE, Integer.valueOf(i + rand.nextInt(3) / 2));
					worldIn.setBlockState(pos, state, 4);
				}

				worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn) + rand.nextInt(10));

				if (!flag) {
					if (!this.canNeighborCatchFire(worldIn, pos)) {
						if (!worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), Direction.UP) || i > 3) {
							worldIn.setBlockToAir(pos);
						}

						return;
					}

					if (!this.canCatchFire(worldIn, pos.down(), Direction.UP) && i == 15 && rand.nextInt(4) == 0) {
						worldIn.setBlockToAir(pos);
						return;
					}
				}

				boolean flag1 = worldIn.isBlockinHighHumidity(pos);
				int j = 0;

				if (flag1) {
					j = -50;
				}

				if (!this.isPosProtected(worldIn, pos)) {
					this.tryCatchFire(worldIn, pos.east(), 300 + j, rand, i, Direction.WEST);
					this.tryCatchFire(worldIn, pos.west(), 300 + j, rand, i, Direction.EAST);
					this.tryCatchFire(worldIn, pos.down(), 250 + j, rand, i, Direction.UP);
					this.tryCatchFire(worldIn, pos.up(), 250 + j, rand, i, Direction.DOWN);
					this.tryCatchFire(worldIn, pos.north(), 300 + j, rand, i, Direction.SOUTH);
					this.tryCatchFire(worldIn, pos.south(), 300 + j, rand, i, Direction.NORTH);
				}

				for (int k = -1; k <= 1; ++k) {
					for (int l = -1; l <= 1; ++l) {
						for (int i1 = -1; i1 <= 4; ++i1) {
							if (k != 0 || i1 != 0 || l != 0) {
								int j1 = 100;

								if (i1 > 1) {
									j1 += (i1 - 1) * 100;
								}

								BlockPos blockpos = pos.add(k, i1, l);
								int k1 = this.getNeighborEncouragement(worldIn, blockpos);

								if (k1 > 0) {
									int l1 = (k1 + 40 + worldIn.getDifficulty().getDifficultyId() * 7) / (i + 30);

									if (flag1) {
										l1 /= 2;
									}

									if (l1 > 0 && rand.nextInt(j1) <= l1 && (!worldIn.isRaining() || !this.canDie(worldIn, blockpos))) {
										int i2 = i + rand.nextInt(5) / 4;

										if (i2 > 15) {
											i2 = 15;
										}

										worldIn.setBlockState(blockpos, state.withProperty(AGE, Integer.valueOf(i2)), 3);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void tryCatchFire(World worldIn, BlockPos pos, int chance, Random random, int age, Direction face) {
		if (this.isPosProtected(worldIn, pos)) {
			return;
		}
		int i = worldIn.getBlockState(pos).getBlock().getFlammability(worldIn, pos, face);

		if (random.nextInt(chance) < i) {
			BlockState iblockstate = worldIn.getBlockState(pos);

			if (random.nextInt(age + 10) < 5 && !worldIn.isRainingAt(pos)) {
				int j = age + random.nextInt(5) / 4;

				if (j > 15) {
					j = 15;
				}

				worldIn.setBlockState(pos, this.getDefaultState().withProperty(AGE, Integer.valueOf(j)), 3);
			} else {
				worldIn.setBlockToAir(pos);
			}

			if (iblockstate.getBlock() == Blocks.TNT) {
				Blocks.TNT.onBlockDestroyedByPlayer(worldIn, pos, iblockstate.withProperty(BlockTNT.EXPLODE, Boolean.valueOf(true)));
			}
		}
	}

	private boolean canNeighborCatchFire(World worldIn, BlockPos pos) {
		for (Direction enumfacing : Direction.values()) {
			if (this.canCatchFire(worldIn, pos.offset(enumfacing), enumfacing.getOpposite())) {
				return true;
			}
		}

		return false;
	}

	private int getNeighborEncouragement(World worldIn, BlockPos pos) {
		if (!worldIn.isAirBlock(pos)) {
			return 0;
		} else {
			int i = 0;

			for (Direction enumfacing : Direction.values()) {
				i = Math.max(worldIn.getBlockState(pos.offset(enumfacing)).getBlock().getFireSpreadSpeed(worldIn, pos.offset(enumfacing), enumfacing.getOpposite()), i);
			}

			return i;
		}
	}

	public boolean isPosProtected(World world, BlockPos pos) {
		ProtectedRegionManager manager = ProtectedRegionManager.getInstance(world);
		if (manager != null) {
			for (ProtectedRegion protectedRegion : manager.getProtectedRegions()) {
				if (protectedRegion.preventFireSpreading() && protectedRegion.isInsideProtectedRegion(pos)) {
					return true;
				}
			}
		}
		return false;
	}

}
