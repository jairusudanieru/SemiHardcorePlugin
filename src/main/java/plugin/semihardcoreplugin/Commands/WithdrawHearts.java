package plugin.semihardcoreplugin.Commands;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WithdrawHearts implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9");
        } else if (args.length == 2 && sender.isOp()) {
            List<String> onlinePlayers = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                onlinePlayers.add(player.getName());
            }
            return onlinePlayers;
        } else {
            return Collections.emptyList();
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("withdrawheart")) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1 || args.length > 2) {
            sender.sendMessage("Usage: /withdrawheart <number of hearts> [player]");
            return true;
        }

        int numHearts;

        try {
            numHearts = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Please enter a valid number!");
            return true;
        }

        if (numHearts <= 0) {
            sender.sendMessage(ChatColor.RED + "Please enter a number greater than 0!");
            return true;
        }

        if (args.length == 2) {
            if (!sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to specify a player.");
                return true;
            }

            player = Bukkit.getPlayer(args[1]);

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
                return true;
            }
        }

        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (maxHealth == null || maxHealth.getBaseValue() <= 2) {
            sender.sendMessage(ChatColor.RED + "Player does not have enough hearts to withdraw.");
            return true;
        }

        double newValue = maxHealth.getBaseValue() - (numHearts * 2);

        if (newValue < 2) {
            sender.sendMessage(ChatColor.RED + "Player cannot have less than 1 heart.");
            return true;
        }

        maxHealth.setBaseValue(newValue);
        player.setHealth(newValue);

        ItemStack result = new ItemStack(Material.NETHER_STAR, numHearts);
        ItemMeta meta = result.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 5, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(ChatColor.RESET + "Player Heart");
        result.setItemMeta(meta);

        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), result);
        } else {
            player.getInventory().addItem(result);
        }

        player.sendTitle(ChatColor.RED + "-" + numHearts + " Heart/s", player.getName(), 10, 60, 10);
        player.spawnParticle(Particle.HEART, player.getLocation(), 30);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 1f, 1f);

        return true;
    }
}