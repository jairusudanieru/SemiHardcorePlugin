package plugin.semihardcoreplugin.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
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
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WithdrawHearts implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        //To show tab completion on the command
        if (args.length == 1) {
            return Arrays.asList("1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19");
        } else {
            return Collections.emptyList();
        }
    }

    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        //Making the command
        if (command.getName().equalsIgnoreCase("withdrawheart")) {
            //Checking the command
            if (!(sender instanceof Player)) { sender.sendMessage("This command can only be run by a player."); return true; }
            if (args.length != 1) { sender.sendMessage("Usage: /withdrawheart <number of hearts>"); return true; }

            //Command variables
            Player player = (Player) sender;
            String playerName = player.getName();
            AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            int numHearts = 0;
            try { numHearts = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) { sender.sendMessage("Please enter a valid number."); return true; }
            if (numHearts <= 0) { sender.sendMessage("Please enter a number greater than 0."); return true; }

            //Getting the custom item
            ItemStack result = new ItemStack(Material.NETHER_STAR);
            ItemMeta meta = result.getItemMeta();
            result.setAmount(numHearts);
            meta.addEnchant(Enchantment.DURABILITY, 5, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setDisplayName(ChatColor.RESET + "Player Heart");
            result.setItemMeta(meta);

            //Checking if the player has enough hearts to withdraw
            if (maxHealth != null && maxHealth.getBaseValue() > 2 && maxHealth.getBaseValue() > (numHearts*2) ) {
                double newValue = maxHealth.getBaseValue() - (numHearts*2);
                maxHealth.setBaseValue(newValue);
                player.setHealth(newValue);
                if (player.getInventory().firstEmpty() == -1) player.getWorld().dropItemNaturally(player.getLocation(), result);
                else player.getInventory().addItem(result);
                player.sendTitle(ChatColor.RED + "-" + numHearts + " Heart/s", playerName, 10, 60, 10);
                player.spawnParticle(Particle.HEART, player.getLocation(), 30);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 1f, 1f);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "You don't have enough hearts to withdraw!");
                return true;
            }
        }

        //I don't really know how this works, but the docs says it's important... lol
        return true;
    }
}