package com.redenexus.season.limit.loader;

import com.redenexus.season.limit.data.Limit;
import com.redenexus.season.limit.manager.LimitManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author oNospher
 **/
public class LimitLoader {

    public static void findAll(FileConfiguration configuration) {
        configuration.getConfigurationSection("settings.limit").getKeys(false).forEach(name -> {
            ConfigurationSection section = configuration.getConfigurationSection("limit." + name);

            double itemLimit = section.getDouble("itemLimit");

            Limit limit = new Limit(
                    name,
                    itemLimit
            );
            LimitManager.getLimits().add(limit);
            System.out.println("[NxSeason] Limit " + name + "loaded.");
        });
    }
}
