package com.teamcqr.chocolatequestrepoured.dungeongen.Generators.castleparts.rooms;

import com.teamcqr.chocolatequestrepoured.util.BlockPlacement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class CastleRoomSelector
{
    private BlockPos startPos;
    private int floorHeight;
    private int roomSize;
    private int numFloors;
    private int numRoomsX;
    private int numRoomsZ;
    private Random random;
    private CastleRoomGrid roomGrid;

    public CastleRoomSelector(BlockPos startPos, int roomSize, int floorHeight, int numFloors, int numRoomsX, int numRoomsZ, Random random)
    {
        this.startPos = startPos;
        this.floorHeight = floorHeight;
        this.roomSize = roomSize;
        this.numFloors = numFloors;
        this.numRoomsX = numRoomsX;
        this.numRoomsZ = numRoomsZ;
        this.random = random;
        this.roomGrid = new CastleRoomGrid(numFloors, numRoomsX, numRoomsZ, random);
    }

    public void generateRooms(ArrayList<BlockPlacement> blocks)
    {
        for (CastleRoom room : roomGrid.getRoomList())
        {
            room.generate(blocks);
        }
    }

    public void fillRooms()
    {
        boolean vertical = random.nextBoolean();

        for (int floor = 0; floor < numFloors; floor++)
        {
            buildFloorHallway(floor, vertical);
            vertical = !vertical;
        }

        addStairCases();

        for (int floor = 0; floor < numFloors; floor++)
        {
            for (int z = 0; z < numRoomsZ; z++)
            {
                for (int x = 0; x < numRoomsX; x++)
                {
                    if (!roomGrid.isRoomFilled(floor, x, z))
                    {
                        roomGrid.addRoomAt(new CastleRoomKitchen(getRoomStart(floor, x, z), roomSize, floorHeight, getPositionFromIndex(x, z)), floor, x, z);
                    }
                }
            }
        }

        connectRooms();
        placeDoors();
    }

    private void buildFloorHallway(int floor, boolean vertical)
    {
        //fill the hallway - each floor must have at least 1 hallway

        //start at a random z index that isn't already filled
        if (vertical)
        {
            int hallStartX = 0;
            //TODO: Try the other side if this side filled already
            do
            {
                hallStartX = random.nextInt(numRoomsX);
            } while (roomGrid.isRoomFilled(floor, hallStartX,0));

            for (int z = 0; z < numRoomsZ; z++)
            {
                addRoomHallway(floor, hallStartX, z, true);
            }
        } else
        {
            int hallStartZ = 0;
            //TODO: Try the other side if this side filled already
            do
            {
                hallStartZ = random.nextInt(numRoomsZ);
            } while (roomGrid.isRoomFilled(floor, 0, hallStartZ));

            for (int x = 0; x < numRoomsX; x++)
            {
                addRoomHallway(floor, x, hallStartZ, false);
            }
        }
    }

    private void placeDoors()
    {
        for (int floor = 0; (floor < numFloors); floor++)
        {
            for (int z = 0; (z < numRoomsZ); z++)
            {
                for (int x = 0; (x < numRoomsX); x++)
                {
                    CastleRoom room = roomGrid.getRoomAt(floor, x, z);
                    if (room != null && roomIsStaircaseOrLanding(room))
                    {
                        EnumFacing hallDirection = getAdjacentHallwayDirection(floor, x, z);
                        if (hallDirection == EnumFacing.SOUTH || hallDirection == EnumFacing.EAST)
                        {
                            room.addDoorOnSide(hallDirection);
                        }
                        else if (hallDirection == EnumFacing.WEST)
                        {
                            roomGrid.getRoomAt(floor, x - 1, z).addDoorOnSide(EnumFacing.EAST);
                        }
                        else if (hallDirection == EnumFacing.NORTH)
                        {
                            roomGrid.getRoomAt(floor, x, z - 1).addDoorOnSide(EnumFacing.SOUTH);
                        }
                    }
                }
            }
        }
    }

    private boolean roomIsStaircaseOrLanding(CastleRoom room)
    {
        boolean result = false;
        if (room != null)
        {
            CastleRoom.RoomType type = room.getRoomType();
            if (type == CastleRoom.RoomType.STAIRCASE || type == CastleRoom.RoomType.LANDING)
            {
                result = true;
            }
        }

        return result;
    }


    private void addStairCases()
    {
        boolean stairsPlaced = false;
        //only iterate through floors thar aren't the top floor
        for (int floor = 0; (floor < numFloors - 1) && (!stairsPlaced); floor++)
        {
            for (int z = 0; (z < numRoomsZ) && (!stairsPlaced); z++)
            {
                for (int x = 0; (x < numRoomsX) && (!stairsPlaced); x++)
                {
                    if (!roomGrid.isRoomFilled(floor, x, z) &&
                            roomBordersHallway(floor, x, z) &&
                            roomBordersHallway(floor + 1, x, z))
                    {
                        addStairCaseAndLanding(floor, x, z);
                        stairsPlaced = true;
                    }
                }
            }
        }
    }

    private boolean roomBordersHallway(int floor, int x, int z)
    {
        return getAdjacentHallwayDirection(floor, x, z) != EnumFacing.DOWN;
    }

    private EnumFacing getAdjacentHallwayDirection(int floor, int x, int z)
    {
        CastleRoom neighborRoom;
        if (x != 0)
        {
            neighborRoom = roomGrid.getRoomAt(floor, x - 1, z);
            if (neighborRoom != null && neighborRoom.roomType == CastleRoom.RoomType.HALLWAY)
            {
                return EnumFacing.WEST;
            }
        }
        if (z != 0)
        {
            neighborRoom = roomGrid.getRoomAt(floor, x, z - 1);
            if (x != 0 && neighborRoom != null && neighborRoom.roomType == CastleRoom.RoomType.HALLWAY)
            {
                return EnumFacing.NORTH;
            }
        }
        if (x < numRoomsX - 1)
        {
            neighborRoom = roomGrid.getRoomAt(floor, x + 1, z);
            if (neighborRoom != null && neighborRoom.roomType == CastleRoom.RoomType.HALLWAY)
            {
                return EnumFacing.EAST;
            }
        }
        if (z < numRoomsZ - 1)
        {
            neighborRoom = roomGrid.getRoomAt(floor, x, z + 1);
            if (x != 0 && neighborRoom != null && neighborRoom.roomType == CastleRoom.RoomType.HALLWAY)
            {
                return EnumFacing.SOUTH;
            }
        }
        return EnumFacing.DOWN;
    }

    private void connectRooms()
    {
        /*
        ArrayList<RoomSelection> roomList = getUnreachableRoomList();
        //while (!roomList.isEmpty())
        //{
            RoomSelection currentRoom;
            EnumFacing directionToConnect;

            currentRoom = roomList.get(random.nextInt(roomList.size()));
            directionToConnect = getDirectionOfNearestReachable(currentRoom.gridLocation);
            if (directionToConnect == EnumFacing.NORTH)
            {
                currentRoom.gridLocation
            }
            currentRoom.room.addDoorOnSide(directionToConnect);
            currentRoom.reachable = true;

            roomList = getUnreachableRoomList();
        //}
        */
    }

    //Translate the room's (x, z) position in the floor array to a RoomPosition enum
    private CastleRoom.RoomPosition getPositionFromIndex(int x, int z)
    {
        if (x == 0 && z == 0)
        {
            return CastleRoom.RoomPosition.TOP_LEFT;
        }
        else if (x == 0 && z < numRoomsZ - 1)
        {
            return CastleRoom.RoomPosition.MID_LEFT;
        }
        else if (x == 0)
        {
            return CastleRoom.RoomPosition.BOT_LEFT;
        }
        else if (x < numRoomsX - 1 && z == 0)
        {
            return CastleRoom.RoomPosition.TOP_MID;
        }
        else if (x < numRoomsX - 1  && z < numRoomsZ - 1)
        {
            return CastleRoom.RoomPosition.MID;
        }
        else if (x < numRoomsX - 1 )
        {
            return CastleRoom.RoomPosition.BOT_MID;
        }
        else if (z == 0)
        {
            return CastleRoom.RoomPosition.TOP_RIGHT;
        }
        else if (z < numRoomsZ - 1)
        {
            return CastleRoom.RoomPosition.MID_RIGHT;
        }
        else
        {
            return CastleRoom.RoomPosition.BOT_RIGHT;
        }
    }

    private BlockPos getRoomStart(int floor, int x, int z)
    {
        return startPos.add(x * roomSize, floor * floorHeight, z * roomSize);
    }

    private void addRoomHallway(int floor, int x, int z, boolean vertical)
    {
        CastleRoom room = new CastleRoomHallway(getRoomStart(floor, x, z), roomSize, floorHeight, getPositionFromIndex(x, z), vertical);
        roomGrid.addRoomAt(room, floor, x, z);
    }

    private void addStairCaseAndLanding(int stairFloor, int stairX, int stairZ)
    {
        addRoomStaircase(stairFloor, stairX, stairZ);
        addRoomLanding(stairFloor + 1, stairX, stairZ, (CastleRoomStaircase) roomGrid.getRoomAt(stairFloor, stairX, stairZ));
    }

    private void addRoomStaircase(int floor, int x, int z)
    {
        EnumFacing doorSide = getAdjacentHallwayDirection(floor, x, z);
        CastleRoom room = new CastleRoomStaircase(getRoomStart(floor, x, z), roomSize, floorHeight, getPositionFromIndex(x, z), doorSide);
        roomGrid.addRoomAt(room, floor, x, z);
        roomGrid.setRoomReachable(floor, x, z);
    }

    private void addRoomLanding(int floor, int x, int z, CastleRoomStaircase stairsBelow)
    {
        CastleRoom room = new CastleRoomLanding(getRoomStart(floor, x, z), roomSize, floorHeight, getPositionFromIndex(x, z), stairsBelow);
        roomGrid.addRoomAt(room, floor, x, z);
        roomGrid.setRoomReachable(floor, x, z);
    }

    private void addRoomUndirected(CastleRoom.RoomType type, int floor, int x, int z)
    {
        CastleRoom room;
        switch (type)
        {
            case KITCHEN:
                room = new CastleRoomKitchen(getRoomStart(floor, x, z), roomSize, floorHeight, getPositionFromIndex(x, z));
                roomGrid.addRoomAt(room, floor, x, z);
                break;
            default:
                break;
        }
    }

    public int getFloorCount()
    {
        return numFloors;
    }

    public int getRoomCountX()
    {
        return numRoomsX;
    }

    public int getRoomCountZ()
    {
        return numRoomsZ;
    }

    //print the room array in a grid, floor by floor
    public String printGrid()
    {
        String result = "";
        for (int floor = 0; floor < numFloors; floor++)
        {
            result += "\nFloor " + floor + "\n";
            for (int z = 0; z < numRoomsZ; z++)
            {
                for (int x = 0; x < numRoomsX; x++)
                {
                    if (roomGrid.isRoomFilled(floor, x, z))
                    {
                        result += "[" + roomGrid.getRoomAt(floor, x, z).getNameShortened() + "|" + roomGrid.getRoomAt(floor, x, z).getPositionString() + "] ";
                    } else
                    {
                        result += "[NUL|--]";
                    }
                }
                result += "\n";
            }
        }
        result += "----------------\n";
        return result;
    }

}