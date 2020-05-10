package com.redenexus.season.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

/**
 * @author oNospher
 **/
@RequiredArgsConstructor
@Getter
public class SeasonUser {

    private final UUID uniqueId;

    @Setter
    private List<ItemStack> items;
}
