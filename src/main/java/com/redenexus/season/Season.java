package com.redenexus.season;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author oNospher
 **/
public class Season extends JavaPlugin {

    @Getter
    private static Season instance;

    @Override
    public void onEnable() {
        instance = this;
    }

}
