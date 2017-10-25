package me.martinitslinda.blockprotect.protocol;

import org.bukkit.World;

public interface Protocol{

    boolean setBlock(final World world, final int x, final int y, final int z, final int blockId, final byte data);

    void doPhysics(final World world, final int x, final int y, final int z);

    void doBlockLighting(final World world, final int x, final int y, final int z);

    int getBlockLighting(final int blockId);

}
