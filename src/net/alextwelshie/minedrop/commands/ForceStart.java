package net.alextwelshie.minedrop.commands;

import net.alextwelshie.minedrop.Main;
import net.alextwelshie.minedrop.SettingsManager;
import net.alextwelshie.minedrop.runnables.LoadWorldInRunnable;
import net.alextwelshie.minedrop.timers.LobbyTimer;
import net.alextwelshie.minedrop.utils.DropAPI;
import net.alextwelshie.minedrop.utils.GameState;
import net.alextwelshie.minedrop.utils.GameType;
import net.alextwelshie.minedrop.voting.VoteHandler;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("unchecked")
public class ForceStart implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (Main.getPlugin().isStaff(player)) {
				if (args.length == 0) {
					if (Main.getPlugin().getState() == GameState.LOBBY) {
						if (!Main.getPlugin().began) {

							Main.getPlugin().began = true;
							Main.getPlugin().neededToStart = 0;

							Main.getPlugin().voting = false;
							VoteHandler.getInstance().pickMap();
							Main.getPlugin().displayName = SettingsManager.getInstance().getData()
									.getString(Main.getPlugin().mapName + ".displayName");
							Bukkit.broadcastMessage(Main.getPlugin().prefix + "§eVoting has ended! §aThe map §b"
									+ Main.getPlugin().mapName + " §ahas won!");
							Main.getPlugin()
									.setType(
											GameType.valueOf(VoteHandler.getInstance().mapGametype.get(Main
													.getPlugin().mapName)));
							Bukkit.getScheduler().callSyncMethod(Main.getPlugin(), new LoadWorldInRunnable());
							DropAPI.getInstance().broadcastMapData();

							LobbyTimer.lobbyTimer = 1;
							player.sendMessage("§eGame began.");
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
