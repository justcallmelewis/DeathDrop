package net.alextwelshie.minedrop.ranks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.alextwelshie.minedrop.HikariGFXDPool;
import net.alextwelshie.minedrop.Main;
import net.alextwelshie.minedrop.achievements.Achievement;
import net.alextwelshie.minedrop.utils.DropAPI;
import net.alextwelshie.minedrop.utils.GameType;

import org.bukkit.entity.Player;

public class PlayerManager {

	private static PlayerManager	instance	= new PlayerManager();

	public static PlayerManager getInstance() {
		return instance;
	}

	HikariGFXDPool hikari = HikariGFXDPool.getInstance();

	public String getRank(Player player)
	{
		Connection connection = null;

		String select = "SELECT rank FROM players WHERE uuid=?;";

		PreparedStatement p = null;
		ResultSet r = null;
		String rank = null;

		try
		{
			connection = hikari.getConnection();

			p = connection.prepareStatement(select);
			p.setString(1, player.getUniqueId().toString().replace("-", ""));
			r = p.executeQuery();
			r.next();

			rank = r.getString("rank");

		}
		catch (Exception e)
		{
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
		return rank;
	}

	public void removeFromArrayLists(Player player) {
		//Not had turn
		DropAPI.getInstance().notHadTurn.remove(player.getName());
		if (Main.getPlugin().getType() == GameType.Elimination
				&& DropAPI.getInstance().eliminated.contains(player.getName())) {
			DropAPI.getInstance().eliminated.remove(player.getName());
		}

	}

	public boolean hasAchievement(Player player, Achievement a) {
		Connection connection = null;

		String select = "SELECT internalname FROM MineDrop_achievements WHERE uuid=?;";

		PreparedStatement p = null;
		ResultSet r = null;
		boolean achievement = false;

		try {
			connection = hikari.getConnection();
			p = connection.prepareStatement(select);
			p.setString(1, player.getUniqueId().toString().replace("-", ""));
			r = p.executeQuery();
			r.next();

			String rank = r.getString("internalname");
			if (rank.equalsIgnoreCase(a.name())) {
				achievement = true;
			}

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
		return achievement;
	}

	public int getAchievementEpoch(Player player, Achievement a) {
		Connection connection = null;

		String select = "SELECT timeachieved FROM MineDrop_achievements WHERE uuid=?;";

		PreparedStatement p = null;
		ResultSet r = null;
		int epoch = 0;
		try {
			connection = hikari.getConnection();
			p = connection.prepareStatement(select);
			p.setString(1, player.getUniqueId().toString().replace("-", ""));
			r = p.executeQuery();
			r.next();

			int time = r.getInt("timeachieved");
			epoch = time;

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
		return epoch;
	}

	public void grantAchievement(Player player, Achievement a) {
		Connection connection = null;

		String insert = "INSERT INTO MineDrop_achievements values(?,?,?)";

		PreparedStatement p = null;

		try {
			connection = hikari.getConnection();
			p = connection.prepareStatement(insert);
			p.setString(1, player.getUniqueId().toString().replace("-", ""));
			p.setString(2, a.name());
			p.setInt(3, ((int) System.currentTimeMillis() / 1000));
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
}
