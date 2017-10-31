package me.martinitslinda.blockprotect;

import com.google.common.collect.Lists;
import me.martinitslinda.blockprotect.protocol.Protocol;
import me.martinitslinda.blockprotect.protocol.v1_10.Protocol_v1_10_R1;
import me.martinitslinda.blockprotect.protocol.v1_11.Protocol_v1_11_R1;
import me.martinitslinda.blockprotect.protocol.v1_12.Protocol_v1_12_R1;
import me.martinitslinda.blockprotect.protocol.v1_9.Protocol_v1_9_R2;
import me.martinitslinda.blockprotect.registery.BlockRegistry;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_9_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockProtect extends JavaPlugin{

    private static BlockProtect plugin=null;

    public static void doAsync(final Runnable runnable){
        final BukkitRunnable bukkitRunnable=new BukkitRunnable(){
            @Override
            public void run(){
                try{
                    runnable.run();
                } catch(final Throwable ex){
                    BlockProtect.plugin.getLogger().info("Task #" + this.getTaskId() + " generated an uncaught exception");
                    ex.printStackTrace();
                }
            }
        };
        bukkitRunnable.runTaskAsynchronously(BlockProtect.plugin);
    }

    public static void doAsyncLater(final Runnable runnable, final long ticksLater){
        final BukkitRunnable bukkitRunnable=new BukkitRunnable(){
            @Override
            public void run(){
                try{
                    runnable.run();
                } catch(final Throwable ex){
                    BlockProtect.plugin.getLogger().info("Task #" + this.getTaskId() + " generated an uncaught exception");
                    ex.printStackTrace();
                }
                //Make sure that the task gets cancelled no matter what
                BlockProtect.plugin.cancelTask(this.getTaskId());
            }
        };
        bukkitRunnable.runTaskLaterAsynchronously(BlockProtect.plugin, ticksLater);
    }

    public static void doAsyncRepeating(final Runnable runnable, final long startAfterTicks, final long repeatDelayTicks){
        final BukkitRunnable bukkitRunnable=new BukkitRunnable(){
            @Override
            public void run(){
                try{
                    runnable.run();
                } catch(final Throwable ex){
                    BlockProtect.plugin.getLogger().info("Task #" + this.getTaskId() + " generated an uncaught exception");
                    ex.printStackTrace();
                    BlockProtect.plugin.cancelTask(this.getTaskId());
                }
            }
        };
        BlockProtect.plugin.getRunnables().add(bukkitRunnable);
        bukkitRunnable.runTaskTimerAsynchronously(BlockProtect.plugin, startAfterTicks, repeatDelayTicks);
    }

    public static void doSync(final Runnable runnable){
        final BukkitRunnable bukkitRunnable=new BukkitRunnable(){
            @Override
            public void run(){
                try{
                    runnable.run();
                } catch(final Throwable ex){
                    BlockProtect.plugin.getLogger().info("Task #" + this.getTaskId() + " generated an uncaught exception");
                    ex.printStackTrace();
                }
            }
        };
        bukkitRunnable.runTask(BlockProtect.plugin);
    }

    public static void doSyncLater(final Runnable runnable, final long ticksLater){
        final BukkitRunnable bukkitRunnable=new BukkitRunnable(){
            @Override
            public void run(){
                try{
                    runnable.run();
                } catch(final Throwable ex){
                    BlockProtect.plugin.getLogger().info("Task #" + this.getTaskId() + " generated an uncaught exception");
                    ex.printStackTrace();
                }
                //Make sure that the task gets cancelled no matter what
                BlockProtect.plugin.cancelTask(this.getTaskId());
            }
        };
        BlockProtect.plugin.getRunnables().add(bukkitRunnable);
        bukkitRunnable.runTaskLater(BlockProtect.plugin, ticksLater);
    }

    public static void doSyncRepeating(final Runnable runnable, final long startAfterTicks, final long repeatDelayTicks){
        final BukkitRunnable bukkitRunnable=new BukkitRunnable(){
            @Override
            public void run(){
                try{
                    runnable.run();
                } catch(final Throwable ex){
                    BlockProtect.plugin.getLogger().info("Task #" + this.getTaskId() + " generated an uncaught exception");
                    ex.printStackTrace();
                    BlockProtect.plugin.cancelTask(this.getTaskId());
                }
            }
        };
        BlockProtect.plugin.getRunnables().add(bukkitRunnable);
        bukkitRunnable.runTaskTimer(BlockProtect.plugin, startAfterTicks, repeatDelayTicks);
    }

    private final List<BukkitRunnable> runnables=Lists.newArrayList();
    private BlockRegistry registry;
    private Protocol protocol;

    @Override
    public void onEnable(){

        this.getLogger().info("Performing plugin startup procedure...");

        BlockProtect.plugin=this;

        final String packageName=this.getServer().getClass().getPackage().getName();
        final String version=packageName.substring(packageName.lastIndexOf('.') + 1);

        this.getLogger().info("Attempting to load protocol implementation for MineCraft version \"" + version + "\"");
        switch(version){
            case "v1_9_R2":
                this.protocol=new Protocol_v1_9_R2();
                break;
            case "v1_10_R1":
                this.protocol=new Protocol_v1_10_R1();
                break;
            case "v1_11_R1":
                this.protocol=new Protocol_v1_11_R1();
                break;
            case "v1_12_R1":
                this.protocol=new Protocol_v1_12_R1();
                break;
            default:
                break;
        }

        if(this.protocol == null){
            this.getLogger().info("This servers current MineCraft version is not supported by BlockProtect (Currently running: " + version + ")");
            this.getLogger().info("Currently supported versions include: 1.9, 1.10, 1.11, 1.12");
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        this.getLogger().info("Loaded protocol implementation for MineCraft version \"" + version + "\"");

        this.getLogger().info("Registering events handlers...");

        final PluginManager manager=this.getServer().getPluginManager();

        this.getLogger().info("Events registered!");

        this.getLogger().info("Plugin startup procedure complete!");

    }

    @Override
    public void onDisable(){

        this.getLogger().info("Performing plugin shutdown procedure...");

        BlockProtect.plugin=null;

        this.getLogger().info("Plugin shutdown procedure complete!");

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        Player player = (Player)sender;

        return true;
    }



    public List<BukkitRunnable> getRunnables(){
        return this.runnables;
    }

    public Protocol getProtocol(){
        return this.protocol;
    }

    private void cancelTask(final int taskId){
        this.cancelTask(taskId, false);
    }

    private void cancelTask(final int taskId, final boolean shouldComplete){
        this.runnables.stream().filter(runnable -> runnable.getTaskId() == taskId).findFirst().ifPresent(runnable -> {
            if(shouldComplete){
                runnable.run();
            }
            runnable.cancel();
            this.runnables.remove(runnable);
        });
    }

}
