package net.alextwelshie.minedrop.commands;

import net.alextwelshie.minedrop.Main;
import net.alextwelshie.minedrop.ranks.RankHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Map implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (RankHandler.getInstance().isStaff(player)) {
				if (args.length == 0) {
					player.sendMessage("§cIncorrect usage - /map <map>");
				} else {
					Main.getPlugin().forceMap(args[0]);
					player.sendMessage("§6Map §b" + args[0] + " §6will be played this round!");
				}
			} else {
				player.sendMessage("§4Illegal command.");
			}
		}
		return true;
	}

}
