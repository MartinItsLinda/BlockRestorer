package me.martinitslinda.blockprotect.block;

import org.bukkit.Location;
import org.bukkit.World;

public class Block{

    private final World world;
    private final int blockX;
    private final int blockY;
    private final int blockZ;
    private final int blockId;
    private final byte data;

    public Block(World world, int blockX, int blockY, int blockZ, int blockId){
        this(world, blockX, blockY, blockZ, blockId, (byte) 0);
    }

    public Block(World world, int blockX, int blockY, int blockZ, int blockId, byte data){
        this.world=world;
        this.blockX=blockX;
        this.blockY=blockY;
        this.blockZ=blockZ;
        this.blockId=blockId;
        this.data=data;
    }

    public Location toLocation(){
        return new Location(this.world, this.blockX, this.blockY, this.blockZ);
    }

    public World getWorld(){
        return this.world;
    }

    public int getBlockX(){
        return this.blockX;
    }

    public int getBlockY(){
        return this.blockY;
    }

    public int getBlockZ(){
        return this.blockZ;
    }

    public int getBlockId(){
        return this.blockId;
    }

    public byte getData(){
        return this.data;
    }
}
