package net.alextwelshie.minedrop.timers;

import java.util.Random;

import net.alextwelshie.minedrop.Main;
import net.alextwelshie.minedrop.SettingsManager;
import net.alextwelshie.minedrop.runnables.LoadWorldInRunnable;
import net.alextwelshie.minedrop.runnables.TeleportInRunnable;
import net.alextwelshie.minedrop.utils.DropAPI;
import net.alextwelshie.minedrop.utils.GameState;
import net.alextwelshie.minedrop.utils.GameType;
import net.alextwelshie.minedrop.utils.OnePointEight;
import net.alextwelshie.minedrop.voting.VoteHandler;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

public class LobbyTimer implements Runnable {

	public static int	lobbyTimer	= Main.getPlugin().config.getInt("lobbytimer") + 1;

	Scoreboard			board		= Bukkit.getScoreboardManager().getMainScoreboard();

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public void run() {
		lobbyTimer--;
		for (Player all : Bukkit.getOnlinePlayers()) {
			all.setLevel(lobbyTimer);
		}

		if (Bukkit.getOnlinePlayers().size() <= Main.getPlugin().neededToStart) {
			if (lobbyTimer % 60 == 0 && lobbyTimer != 0) {
				Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6Players waiting: §b"
						+ Bukkit.getOnlinePlayers().size());
				Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6Players needed to start: §b"
						+ Main.getPlugin().config.getInt("neededToStart"));
				Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6Time till start: §b"
						+ Main.getPlugin().getFormattedTime(lobbyTimer));
				if (!Main.getPlugin().voting && !Main.getPlugin().forcevoted) {
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage(Main.getPlugin().prefix
							+ "§cVoting will be enabled when we get enough players.");
				}
			}
		} else {
			if (lobbyTimer % 20 == 0 && lobbyTimer != 0) {
				Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6Time till start: §b"
						+ Main.getPlugin().getFormattedTime(lobbyTimer));
				for (Player all : Bukkit.getOnlinePlayers()) {
					VoteHandler.getInstance().sendVotingMessage(all);
				}
			}
		}

		switch (lobbyTimer) {
		case 20:
			if (Bukkit.getOnlinePlayers().size() >= Main.getPlugin().neededToStart) {
				for (Player all : Bukkit.getOnlinePlayers()) {
					OnePointEight.getInstance().sendTitleAndSubtitle(all, "§bChoose your block!",
							"§620 seconds remaining..", 15, 80, 15);
				}
			}
			break;
		case 10:

			if (Bukkit.getOnlinePlayers().size() >= Main.getPlugin().neededToStart) {
				Main.getPlugin().voting = false;
				VoteHandler.getInstance().pickMap();
				Main.getPlugin().displayName = SettingsManager.getInstance().getData()
						.getString(Main.getPlugin().mapName + ".displayName");
				Bukkit.broadcastMessage(Main.getPlugin().prefix + "§eVoting has ended! §aThe map §b"
						+ Main.getPlugin().mapName + " §ahas won!");
				Main.getPlugin().setType(
						GameType.valueOf(VoteHandler.getInstance().mapGametype.get(Main.getPlugin().mapName)));
				Bukkit.getScheduler().callSyncMethod(Main.getPlugin(), new LoadWorldInRunnable());
				DropAPI.getInstance().broadcastMapData();
			}
			break;
		case 5:
			if(Bukkit.getOnlinePlayers().size() >= Main.getPlugin().config.getInt("neededToStart")){
				if(Main.getPlugin().getType() == GameType.Normal){
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§b—————————[ §a§lHow To Play §b]—————————");
					Bukkit.broadcastMessage("§6— You goal is to land in the water.");
					Bukkit.broadcastMessage("§6— Hitting a block has no penalty");
					Bukkit.broadcastMessage("§6— The person with the most points wins.");
					Bukkit.broadcastMessage("§b——————————————————————————————");
					Bukkit.broadcastMessage("");
				} else if(Main.getPlugin().getType() == GameType.Elimination){
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§b—————————[ §a§lHow To Play §b]—————————");
					Bukkit.broadcastMessage("§6— You goal is to land in the water.");
					Bukkit.broadcastMessage("§6— If you hit a block, you are out.");
					Bukkit.broadcastMessage("§6— The last person remaining wins.");
					Bukkit.broadcastMessage("§b——————————————————————————————");
					Bukkit.broadcastMessage("");
				} else {
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§b—————————[ §a§lHow To Play §b]—————————");
					Bukkit.broadcastMessage("§6— You goal is to land in the water.");
					Bukkit.broadcastMessage("§6— Jumping by blocks or in between gives you bonus points.");
					Bukkit.broadcastMessage("§6— Hitting a block has no penalty");
					Bukkit.broadcastMessage("§6— The person with the most points wins.");
					Bukkit.broadcastMessage("§b——————————————————————————————");
					Bukkit.broadcastMessage("");
				}
			}
			
			break;
		case 0:
			if (Bukkit.getOnlinePlayers().size() >= Main.getPlugin().neededToStart) {
				Main.getPlugin().setState(GameState.INGAME);
				lobbyTimer = 999;
				Main.getPlugin().board.getObjective("scoreboard").setDisplaySlot(DisplaySlot.SIDEBAR);
				Main.getPlugin().began = true;

				board.getObjective("scoreboard").setDisplayName("§6#1 §7" + Main.getPlugin().displayName);

				OnePointEight onepointeight = OnePointEight.getInstance();
				for (Player all : Bukkit.getOnlinePlayers()) {

					onepointeight.sendTitle(all, "§aHere.. §bwe.. §cgo!");
					all.getInventory().clear();
					if (!Main.getPlugin().blocks.containsKey(all.getName())
							&& !Main.getPlugin().blockData.containsKey(all.getName())) {
						byte random = (byte) (new Random().nextInt(14) + 1);
						Main.getPlugin().blocks.put(all.getName(), Material.STAINED_CLAY);
						Main.getPlugin().blockData.put(all.getName(), random);
					}

					DropAPI.getInstance().notHadTurn.add(all.getName());

					if (Main.getPlugin().blockData.get(all.getName()) == 0) {
						Material material = Main.getPlugin().blocks.get(all.getName());
						Byte data = 0;

						all.getInventory().setHelmet(new ItemStack(material, 1, data));
					} else {
						Material material = Main.getPlugin().blocks.get(all.getName());
						Byte data = Main.getPlugin().blockData.get(all.getName());

						all.getInventory().setHelmet(new ItemStack(material, 1, data));
					}
				}

				Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.getPlugin(), new Runnable() {
					@Override
					public void run() {
						Player player = Bukkit.getPlayerExact(DropAPI.getInstance().notHadTurn.get(Main
								.getPlugin().turns));
						DropAPI.getInstance().setupPlayer(player);
					}
				}, 120L);
				Bukkit.getScheduler().callSyncMethod(Main.getPlugin(), new TeleportInRunnable());
				Bukkit.getScheduler().cancelTask(Main.getPlugin().lobbyTimer);

			} else {
				lobbyTimer = Main.getPlugin().config.getInt("lobbytimer");
			}
			break;
		}

	}

}
