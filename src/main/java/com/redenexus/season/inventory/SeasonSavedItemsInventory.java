package com.redenexus.season.inventory;

import com.redenexus.season.Season;
import com.redenexus.season.user.data.SeasonUser;
import com.redenexus.season.util.inventory.InventoryBuilder;
import com.redenexus.season.util.inventory.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author oNospher
 **/
public class SeasonSavedItemsInventory {

    public static InventoryBuilder getInventory(SeasonUser user) {
        InventoryBuilder inventory = new InventoryBuilder(6, "Items armazenados");

        user.getItems().forEach(itemStack -> {
            ItemBuilder item = new ItemBuilder(itemStack);
            inventory.addItem(item);
        });

        ItemBuilder collect;

        if(Season.getInstance().canCollect()) {
            collect = new ItemBuilder(
                    Material.MINECART
            ).name(
                    "§aColetar"
            ).lore(
                    "§7Clique aqui para coletar todos os itens que você guardou para a próxima temporada.",
                    "",
                    "§c§l! §fCaso você seu inventário não tenha espaço, ele irá dropar os itens no chão."
            ).setConsumer(event -> {
                Player player = (Player) event.getWhoClicked();
                if(!Season.getInstance().canCollect()) {
                    player.sendMessage(
                            "§cA nova temporada ainda não foi iniciada."
                    );
                    return;
                }
                user.getItems().forEach(itemStack -> {
                    if(player.getInventory().firstEmpty() != 1) {
                        player.getLocation().getWorld().dropItemNaturally(player.getLocation(), itemStack);
                    } else {
                        player.getInventory().addItem(itemStack);
                    }
                });
                player.sendMessage(
                        "§eVocê resgatou todos os itens que você guardou para próxima temporada."
                );
            });
        } else {
            collect = new ItemBuilder(
                    Material.BARRIER
            ).name(
                    "§aVocê não pode coletar"
            ).lore(
                    "§7Você só pode coletar esses items quando a nova temporada começar."
            );
        }

        inventory.setItem((6*9-5), collect);
        inventory.setDesign("XXXXXXXXX", "XOOOOOOOX", "XOOOOOOOX", "XOOOOOOOX", "XOOOOOOOX", "XXXXXXXXX");
        inventory.setCancel(true);
        return inventory;
    }
}
