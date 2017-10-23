package me.martinitslinda.blockprotect.registery;

import me.martinitslinda.blockprotect.block.Block;

import java.util.*;

public class BlockRegistry{

    private final Queue<Block> blockQueue;

    public BlockRegistry(){
        this.blockQueue=new ArrayDeque<>();
    }

}
