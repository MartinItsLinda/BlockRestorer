package me.martinitslinda.blockprotect.protocol.v1_10;

import me.martinitslinda.blockprotect.protocol.Protocol;

import net.minecraft.server.v1_10_R1.Block;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.IBlockData;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;

public class Protocol_v1_10_R1 implements Protocol{

    @Override
    public boolean setBlock(World world, int x, int y, int z, int blockId, byte data){
        net.minecraft.server.v1_10_R1.World craftWorld = ((CraftWorld)world).getHandle();
        net.minecraft.server.v1_10_R1.Chunk craftChunk = craftWorld.getChunkAt(x >> 4, z >> 4);

        BlockPosition position = new BlockPosition(x, y, z);
        IBlockData blockData = Block.getByCombinedId(blockId + (data << 12));

        IBlockData refresh = craftChunk.a(position, blockData);

        return refresh != null;
    }

    @Override
    public void doPhysics(World world, int x, int y, int z){
        net.minecraft.server.v1_10_R1.World craftWorld = ((CraftWorld)world).getHandle();
        net.minecraft.server.v1_10_R1.Chunk craftChunk = craftWorld.getChunkAt(x >> 4, z >> 4);

        BlockPosition position = new BlockPosition(x, y, z);
        Block block = craftChunk.getBlockData(position).getBlock();

        craftWorld.update(position, block);
    }

    @Override
    public void doBlockLighting(World world, int x, int y, int z){

    }

    @Override
    public int getBlockLighting(int blockId){
        return 0;
    }

}
