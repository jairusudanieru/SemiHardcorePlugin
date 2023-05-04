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
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WithdrawCommand implements CommandExecutor, TabCompleter {

    //Getting the plugin instance
    private final JavaPlugin plugin;
    public WithdrawCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        //Checking if the args is 1, then show the array for tab completion
        int maxHeart = plugin.getConfig().getInt("maxHearts");
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            for (int i = 1; i <= (maxHeart-1); i++) list.add(String.valueOf(i));
            return list;
        } else {
            return Collections.emptyList();
        }
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        //Messages variables
        String playerOnly = "This command is for players only!";
        String specify = "§cPlease specify the number of hearts you want to withdraw!";
        String usage = "Usage: /withdrawheart <number of hearts>";
        String invalid = "§cPlease enter a valid number!";
        String notZero = "§cPlease enter a number greater than 0!";
        String noHearts = "§cYou don't have enough hearts to withdraw!";
        String fullHealth = "§cYou need to have full health first before you can withdraw!";

        //Checking if it's possible to use the command
        if (!command.getName().equalsIgnoreCase("withdraw")) return false;
        boolean isPlayer = sender instanceof Player;
        if (!isPlayer) { Bukkit.getLogger().info(playerOnly); return true; }

        //If the args is not equal to 1
        if (args.length < 1) { sender.sendMessage(specify); return true; }
        else if (args.length > 1) { sender.sendMessage(usage); return true; }

        //Getting the player command args
        int numHearts;
        Player player = (Player) sender;
        try { numHearts = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) { sender.sendMessage(invalid); return true; }
        if (numHearts <= 0) { sender.sendMessage(notZero); return true; }

        //Getting the heart item
        ItemStack result = new ItemStack(Material.NETHER_STAR, numHearts);
        ItemMeta meta = result.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 5, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(ChatColor.RESET + "Player Heart");
        result.setItemMeta(meta);

        //Checking if it's possible to withdraw hearts!
        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        Location playerLocation = player.getLocation();
        World world = player.getWorld();
        double maxHealthValue = maxHealth != null ? maxHealth.getBaseValue() : 0;
        double health = player.getHealth();
        int maxHealthIntValue = (int) maxHealthValue;
        if (maxHealthIntValue > 2 && (numHearts * 2) < maxHealthIntValue) {
            //Checking if health is not full
            if (health != maxHealthValue) { sender.sendMessage(fullHealth); return true; }

            //Checking if the inventory of the player is full
            if (player.getInventory().firstEmpty() == -1) world.dropItemNaturally(playerLocation, result);
            else player.getInventory().addItem(result);

            //Setting the player new heart count
            double newValue = maxHealth.getBaseValue() - (numHearts * 2);
            maxHealth.setBaseValue(newValue);
            player.setHealth(newValue);
            player.sendTitle(ChatColor.RED + "-" + numHearts + " Heart/s", player.getName(), 10, 60, 10);
            player.spawnParticle(Particle.HEART, playerLocation, 30);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 1f, 1f);
        } else {
            sender.sendMessage(noHearts);
            return true;
        }
        return true;
    }
}