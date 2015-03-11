package net.alextwelshie.minedrop.listeners;

import java.util.Random;

import net.alextwelshie.minedrop.Main;
import net.alextwelshie.minedrop.achievements.Achievement;
import net.alextwelshie.minedrop.achievements.AchievementAPI;
import net.alextwelshie.minedrop.achievements.AchievementMenu;
import net.alextwelshie.minedrop.ranks.RankHandler;
import net.alextwelshie.minedrop.timers.LobbyTimer;
import net.alextwelshie.minedrop.utils.BlockChooserGUI;
import net.alextwelshie.minedrop.utils.DropAPI;
import net.alextwelshie.minedrop.utils.GameState;
import net.alextwelshie.minedrop.utils.GameType;
import net.alextwelshie.minedrop.utils.OnePointEight;

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
import org.bukkit.scoreboard.Scoreboard;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

@SuppressWarnings("deprecation")
public class Listeners implements Listener {

	Scoreboard		board			= Bukkit.getScoreboardManager().getMainScoreboard();

	int				message			= 0;

	OnePointEight	onepointeight	= OnePointEight.getInstance();

	private void countBlocks(Player player, Location loc) {
		int count = 0;
		BlockFace[] faces = new BlockFace[] { BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH };
		for (BlockFace bf : faces) {
			Location block1 = loc.getBlock().getRelative(bf).getLocation();
			if(Main.getPlugin().mapName == "Brickwork"){
				if (block1.getBlock().getType() != Material.WATER
						&& block1.getBlock().getType() != Material.COAL_BLOCK) {
					count++;
				} 
			} else {
				if (block1.getBlock().getType() != Material.WATER
						&& block1.getBlock().getType() != Material.OBSIDIAN) {
					count++;
				} 
			}
			
		}

		switch (count) {
		case 2:
			Main.getPlugin().updateScore(player, 2);
			Bukkit.broadcastMessage(Main.getPlugin().prefix + board.getPlayerTeam(player).getPrefix()
					+ player.getName() + " §bscored a §a§lDOUBLE!!");
			Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6(+2 points)");
			break;
		case 3:
			Main.getPlugin().updateScore(player, 3);
			Bukkit.broadcastMessage(Main.getPlugin().prefix + board.getPlayerTeam(player).getPrefix()
					+ player.getName() + " §bscored a §a§lTRIPLE!!");
			Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6(+3 points)");
			break;
		case 4:
			Main.getPlugin().updateScore(player, 4);
			Bukkit.broadcastMessage(Main.getPlugin().prefix + board.getPlayerTeam(player).getPrefix()
					+ player.getName() + " §bscored a §a§lQUAD!!");
			Bukkit.broadcastMessage(Main.getPlugin().prefix + "§6(+4 points)");
			break;
		default:
			Main.getPlugin().increaseScore(player);
			break;
		}
	}

	private void givePlayerItems(Player player) {
		ItemStack clay = new ItemStack(Material.STAINED_CLAY, 1, (short) new Random().nextInt(15));
		ItemMeta claymeta = clay.getItemMeta();
		claymeta.setDisplayName("§bBlock §cChooser");
		clay.setItemMeta(claymeta);
		player.getInventory().setItem(0, clay);

		ItemStack achieve = new ItemStack(Material.BEACON, 1);
		ItemMeta achievemeta = achieve.getItemMeta();
		achievemeta.setDisplayName("§aAchievement §6Menu");
		achieve.setItemMeta(achievemeta);
		player.getInventory().setItem(4, achieve);

		ItemStack quartz = new ItemStack(Material.QUARTZ, 1);
		ItemMeta quartzmeta = quartz.getItemMeta();
		quartzmeta.setDisplayName("§6Return to Hub");
		quartz.setItemMeta(quartzmeta);
		player.getInventory().setItem(8, quartz);
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		event.setFormat(board.getPlayerTeam(player).getPrefix() + "%s" + ChatColor.DARK_GRAY + " » "
				+ ChatColor.WHITE + "%s");
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		if (Main.getPlugin().getState() == GameState.LOBBY) {
			if (Bukkit.getOnlinePlayers().size() >= Main.getPlugin().maxPlayers) {
				event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "The game is full! Sorry :(");
			} else {
				event.allow();
			}
		} else {
			event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "The game has started. Please come back later.");
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
		event.setJoinMessage(Main.getPlugin().prefix + board.getPlayerTeam(player).getPrefix() + player.getName()
				+ " §6has joined the game");
		Main.getPlugin().registerPlayerOnScoreboard(player);
		//AchievementAPI.getInstance().grantAchievement(player, Achievement.FIRSTJOIN);
		onepointeight.sendTitleAndSubtitle(player, "§6Welcome to §6MineDrop!", "§3(Early beta, expect bugs!)", 40,
				80, 40);
		onepointeight
				.sendHeaderAndFooter(player, "§6SurvivalMC§8.§aeu §3- §aPrivate Server", "§aPlaying on §6MD1");

		player.teleport(new Location(Bukkit.getWorld("world"), -1386.5, 10, 941.5, 0, 0));

