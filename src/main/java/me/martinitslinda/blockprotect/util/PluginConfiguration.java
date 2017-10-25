package me.martinitslinda.blockprotect.util;

import com.google.common.collect.Maps;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class PluginConfiguration{

    private final JavaPlugin plugin;
    private final Map<String, Object> values=Maps.newHashMap();

    public PluginConfiguration(final JavaPlugin plugin){
        this.plugin=plugin;
    }

    public void loadConfig(final FileConfiguration configuration){

        final Map<String, Object> values=Maps.newHashMap();

    }

    public JavaPlugin getPlugin(){
        return this.plugin;
    }
}
