package net.alextwelshie.minedrop.commands;

import net.alextwelshie.minedrop.Main;
import net.alextwelshie.minedrop.utils.DropAPI;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class EndGame implements CommandExecutor {

	Scoreboard	board	= Bukkit.getScoreboardManager().getMainScoreboard();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (Main.getPlugin().isStaff(player)) {
				if (args.length == 0) {
					if (!Bukkit.getScheduler().isCurrentlyRunning(Main.getPlugin().lobbyTimer)) {
						if (!Main.getPlugin().ended) {
							Main.getPlugin().ended = true;

							Main.getPlugin().whosDropping = null;
							for (Player all : Bukkit.getOnlinePlayers()) {
								DropAPI.getInstance().teleportToMapSpawn(all);
							}

							DropAPI.getInstance().gameOver();
							} else {
								player.sendMessage("§cGame already ended.");
							}
						} else {
							player.sendMessage("§cGame can't end - lobby is still in progress! Try /bg instead.");
						}
					} else {

					}
				} else {
					player.sendMessage("§4Illegal command.");
				}
			}
		return true;
	}
		
}
