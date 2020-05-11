package com.redenexus.season.user.data;

import com.redenexus.season.database.data.Parameters;
import com.redenexus.season.user.dao.SeasonUserDAO;
import com.redenexus.season.util.serializer.ItemSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
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

    public void addItem(ItemStack itemStack) throws SQLException {
        items.add(itemStack);

        Parameters<String, String> parameters = new Parameters<>(
                "items",
                ItemSerializer.toBase64List(items)
        );

        new SeasonUserDAO<>()
                .update(
                        parameters,
                        this
                );

    }

    public Integer getLimit() {
        int limit = 0;
        for(int i = limit; i < 100; i++) {
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
