package net.alextwelshie.minedrop.commands;

import net.alextwelshie.minedrop.Main;
import net.alextwelshie.minedrop.utils.GameState;
import net.alextwelshie.minedrop.voting.VoteHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vote implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			switch (args.length) {
				case 0:
					break;
				case 1:
					if(Main.getPlugin().voting) {
						if(Main.getPlugin().getState() == GameState.LOBBY) {
							int map = Integer.parseInt(args[0]);
							if(map <= VoteHandler.getInstance().maps.size()) {
								if(!VoteHandler.getInstance().voted.contains(player.getName())) {
									VoteHandler.getInstance().addVote(map, player);
									player.sendMessage(Main.getPlugin().prefix + "§eSuccessfully voted for: §b" + VoteHandler.getInstance().maps.get(map));
									player.sendMessage(Main.getPlugin().prefix + "§eYour map now has §b" + VoteHandler.getInstance().votes.get(VoteHandler.getInstance().maps.get(map)) + " §evotes.");
								} else {
									player.sendMessage(Main.getPlugin().prefix + "§cSadly, you have already voted once; you may not vote again.");
								}
							} else {
								player.sendMessage(Main.getPlugin().prefix + "§cThat is not a valid option. Please try again.");
							}
						} else {
							player.sendMessage(Main.getPlugin().prefix + "§cVoting is not enabled outside of lobby.");
						}
					} else {
						player.sendMessage(Main.getPlugin().prefix + "§cVoting is not active - has the map already been chosen? :o");
					}
					break;
			}
		}
		return true;
	}

}
