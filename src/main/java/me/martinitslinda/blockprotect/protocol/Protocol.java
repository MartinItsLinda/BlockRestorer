package me.martinitslinda.blockprotect.protocol;

import org.bukkit.World;

public interface Protocol{

    boolean setBlock(World world, int x, int y, int z, int blockId, byte data);

    void doPhysics(World world, int x, int y, int z);

    void doBlockLighting(World world, int x, int y, int z);

    int getBlockLighting(int blockId);

}
