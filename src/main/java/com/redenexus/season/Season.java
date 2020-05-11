package com.redenexus.season;

import com.redenexus.season.manager.StartManager;
import com.redenexus.season.util.reflection.Reflection;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author oNospher
 **/
public class Season extends JavaPlugin {

    @Getter
    private static Season instance;

    private static Boolean collect;

    @Override
    public void onEnable() {
        instance = this;
        collect = getConfig().getBoolean(
                "settings.general.collect"
        );

        new Reflection(this);
        new StartManager();
        saveDefaultConfig();
    }

    public Boolean canCollect() {
        return collect;
    }

}
