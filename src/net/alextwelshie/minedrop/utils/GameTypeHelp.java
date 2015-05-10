package net.alextwelshie.minedrop.utils;

import net.alextwelshie.minedrop.Main;
import org.bukkit.Bukkit;

/**
 * Created by Lewis on 10/05/2015.
 */
public class GameTypeHelp {

    private static final GameTypeHelp instance = new GameTypeHelp();

    public static GameTypeHelp getInstance() {
        return instance;
    }

    public void getHelp(GameType gametype) {
        if (Bukkit.getOnlinePlayers().size() >= Main.getPlugin().config.getInt("neededToStart")
                && Main.getPlugin().getState() == GameState.LOBBY) {
            switch (gametype) {
                case Normal:
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage("§b—————————[ §a§lHow To Play §b]—————————");
                    Bukkit.broadcastMessage("§6— You goal is to land in the water.");
                    Bukkit.broadcastMessage("§6— Hitting a block has no penalty");
                    Bukkit.broadcastMessage("§6— The person with the most points wins.");
                    Bukkit.broadcastMessage("§b——————————————————————————————-————————");
                    Bukkit.broadcastMessage("");
                    break;
                case Elimination:
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage("§b—————————[ §a§lHow To Play §b]—————————");
                    Bukkit.broadcastMessage("§6— You goal is to land in the water.");
                    Bukkit.broadcastMessage("§6— If you hit a block, you are out.");
                    Bukkit.broadcastMessage("§6— The last person remaining wins.");
                    Bukkit.broadcastMessage("§b——————————————————————————————-————————");
                    Bukkit.broadcastMessage("");
                    break;
                case Enhanced:
                    Bukkit.broadcastMessage("");
                    Bukkit.broadcastMessage("§b—————————[ §a§lHow To Play §b]—————————");
                    Bukkit.broadcastMessage("§6— You goal is to land in the water.");
                    Bukkit.broadcastMessage("§6— Jumping by blocks or in between gives you bonus points.");
                    Bukkit.broadcastMessage("§6— Hitting a block has no penalty");
                    Bukkit.broadcastMessage("§6— The person with the most points wins.");
                    Bukkit.broadcastMessage("§b——————————————————————————————-————————");
                    Bukkit.broadcastMessage("");
                    break;
            }
        }
    }
}
