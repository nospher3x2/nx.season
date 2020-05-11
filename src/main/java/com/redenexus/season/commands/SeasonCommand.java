package com.redenexus.season.commands;

import com.redenexus.season.Season;
import com.redenexus.season.inventory.SeasonSavedItemsInventory;
import com.redenexus.season.user.data.SeasonUser;
import com.redenexus.season.user.manager.SeasonUserManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.List;

/**
 * @author oNospher
 **/
public class SeasonCommand extends Command {

    final List<String> items_blacklist = Season.getInstance().getConfig().getStringList(
            "settings." +
                    "general." +
                    "items_blacklist"
    );

    public SeasonCommand() {
        super("temporada");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(
                    "§cÉ necessário ser um jogador para executar este comando."
            );
            return true;
        }
        if(args.length != 1) {
            commandSender.sendMessage(
                    "§cUtilize /temporada <importar/resgatar>."
            );
            return true;
        }

        Player player = (Player) commandSender;
        SeasonUser user = SeasonUserManager.find(player.getUniqueId());

        switch (args[0].toLowerCase()) {
            case "importar": {
                if(user.getItems().size() >= user.getLimit()) {
                    commandSender.sendMessage(
                            "§cVocê atingiu o seu limite de itens que irá guardar para próxima temporada."
                    );
                    return true;
                }
                if(Season.getInstance().canCollect()) {
                    commandSender.sendMessage(
                            "§cA nova temporada já foi iniciada, utilize /temporada resgatar."
                    );
                    return true;
                }
                ItemStack itemStack = player.getItemInHand();
                if(itemStack == null || itemStack.getType() == Material.AIR) {
                    commandSender.sendMessage(
                            "§cVocê precisa estar com o item que você quer guardar pra próxima temporada na mão."
                    );
                    return true;
                }
                if(items_blacklist.contains(itemStack.getType().name())) {
                    commandSender.sendMessage(
                            "§cVocê não pode guardar este item para a próxima temporada."
                    );
                    return true;
                }
                commandSender.sendMessage(
                        "§eVocê guardou este item para a próxima temporada, quando a nova temporada começar utilize §7\"/temporada resgatar\"§e."
                );
                try {
                    user.addItem(itemStack);
                    player.getInventory().remove(itemStack);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                return true;
            }
            case "resgatar": {
                SeasonSavedItemsInventory.getInventory(user).open(player);
            }
        }
        return true;
    }
}
