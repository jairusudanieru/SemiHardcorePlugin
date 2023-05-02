package plugin.semihardcoreplugin.Commands;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Withdraw implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9");
        } else {
            return Collections.emptyList();
        }
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        boolean isPlayer = sender instanceof Player;
        if (!command.getName().equalsIgnoreCase("withdraw")) return false;
        if (!isPlayer) {
            Bukkit.getLogger().info("This command is for players only!");
            return true;
        }

        //If the args is not equal to 1
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Please specify the number of hearts you want to withdraw!");
            return true;
        } else if (args.length > 1) {
            sender.sendMessage("Usage: /withdrawheart <number of hearts> ");
            return true;
        }

        //Getting the player command args
        Player player = (Player) sender;
        int numHearts;
        try { numHearts = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Please enter a valid number!");
            return true;
        }
        if (numHearts <= 0) {
            sender.sendMessage(ChatColor.RED + "Please enter a number greater than 0!");
            return true;
        }

        //Getting the heart item
        ItemStack result = new ItemStack(Material.NETHER_STAR, numHearts);
        ItemMeta meta = result.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 5, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(ChatColor.RESET + "Player Heart");
        result.setItemMeta(meta);

        //Checking if the inventory of the player is full
        if (player.getInventory().firstEmpty() == -1) player.getWorld().dropItemNaturally(player.getLocation(), result);
        else player.getInventory().addItem(result);

        //Checking if it's possible to withdraw hearts!
        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth != null && maxHealth.getBaseValue() > 2) {
            //Setting the player new heart count
            double newValue = maxHealth.getBaseValue() - (numHearts * 2);
            maxHealth.setBaseValue(newValue);
            player.setHealth(newValue);
            player.sendTitle(ChatColor.RED + "-" + numHearts + " Heart/s", player.getName(), 10, 60, 10);
            player.spawnParticle(Particle.HEART, player.getLocation(), 30);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 1f, 1f);
        } else {
            sender.sendMessage(ChatColor.RED + "You don't have enough hearts to withdraw!");
            return true;
        }
        return true;
    }
}