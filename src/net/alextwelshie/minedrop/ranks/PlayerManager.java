package net.alextwelshie.minedrop.ranks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.alextwelshie.minedrop.achievements.Achievement;

import org.bukkit.entity.Player;

public class PlayerManager {

	private static PlayerManager	instance	= new PlayerManager();

	public static PlayerManager getInstance() {
		return instance;
	}

	ConnectionPoolManager	cpm			= new ConnectionPoolManager();
	Connection				connection	= cpm.getConnectionFromPool();

	public String getRank(Player p) {
		String rank = null;
		try {
			PreparedStatement getRank = connection.prepareStatement("SELECT rank FROM `players` WHERE uuid=?;");
			getRank.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet result = getRank.executeQuery();
			result.next();

			rank = result.getString("rank");

			getRank.close();
			result.close();

			cpm.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rank;
	}

	public boolean hasAchievement(Player p, Achievement a) {
		boolean achievement = false;
		try {
			PreparedStatement hasAchievement = connection
					.prepareStatement("SELECT internalname FROM `MineDrop_achievements` WHERE uuid=?;");
			hasAchievement.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet result = hasAchievement.executeQuery();
			result.next();

			String rank = result.getString("internalname");
			if (rank.equalsIgnoreCase(a.name())) {
				achievement = true;
			}

			hasAchievement.close();
			result.close();

			cpm.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return achievement;
	}

	public int getAchievementEpoch(Player p, Achievement a) {
		int epoch = 0;
		try {
			PreparedStatement getAchievementEpoch = connection
					.prepareStatement("SELECT timeachieved FROM `MineDrop_achievements` WHERE uuid=?;");
			getAchievementEpoch.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet result = getAchievementEpoch.executeQuery();
			result.next();

			int time = result.getInt("timeachieved");
			epoch = time;

			getAchievementEpoch.close();
			result.close();

			cpm.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return epoch;
	}

	public void grantAchievement(Player p, Achievement a) {
		try {
			PreparedStatement achievement = connection
					.prepareStatement("INSERT INTO `MineDrop_achievements` values(?,?,?)");
			achievement.setString(1, p.getUniqueId().toString().replace("-", ""));
			achievement.setString(2, a.name());
			achievement.setInt(3, ((int) System.currentTimeMillis() / 1000));
			achievement.execute();
			achievement.close();

			cpm.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
