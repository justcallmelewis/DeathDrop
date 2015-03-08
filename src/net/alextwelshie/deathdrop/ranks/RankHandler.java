package net.alextwelshie.deathdrop.ranks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class RankHandler {

    private static RankHandler instance = new RankHandler();

    public static RankHandler getInstance() {
        return instance;
    }

    PlayerManager pl = PlayerManager.getInstance();

    public void setRankTeam(Player p) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();

        board.getTeam(getRankTeam(p)).addPlayer(p);
        p.setScoreboard(board);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(board);
        }
    }

    public void clearRank(Player p) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        
        board.getPlayerTeam(p).removePlayer(p);
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
    
    public String getRankColour(Player player) {
         switch (pl.getRank(player)) {
            case "Regular":
                return "§a";
            case "Special":
                return "§a";
            case "Hive":
                return "§5";
            case "Mod":
                return "§c";
            case "Admin":
                return "§4";
            case "Owner":
                return "§e";
            default:
                return null;
        }
    }
    
    public boolean isPremium(Player player) {
        return PlayerManager.getInstance().getRank(player).equalsIgnoreCase("Hive") || PlayerManager.getInstance().getRank(player).equalsIgnoreCase("Special");
    }
    
    public boolean isStaff(Player player) {
        return PlayerManager.getInstance().getRank(player).equalsIgnoreCase("Mod") | PlayerManager.getInstance().getRank(player).equalsIgnoreCase("Admin") || PlayerManager.getInstance().getRank(player).equalsIgnoreCase("Owner");
    }
    
    
}
