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

/**
 * @author oNospher
 **/
@RequiredArgsConstructor
@Getter @Setter
public class SeasonUser {

    private final String username;
    private final List<ItemStack> items;

    public void addItem(ItemStack itemStack) throws SQLException {
        items.add(itemStack);
        this.update();
    }

    public void update() throws SQLException {
        Parameters<String, String> parameters = new Parameters<>(
                "items",
                ItemSerializer.toBase64List(this.getItems())
        );

        new SeasonUserDAO<>()
                .update(
                        parameters,
                        this
                );
    }

    public Integer getLimit() {
        int limit = 1;
        for(int i = limit; i <= 27; i++) {
            if(this.getPlayer().hasPermission("nxseason.nospher.limit." + i)) {
                limit = i;
            }
        }
        return limit;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.username);
    }
}
