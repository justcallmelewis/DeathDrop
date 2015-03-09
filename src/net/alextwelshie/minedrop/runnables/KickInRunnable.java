package net.alextwelshie.minedrop.runnables;

import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings({ "rawtypes" })
public class KickInRunnable implements Callable {

	@Override
	public Object call() throws Exception {
		for (Player all : Bukkit.getOnlinePlayers()) {
			all.kickPlayer("§2[MineDrop]\n §6Thanks for dropping by!\n§cThe server is currently §4restarting.\n§aWe'll be back soon!");
		}
		return null;
	}

}
