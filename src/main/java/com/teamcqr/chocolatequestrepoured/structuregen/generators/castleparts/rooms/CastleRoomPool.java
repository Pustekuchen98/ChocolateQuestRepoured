package com.teamcqr.chocolatequestrepoured.structuregen.generators.castleparts.rooms;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import com.teamcqr.chocolatequestrepoured.objects.factories.GearedMobFactory;
import com.teamcqr.chocolatequestrepoured.structuregen.dungeons.DungeonCastle;
import com.teamcqr.chocolatequestrepoured.util.BlockStateGenArray;
import com.teamcqr.chocolatequestrepoured.util.GenerationTemplate;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class CastleRoomPool extends CastleRoomDecoratedBase
{
    public CastleRoomPool(BlockPos startOffset, int sideLength, int height, int floor) {
        super(startOffset, sideLength, height, floor);
        this.roomType = EnumRoomType.POOL;
        this.maxSlotsUsed = 1;
        this.defaultCeiling = true;
        this.defaultFloor = true;
    }

    @Override
    protected void generateRoom(BlockStateGenArray genArray, DungeonCastle dungeon) {
        int endX = getDecorationLengthX() - 1;
        int endZ = getDecorationLengthZ() - 1;
        Predicate<Vec3i> northRow = (v -> ((v.getY() == 0) && (v.getZ() == 1) && ((v.getX() >= 1) && (v.getX() <= endX - 1))));
        Predicate<Vec3i> southRow = (v -> ((v.getY() == 0) && (v.getZ() == endZ - 1) && ((v.getX() >= 1) && (v.getX() <= endX - 1))));
        Predicate<Vec3i> westRow = (v -> ((v.getY() == 0) && (v.getX() == 1) && ((v.getZ() >= 1) && (v.getZ() <= endZ - 1))));
        Predicate<Vec3i> eastRow = (v -> ((v.getY() == 0) && (v.getX() == endZ - 1) && ((v.getZ() >= 1) && (v.getZ() <= endZ - 1))));
        Predicate<Vec3i> water = (v -> ((v.getY() == 0) && (v.getX() > 1) && (v.getX() < endX - 1) && (v.getZ() > 1) && (v.getZ() < endZ - 1)));

        GenerationTemplate poolRoomTemplate = new GenerationTemplate(getDecorationLengthX(), getDecorationLengthY(), getDecorationLengthZ());
        poolRoomTemplate.addRule(northRow, Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, Direction.SOUTH));
        poolRoomTemplate.addRule(southRow, Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, Direction.NORTH));
        poolRoomTemplate.addRule(westRow, Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, Direction.EAST));
        poolRoomTemplate.addRule(eastRow, Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, Direction.WEST));
        poolRoomTemplate.addRule(water, Blocks.WATER.getDefaultState());

        HashMap<BlockPos, IBlockState> genMap = poolRoomTemplate.GetGenerationMap(getDecorationStartPos(), true);
        genArray.addBlockStateMap(genMap, BlockStateGenArray.GenerationPhase.MAIN);
        for (Map.Entry<BlockPos, IBlockState> entry : genMap.entrySet()) {
            if (entry.getValue().getBlock() != Blocks.AIR) {
                usedDecoPositions.add(entry.getKey());
            }
        }

    }

    @Override
    protected IBlockState getFloorBlock(DungeonCastle dungeon) {
        return dungeon.getMainBlockState();
    }

    @Override
    public void decorate(World world, BlockStateGenArray genArray, DungeonCastle dungeon, GearedMobFactory mobFactory)
    {
        setupDecoration(genArray);
        addWallDecoration(world, genArray, dungeon);
        addSpawners(world, genArray, dungeon, mobFactory);
        fillEmptySpaceWithAir(genArray);
    }

    @Override
    boolean shouldBuildEdgeDecoration() {
        return false;
    }

    @Override
    boolean shouldBuildWallDecoration() {
        return true;
    }

    @Override
    boolean shouldBuildMidDecoration() {
        return false;
    }

    @Override
    boolean shouldAddSpawners() {
        return true;
    }

    @Override
    boolean shouldAddChests() {
        return false;
    }
}
