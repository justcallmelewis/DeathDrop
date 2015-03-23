package net.alextwelshie.minedrop.statistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.alextwelshie.minedrop.Main;
import net.alextwelshie.minedrop.ranks.ConnectionPoolManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StatisticsManager {

	private static StatisticsManager	instance	= new StatisticsManager();

	public static StatisticsManager getInstance() {
		return instance;
	}

	ConnectionPoolManager	cpm			= new ConnectionPoolManager();
	Connection				connection	= cpm.getConnectionFromPool();

	public int getPoints(Player p) {
		int points = 0;
		try {
			PreparedStatement statement = connection
					.prepareStatement("SELECT points FROM `minedrop` WHERE uuid=?;");
			statement.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet result = statement.executeQuery();
			result.next();

			points = result.getInt("points");

			statement.close();
			result.close();

			cpm.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return points;
	}

	public int getSuccessfulDrops(Player p) {
		int drops = 0;
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT successfulDrops FROM `minedrop` WHERE uuid=?;");
			statement.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet result = statement.executeQuery();
			result.next();

			drops = result.getInt("successfulDrops");

			statement.close();
			result.close();

			cpm.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drops;
	}

	public int getFailedDrops(Player p) {
	int drops = 0;
	try {
		PreparedStatement statement = connection.prepareStatement("SELECT failedDrops FROM `minedrop` WHERE uuid=?;");
		statement.setString(1, p.getUniqueId().toString().replace("-", ""));
		ResultSet result = statement.executeQuery();
		result.next();

		drops = result.getInt("failedDrops");

		statement.close();
		result.close();

		cpm.returnConnectionToPool(connection);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return drops;
	}

	public int getVictories(Player p) {
		int victories = 0;
		try {
			PreparedStatement statement = connection
					.prepareStatement("SELECT victories FROM `minedrop` WHERE uuid=?;");
			statement.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet result = statement.executeQuery();
			result.next();

			victories = result.getInt("victories");
			
			statement.close();
			result.close();

			cpm.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return victories;
	}

	public int getGamesPlayed(Player p) {
		int games = 0;
		try {
			PreparedStatement statement = connection
					.prepareStatement("SELECT gamesPlayed FROM `minedrop` WHERE uuid=?;");
			statement.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet result = statement.executeQuery();
			result.next();

			games = result.getInt("gamesPlayed");

			statement.close();
			result.close();

			cpm.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return games;
	}

	public void addPoints(Player p, int points) {
		try {
			PreparedStatement achievement = connection
					.prepareStatement("UPDATE `minedrop` SET points=? WHERE uuid=?;");
			achievement.setInt(1, getPoints(p) + points);
			achievement.setString(2, p.getUniqueId().toString().replace("-", ""));
			achievement.execute();
			achievement.close();

			cpm.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addVictory(Player p) {
		try {
			PreparedStatement achievement = connection
					.prepareStatement("UPDATE `minedrop` SET victories=? WHERE uuid=?;");
			achievement.setInt(1, getVictories(p) + 1);
			achievement.setString(2, p.getUniqueId().toString().replace("-", ""));
			achievement.execute();
			achievement.close();

			cpm.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addGamePlayed(Player p) {
		try {
			PreparedStatement achievement = connection
					.prepareStatement("UPDATE `minedrop` SET gamesPlayed=? WHERE uuid=?;");
			achievement.setInt(1, getGamesPlayed(p) + 1);
			achievement.setString(2, p.getUniqueId().toString().replace("-", ""));
			achievement.execute();
			achievement.close();

			cpm.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void syncMapToDatabase() {
		for (Player all : Bukkit.getOnlinePlayers()) {
			int points = Main.getPlugin().points.get(all.getName());
			addPoints(all, points);
			System.out.println("Added " + points + " points to " + all.getName() + " in database.");
		}

	}
}
