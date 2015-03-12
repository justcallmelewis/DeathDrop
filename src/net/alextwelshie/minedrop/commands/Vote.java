package net.alextwelshie.minedrop.commands;

import net.alextwelshie.minedrop.Main;
import net.alextwelshie.minedrop.ranks.PlayerManager;
import net.alextwelshie.minedrop.voting.VoteHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vote implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length == 0 || args.length > 1) {
				player.sendMessage(Main.getPlugin().prefix + "§eMaps you can vote for:");
				player.sendMessage(Main.getPlugin().prefix + "§31. §a" + VoteHandler.getInstance().maps.get(0) +"§8 - §ecurrently at §b" + VoteHandler.getInstance().getVotes(0) + " §evotes.");
				player.sendMessage(Main.getPlugin().prefix + "§32. §a" + VoteHandler.getInstance().maps.get(1) +"§8 - §ecurrently at §b" + VoteHandler.getInstance().getVotes(1) + " §evotes.");
				player.sendMessage(Main.getPlugin().prefix + "§33. §a" + VoteHandler.getInstance().maps.get(2) +"§8 - §ecurrently at §b" + VoteHandler.getInstance().getVotes(2) + " §evotes.");
				player.sendMessage(Main.getPlugin().prefix + "§34. §a" + VoteHandler.getInstance().maps.get(3) +"§8 - §ecurrently at §b" + VoteHandler.getInstance().getVotes(3) + " §evotes.");
			} else if(args.length == 1) {
				if(Main.getPlugin().voting) {
					if(!VoteHandler.getInstance().voted.contains(player.getName())) {
						VoteHandler.getInstance().voted.add(player.getName());
						if(args[0].equalsIgnoreCase("1")) {
							switch(PlayerManager.getInstance().getRank(player)) {
							case "Regular":
							case "Special":
								break;
							case "Hive":
								break;
							case "Mod":
								break;
							case "Admin":
								break;
							case "Owner":
								break;
							}
							player.sendMessage(Main.getPlugin().prefix + "§6Vote received. Your map now has §b" + VoteHandler.getInstance().getVotes(1) + " §6votes.");
						} else if(args[0].equalsIgnoreCase("2")) {
							switch(PlayerManager.getInstance().getRank(player)) {
							case "Regular":
							case "Special":
								break;
							case "Hive":
								break;
							case "Mod":
								break;
							case "Admin":
								break;
							case "Owner":
								break;
							}
							player.sendMessage(Main.getPlugin().prefix + "§6Vote received. Your map now has §b" + VoteHandler.getInstance().getVotes(2) + " §6votes.");
						} else if(args[0].equalsIgnoreCase("3")) {
							switch(PlayerManager.getInstance().getRank(player)) {
							case "Regular":
							case "Special":
								break;
							case "Hive":
								break;
							case "Mod":
								break;
							case "Admin":
								break;
							case "Owner":
								break;
							}
							player.sendMessage(Main.getPlugin().prefix + "§6Vote received. Your map now has §b" + VoteHandler.getInstance().getVotes(3) + " §6votes.");
						} else if(args[0].equalsIgnoreCase("4")) {
							switch(PlayerManager.getInstance().getRank(player)) {
							case "Regular":
							case "Special":
								break;
							case "Hive":
								break;
							case "Mod":
								break;
							case "Admin":
								break;
							case "Owner":
								break;
							}
							player.sendMessage(Main.getPlugin().prefix + "§6Vote received. Your map now has §b" + VoteHandler.getInstance().getVotes(4) + " §6votes.");
						} else {
							player.sendMessage(Main.getPlugin().prefix + "§cThat is not a valid option.");
						}
					} else {
						player.sendMessage(Main.getPlugin().prefix + "§cYou can only vote once.");
					}
				} else {
					player.sendMessage(Main.getPlugin().prefix + "§cVoting is not active right now!");
				}
			}
		}
		return true;
	}

}
