package net.alextwelshie.deathdrop.listeners;

import java.util.Random;

import net.alextwelshie.deathdrop.Main;
import net.alextwelshie.deathdrop.achievements.Achievement;
import net.alextwelshie.deathdrop.achievements.AchievementAPI;
import net.alextwelshie.deathdrop.achievements.AchievementMenu;
import net.alextwelshie.deathdrop.ranks.RankHandler;
import net.alextwelshie.deathdrop.timers.LobbyTimer;
import net.alextwelshie.deathdrop.utils.BlockChooserGUI;
import net.alextwelshie.deathdrop.utils.DropAPI;
import net.alextwelshie.deathdrop.utils.GameState;
import net.alextwelshie.deathdrop.utils.GameType;
import net.alextwelshie.deathdrop.utils.OnePointEight;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

@SuppressWarnings("deprecation")
public class Listeners implements Listener {

	int				message			= 0;

	OnePointEight	onepointeight	= OnePointEight.getInstance();

	private void countBlocks(Player player, Location loc) {
		int count = 0;
		BlockFace[] faces = new BlockFace[] { BlockFace.EAST, BlockFace.WEST,
				BlockFace.NORTH, BlockFace.SOUTH };
		for (BlockFace bf : faces) {
			Location block1 = loc.getBlock().getRelative(bf).getLocation();
			if (block1.getBlock().getType() != Material.WATER
					&& block1.getBlock().getType() != Material.COAL_BLOCK) {
				count++;
			}
		}

		switch (count) {
		case 2:
			Main.getPlugin().updateScore(player, 2);
			Bukkit.broadcastMessage(Main.getPlugin().prefix + "§e"
					+ player.getName() + " §bscored a §a§lDOUBLE!!");
			Bukkit.broadcastMessage(Main.getPlugin().prefix + "§e(+2 points)");
			break;
		case 3:
			Main.getPlugin().updateScore(player, 3);
			Bukkit.broadcastMessage(Main.getPlugin().prefix + "§e"
					+ player.getName() + " §bscored a §a§lTRIPLE!!");
			Bukkit.broadcastMessage(Main.getPlugin().prefix + "§e(+3 points)");
			break;
		case 4:
			Main.getPlugin().updateScore(player, 4);
			Bukkit.broadcastMessage(Main.getPlugin().prefix + "§e"
					+ player.getName() + " §bscored a §a§lQUAD!!");
			Bukkit.broadcastMessage(Main.getPlugin().prefix + "§e(+4 points)");
			break;
		default:
			Main.getPlugin().increaseScore(player);
			break;
		}
	}

