package net.alextwelshie.minedrop.timers;

import java.util.Random;

import net.alextwelshie.minedrop.Main;
import net.alextwelshie.minedrop.runnables.LoadWorldInRunnable;
import net.alextwelshie.minedrop.runnables.TeleportInRunnable;
import net.alextwelshie.minedrop.utils.DropAPI;
import net.alextwelshie.minedrop.utils.GameState;
import net.alextwelshie.minedrop.utils.OnePointEight;
import net.alextwelshie.minedrop.voting.VoteHandler;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

public class LobbyTimer implements Runnable {

	public static int	lobbyTimer	= 181;
	
	Scoreboard		board			= Bukkit.getScoreboardManager().getMainScoreboard();

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public void run() {
		lobbyTimer--;
		for (Player all : Bukkit.getOnlinePlayers()) {
			all.setLevel(lobbyTimer);
		}

		switch (lobbyTimer) {
		case 20:
			if (Bukkit.getOnlinePlayers().size() >= Main.getPlugin().neededToStart) {
				for (Player all : Bukkit.getOnlinePlayers()) {
					OnePointEight.getInstance().sendTitleAndSubtitle(all, "§bChoose your Block",
							"§620 Seconds remaining..", 15, 80, 15);
				}
			}
			break;
		case 10:
			
			if (Bukkit.getOnlinePlayers().size() >= Main.getPlugin().neededToStart) {
				Main.getPlugin().voting = false;
				VoteHandler.getInstance().pickMap();
				Bukkit.broadcastMessage(Main.getPlugin().prefix + "§eVoting has ended! §aThe map §b" + Main.getPlugin().mapName + " §ahas won!");
				
				if(Main.getPlugin().mapName.equalsIgnoreCase("Valley")) {
					Main.getPlugin().displayName = "The Valley";
				} else if(Main.getPlugin().mapName.equalsIgnoreCase("AquaticDepths")) {
					Main.getPlugin().displayName = "Aquatic Depths";
				} else {
					Main.getPlugin().displayName = Main.getPlugin().mapName;
				}
				Bukkit.getScheduler().callSyncMethod(Main.getPlugin(), new LoadWorldInRunnable());
				DropAPI.getInstance().broadcastMapData();
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
