package net.alextwelshie.minedrop.statistics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


import net.alextwelshie.minedrop.ranks.HikariGFXDPool;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.xml.transform.Result;

public class StatisticsManager {

	private static StatisticsManager	instance	= new StatisticsManager();

	public static StatisticsManager getInstance() {
		return instance;
	}

	HikariGFXDPool hikari = HikariGFXDPool.getInstance();
	
	public HashMap<String, Integer>	successDrops = new HashMap<>();
	public HashMap<String, Integer>	failedDrops = new HashMap<>();
	public HashMap<String, Integer>	chatPoints = new HashMap<>();
	public HashMap<String, Integer>	points = new HashMap<>();
	
	public int getPoints(Player player) {
		Connection connection = null;

		String select = "SELECT points FROM minedrop WHERE uuid=?;";
		PreparedStatement p = null;
		ResultSet r = null;
		int points = 0;

		try {
			connection = hikari.getConnection();
			p = connection.prepareStatement(select);
			p.setString(1, player.getUniqueId().toString().replace("-", ""));
			r = p.executeQuery();
			r.next();

			points = r.getInt("points");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(p != null){
				try {
					p.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(r != null){
				try {
					r.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return points;
	}

	public int getSuccessfulDrops(Player player) {
		Connection connection = null;

		String select = "SELECT successfulDrops FROM minedrop WHERE uuid=?;";
		PreparedStatement p = null;
		ResultSet r = null;
		int drops = 0;

		try {
			connection = hikari.getConnection();
			p = connection.prepareStatement(select);
			p.setString(1, player.getUniqueId().toString().replace("-", ""));
			r = p.executeQuery();
			r.next();

			drops = r.getInt("successfulDrops");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(p != null){
				try {
					p.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(r != null){
				try {
					r.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return drops;
	}

	public int getFailedDrops(Player player) {
		Connection connection = null;

		String select = "SELECT failedDrops FROM minedrop WHERE uuid=?;";
		PreparedStatement p = null;
		ResultSet r = null;
		int drops = 0;

		try {
			connection = hikari.getConnection();
			p = connection.prepareStatement(select);
			p.setString(1, player.getUniqueId().toString().replace("-", ""));
			r = p.executeQuery();
			r.next();

			drops = r.getInt("failedDrops");

		} catch (Exception e) {
		e.printStackTrace();
		} finally {
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(p != null){
				try {
					p.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(r != null){
				try {
					r.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return drops;
	}

	public int getVictories(Player player) {
		Connection connection = null;

		String select = "SELECT victories FROM minedrop WHERE uuid=?;";
		PreparedStatement p = null;
		ResultSet r = null;
		int victories = 0;

		try {
			connection = hikari.getConnection();
			p = connection.prepareStatement(select);
			p.setString(1, player.getUniqueId().toString().replace("-", ""));
			r = p.executeQuery();
			r.next();

			victories = r.getInt("victories");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(p != null){
				try {
					p.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(r != null){
				try {
					r.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return victories;
	}

	public int getGamesPlayed(Player player) {
		Connection connection = null;
		String select = "SELECT gamesPlayed FROM minedrop WHERE uuid=?;";
		PreparedStatement p = null;
		ResultSet r = null;
		int games = 0;

		try {
			connection = hikari.getConnection();
			p = connection.prepareStatement(select);
			p.setString(1, player.getUniqueId().toString().replace("-", ""));
			r = p.executeQuery();
			r.next();

			games = r.getInt("gamesPlayed");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(p != null){
				try {
					p.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(r != null){
				try {
					r.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return games;
	}

	public void addPoints(Player player, int points) {
		Connection connection = null;
		String update = "UPDATE minedrop SET points=? WHERE uuid=?;";
		PreparedStatement p = null;

		try {
			connection = hikari.getConnection();
			p = connection.prepareStatement(update);
			p.setInt(1, getPoints(player) + points);
			p.setString(2, player.getUniqueId().toString().replace("-", ""));
			p.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(p != null){
				try {
					p.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void addVictory(Player player) {
		Connection connection = null;
		String update = "UPDATE minedrop SET victories=? WHERE uuid=?;";
		PreparedStatement p = null;

		try {
			connection = hikari.getConnection();
			p = connection.prepareStatement(update);
			p.setInt(1, getVictories(player) + 1);
			p.setString(2, player.getUniqueId().toString().replace("-", ""));
			p.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(p != null){
				try {
					p.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void addSuccessfulDrops(Player player, int amount) {
		Connection connection = null;
		String update = "UPDATE minedrop SET successfulDrops=? WHERE uuid=?;";
		PreparedStatement p = null;

		try {
			connection = hikari.getConnection();
			p = connection.prepareStatement(update);
			p.setInt(1, getSuccessfulDrops(player) + amount);
			p.setString(2, player.getUniqueId().toString().replace("-", ""));
			p.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(p != null){
				try {
					p.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void addFailedDrops(Player player, int amount) {
		Connection connection = null;
		String update = "UPDATE minedrop SET failedDrops=? WHERE uuid=?;";
		PreparedStatement p = null;

		try {
			connection = hikari.getConnection();
			p = connection.prepareStatement(update);
			p.setInt(1, getFailedDrops(player) + amount);
			p.setString(2, player.getUniqueId().toString().replace("-", ""));
			p.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(p != null){
				try {
					p.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void addGamePlayed(Player player) {
		Connection connection = null;
		String update = "UPDATE minedrop SET gamesPlayed=? WHERE uuid=?;";
		PreparedStatement p = null;

		try {
			connection = hikari.getConnection();
			p = connection.prepareStatement(update);
			p.setInt(1, getGamesPlayed(player) + 1);
			p.setString(2, player.getUniqueId().toString().replace("-", ""));
			p.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(p != null){
				try {
					p.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getChatRank(Player player) {
		int points = chatPoints.get(player.getName());
		if(points >= 0 && points <= 1000) {
			return "§6Dropper";
		} else if(points >= 1001 && points <= 10000) {
			return "§5Amateur";
		} else if(points >= 10001 && points <= 25000) {
			return "§eClumsy";
		} else if(points >= 25001 && points <= 50000) {
			return "§dPattern Creator";
		} else if(points >= 50001 && points <= 100000) {
			return "§cSemi-Pro";
		} else if(points >= 100001 && points <= 250000) {
			return "§bNoscoper";
		} else {
			return "§aHacker";
		}
	}
	
	public void syncPointsToDatabase() {
		for (Player all : Bukkit.getOnlinePlayers()) {
			int points = this.points.get(all.getName());
			addPoints(all, points);
			System.out.println("Added " + points + " points to " + all.getName() + " in database.");
		}
	}
	
	public void syncDropsToDatabase() {
		for (Player all : Bukkit.getOnlinePlayers()) {
			int success = this.successDrops.get(all.getName());
			int fail = this.failedDrops.get(all.getName());
			
			addSuccessfulDrops(all, success);
			System.out.println("Added " + success + " successes to " + all.getName() + " in database.");			
			addFailedDrops(all, fail);
			System.out.println("Added " + fail + " fails to " + all.getName() + " in database.");
		}
	}
}
