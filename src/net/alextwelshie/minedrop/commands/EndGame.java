package net.alextwelshie.minedrop.commands;

import java.util.ArrayList;

import net.alextwelshie.minedrop.Main;
import net.alextwelshie.minedrop.runnables.KickInRunnable;
import net.alextwelshie.minedrop.utils.DropAPI;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;

public class EndGame implements CommandExecutor {

	@SuppressWarnings({ "deprecation", "unused" })
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
								DropAPI.getInstance().teleportToMapSpawn(player);
							}

								Main.getPlugin().round = 0;

								int highest = 0;
								ArrayList<String> winners = new ArrayList<>();
								for (Player all : Bukkit.getOnlinePlayers()) {
									Score score = Main.getPlugin().board.getObjective("scoreboard").getScore(
											all.getName());
									if (score.getScore() >= highest) {
										highest = score.getScore();
										winners.add(score.getPlayer().getName());
									}
								}

								if (winners.size() >= 2) {
									String[] winnerName = new String[winners.size()];
									for (int i = 0; i < winners.size(); i++) {
										winnerName[i] = winners.get(i);
										Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6§lWINNER: §a"
												+ winnerName[i]);
									}
									Bukkit.broadcastMessage(Main.getPlugin().prefix + "§b§lCONGRATULATIONS!!");
									Bukkit.broadcastMessage(Main.getPlugin().prefix + " ");
									Bukkit.broadcastMessage(Main.getPlugin().prefix
											+ "§3All players were tied at §d" + highest + " §3points.");
								} else {
									Player winner = Bukkit.getPlayer(winners.get(0));
									Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6§lWINNER: §a"
											+ winner.getName());
									Bukkit.broadcastMessage(Main.getPlugin().prefix + "§b§lCONGRATULATIONS!!");
									Bukkit.broadcastMessage(Main.getPlugin().prefix + " ");
									Bukkit.broadcastMessage(Main.getPlugin().prefix + "§3Player §d"
											+ winner.getName() + " §3won with §d" + highest + " §3points.");
								}

								Bukkit.broadcastMessage(Main.getPlugin().prefix
										+ "§cRestarting in §4§l10 seconds.");
								Main.getPlugin().board.getObjective("scoreboard").setDisplaySlot(null);
								Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.getPlugin(), new Runnable() {

									@SuppressWarnings("unchecked")
									@Override
									public void run() {
										Bukkit.getScheduler().callSyncMethod(Main.getPlugin(),
												new KickInRunnable());
										Bukkit.shutdown();
									}
								}, 220L);

								player.sendMessage("§6Game ended.");
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
