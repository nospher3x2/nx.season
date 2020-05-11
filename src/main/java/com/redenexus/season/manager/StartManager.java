package com.redenexus.season.manager;

import com.redenexus.season.Season;
import com.redenexus.season.database.manager.MySQLManager;
import com.redenexus.season.database.runnable.MySQLRefreshRunnable;
import com.redenexus.season.user.dao.SeasonUserDAO;
import com.redenexus.season.util.ClassGetter;
import com.redenexus.season.util.inventory.InventoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.event.Listener;

/**
 * @author oNospher
 **/
public class StartManager {

    public StartManager() {
        new ListenerManager();
        new MySQLManager();
        new CommandManager();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(Season.getInstance(), new MySQLRefreshRunnable(), 0L, 20L * 60);
        new SeasonUserDAO<>().createTable();
    }

}


class ListenerManager {
    /**
     * Registering all listeners on call constructor of this class
     */
    ListenerManager() {
        ClassGetter.getClassesForPackage(Season.getInstance(), "com.redenexus").forEach(clazz -> {
            if (Listener.class.isAssignableFrom(clazz) && !clazz.equals(InventoryBuilder.class)) {
                try {
                    Listener listener = (Listener) clazz.newInstance();
                    Bukkit.getPluginManager().registerEvents(listener, Season.getInstance());
                } catch (InstantiationException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}

class CommandManager {
    /**
     * Registering all commands on call constructor of this class
     */
    CommandManager() {
        ClassGetter.getClassesForPackage(Season.getInstance(), "com.redenexus").forEach(clazz -> {
            if (Command.class.isAssignableFrom(clazz) && !clazz.equals(Command.class)) {
                try {
                    Command command = (Command) clazz.newInstance();

                    ((CraftServer) Bukkit.getServer()).getCommandMap().register(command.getName(), command);
                } catch (InstantiationException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }
}

