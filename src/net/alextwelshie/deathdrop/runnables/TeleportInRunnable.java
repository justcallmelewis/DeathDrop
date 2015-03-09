package net.alextwelshie.deathdrop.runnables;

import java.util.concurrent.Callable;

import net.alextwelshie.deathdrop.utils.DropAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings("rawtypes")
public class TeleportInRunnable implements Callable {

	@Override
	public Object call() throws Exception {
		for (Player all : Bukkit.getOnlinePlayers()) {
			DropAPI.getInstance().teleportToMapSpawn(all);
		}
		return null;
	}

}
