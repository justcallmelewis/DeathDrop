package net.alextwelshie.minedrop.commands;

import net.alextwelshie.minedrop.Main;
import net.alextwelshie.minedrop.timers.LobbyTimer;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ForceStart implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (Main.getPlugin().isStaff(player)) {
				if (args.length == 0) {
					if (Bukkit.getScheduler().isCurrentlyRunning(Main.getPlugin().lobbyTimer)) {
						if (!Main.getPlugin().began) {
							Main.getPlugin().began = true;
							Main.getPlugin().neededToStart = 0;

							Bukkit.createWorld(WorldCreator.name(Main.getPlugin().mapName)).setAutoSave(false);
							Main.getPlugin().mapWorld = Bukkit.getWorld(Main.getPlugin().mapName);

							LobbyTimer.lobbyTimer = 1;
						} else {
							player.sendMessage("§cGame already started.");
						}
					} else {
						player.sendMessage("§cGame already started.");
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
