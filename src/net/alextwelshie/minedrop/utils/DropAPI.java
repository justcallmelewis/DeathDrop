package net.alextwelshie.minedrop.utils;

import java.util.ArrayList;
import java.util.Random;

import net.alextwelshie.minedrop.Main;
import net.alextwelshie.minedrop.SettingsManager;
import net.alextwelshie.minedrop.runnables.EffectAddInRunnable;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
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
	public ArrayList<String>		eliminated		= new ArrayList<>();
	public ArrayList<String>		notHadTurn		= new ArrayList<>();

	private static final DropAPI	instance		= new DropAPI();
	public int						timer			= 16;
	public int						timerTask;

	public static DropAPI getInstance() {
		return instance;
	}

	Scoreboard		board			= Bukkit.getScoreboardManager().getMainScoreboard();

	OnePointEight	onepointeight	= OnePointEight.getInstance();
	SettingsManager	settings		= SettingsManager.getInstance();

	private void jumpCountdown(Player player) {
		double jumpY = SettingsManager.getInstance().getData().getDouble(Main.getPlugin().mapName + ".jump.y");
		timer--;
		if (player.getLocation().getY() > jumpY - 1) {
			if (timer == 5) {
				player.playSound(player.getLocation(), Sound.NOTE_PLING, 5, 1);
				player.sendMessage(Main.getPlugin().prefix + "§c§lAre you jumping?");
				player.sendMessage(Main.getPlugin().prefix + "§c§lIf not, please use /hub.");
			} else if (timer == 0) {
				player.getWorld().strikeLightning(player.getLocation());
				Bukkit.getScheduler().cancelTask(timerTask);
			}
		}
	}

	public void setupPlayer(Player player) {

		notHadTurn.remove(player.getName());
		Main.getPlugin().whosDropping = player.getName();
		Bukkit.broadcastMessage(Main.getPlugin().prefix + "§aPlayer " + board.getPlayerTeam(player).getPrefix()
				+ player.getName() + "§a, you're up!");
		teleportToDropZone(player);
		timerTask = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Main.getPlugin(), new Runnable() {
			@Override
			public void run() {
				jumpCountdown(player);
			}
		}, 20L, 20L);

		if (Main.getPlugin().round == 1 && Main.getPlugin().turns == 0) {
			Bukkit.getScheduler().callSyncMethod(Main.getPlugin(),
					new EffectAddInRunnable(player, PotionEffectType.SLOW, 30, 255));
		} else {
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 255));
		}
	}

	public void broadcastMapData() {
		Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6Map: §b" + Main.getPlugin().displayName);
		Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6Author: §bN/A");
		Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6Gametype: §b" + Main.getPlugin().getType().name());
		Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6Rounds: §b" + Main.getPlugin().maxRounds);
	}

	public void finishDrop(Player player) {
		Main.getPlugin().whosDropping = null;
		teleportToMapSpawn(player);
	}

	public void teleportToMapSpawn(Player player) {
		World w = Bukkit.getServer().getWorld(settings.getData().getString(Main.getPlugin().mapName + ".world"));
		double x = settings.getData().getDouble(Main.getPlugin().mapName + ".spec.x");
		double y = settings.getData().getDouble(Main.getPlugin().mapName + ".spec.y");
		double z = settings.getData().getDouble(Main.getPlugin().mapName + ".spec.z");
		double yaw = settings.getData().getDouble(Main.getPlugin().mapName + ".spec.yaw");
		double pitch = settings.getData().getDouble(Main.getPlugin().mapName + ".spec.pitch");
		Location spec = new Location(w, x, y, z, (float) yaw, (float) pitch);
		player.teleport(spec);
	}

	public void teleportToDropZone(Player player) {
		World w = Bukkit.getServer().getWorld(settings.getData().getString(Main.getPlugin().mapName + ".world"));
		double x = settings.getData().getDouble(Main.getPlugin().mapName + ".jump.x");
		double y = settings.getData().getDouble(Main.getPlugin().mapName + ".jump.y");
		double z = settings.getData().getDouble(Main.getPlugin().mapName + ".jump.z");
		double yaw = settings.getData().getDouble(Main.getPlugin().mapName + ".jump.yaw");
		double pitch = settings.getData().getDouble(Main.getPlugin().mapName + ".jump.pitch");
		Location jump = new Location(w, x, y, z, (float) yaw, (float) pitch);
		player.teleport(jump);
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

	public void eliminatePlayer(Player player) {
		eliminated.add(player.getName());

		int score = Main.getPlugin().getScore(player);
		String prefix = Main.getPlugin().board.getPlayerTeam(player).getPrefix();
		Main.getPlugin().removePlayerFromScoreboard(player);
		Main.getPlugin().registerFakePlayer(prefix + "§m" + player.getName(), score);
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

		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {

			@Override
			public void run() {
				fw.detonate();
			}
		}, 12L);
	}

	public void gameOver() {
		Main.getPlugin().round = 0;
		Bukkit.getScheduler().cancelAllTasks();

		int highest = 0;
		ArrayList<String> winners = new ArrayList<>();
		for (Player all : Bukkit.getOnlinePlayers()) {
			Score score = Main.getPlugin().board.getObjective("scoreboard").getScore(all.getName());
			if (Main.getPlugin().getType() == GameType.Elimination) {
				if (score.getScore() >= highest && score.getScore() != 0 && notHadTurn.size() == 1) {
					highest = score.getScore();
					winners.add(score.getPlayer().getName());
				} else {
					winners.clear();
				}
			} else {
				if (score.getScore() >= highest && score.getScore() != highest) {
					highest = score.getScore();
					winners.add(score.getPlayer().getName());
				}
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
		} else if (winners.size() == 1) {
			Player winner = Bukkit.getPlayer(winners.get(0));
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6§lWINNER: "
					+ board.getPlayerTeam(winner).getPrefix() + winner.getName() + "§8 - §b" + highest
					+ " §6Points");
			Bukkit.broadcastMessage(Main.getPlugin().prefix + "§b§lCONGRATULATIONS!!");
			Bukkit.broadcastMessage("");
		} else {
			Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6§lWINNER: §cNobody");
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
		timer = 16;

		if (Main.getPlugin().getType() == GameType.Elimination) {
			for (Player all : Bukkit.getOnlinePlayers()) {
				if (!eliminated.contains(all.getName())) {
					notHadTurn.add(all.getName());
				}
			}
		} else {
			for (Player all : Bukkit.getOnlinePlayers()) {
				notHadTurn.add(all.getName());
			}
		}

		if (Main.getPlugin().getType() == GameType.Elimination) {
			if (notHadTurn.size() <= 1) {
				gameOver();
				return;
			}
		}

		Main.getPlugin().board.getObjective("scoreboard").setDisplayName("§b§lNEW ROUND!!");
		for (Player all : Bukkit.getOnlinePlayers()) {
			onepointeight.sendTitleAndSubtitle(all, "§b§lNEW ROUND!!", "§aRound §6" + Main.getPlugin().round, 10,
					40, 10);
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Main.getPlugin().board.getObjective("scoreboard").setDisplayName(
						"§6#" + Main.getPlugin().round + " §7" + Main.getPlugin().displayName);
			}
		}, 80L);

		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Random random = new Random();
				int playerInt = random.nextInt(notHadTurn.size());
				setupPlayer(Bukkit.getPlayerExact(notHadTurn.get(playerInt)));
			}

		}, 60L);
	}

	public void setupNextTurn() {
		timer = 16;

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
					int playerInt = random.nextInt((notHadTurn.size()));
					setupPlayer(Bukkit.getPlayerExact(notHadTurn.get(playerInt)));
				}

			}, 60L);
		}
	}
}
