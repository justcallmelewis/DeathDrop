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

	public void addNewUser(Player p) {
		try {
			PreparedStatement achievement = connection.prepareStatement("INSERT INTO `minedrop` values(?,?,?,?,?,?,?)");
			achievement.setString(1, p.getUniqueId().toString().replace("-", ""));
			achievement.setString(2, p.getName());
			achievement.setInt(3, 0);
			achievement.setInt(4, 0);
			achievement.setInt(5, 0);
			achievement.setInt(6, 0);
			achievement.setInt(7, 0);
			achievement.execute();
			achievement.close();

			cpm.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean isExisting(Player p) {
        try {
           PreparedStatement getPlayer = connection.prepareStatement("SELECT uuid FROM `minedrop` WHERE uuid=?;");
           getPlayer.setString(1, p.getUniqueId().toString().replace("-", ""));
           ResultSet resultset = getPlayer.executeQuery();
           boolean containsPlayer = resultset.next();

           getPlayer.close();
           resultset.close();

           cpm.returnConnectionToPool(connection);

           return containsPlayer;
        } catch (Exception e) {
           e.printStackTrace();
        }
        return false;
}
	
	public int getPoints(Player p) {
		int points = 0;
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT points FROM `minedrop` WHERE uuid=?;");
			statement.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet result = statement.executeQuery();
			result.next();

			points = result.getInt("points");
			if(result.wasNull()) {
				points = 0;
			}

			statement.close();
			result.close();

			cpm.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return points;
	}

	/*public int getSuccessfulDrops(Player p) {
		int drops = 0;
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT successfulDrops FROM `minedrop` WHERE uuid=?;");
			statement.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet result = statement.executeQuery();
			result.next();

			drops = result.getInt("successfulDrops");
			if(result.wasNull()) {
				drops = 0;
			}

			statement.close();
			result.close();

			cpm.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drops;
	}*/
	
	/*public int getFailedDrops(Player p) {
	int drops = 0;
	try {
		PreparedStatement statement = connection.prepareStatement("SELECT failedDrops FROM `minedrop` WHERE uuid=?;");
		statement.setString(1, p.getUniqueId().toString().replace("-", ""));
		ResultSet result = statement.executeQuery();
		result.next();

		drops = result.getInt("failedDrops");
		if(result.wasNull()) {
			drops = 0;
		}

		statement.close();
		result.close();

		cpm.returnConnectionToPool(connection);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return drops;
}*/
	
	
	
	public int getVictories(Player p) {
		int drops = 0;
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT victories FROM `minedrop` WHERE uuid=?;");
			statement.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet result = statement.executeQuery();
			result.next();

			drops = result.getInt("victories");
			if(result.wasNull()) {
				drops = 0;
			}

			statement.close();
			result.close();

			cpm.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drops;
	}
	
	public int getGamesPlayed(Player p) {
		int drops = 0;
		try {
			PreparedStatement statement = connection.prepareStatement("SELECT gamesPlayed FROM `minedrop` WHERE uuid=?;");
			statement.setString(1, p.getUniqueId().toString().replace("-", ""));
			ResultSet result = statement.executeQuery();
			result.next();

			drops = result.getInt("gamesPlayed");
			if(result.wasNull()) {
				drops = 0;
			}

			statement.close();
			result.close();

			cpm.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drops;
	}
	
	public void addPoints(Player p, int points) {
		try {
			PreparedStatement achievement = connection
					.prepareStatement("UPDATE `minedrop` SET points=? WHERE uuid=?;");
			achievement.setInt(1, points);
			achievement.setString(2, p.getUniqueId().toString().replace("-", ""));
			achievement.execute();
			achievement.close();

			cpm.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addVictory(Player p) {
		int victories = 1;
		try {
			PreparedStatement achievement = connection
					.prepareStatement("UPDATE `minedrop` SET points=? WHERE uuid=?;");
			achievement.setInt(1, victories);
			achievement.setString(2, p.getUniqueId().toString().replace("-", ""));
			achievement.execute();
			achievement.close();

			cpm.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addGamePlayed(Player p) {
		int played = 1;
		try {
			PreparedStatement achievement = connection
					.prepareStatement("UPDATE `minedrop` SET points=? WHERE uuid=?;");
			achievement.setInt(1, played);
			achievement.setString(2, p.getUniqueId().toString().replace("-", ""));
			achievement.execute();
			achievement.close();

			cpm.returnConnectionToPool(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void syncMapToDatabase() {
		for(Player all : Bukkit.getOnlinePlayers()) {
			int points = Main.getPlugin().points.get(all.getName());
			addPoints(all, points);
			System.out.println("Added " + points + " points to " + all.getName() + " in database.");
		}
		
	}
}
