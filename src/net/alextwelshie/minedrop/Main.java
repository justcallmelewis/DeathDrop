package net.alextwelshie.minedrop;

import java.util.HashMap;
import java.util.Random;

import net.alextwelshie.minedrop.commands.EndGame;
import net.alextwelshie.minedrop.commands.ForceStart;
import net.alextwelshie.minedrop.commands.Map;
import net.alextwelshie.minedrop.commands.SetConfig;
import net.alextwelshie.minedrop.commands.ShortStart;
import net.alextwelshie.minedrop.listeners.Listeners;
import net.alextwelshie.minedrop.ranks.PlayerManager;
import net.alextwelshie.minedrop.timers.LobbyTimer;
import net.alextwelshie.minedrop.utils.BlockChooserGUI;
import net.alextwelshie.minedrop.utils.DropAPI;
import net.alextwelshie.minedrop.utils.GameState;
import net.alextwelshie.minedrop.utils.GameType;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

@SuppressWarnings("deprecation")
public class Main extends JavaPlugin {

	Random					random			= new Random();

	public String			prefix			= "§3MineDrop §7| ";
	public Scoreboard		board;
	public int				lobbyTimer		= 23;
	public String			mapName;
	public int				randomMap		= random.nextInt(1);
	public World			mapWorld		= null;
	public Integer			neededToStart	= null;
	public Integer			maxPlayers		= null;
	public String			whosDropping	= null;
	public int				turns			= 0;
	public int				round			= 1;
	public Integer			maxRounds		= null;
	public boolean			began			= false;
	public boolean			ended			= false;
	public boolean			shortened		= false;

	public Configuration	config;

	public GameState		state;
	public GameType			type;
	
	PlayerManager	pl	= PlayerManager.getInstance();

	public static Main getPlugin() {
		return JavaPlugin.getPlugin(Main.class);
	}

	public HashMap<String, Material>	blocks		= new HashMap<>();
	public HashMap<String, Byte>		blockData	= new HashMap<>();

	@Override
	public void onEnable() {
		setupConfig();
		setupScoreboards();
		setupMechanics();
		fillErrorMessages();
		fillSuccessMessages();
		fillBlockChooser();
		registration();
		randomMap();
	}
	
	private void setupScoreboards() {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		board = manager.getMainScoreboard();
		board.registerNewObjective("scoreboard", "dummy");
	}

	private void randomMap() {
		if (randomMap == 0) {
			mapName = "Brickwork";
		} else {
			mapName = "Chamber";
		}
	}

	public void forceMap(String map) {
		mapName = map;
	}

	private void setupConfig() {
		config = getConfig();
		saveDefaultConfig();
	}
	
	public boolean isPremium(Player player) {
		return PlayerManager.getInstance().getRank(player).equalsIgnoreCase("Hive")
				|| PlayerManager.getInstance().getRank(player).equalsIgnoreCase("Special");
	}

	public boolean isStaff(Player player) {
		return PlayerManager.getInstance().getRank(player).equalsIgnoreCase("Mod")
				| PlayerManager.getInstance().getRank(player).equalsIgnoreCase("Admin")
				|| PlayerManager.getInstance().getRank(player).equalsIgnoreCase("Owner");
	}
	
	private String getRankTeam(Player p) {
		switch (pl.getRank(p)) {
		case "Regular":
			return "E-Regular";
		case "Special":
			return "E-Regular";
		case "Hive":
			return "D-Vip";
		case "Mod":
			return "C-Mod";
		case "Admin":
			return "B-Admin";
		case "Owner":
			return "A-Owner";
		default:
			return null;
		}
	}

