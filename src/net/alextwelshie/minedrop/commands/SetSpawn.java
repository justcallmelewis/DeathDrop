package net.alextwelshie.minedrop.commands;

import net.alextwelshie.minedrop.Main;
import net.alextwelshie.minedrop.SettingsManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawn implements CommandExecutor {
	
	SettingsManager settings = SettingsManager.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if (sender instanceof Player) {
			if(Main.getPlugin().isStaff(player)){
				if(args.length <= 1){
					player.sendMessage(Main.getPlugin().prefix + "§cIncorrect Usage - /setspawn jump/spec <mapname>");
					return false;
				}
				if(args[0].equalsIgnoreCase("spec")){
					settings.getData().set(player.getLocation().getWorld().getName() + ".world",
							player.getLocation().getWorld().getName());
					settings.getData().set(player.getLocation().getWorld().getName() + ".spec.x",
							Double.valueOf(player.getLocation().getX()));
					settings.getData().set(player.getLocation().getWorld().getName() + ".spec.y",
							Double.valueOf(player.getLocation().getY()) + 2);
					settings.getData().set(player.getLocation().getWorld().getName() + ".spec.z",
							Double.valueOf(player.getLocation().getZ()));
					settings.getData().set(player.getLocation().getWorld().getName() + ".spec.yaw",
							Double.valueOf(player.getEyeLocation().getYaw()));
					settings.getData().set(player.getLocation().getWorld().getName() + ".spec.pitch",
							Double.valueOf(player.getEyeLocation().getPitch()));
					settings.saveData();
					player.sendMessage(Main.getPlugin().prefix + "§6Set spec spawn for map §b" + player.getLocation().getWorld().getName());
					return true;
					
				} else if(args[0].equalsIgnoreCase("jump")){
					settings.getData().set(player.getLocation().getWorld().getName() + ".world",
							player.getLocation().getWorld().getName());
					settings.getData().set(player.getLocation().getWorld().getName() + ".jump.x",
							Double.valueOf(player.getLocation().getX()));
					settings.getData().set(player.getLocation().getWorld().getName() + ".jump.y",
							Double.valueOf(player.getLocation().getY()) + 2);
					settings.getData().set(player.getLocation().getWorld().getName() + ".jump.z",
							Double.valueOf(player.getLocation().getZ()));
					settings.getData().set(player.getLocation().getWorld().getName() + ".jump.yaw",
							Double.valueOf(player.getEyeLocation().getYaw()));
					settings.getData().set(player.getLocation().getWorld().getName() + ".jump.pitch",
							Double.valueOf(player.getEyeLocation().getPitch()));
					settings.saveData();
					player.sendMessage(Main.getPlugin().prefix + "§6Set jump spawn for map §b" + player.getLocation().getWorld().getName());
					return true;
				}
				
				
			} else {
				player.sendMessage("§4Illegal command.");
			}
			
		}
			
		
		return false;
		
	}

}