package net.alextwelshie.minedrop.runnables;

import java.util.concurrent.Callable;

import net.alextwelshie.minedrop.Main;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

@SuppressWarnings("rawtypes")
public class LoadWorldInRunnable implements Callable {

	@Override
	public Object call() {
		Bukkit.createWorld(WorldCreator.name(Main.getPlugin().mapName)).setAutoSave(false);
		Main.getPlugin().mapWorld = Bukkit.getWorld(Main.getPlugin().mapName);
		return null;
	}

}
