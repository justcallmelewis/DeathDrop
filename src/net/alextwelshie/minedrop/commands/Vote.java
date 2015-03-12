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
			if(Main.getPlugin().voting) {
			if(args.length == 0 || args.length > 1) {
				player.sendMessage("§6You can vote like this - /vote #.");
				player.sendMessage(Main.getPlugin().prefix + "§6Maps choices up for voting:");
				player.sendMessage(Main.getPlugin().prefix + "§3§l1. §b" + VoteHandler.getInstance().maps.get(0) +"§8 - §a" + VoteHandler.getInstance().getVotes(0) + " §6votes.");
				player.sendMessage(Main.getPlugin().prefix + "§3§l2. §b" + VoteHandler.getInstance().maps.get(1) +"§8 - §a" + VoteHandler.getInstance().getVotes(1) + " §6votes.");
				player.sendMessage(Main.getPlugin().prefix + "§3§l3. §b" + VoteHandler.getInstance().maps.get(2) +"§8 - §a" + VoteHandler.getInstance().getVotes(2) + " §6votes.");
				player.sendMessage(Main.getPlugin().prefix + "§3§l4. §b" + VoteHandler.getInstance().maps.get(3) +"§8 - §a" + VoteHandler.getInstance().getVotes(3) + " §6votes.");
			} else if(args.length == 1) {
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
