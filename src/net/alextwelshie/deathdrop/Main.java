package net.alextwelshie.deathdrop;

import net.alextwelshie.deathdrop.utils.GameType;
import net.alextwelshie.deathdrop.timers.LobbyTimer;
import net.alextwelshie.deathdrop.listeners.Listeners;
import net.alextwelshie.deathdrop.utils.BlockChooserGUI;
import net.alextwelshie.deathdrop.utils.DropAPI;
import net.alextwelshie.deathdrop.utils.GameState;
import net.alextwelshie.deathdrop.commands.StartGame;
import java.util.HashMap;
import java.util.Random;
import net.alextwelshie.deathdrop.commands.EndGame;
import net.alextwelshie.deathdrop.commands.SetConfig;
import net.alextwelshie.deathdrop.ranks.RankHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

@SuppressWarnings("deprecation")
public class Main extends JavaPlugin {

    public String prefix = "§6[DeathDrop] ";
    public Scoreboard board;
    public int lobbyTimer = 23;
    public String mapName = "Brickwork";
    public World mapWorld = null;
    public Integer neededToStart = null;
    public Integer maxPlayers = null;
    public String whosDropping = null;
    public int turns = 0;
    public int round = 1;
    public Integer maxRounds = null;
    public boolean began = false;
    public boolean ended = false;
    public boolean shortened = false;

    public Configuration config;

    public GameState state;
    public GameType type;

    public static Main getPlugin() {
        return Main.getPlugin(Main.class);
    }

    public HashMap<String, Material> blocks = new HashMap<>();
    public HashMap<String, Byte> blockData = new HashMap<>();

    @Override
    public void onEnable() {
        setupConfig();
        setupScoreboards();
        setupMechanics();
        fillErrorMessages();
        fillSuccessMessages();
        fillBlockChooser();
        registration();
    }

    public String getTabListName(Player player) {
        if (player.getName().length() == 16) {
            return RankHandler.getInstance().getRankColour(player) + player.getName().substring(0, 14);
        } else if (player.getName().length() == 15) {
            return RankHandler.getInstance().getRankColour(player) + player.getName().substring(0, 15);
        } else {
            return RankHandler.getInstance().getRankColour(player) + player.getName();
        }
    }

    private void setupScoreboards() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("scoreboard", "dummy");
        objective.setDisplayName("§e#1 §7" + mapName);
    }

    private void setupConfig() {
        config = getConfig();
        saveDefaultConfig();
    }

    public void registerPlayerOnScoreboard(Player player) {
        Score score = board.getObjective("scoreboard").getScore(player.getDisplayName()); //Get a fake offline player
        score.setScore(0);

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

        for (Player all : Bukkit.getOnlinePlayers()) {
            all.setScoreboard(board);
        }
    }

    public Integer getScore(Player player) {
        Score score = board.getObjective("scoreboard").getScore(player.getDisplayName()); //Get a fake offline player
        return score.getScore();
    }

    public void updateScore(Player player, int amount) {
        Score score = board.getObjective("scoreboard").getScore(player.getDisplayName()); //Get a fake offline player
        score.setScore(getScore(player) + amount);

        for (Player all : Bukkit.getOnlinePlayers()) {
            all.setScoreboard(board);
        }
    }

    public void increaseScore(Player player) {
        Score score = board.getObjective("scoreboard").getScore(player.getDisplayName()); //Get a fake offline player
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
        getCommand("bg").setExecutor(new StartGame());
        getCommand("eg").setExecutor(new EndGame());
        getCommand("setconfig").setExecutor(new SetConfig());
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    private void setupMechanics() {
        lobbyTimer = Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new LobbyTimer(), 0L, 20L);

        setState(GameState.LOBBY);

        int needed = config.getInt("neededToStart");
        int max = config.getInt("maxPlayers");
        int maxrounds = config.getInt("maxRounds");
        String gametype = config.getString("gametype");

        this.neededToStart = needed;
        this.maxPlayers = max;
        this.maxRounds = maxrounds;
        switch (gametype) {
            case "Enhanced":
                setType(GameType.Enhanced);
                break;
            case "Normal":
                setType(GameType.Normal);
                break;
            case "Auto":
                int random = new Random().nextInt(2);
                if (random == 0 || random == 1) {
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
