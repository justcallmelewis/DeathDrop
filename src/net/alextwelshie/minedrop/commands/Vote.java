package net.alextwelshie.minedrop.commands;

import net.alextwelshie.minedrop.Main;
import net.alextwelshie.minedrop.SettingsManager;
import net.alextwelshie.minedrop.ranks.PlayerManager;
import net.alextwelshie.minedrop.voting.VoteHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vote implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (Main.getPlugin().voting) {
				if (args.length == 0 || args.length > 1) {
					player.sendMessage(Main.getPlugin().prefix + "§6You can vote like this - /vote #.");
					player.sendMessage(Main.getPlugin().prefix + "§6Maps choices up for voting:");
					player.sendMessage(Main.getPlugin().prefix
							+ "§3§l1. §b"
							+ SettingsManager.getInstance().getData()
									.getString(VoteHandler.getInstance().maps.get(0) + ".displayName") + "§8 - §a"
							+ VoteHandler.getInstance().getVotes(0) + " §6votes.");
					player.sendMessage(Main.getPlugin().prefix
							+ "§3§l2. §b"
							+ SettingsManager.getInstance().getData()
									.getString(VoteHandler.getInstance().maps.get(1) + ".displayName") + "§8 - §a"
							+ VoteHandler.getInstance().getVotes(1) + " §6votes.");
					player.sendMessage(Main.getPlugin().prefix
							+ "§3§l3. §b"
							+ SettingsManager.getInstance().getData()
									.getString(VoteHandler.getInstance().maps.get(2) + ".displayName") + "§8 - §a"
							+ VoteHandler.getInstance().getVotes(2) + " §6votes.");
					player.sendMessage(Main.getPlugin().prefix
							+ "§3§l4. §b"
							+ SettingsManager.getInstance().getData()
									.getString(VoteHandler.getInstance().maps.get(3) + ".displayName") + "§8 - §a"
							+ VoteHandler.getInstance().getVotes(3) + " §6votes.");
				} else if (args.length == 1) {
					if (!VoteHandler.getInstance().voted.contains(player.getName())) {
						VoteHandler.getInstance().voted.add(player.getName());
						if (args[0].equalsIgnoreCase("1")) {
							switch (PlayerManager.getInstance().getRank(player)) {
							case "Regular":
							case "Special":
								VoteHandler.getInstance().addVote(0, 1);
								break;
							case "Hive":
								VoteHandler.getInstance().addVote(0, 2);
								break;
							case "Mod":
								VoteHandler.getInstance().addVote(0, 3);
								break;
							case "Admin":
								VoteHandler.getInstance().addVote(0, 4);
								break;
							case "Owner":
								VoteHandler.getInstance().addVote(0, 5);
								break;
							}
							player.sendMessage(Main.getPlugin().prefix + "§6Vote received. Your map now has §b"
									+ VoteHandler.getInstance().getVotes(0) + " §6votes.");
						} else if (args[0].equalsIgnoreCase("2")) {
							switch (PlayerManager.getInstance().getRank(player)) {
							case "Regular":
							case "Special":
								VoteHandler.getInstance().addVote(1, 1);
								break;
							case "Hive":
								VoteHandler.getInstance().addVote(1, 2);
								break;
							case "Mod":
								VoteHandler.getInstance().addVote(1, 3);
								break;
							case "Admin":
								VoteHandler.getInstance().addVote(1, 4);
								break;
							case "Owner":
								VoteHandler.getInstance().addVote(1, 5);
								break;
							}
							player.sendMessage(Main.getPlugin().prefix + "§6Vote received. Your map now has §b"
									+ VoteHandler.getInstance().getVotes(1) + " §6votes.");
						} else if (args[0].equalsIgnoreCase("3")) {
							switch (PlayerManager.getInstance().getRank(player)) {
							case "Regular":
							case "Special":
								VoteHandler.getInstance().addVote(2, 1);
								break;
							case "Hive":
								VoteHandler.getInstance().addVote(2, 2);
								break;
							case "Mod":
								VoteHandler.getInstance().addVote(2, 3);
								break;
							case "Admin":
								VoteHandler.getInstance().addVote(2, 4);
								break;
							case "Owner":
								VoteHandler.getInstance().addVote(2, 5);
								break;
							}
							player.sendMessage(Main.getPlugin().prefix + "§6Vote received. Your map now has §b"
									+ VoteHandler.getInstance().getVotes(2) + " §6votes.");
						} else if (args[0].equalsIgnoreCase("4")) {
							switch (PlayerManager.getInstance().getRank(player)) {
							case "Regular":
							case "Special":
								VoteHandler.getInstance().addVote(3, 1);
								break;
							case "Hive":
								VoteHandler.getInstance().addVote(3, 2);
								break;
							case "Mod":
								VoteHandler.getInstance().addVote(3, 3);
								break;
							case "Admin":
								VoteHandler.getInstance().addVote(3, 4);
								break;
							case "Owner":
								VoteHandler.getInstance().addVote(3, 5);
								break;
							}
							player.sendMessage(Main.getPlugin().prefix + "§6Vote received. Your map now has §b"
									+ VoteHandler.getInstance().getVotes(3) + " §6votes.");
						} else {
							player.sendMessage(Main.getPlugin().prefix + "§cThat is not a valid option.");
						}
					} else {
						player.sendMessage(Main.getPlugin().prefix + "§cYou can only vote once.");
					}
				}
			} else {
				player.sendMessage(Main.getPlugin().prefix + "§cVoting is not active right now!");
			}
		}
		return true;
	}

}
