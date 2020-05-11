package com.redenexus.season.inventory;

import com.redenexus.season.Season;
import com.redenexus.season.user.data.SeasonUser;
import com.redenexus.season.util.inventory.InventoryBuilder;
import com.redenexus.season.util.inventory.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.sql.SQLException;

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
                    "§7Clique aqui para coletar todos os itens",
                    "§7que você guardou para a próxima temporada.",
                    "",
                    "§c§l! §fCaso você não tenha espaço no inventário,",
                    "§fos itens serão dropados no chão."
            ).setConsumer(event -> {
                Player player = (Player) event.getWhoClicked();
                if(!Season.getInstance().canCollect()) {
                    player.sendMessage(
                            "§cA nova temporada ainda não foi iniciada."
                    );
                    return;
                }
                user.getItems().forEach(itemStack -> {
                    if(SeasonSavedItemsInventory.checkSpaceInventory(player.getInventory()) <= 0) {
                        player.getLocation().getWorld().dropItemNaturally(player.getLocation(), itemStack);
                    } else {
                        player.getInventory().addItem(itemStack);
                    }
                });
                user.getItems().clear();
                try {
                    user.update();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                player.sendMessage(
                        "§eVocê resgatou todos os itens que você guardou para próxima temporada."
                );
                player.closeInventory();
            });
        } else {
            collect = new ItemBuilder(
                    Material.BARRIER
            ).name(
                    "§aVocê não pode coletar"
            ).lore(
                    "§7Você só pode coletar esses items",
                    "§7quando a nova temporada começar."
            );
        }

        collect.setEditable(false);

        inventory.setDesign("XXXXXXXXX", "XOOOOOOOX", "XOOOOOOOX", "XOOOOOOOX", "XOOOOOOOX", "XXXXXXXXX");
        inventory.setItem((6*9-5), collect);
        inventory.setCancel(true);
        return inventory;
    }

    public static int checkSpaceInventory(Inventory inventory) {
        int slot = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) {
                slot++;
            }
        }
        return slot;
    }
}