	public void registerPlayerOnScoreboard(Player player) {
		Score score = board.getObjective("scoreboard").getScore(player.getDisplayName());
		score.setScore(0);
		
		board.getTeam(getRankTeam(player)).addPlayer(player);

		for (Player all : Bukkit.getOnlinePlayers()) {
			all.setScoreboard(board);
		}
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public GameType getType() {
		return type;
	}

	public void setType(GameType type) {
		this.type = type;
	}

	public void removePlayerFromScoreboard(Player player) {
		board.resetScores(player.getDisplayName());
		
		board.getTeam(getRankTeam(player)).removePlayer(player);

		for (Player all : Bukkit.getOnlinePlayers()) {
			all.setScoreboard(board);
		}
	}

	public Integer getScore(Player player) {
		Score score = board.getObjective("scoreboard").getScore(player.getDisplayName());
		return score.getScore();
	}

	public void updateScore(Player player, int amount) {
		Score score = board.getObjective("scoreboard").getScore(player.getDisplayName());
		score.setScore(getScore(player) + amount);

		for (Player all : Bukkit.getOnlinePlayers()) {
			all.setScoreboard(board);
		}
	}

	public void increaseScore(Player player) {
		Score score = board.getObjective("scoreboard").getScore(player.getDisplayName());
		score.setScore(getScore(player) + 1);

		for (Player all : Bukkit.getOnlinePlayers()) {
			all.setScoreboard(board);
		}
	}

	public void fillSuccessMessages() {
		DropAPI drop = DropAPI.getInstance();
		drop.successMessages.add(" landed like a cat!");
		drop.successMessages.add(" splooshed successfully into the water.");
		drop.successMessages.add(" pooped out a block. Yaay.");
		drop.successMessages.add(" wedi glanio yn y ddŵr.");
		drop.successMessages.add(" cheated.. probably.");
	}

	public void fillErrorMessages() {
		DropAPI drop = DropAPI.getInstance();
		drop.failMessages.add(" did a Sherlock Holmes.");
		drop.failMessages.add(" did the flop.");
		drop.failMessages.add(" failed to become Tom Daley.");
		drop.failMessages.add("'s face became the floor.");
		drop.failMessages.add(" suicided. Maybe on purpose?");
	}

	private void registration() {
		getCommand("forcestart").setExecutor(new ForceStart());
		getCommand("endgame").setExecutor(new EndGame());
		getCommand("setconfig").setExecutor(new SetConfig());
		getCommand("map").setExecutor(new Map());
		getCommand("shortstart").setExecutor(new ShortStart());
		Bukkit.getPluginManager().registerEvents(new Listeners(), this);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}

	private void setupMechanics() {
		lobbyTimer = Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new LobbyTimer(), 0L, 20L);

		setState(GameState.LOBBY);

		int needed = config.getInt("neededToStart");
		int max = config.getInt("maxPlayers");
		int maxrounds = config.getInt("maxRounds");
		int lobbytimer = config.getInt("lobbytimer");
		String gametype = config.getString("gametype");

		this.neededToStart = needed;
		this.maxPlayers = max;
		this.maxRounds = maxrounds;
		LobbyTimer.lobbyTimer = lobbytimer + 1;

		switch (gametype) {
		case "Enhanced":
			setType(GameType.Enhanced);
			break;
		case "Normal":
			setType(GameType.Normal);
			break;
		case "Auto":
			Random random = new Random();
			int Chance = random.nextInt(1);
			if (Chance == 0) {
				setType(GameType.Enhanced);
			} else {
				setType(GameType.Normal);
			}
			break;
		}

		if (this.neededToStart == null) {
			neededToStart = 2;
		}

		if (this.maxPlayers == null) {
			maxPlayers = 16;
		}

		if (this.maxRounds == null) {
			maxRounds = 7;
		}

		if (getType() == null) {
			setType(GameType.Normal);
		}

		if (LobbyTimer.lobbyTimer == 999) {
			LobbyTimer.lobbyTimer = 181;
		}
	}

	private void fillBlockChooser() {
		for (int i = 0; i < 16; i++) {
			BlockChooserGUI.normal.put((byte) i, Material.STAINED_CLAY);
		}

		BlockChooserGUI.premium.put(Material.TNT, (byte) 0);
		BlockChooserGUI.premium.put(Material.IRON_BLOCK, (byte) 0);
		BlockChooserGUI.premium.put(Material.GOLD_BLOCK, (byte) 0);
		BlockChooserGUI.premium.put(Material.EMERALD_BLOCK, (byte) 0);
		BlockChooserGUI.premium.put(Material.DIAMOND_BLOCK, (byte) 0);
		BlockChooserGUI.premium.put(Material.PUMPKIN, (byte) 0);
		BlockChooserGUI.premium.put(Material.STONE, (byte) 0);
		BlockChooserGUI.premium.put(Material.BRICK, (byte) 0);
		BlockChooserGUI.premium.put(Material.SANDSTONE, (byte) 0);

		BlockChooserGUI.staff.put(Material.COMMAND, (byte) 0);
		BlockChooserGUI.staff.put(Material.BEACON, (byte) 0);
	}

}
