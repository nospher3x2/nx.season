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

    public static SeasonUser find(UUID uuid) {
        return users.stream()
                .filter(Objects::nonNull)
                .filter(seasonUser -> seasonUser.getUniqueId().equals(uuid))
                .findFirst()
                .orElse(
                        new SeasonUserDAO<>()
                        .findOne(
                                "uuid",
                                uuid
                        )
                );
    }

    public static SeasonUser toUser(TableRow row) {
        List<ItemStack> items = ItemSerializer.fromBase64List(
                row.getString("items")
        );
        return new SeasonUser(
                UUID.fromString(row.getString("uuid")),
                items
        );
    }
}
