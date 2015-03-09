package net.alextwelshie.minedrop.timers;

import java.util.Random;

import net.alextwelshie.minedrop.Main;
import net.alextwelshie.minedrop.runnables.LoadWorldInRunnable;
import net.alextwelshie.minedrop.runnables.TeleportInRunnable;
import net.alextwelshie.minedrop.utils.DropAPI;
import net.alextwelshie.minedrop.utils.GameState;
import net.alextwelshie.minedrop.utils.OnePointEight;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;

public class LobbyTimer implements Runnable {

	public static int	lobbyTimer	= 181;

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public void run() {
		lobbyTimer--;
		for (Player all : Bukkit.getOnlinePlayers()) {
			all.setLevel(lobbyTimer);
		}

		switch (lobbyTimer) {
		case 181:
			Main.getPlugin().setState(GameState.LOBBY);
			break;
		case 20:
			if (Bukkit.getOnlinePlayers().size() >= Main.getPlugin().neededToStart) {
				for (Player all : Bukkit.getOnlinePlayers()) {
					OnePointEight.getInstance().sendTitleAndSubtitle(all, "§bChoose your Block",
							"§620 Seconds remaining..", 15, 80, 15);
				}
			}
			break;
		case 11:
			if (Bukkit.getOnlinePlayers().size() >= Main.getPlugin().neededToStart) {
				Bukkit.getScheduler().callSyncMethod(Main.getPlugin(), new LoadWorldInRunnable());
				DropAPI.getInstance().broadcastMapData("Brickwork");
			}
			break;
		case 0:
			if (Bukkit.getOnlinePlayers().size() >= Main.getPlugin().neededToStart) {
				Main.getPlugin().setState(GameState.INGAME);
				lobbyTimer = 999;
				Main.getPlugin().board.getObjective("scoreboard").setDisplaySlot(DisplaySlot.SIDEBAR);
				Main.getPlugin().began = true;

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
