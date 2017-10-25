package me.martinitslinda.blockprotect.protocol.v1_10;

import me.martinitslinda.blockprotect.protocol.Protocol;

import net.minecraft.server.v1_10_R1.Block;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.IBlockData;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;

public class Protocol_v1_10_R1 implements Protocol{

    @Override
    public boolean setBlock(final World world, final int x, final int y, final int z, final int blockId, final byte data){

        if (y <= 0 || y > world.getMaxHeight()){ return false; }

        final net.minecraft.server.v1_10_R1.World craftWorld = ((CraftWorld)world).getHandle();
        final net.minecraft.server.v1_10_R1.Chunk craftChunk = craftWorld.getChunkAt(x >> 4, z >> 4);

        final BlockPosition position = new BlockPosition(x, y, z);
        final IBlockData blockData = Block.getByCombinedId(blockId + (data << 12));

        final IBlockData refresh = craftChunk.a(position, blockData);

        return refresh != null;
    }

    @Override
    public void doPhysics(final World world, final int x, final int y, final int z){
        final net.minecraft.server.v1_10_R1.World craftWorld = ((CraftWorld)world).getHandle();
        final net.minecraft.server.v1_10_R1.Chunk craftChunk = craftWorld.getChunkAt(x >> 4, z >> 4);

        final BlockPosition position = new BlockPosition(x, y, z);
        final Block block = craftChunk.getBlockData(position).getBlock();

        craftWorld.update(position, block);
    }

    @Override
    public void doBlockLighting(final World world, final int x, final int y, final int z){

    }

    @Override
    public int getBlockLighting(final int blockId){
        return 0;
    }

}
