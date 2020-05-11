package com.redenexus.season.user.manager;

import com.google.common.collect.Lists;
import com.redenexus.season.database.table.TableRow;
import com.redenexus.season.user.dao.SeasonUserDAO;
import com.redenexus.season.user.data.SeasonUser;
import com.redenexus.season.util.serializer.ItemSerializer;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author oNospher
 **/
public class SeasonUserManager {

    @Getter
    private static List<SeasonUser> users = Lists.newArrayList();

    public static SeasonUser find(String username) {
        return users
                .stream()
                .filter(Objects::nonNull)
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(new SeasonUserDAO<>().find("username", username));
    }

    public static SeasonUser toUser(TableRow row) {
        String serializedItems = row.getString("items");
        List<ItemStack> items = serializedItems.isEmpty() ? Lists.newArrayList() : ItemSerializer.fromBase64List(serializedItems);

        return new SeasonUser(
                row.getString("username"),
                items
        );
    }
}
