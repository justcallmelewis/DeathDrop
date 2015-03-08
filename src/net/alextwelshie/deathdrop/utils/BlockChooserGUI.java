package net.alextwelshie.deathdrop.utils;

import java.util.HashMap;
import java.util.Map.Entry;
import net.alextwelshie.deathdrop.Main;
import net.alextwelshie.deathdrop.ranks.PlayerManager;
import net.alextwelshie.deathdrop.ranks.RankHandler;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("deprecation")
public class BlockChooserGUI {

    public static HashMap<Byte, Material> normal = new HashMap<>();
    public static HashMap<Material, Byte> premium = new HashMap<>();
    public static HashMap<Material, Byte> staff = new HashMap<>();

    public static void pickBlock(Player player, ItemStack block) {
        if (player.isOp()) {
            player.getInventory().getItemInHand().setType(block.getType());
            Main.getPlugin().blocks.put(player.getName(), block.getType());
            Main.getPlugin().blockData.put(player.getName(), block.getData().getData());
        } else if (!player.isOp() && RankHandler.getInstance().isPremium(player) && !RankHandler.getInstance().isStaff(player)) {
            //premium
            if (!staff.containsKey(block.getType())) {
                player.getInventory().getItemInHand().setType(block.getType());
                Main.getPlugin().blocks.put(player.getName(), block.getType());
                Main.getPlugin().blockData.put(player.getName(), block.getData().getData());
            } else {
                player.closeInventory();
                player.sendMessage(Main.getPlugin().prefix + "§cThese blocks are reserved for staff only. Sorry.");
            }
        } else if (!player.isOp() && !RankHandler.getInstance().isPremium(player) && !RankHandler.getInstance().isStaff(player)) {
            if (!staff.containsKey(block.getType())) {
                if (!premium.containsKey(block.getType())) {
                    player.getInventory().getItemInHand().setType(block.getType());
                    Main.getPlugin().blocks.put(player.getName(), block.getType());
                    Main.getPlugin().blockData.put(player.getName(), block.getData().getData());
                } else {
                    player.closeInventory();
                    player.sendMessage(Main.getPlugin().prefix + "§cThese blocks are reserved for premium members & testers only. Sorry.");
                }
            } else {
                player.closeInventory();
                player.sendMessage(Main.getPlugin().prefix + "§cThese blocks are reserved for staff only. Sorry.");
            }
        }
    }

    public static Inventory getInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§8Pick your block!");
        if (PlayerManager.getInstance().getRank(player).equalsIgnoreCase("Hive") || PlayerManager.getInstance().getRank(player).equalsIgnoreCase("Special")) {
            for (Entry<Byte, Material> entry : normal.entrySet()) {
                byte data = entry.getKey();
                Material material = entry.getValue();

                inv.addItem(new ItemStack(material, 1, data));
            }

            for (Entry<Material, Byte> entry : premium.entrySet()) {
                Material material = entry.getKey();
                byte data = entry.getValue();

                inv.addItem(new ItemStack(material, 1, data));
            }
        } else if (PlayerManager.getInstance().getRank(player).equalsIgnoreCase("Mod") | PlayerManager.getInstance().getRank(player).equalsIgnoreCase("Admin") || PlayerManager.getInstance().getRank(player).equalsIgnoreCase("Owner")) {
            for (Entry<Byte, Material> entry : normal.entrySet()) {
                byte data = entry.getKey();
                Material material = entry.getValue();

                inv.addItem(new ItemStack(material, 1, data));
            }

            for (Entry<Material, Byte> entry : premium.entrySet()) {
                Material material = entry.getKey();
                byte data = entry.getValue();

                inv.addItem(new ItemStack(material, 1, data));
            }

            for (Entry<Material, Byte> entry : staff.entrySet()) {
                Material material = entry.getKey();
                byte data = entry.getValue();

                inv.addItem(new ItemStack(material, 1, data));
            }
        } else {
            for (Entry<Byte, Material> entry : normal.entrySet()) {
                byte data = entry.getKey();
                Material material = entry.getValue();

                inv.addItem(new ItemStack(material, 1, data));
            }
        }
        return inv;
    }

}
