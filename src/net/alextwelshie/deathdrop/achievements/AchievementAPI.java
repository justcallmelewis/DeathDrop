package net.alextwelshie.deathdrop.achievements;

import org.bukkit.entity.Player;

public class AchievementAPI {

	private static final AchievementAPI	instance	= new AchievementAPI();

	public static AchievementAPI getInstance() {
		return instance;
	}

	public String getDisplayName(Achievement internalname) {
		String name = null;
		switch (internalname) {
		case FIRSTJOIN:
			name = "Welcome Young Adventurer!";
			break;
		case FIRST_LAND_FAIL:
			name = "Houston, We Have A Problem";
			break;
		case FIRST_LAND_SUCCESS:
			name = "One Small Step..";
			break;
		case GOODGAME:
			name = "Such A Sport";
			break;
		case PICKBLOCK:
			name = "Don't Be So Picky";
			break;
		case COMPLETED:
			name = "Death Defying";
			break;
		}
		return name;
	}

	public String getDescription(Achievement internalname) {
		String name = null;
		switch (internalname) {
		case FIRSTJOIN:
			name = "Join your first game of DeathDrop.";
			break;
		case FIRST_LAND_FAIL:
			name = "Fail a landing for the first time.";
			break;
		case FIRST_LAND_SUCCESS:
			name = "Succeed a landing for the first time.";
			break;
		case GOODGAME:
			name = "Say GG at the end of a game.";
			break;
		case PICKBLOCK:
			name = "Pick a block from the Block Chooser.";
			break;
		case COMPLETED:
			name = "Successfully complete all DeathDrop achievements.";
			break;
		}
		return name;
	}

	public void grantAchievement(Player player, Achievement achievement) {
		/*if (PlayerManager.getInstance().hasAchievement(player, achievement)) {
		//do nothing
		} else {
		player.sendMessage("§8=========================================");
		player.sendMessage("                §3§lACHIEVEMENT GET!            ");
		player.sendMessage("§eName: \n§b" + getDisplayName(achievement));
		player.sendMessage("§eDescription: \n§b" + getDescription(achievement));
		player.sendMessage("§8=========================================");

		PlayerManager.getInstance().grantAchievement(player, achievement);
		System.out.println("Player" + player.getName() + " got the achievement " + getDisplayName(achievement));

		int count = 0;
		for(Achievement a : Achievement.values()) {
		if(PlayerManager.getInstance().hasAchievement(player, a)) {
		count++;
		}
		}
		if (count == 5) {
		achievement = Achievement.COMPLETED;
		player.sendMessage("§8=========================================");
		player.sendMessage("                §3§lACHIEVEMENT GET!            ");
		player.sendMessage("§eName: \n§b" + getDisplayName(achievement));
		player.sendMessage("§eDescription: \n§b" + getDescription(achievement));
		player.sendMessage("§8=========================================");

		PlayerManager.getInstance().grantAchievement(player, achievement);
		System.out.println("Player" + player.getName() + " got the achievement " + getDisplayName(achievement));
		}
		}*/
	}

}
