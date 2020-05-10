package com.redenexus.season.user.manager;

import com.google.common.collect.Lists;
import com.redenexus.season.user.dao.SeasonUserDAO;
import com.redenexus.season.user.data.SeasonUser;
import lombok.Getter;

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
                        .findOne("uuid", uuid)
                );
    }
}