	private void givePlayerItems(Player player) {
		ItemStack clay = new ItemStack(Material.STAINED_CLAY, 1,
				(short) new Random().nextInt(15));
		ItemMeta claymeta = clay.getItemMeta();
		claymeta.setDisplayName("§bBlock §cChooser");
		clay.setItemMeta(claymeta);
		player.getInventory().setItem(0, clay);

		ItemStack achieve = new ItemStack(Material.BEACON, 1);
		ItemMeta achievemeta = achieve.getItemMeta();
		achievemeta.setDisplayName("§aAchievement §eMenu");
		achieve.setItemMeta(achievemeta);
		player.getInventory().setItem(4, achieve);

		ItemStack quartz = new ItemStack(Material.QUARTZ, 1);
		ItemMeta quartzmeta = quartz.getItemMeta();
		quartzmeta.setDisplayName("§eReturn to Hub");
		quartz.setItemMeta(quartzmeta);
		player.getInventory().setItem(8, quartz);
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		event.setFormat(Bukkit.getScoreboardManager().getMainScoreboard()
				.getPlayerTeam(player).getPrefix()
				+ "%s" + ChatColor.DARK_GRAY + " » " + ChatColor.WHITE + "%s");

		if (Main.getPlugin().getState() == GameState.RESTARTING) {
			if (event.getMessage().toLowerCase().contains("gg")) {
				AchievementAPI.getInstance().grantAchievement(
						event.getPlayer(), Achievement.GOODGAME);
			}
		}
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		if (Main.getPlugin().getState() == GameState.LOBBY) {
			if (Bukkit.getOnlinePlayers().size() >= Main.getPlugin().maxPlayers) {
				event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST,
						"The game is full! Sorry :(");
			} else {
				event.allow();
			}
		} else {
			event.disallow(PlayerLoginEvent.Result.KICK_WHITELIST,
					"The game has started. Please come back later.");
		}
	}

	@EventHandler
	public void onFoodLevel(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onItemThrow(PlayerDropItemEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onItemThrow(PlayerPickupItemEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		event.setJoinMessage(Main.getPlugin().prefix + "§ePlayer §a"
				+ player.getName() + " §ehas joined us!");
		Main.getPlugin().registerPlayerOnScoreboard(player);
		//AchievementAPI.getInstance().grantAchievement(player, Achievement.FIRSTJOIN);
		onepointeight.sendTitleAndSubtitle(player, "§eWelcome to §6DeathDrop!",
				"§b(Early beta, expect bugs!)", 40, 80, 40);
		onepointeight.sendHeaderAndFooter(player,
				"§eSurvivalMC§8.§aeu §3- §aPrivate Server",
				"§aPlaying on §6DD1");

		player.teleport(new Location(Bukkit.getWorld("world"), -1386.5, 10,
				941.5, 0, 0));

		givePlayerItems(player);

		if (Bukkit.getOnlinePlayers().size() >= Main.getPlugin().neededToStart) {
			if (!Main.getPlugin().shortened) {
				Main.getPlugin().shortened = true;
				LobbyTimer.lobbyTimer = 46;
				for (Player all : Bukkit.getOnlinePlayers()) {
					all.sendMessage(Main.getPlugin().prefix
							+ "§eWe have all the droppers we need!");
					all.sendMessage(Main.getPlugin().prefix
							+ "§6Shortening timer to "
							+ (LobbyTimer.lobbyTimer - 1) + " seconds..");
				}
			}
		}

		RankHandler.getInstance().setRankTeam(player);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		event.setQuitMessage(Main.getPlugin().prefix + "§ePlayer §6"
				+ player.getName() + " §ehas left us!");
		Main.getPlugin().removePlayerFromScoreboard(player);

		RankHandler.getInstance().clearRank(player);
		if (Bukkit.getOnlinePlayers().size() <= 0) {
			Bukkit.shutdown();
		}
	}

	@EventHandler
	public void onWeather(WeatherChangeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onRedstone(BlockRedstoneEvent e) {
		if (e.getBlock().getType() == Material.REDSTONE_LAMP_ON) {
			e.setNewCurrent(100);
		}
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		Block block = loc.getWorld().getBlockAt(loc);
		if (loc.getY() <= 55 && block.getType() != Material.AIR) {
			if (Main.getPlugin().getState() == GameState.INGAME) {
				if (Main.getPlugin().whosDropping == null) {
				} else if (Main.getPlugin().whosDropping
						.equalsIgnoreCase(player.getName())) {
					if (block.getTypeId() == 9) {

						block.setType(Main.getPlugin().blocks.get(player
								.getName()));
						block.setData(Main.getPlugin().blockData.get(player
								.getName()));

						DropAPI.getInstance().launchFirework("success",
								block.getLocation().subtract(0, 2, 0));
						DropAPI.getInstance().finishDrop(player);
						Bukkit.broadcastMessage(Main.getPlugin().prefix + "§e"
								+ player.getName() + "§a"
								+ DropAPI.getInstance().pickSuccessMessage());
						if (Main.getPlugin().getType() == GameType.Enhanced) {
							countBlocks(player, loc);
						} else {
							Main.getPlugin().increaseScore(player);
						}
						DropAPI.getInstance().setupNextTurn();
					}
				}
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Material material;
		if (event.getItem().getType() == null) {
			return;
		} else if (event.getItem().getType() != null
				&& event.getItem().getType() != Material.AIR) {
			material = event.getItem().getType();
		} else {
			return;
		}
		final Player player = event.getPlayer();
		if (event.getAction() == null) {
		} else if (event.getAction() == Action.RIGHT_CLICK_AIR
				|| event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (player.hasPermission("srv.build")) {
				event.setCancelled(false);
			} else {
				event.setCancelled(true);
			}
			switch (material) {
			case STAINED_CLAY:
				if (Main.getPlugin().getState() == GameState.INGAME) {
					player.sendMessage(Main.getPlugin().prefix
							+ "§cYou can't change your block ingame silly!");
				} else {
					event.getPlayer().openInventory(
							BlockChooserGUI.getInventory(event.getPlayer()));
				}
				break;
			case BEACON:
				player.sendMessage(Main.getPlugin().prefix
						+ "§cAchievements aren't enabled at the moment due to certain reasons beyond our control. Please check back later.");
				break;
			case QUARTZ:
				ByteArrayDataOutput quartzout = ByteStreams.newDataOutput();
				quartzout.writeUTF("Connect");
				quartzout.writeUTF("hub");
				player.sendPluginMessage(Main.getPlugin(), "BungeeCord",
						quartzout.toByteArray());
				break;
			default:
				break;
			}
		}
	}

	@EventHandler
	public void onBlock(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPermission("srv.build")) {
			event.setCancelled(true);
		} else {
			event.setCancelled(false);
		}
	}

	@EventHandler
	public void onBlock(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPermission("srv.build")) {
			event.setCancelled(true);
		} else {
			event.setCancelled(false);
		}
	}

	@EventHandler
	public void onInvClick(InventoryClickEvent event) {
		if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
			event.setCancelled(!(event.getWhoClicked().getGameMode() == GameMode.CREATIVE));
		} else if (event.getInventory() instanceof PlayerInventory) {
			event.setCancelled(!(event.getWhoClicked().getGameMode() == GameMode.CREATIVE));
		} else {
			Player player = (Player) event.getWhoClicked();
			ItemStack clicked = event.getCurrentItem();
			Inventory inventory = event.getInventory();
			if (inventory.getName().equals(
					BlockChooserGUI.getInventory(player).getName())) {
				if (clicked.getType() != Material.AIR) {
					event.setCancelled(true);
					Material material = clicked.getType();
					byte data = 0;
					if (clicked.getData().getData() != 0) {
						data = clicked.getData().getData();
					}
					player.closeInventory();

					if (inventory.contains(clicked)) {
						if (player.getItemInHand().getType() == Material.STAINED_CLAY
								&& !player.getItemInHand().containsEnchantment(
										Enchantment.DURABILITY)) {
							player.getItemInHand()
							.getItemMeta()
							.addEnchant(Enchantment.DURABILITY, 1, true);
						}
						Main.getPlugin().blocks.put(player.getName(), material);
						Main.getPlugin().blockData.put(player.getName(), data);
						AchievementAPI.getInstance().grantAchievement(player,
								Achievement.PICKBLOCK);
						player.sendMessage(Main.getPlugin().prefix
								+ "§eBlock chosen.");
					}
				}
			} else if (inventory.getName().equals(
					AchievementMenu.getInstance().getInventory(player)
					.getName())) {
				event.setCancelled(true);
				if (clicked == null) {
					player.closeInventory();
				} else if (clicked.getType() == Material.AIR) {
					player.closeInventory();
				}
			}
		}

	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		Player player = null;
		if(event.getEntity() instanceof Player){
			player = (Player) event.getEntity();
		}
		
		if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
			Block block = player.getLocation().getBlock()
					.getRelative(BlockFace.DOWN);
			if (Main.getPlugin().getState() == GameState.INGAME) {
				if (block.getTypeId() != 8 || block.getTypeId() != 9) {
					if (Main.getPlugin().whosDropping == null) {

					} else if (Main.getPlugin().whosDropping
							.equalsIgnoreCase(player.getName())) {
						event.setCancelled(true);

						for (Player all : Bukkit.getOnlinePlayers()) {
							onepointeight.sendTitle(all, "§4Fail!", 5, 20, 5);
						}

						DropAPI.getInstance().launchFirework("fail",
								block.getLocation().subtract(0, 2, 0));
						DropAPI.getInstance().finishDrop(player);
						Bukkit.broadcastMessage(Main.getPlugin().prefix + "§e"
								+ player.getName() + "§c"
								+ DropAPI.getInstance().pickFailMessage());
						AchievementAPI.getInstance().grantAchievement(player,
								Achievement.FIRST_LAND_FAIL);
						DropAPI.getInstance().setupNextTurn();
					}
				}
			} else if (Main.getPlugin().getState() == GameState.LOBBY
					|| event.getCause() == DamageCause.SUFFOCATION) {
				event.setCancelled(true);
			}
		}
	}

}
