package net.alextwelshie.deathdrop.achievements;

import java.util.ArrayList;
import net.alextwelshie.deathdrop.ranks.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AchievementMenu {

    private static final AchievementMenu instance = new AchievementMenu();

    public static AchievementMenu getInstance() {
        return instance;
    }

    public Inventory getInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "§8[AM] Home");
        {
            ItemStack item;
            ArrayList<String> lore = new ArrayList<>();
            if (PlayerManager.getInstance().hasAchievement(player, Achievement.FIRSTJOIN)) {
                item = new ItemStack(Material.INK_SACK, 1, (short) 10);
                lore.add("§eDescription: §b" + AchievementAPI.getInstance().getDescription(Achievement.FIRSTJOIN));
                lore.add("§eStatus: §aCompleted!");
            } else {
                item = new ItemStack(Material.INK_SACK, 1, (short) 8);
                lore.add("§eDescription: §b" + AchievementAPI.getInstance().getDescription(Achievement.FIRSTJOIN));
                lore.add("§eStatus: §cUncompleted!");
            }
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§eName: §b" + AchievementAPI.getInstance().getDisplayName(Achievement.FIRSTJOIN));
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(0, item);
        }
        {
            ItemStack item;
            ArrayList<String> lore = new ArrayList<>();
            if (PlayerManager.getInstance().hasAchievement(player, Achievement.FIRST_LAND_FAIL)) {
                item = new ItemStack(Material.INK_SACK, 1, (short) 10);
                lore.add("§eDescription: §b" + AchievementAPI.getInstance().getDescription(Achievement.FIRST_LAND_FAIL));
                lore.add("§eStatus: §aCompleted!");
            } else {
                item = new ItemStack(Material.INK_SACK, 1, (short) 8);
                lore.add("§eDescription: §b" + AchievementAPI.getInstance().getDescription(Achievement.FIRST_LAND_FAIL));
                lore.add("§eStatus: §cUncompleted!");
            }
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§eName: §b" + AchievementAPI.getInstance().getDisplayName(Achievement.FIRST_LAND_FAIL));
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(1, item);
        }
        {
            ItemStack item;
            ArrayList<String> lore = new ArrayList<>();
            if (PlayerManager.getInstance().hasAchievement(player, Achievement.FIRST_LAND_SUCCESS)) {
                item = new ItemStack(Material.INK_SACK, 1, (short) 10);
                lore.add("§eDescription: §b" + AchievementAPI.getInstance().getDescription(Achievement.FIRST_LAND_SUCCESS));
                lore.add("§eStatus: §aCompleted!");
            } else {
                item = new ItemStack(Material.INK_SACK, 1, (short) 8);
                lore.add("§eDescription: §b" + AchievementAPI.getInstance().getDescription(Achievement.FIRST_LAND_SUCCESS));
                lore.add("§eStatus: §cUncompleted!");
            }
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§eName: §b" + AchievementAPI.getInstance().getDisplayName(Achievement.FIRST_LAND_SUCCESS));
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(2, item);
        }
        {
            ItemStack item;
            ArrayList<String> lore = new ArrayList<>();
            if (PlayerManager.getInstance().hasAchievement(player, Achievement.PICKBLOCK)) {
                item = new ItemStack(Material.INK_SACK, 1, (short) 10);
                lore.add("§eDescription: §b" + AchievementAPI.getInstance().getDescription(Achievement.PICKBLOCK));
                lore.add("§eStatus: §aCompleted!");
            } else {
                item = new ItemStack(Material.INK_SACK, 1, (short) 8);
                lore.add("§eDescription: §b" + AchievementAPI.getInstance().getDescription(Achievement.PICKBLOCK));
                lore.add("§eStatus: §cUncompleted!");
            }
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§eName: §b" + AchievementAPI.getInstance().getDisplayName(Achievement.PICKBLOCK));
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(3, item);
        }
        {
            ItemStack item;
            ArrayList<String> lore = new ArrayList<>();
            if (PlayerManager.getInstance().hasAchievement(player, Achievement.GOODGAME)) {
                item = new ItemStack(Material.INK_SACK, 1, (short) 10);
                lore.add("§eDescription: §b" + AchievementAPI.getInstance().getDescription(Achievement.GOODGAME));
                lore.add("§eStatus: §aCompleted!");
            } else {
                item = new ItemStack(Material.INK_SACK, 1, (short) 8);
                lore.add("§eDescription: §b" + AchievementAPI.getInstance().getDescription(Achievement.GOODGAME));
                lore.add("§eStatus: §cUncompleted!");
            }
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§eName: §b" + AchievementAPI.getInstance().getDisplayName(Achievement.GOODGAME));
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(4, item);
        }
        {
            ItemStack item;
            ArrayList<String> lore = new ArrayList<>();
            if (PlayerManager.getInstance().hasAchievement(player, Achievement.COMPLETED)) {
                item = new ItemStack(Material.INK_SACK, 1, (short) 10);
                lore.add("§eDescription: §b" + AchievementAPI.getInstance().getDescription(Achievement.COMPLETED));
                lore.add("§eStatus: §aCompleted!");
            } else {
                item = new ItemStack(Material.INK_SACK, 1, (short) 8);
                lore.add("§eDescription: §b" + AchievementAPI.getInstance().getDescription(Achievement.COMPLETED));
                lore.add("§eStatus: §cUncompleted!");
            }
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§eName: §b" + AchievementAPI.getInstance().getDisplayName(Achievement.COMPLETED));
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(5, item);
        }

        return inventory;
    }

}
