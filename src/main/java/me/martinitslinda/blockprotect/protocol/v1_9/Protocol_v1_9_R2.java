package me.martinitslinda.blockprotect.protocol.v1_9;

import me.martinitslinda.blockprotect.protocol.Protocol;

import net.minecraft.server.v1_9_R2.Block;
import net.minecraft.server.v1_9_R2.BlockPosition;
import net.minecraft.server.v1_9_R2.IBlockData;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;

public class Protocol_v1_9_R2 implements Protocol{

    @Override
    public boolean setBlock(World world, int x, int y, int z, int blockId, byte data){
        net.minecraft.server.v1_9_R2.World craftWorld = ((CraftWorld)world).getHandle();
        net.minecraft.server.v1_9_R2.Chunk craftChunk = craftWorld.getChunkAt(x >> 4,z >> 4);

        BlockPosition position = new BlockPosition(x, y, z);
        IBlockData blockData = Block.getByCombinedId(blockId + (data << 12));

        IBlockData refresh = craftChunk.a(position, blockData);

        return refresh != null;
    }

    @Override
    public void doPhysics(World world, int x, int y, int z){
        net.minecraft.server.v1_9_R2.World craftWorld = ((CraftWorld)world).getHandle();
        net.minecraft.server.v1_9_R2.Chunk craftChunk = craftWorld.getChunkAt(x >> 4,z >> 4);

        BlockPosition position = new BlockPosition(x, y, z);
        Block block = craftChunk.getBlockData(position).getBlock();

        craftWorld.update(position, block);
    }

    @Override
    public void doBlockLighting(World world, int x, int y, int z){

    }

    @Override
    public int getBlockLighting(int blockId){
        Block block = Block.getById(blockId);
        return block.m(block.getBlockData());
    }

}
