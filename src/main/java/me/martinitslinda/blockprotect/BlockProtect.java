package me.martinitslinda.blockprotect;

import com.google.common.collect.Lists;

import me.martinitslinda.blockprotect.protocol.Protocol;
import me.martinitslinda.blockprotect.protocol.v1_10.Protocol_v1_10_R1;
import me.martinitslinda.blockprotect.protocol.v1_11.Protocol_v1_11_R1;
import me.martinitslinda.blockprotect.protocol.v1_12.Protocol_v1_12_R1;
import me.martinitslinda.blockprotect.protocol.v1_9.Protocol_v1_9_R2;
import me.martinitslinda.blockprotect.registery.BlockRegistry;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BlockProtect extends JavaPlugin{

    private static BlockProtect plugin=null;

    public static void doAsync(Runnable runnable){
        BukkitRunnable bukkitRunnable=new BukkitRunnable(){
            @Override
            public void run(){
                try{
                    runnable.run();
                } catch(Throwable ex){
                    plugin.getLogger().info("Task #" + getTaskId() + " generated an uncaught exception");
                    ex.getCause().printStackTrace();
                }
            }
        };
        bukkitRunnable.runTaskAsynchronously(plugin);
    }

    public static void doAsyncLater(Runnable runnable, long ticksLater){
        BukkitRunnable bukkitRunnable=new BukkitRunnable(){
            @Override
            public void run(){
                try{
                    runnable.run();
                } catch(Throwable ex){
                    plugin.getLogger().info("Task #" + getTaskId() + " generated an uncaught exception");
                    ex.getCause().printStackTrace();
                }
                //Make sure that the task gets cancelled no matter what
                plugin.cancelTask(this.getTaskId());
            }
        };
        bukkitRunnable.runTaskLaterAsynchronously(plugin, ticksLater);
    }

    public static void doAsyncRepeating(Runnable runnable, long startAfterTicks, long repeatDelayTicks){
        BukkitRunnable bukkitRunnable=new BukkitRunnable(){
            @Override
            public void run(){
                try{
                    runnable.run();
                } catch(Throwable ex){
                    plugin.getLogger().info("Task #" + getTaskId() + " generated an uncaught exception");
                    ex.getCause().printStackTrace();
                    plugin.cancelTask(this.getTaskId());
                }
            }
        };
        plugin.getRunnables().add(bukkitRunnable);
        bukkitRunnable.runTaskTimerAsynchronously(plugin, startAfterTicks, repeatDelayTicks);
    }

    public static void doSync(Runnable runnable){
        BukkitRunnable bukkitRunnable=new BukkitRunnable(){
            @Override
            public void run(){
                try{
                    runnable.run();
                } catch(Throwable ex){
                    plugin.getLogger().info("Task #" + getTaskId() + " generated an uncaught exception");
                    ex.getCause().printStackTrace();
                }
            }
        };
        bukkitRunnable.runTask(plugin);
    }

    public static void doSyncLater(Runnable runnable, long ticksLater){
        BukkitRunnable bukkitRunnable=new BukkitRunnable(){
            @Override
            public void run(){
                try{
                    runnable.run();
                } catch(Throwable ex){
                    plugin.getLogger().info("Task #" + getTaskId() + " generated an uncaught exception");
                    ex.getCause().printStackTrace();
                }
                //Make sure that the task gets cancelled no matter what
                plugin.cancelTask(this.getTaskId());
            }
        };
        plugin.getRunnables().add(bukkitRunnable);
        bukkitRunnable.runTaskLater(plugin, ticksLater);
    }

    public static void doSyncRepeating(Runnable runnable, long startAfterTicks, long repeatDelayTicks){
        BukkitRunnable bukkitRunnable=new BukkitRunnable(){
            @Override
            public void run(){
                try{
                    runnable.run();
                } catch(Throwable ex){
                    plugin.getLogger().info("Task #" + getTaskId() + " generated an uncaught exception");
                    ex.getCause().printStackTrace();
                    plugin.cancelTask(this.getTaskId());
                }
            }
        };
        plugin.getRunnables().add(bukkitRunnable);
        bukkitRunnable.runTaskTimer(plugin, startAfterTicks, repeatDelayTicks);
    }

    private List<BukkitRunnable> runnables=Lists.newArrayList();
    private BlockRegistry registry;
    private Protocol protocol;

    @Override
    public void onEnable(){

        super.getLogger().info("Performing plugin startup procedure...");

        BlockProtect.plugin=this;

        String packageName=super.getServer().getClass().getPackage().getName();
        String version=packageName.substring(packageName.lastIndexOf('.') + 1);

        super.getLogger().info("Attempting to load protocol implementation for MineCraft version \""+version+"\"");
        switch(version){
            case "v1_9_R1":
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
            super.getLogger().info("This servers current MineCraft version is not supported by BlockProtect (Currently running: " + version + ")");
            super.getLogger().info("Currently supported versions include: 1.9, 1.10, 1.11, 1.12");
            super.getPluginLoader().disablePlugin(this);
            return;
        }

        PluginManager manager=super.getServer().getPluginManager();

        super.getLogger().info("Plugin startup procedure complete!");

    }

    @Override
    public void onDisable(){

        super.getLogger().info("Performing plugin shutdown procedure...");

        BlockProtect.plugin=null;

        super.getLogger().info("Plugin shutdown procedure complete!");

    }

    public List<BukkitRunnable> getRunnables(){
        return this.runnables;
    }

    public Protocol getProtocol(){
        return this.protocol;
    }

    private void cancelTask(int taskId){
        this.cancelTask(taskId, false);
    }

    private void cancelTask(int taskId, boolean shouldComplete){
        this.runnables.stream().filter(runnable -> runnable.getTaskId() == taskId).findFirst().ifPresent(runnable -> {
            if(shouldComplete) runnable.run();
            runnable.cancel();
            runnables.remove(runnable);
        });
    }

}
