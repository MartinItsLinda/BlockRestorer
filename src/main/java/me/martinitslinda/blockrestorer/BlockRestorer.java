package me.martinitslinda.blockrestorer;

import org.bukkit.plugin.java.JavaPlugin;

public class BlockRestorer extends JavaPlugin{

    private static BlockRestorer plugin = null;

    @Override
    public void onEnable(){

        BlockRestorer.plugin = this;

    }

    @Override
    public void onDisable(){

        BlockRestorer.plugin = null;

    }
}
