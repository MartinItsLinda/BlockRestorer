package me.martinitslinda.blockprotect.protocol.v1_9;

import me.martinitslinda.blockprotect.protocol.Protocol;

import net.minecraft.server.v1_9_R2.Block;
import net.minecraft.server.v1_9_R2.BlockPosition;
import net.minecraft.server.v1_9_R2.IBlockData;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;

public class Protocol_v1_9_R2 implements Protocol{

    @Override
    public boolean setBlock(final World world, final int x, final int y, final int z, final int blockId, final byte data){

        if (y <= 0 || y > world.getMaxHeight()){ return false; }

        final net.minecraft.server.v1_9_R2.World craftWorld=((CraftWorld) world).getHandle();
        final net.minecraft.server.v1_9_R2.Chunk craftChunk=craftWorld.getChunkAt(x >> 4, z >> 4);

        final BlockPosition position=new BlockPosition(x, y, z);
        final IBlockData blockData=Block.getByCombinedId(blockId + (data << 12));

        final IBlockData refresh=craftChunk.a(position, blockData);

        return refresh != null;
    }

    @Override
    public void doPhysics(final World world, final int x, final int y, final int z){
        final net.minecraft.server.v1_9_R2.World craftWorld=((CraftWorld) world).getHandle();
        final net.minecraft.server.v1_9_R2.Chunk craftChunk=craftWorld.getChunkAt(x >> 4, z >> 4);

        final BlockPosition position=new BlockPosition(x, y, z);
        final Block block=craftChunk.getBlockData(position).getBlock();

        craftWorld.update(position, block);
    }

    @Override
    public void doBlockLighting(final World world, final int x, final int y, final int z){

    }

    @Override
    public int getBlockLighting(int blockId){
        Block block=Block.getById(blockId);
        return block.m(block.getBlockData());
    }

}
