package com.redenexus.season.limit.manager;

import com.google.common.collect.Lists;
import com.redenexus.season.limit.data.Limit;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

/**
 * @author oNospher
 **/
public class LimitManager {

    @Getter
    private static List<Limit> limits = Lists.newArrayList();

    public static Limit find(Player player) {
        return limits.stream()
                .filter(Objects::nonNull)
                .filter(limit -> player.hasPermission(
                        "nxseason.nospher." + limit.getName()
                )).findFirst()
                .orElse(null);
    }
}
