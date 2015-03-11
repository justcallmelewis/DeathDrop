package net.alextwelshie.minedrop.utils;

import java.util.ArrayList;
import java.util.Random;

import net.alextwelshie.minedrop.Main;
import net.alextwelshie.minedrop.runnables.EffectAddInRunnable;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

@SuppressWarnings({ "deprecation", "unchecked" })
public class DropAPI {

	public ArrayList<String>		successMessages	= new ArrayList<>();
	public ArrayList<String>		failMessages	= new ArrayList<>();

	public ArrayList<String>		notHadTurn		= new ArrayList<>();

	private static final DropAPI	instance		= new DropAPI();

	public static DropAPI getInstance() {
		return instance;
	}

	Scoreboard		board			= Bukkit.getScoreboardManager().getMainScoreboard();

	OnePointEight	onepointeight	= OnePointEight.getInstance();

	public void setupPlayer(Player player) {
		notHadTurn.remove(player.getName());
		Main.getPlugin().whosDropping = player.getName();
		Bukkit.broadcastMessage(Main.getPlugin().prefix + "§aPlayer " + board.getPlayerTeam(player).getPrefix()
				+ player.getName() + "§a, you're up!");
		teleportToDropZone(player);
		if (Main.getPlugin().round == 1 && Main.getPlugin().turns == 0) {
			Bukkit.getScheduler().callSyncMethod(Main.getPlugin(),
					new EffectAddInRunnable(player, PotionEffectType.SLOW, 30, 255));
		} else {
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 255));
		}
	}

	public void broadcastMapData() {
		Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6Map: §b" + Main.getPlugin().mapName);
		Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6Author: §bN/A");
		Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6Gametype: §b" + Main.getPlugin().getType().name());
	}

	public void finishDrop(Player player) {
		Main.getPlugin().whosDropping = null;
		teleportToMapSpawn(player);
	}

	public void teleportToMapSpawn(Player player) {
		switch (Main.getPlugin().mapName) {
		case "Brickwork":
			player.teleport(new Location(Main.getPlugin().mapWorld, 728.5, 47.5, 643.5, 180, 0));
			break;

		case "Chamber":
			player.teleport(new Location(Main.getPlugin().mapWorld, -167.5, 77.5, 207.5, 90, 0));
			break;
		}

	}

	public void teleportToDropZone(Player player) {
		switch (Main.getPlugin().mapName) {
		case "Brickwork":
			player.teleport(new Location(Main.getPlugin().mapWorld, 728.5, 92.5, 631.5, 0, 0));
			break;

		case "Chamber":
			player.teleport(new Location(Main.getPlugin().mapWorld, -172.5, 95.5, 206.5, 90, 0));
			break;
		}

	}

	public String pickSuccessMessage() {
		int random = new Random().nextInt((successMessages.size() - 1));
		if (random == -1) {
			random = 0;
		}
		return successMessages.get(random);
	}

	public String pickFailMessage() {
		int random = new Random().nextInt((failMessages.size() - 1));
		if (random == -1) {
			random = 0;
		}
		return failMessages.get(random);
	}

	public void launchFirework(String occasion, Location location) {
		Color colour = null;
		if (occasion.equalsIgnoreCase("success")) {
			colour = Color.LIME;
		} else if (occasion.equalsIgnoreCase("fail")) {
			colour = Color.RED;
		}
		Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();

		FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(colour).with(Type.BURST)
				.trail(true).build();

		fwm.addEffect(effect);
		fwm.setPower(1);
		fw.setFireworkMeta(fwm);
	}

	private void gameOver() {

		Main.getPlugin().round = 0;

		int highest = 0;
		ArrayList<String> winners = new ArrayList<>();
		for (Player all : Bukkit.getOnlinePlayers()) {
			Score score = Main.getPlugin().board.getObjective("scoreboard").getScore(all.getName());
			if (score.getScore() >= highest) {
				highest = score.getScore();
				winners.add(score.getPlayer().getName());
			}
		}

		if (winners.size() >= 2) {
			String[] winnerName = new String[winners.size()];
			for (int i = 0; i < winners.size(); i++) {
				winnerName[i] = winners.get(i);
				Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6§lWINNER: §a" + winnerName[i]);
			}
			Bukkit.broadcastMessage(Main.getPlugin().prefix + "§b§lCONGRATULATIONS!!");
			Bukkit.broadcastMessage(Main.getPlugin().prefix + " ");
			Bukkit.broadcastMessage(Main.getPlugin().prefix + "§3All players were tied at §d" + highest
					+ " §3points.");
		} else {
			Player winner = Bukkit.getPlayer(winners.get(0));
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6§lWINNER: §a"
					+ board.getPlayerTeam(winner).getPrefix() + winner.getName() + "§8 - §b" + highest
					+ " §6Points");
			Bukkit.broadcastMessage(Main.getPlugin().prefix + "§b§lCONGRATULATIONS!!");
			Bukkit.broadcastMessage("");
		}

		Bukkit.broadcastMessage(Main.getPlugin().prefix + "§cRestarting in §4§l10 seconds.");
		Main.getPlugin().board.getObjective("scoreboard").setDisplaySlot(null);
		Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.getPlugin(), new Runnable() {
			@Override
			public void run() {
				for (Player all : Bukkit.getOnlinePlayers()) {
					ByteArrayDataOutput out = ByteStreams.newDataOutput();
					out.writeUTF("Connect");
					out.writeUTF("hub");
					all.sendPluginMessage(Main.getPlugin(), "BungeeCord", out.toByteArray());
				}
			}
		}, 220L);

		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			@Override
			public void run() {
				if (Bukkit.getOnlinePlayers().size() <= 0) {
					Bukkit.shutdown();
				}
			}
		}, 0L, 40L);

	}

	private void newRound() {

		Main.getPlugin().board.getObjective("scoreboard").setDisplayName("§b§lNEW ROUND!!");
		for (Player all : Bukkit.getOnlinePlayers()) {
			onepointeight.sendTitleAndSubtitle(all, "§b§lNEW ROUND!!", "§aRound §6" + Main.getPlugin().round, 10,
					40, 10);
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Main.getPlugin().board.getObjective("scoreboard").setDisplayName(
						"§6#" + Main.getPlugin().round + " §7" + Main.getPlugin().mapName);
			}
		}, 80L);

		notHadTurn.clear();
		for (Player all : Bukkit.getOnlinePlayers()) {
			notHadTurn.add(all.getName());
		}

		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Random random = new Random();
				int playerInt = random.nextInt(notHadTurn.size());
				setupPlayer(Bukkit.getPlayerExact(notHadTurn.get(playerInt)));
			}

		}, 60l);
	}

	public void setupNextTurn() {
		Main.getPlugin().turns++;

		if (Main.getPlugin().turns == Bukkit.getOnlinePlayers().size()) {
			Main.getPlugin().turns = 0;
			Main.getPlugin().round++;
			if (Main.getPlugin().round == (Main.getPlugin().maxRounds + 1)) {
				gameOver();
			} else {
				newRound();
			}
		} else {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
				@Override
				public void run() {
					Random random = new Random();
					int playerInt = random.nextInt(notHadTurn.size());
					setupPlayer(Bukkit.getPlayerExact(notHadTurn.get(playerInt)));
				}

			}, 50L);
		}
	}
}
