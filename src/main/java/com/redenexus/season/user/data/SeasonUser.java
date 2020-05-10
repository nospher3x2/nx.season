package com.redenexus.season.user.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * @author oNospher
 **/
@RequiredArgsConstructor
@Getter @Setter
public class SeasonUser {

    private final UUID uniqueId;
    private final List<ItemStack> items;

    public Integer getLimit() {
        int limit = 1;
        for(int i = limit; i < 36; i++) {
            if(this.getPlayer().hasPermission("nxseason.nospher.limit." + i)) {
                limit = i;
            }
        }
        return limit;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uniqueId);
    }
}