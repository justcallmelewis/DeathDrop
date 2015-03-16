package net.alextwelshie.minedrop.commands;

import net.alextwelshie.minedrop.Main;
import net.alextwelshie.minedrop.SettingsManager;
import net.alextwelshie.minedrop.ranks.PlayerManager;
import net.alextwelshie.minedrop.voting.VoteHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceVote implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (PlayerManager.getInstance().getRank(player).equals("Admin")
					|| PlayerManager.getInstance().getRank(player).equals("Owner")) {
				if (Main.getPlugin().voting) {
					if (args.length == 0 || args.length > 1) {
						player.sendMessage("§e");
					} else if (args.length == 1) {
						if (!VoteHandler.getInstance().voted.contains(player.getName())) {
							VoteHandler.getInstance().voted.add(player.getName());
							if (args[0].equalsIgnoreCase("1")) {
								VoteHandler.getInstance().addVote(0, 9001);
								VoteHandler.getInstance().pickMap();
								Main.getPlugin().displayName = SettingsManager.getInstance().getData()
										.getString(Main.getPlugin().mapName + ".displayName");
								player.sendMessage(Main.getPlugin().prefix + "§6Force voted map. Voting disabled.");
								Main.getPlugin().voting = false;
								Main.getPlugin().forcevoted = true;
							} else if (args[0].equalsIgnoreCase("2")) {
								VoteHandler.getInstance().addVote(1, 9001);
								VoteHandler.getInstance().pickMap();
								Main.getPlugin().displayName = SettingsManager.getInstance().getData()
										.getString(Main.getPlugin().mapName + ".displayName");
								player.sendMessage(Main.getPlugin().prefix + "§6Force voted map. Voting disabled.");
								Main.getPlugin().voting = false;
								Main.getPlugin().forcevoted = true;
							} else if (args[0].equalsIgnoreCase("3")) {
								VoteHandler.getInstance().addVote(2, 9001);
								VoteHandler.getInstance().pickMap();
								Main.getPlugin().displayName = SettingsManager.getInstance().getData()
										.getString(Main.getPlugin().mapName + ".displayName");
								player.sendMessage(Main.getPlugin().prefix + "§6Force voted map. Voting disabled.");
								Main.getPlugin().voting = false;
								Main.getPlugin().forcevoted = true;
							} else if (args[0].equalsIgnoreCase("4")) {
								VoteHandler.getInstance().addVote(3, 9001);
								VoteHandler.getInstance().pickMap();
								Main.getPlugin().displayName = SettingsManager.getInstance().getData()
										.getString(Main.getPlugin().mapName + ".displayName");
								player.sendMessage(Main.getPlugin().prefix + "§6Force voted map. Voting disabled.");
								Main.getPlugin().voting = false;
								Main.getPlugin().forcevoted = true;
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
			} else {
				player.sendMessage(Main.getPlugin().prefix + "§cNo permission.");
			}
		}
		return true;
	}

}