		givePlayerItems(player);

		if (Bukkit.getOnlinePlayers().size() >= Main.getPlugin().neededToStart) {
			if (!Main.getPlugin().shortened) {
				Main.getPlugin().shortened = true;
				LobbyTimer.lobbyTimer = 46;
				for (Player all : Bukkit.getOnlinePlayers()) {
					all.sendMessage(Main.getPlugin().prefix + "§6We have all the droppers we need!");
					all.sendMessage(Main.getPlugin().prefix + "§6Shortening timer to "
							+ (LobbyTimer.lobbyTimer - 1) + " seconds..");
				}
			}
		}

		RankHandler.getInstance().setRankTeam(player);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		event.setQuitMessage(Main.getPlugin().prefix + "§6Player §6" + player.getName() + " §6has left us!");
		Main.getPlugin().removePlayerFromScoreboard(player);

		if (Main.getPlugin().getState() == GameState.INGAME && Bukkit.getOnlinePlayers().size() == 0) {
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
		if (block.getType() != Material.AIR) {
			if (Main.getPlugin().getState() == GameState.INGAME) {
				if (Main.getPlugin().whosDropping == null) {
				} else if (Main.getPlugin().whosDropping.equalsIgnoreCase(player.getName())) {
					if (block.getType() == Material.STATIONARY_WATER) {

						block.setType(Main.getPlugin().blocks.get(player.getName()));
						block.setData(Main.getPlugin().blockData.get(player.getName()));
						
						onepointeight.sendActionBarText(player, "§aYou successfully landed in the water!");
						
						DropAPI.getInstance().launchFirework("success", block.getLocation().subtract(0, 2, 0));
						DropAPI.getInstance().finishDrop(player);
						Bukkit.broadcastMessage(Main.getPlugin().prefix + board.getPlayerTeam(player).getPrefix()
								+ player.getName() + "§a" + DropAPI.getInstance().pickSuccessMessage());
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
		Player player = event.getPlayer();
		Material material;
		if (event.getItem().getType() == null) {
			return;
		} else if (event.getItem().getType() != null && event.getItem().getType() != Material.AIR) {
			material = event.getItem().getType();
		} else {
			return;
		}
		if (event.getAction() == null) {
		} else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (player.hasPermission("srv.build")) {
				event.setCancelled(false);
			} else {
				event.setCancelled(true);
			}
			switch (material) {
			case STAINED_CLAY:
				if (Main.getPlugin().getState() == GameState.INGAME) {
					player.sendMessage(Main.getPlugin().prefix + "§cYou can't change your block ingame silly!");
				} else {
					player.openInventory(BlockChooserGUI.getInventory(player));
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
				player.sendPluginMessage(Main.getPlugin(), "BungeeCord", quartzout.toByteArray());
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
			if (inventory.getName().equals(BlockChooserGUI.getInventory(player).getName())) {
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
								&& !player.getItemInHand().containsEnchantment(Enchantment.DURABILITY)) {
							ItemStack stack = new ItemStack(player.getItemInHand().getType());
							
							ItemMeta meta = player.getItemInHand().getItemMeta();
							meta.addEnchant(Enchantment.DURABILITY, 2, true);
							player.getItemInHand().setItem(stack);
						}
						Main.getPlugin().blocks.put(player.getName(), material);
						Main.getPlugin().blockData.put(player.getName(), data);
						AchievementAPI.getInstance().grantAchievement(player, Achievement.PICKBLOCK);
						player.sendMessage(Main.getPlugin().prefix + "§6Block chosen.");
					}
				}
			} else if (inventory.getName().equals(AchievementMenu.getInstance().getInventory(player).getName())) {
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
		if (event.getEntity() instanceof Player) {
			player = (Player) event.getEntity();
		}

		if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
			Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
			if (Main.getPlugin().getState() == GameState.INGAME) {
				if (block.getType() != Material.STATIONARY_WATER || block.getType() != Material.WATER) {
					if (Main.getPlugin().whosDropping == null) {

					} else if (Main.getPlugin().whosDropping.equalsIgnoreCase(player.getName())) {
						event.setCancelled(true);

						for (Player all : Bukkit.getOnlinePlayers()) {
							onepointeight.sendTitle(all, "§4Fail!", 5, 20, 5);
						}

						onepointeight.sendActionBarText(player, "§cYou failed to land in the water.");
						DropAPI.getInstance().launchFirework("fail", block.getLocation().subtract(0, 2, 0));
						DropAPI.getInstance().finishDrop(player);
						Bukkit.broadcastMessage(Main.getPlugin().prefix + board.getPlayerTeam(player).getPrefix()
								+ player.getName() + "§c" + DropAPI.getInstance().pickFailMessage());
						AchievementAPI.getInstance().grantAchievement(player, Achievement.FIRST_LAND_FAIL);
						DropAPI.getInstance().setupNextTurn();
					}
				}
				
				if(event.getCause() == DamageCause.FALL){
					event.setCancelled(true);
				}
			} 
		}
	}

}