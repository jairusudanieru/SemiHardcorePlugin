package plugin.semihardcoreplugin.Commands;

import org.bukkit.*;
import org.bukkit.attribute.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class RevivePlayer implements CommandExecutor {

    //Getting the plugin instance
    private final JavaPlugin plugin;
    public RevivePlayer(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        //Checking if it's possible to use the command
        boolean isPlayer = sender instanceof Player;
        boolean isOperator = sender.isOp();
        if (!command.getName().equalsIgnoreCase("reviveplayer")) return false;
        if (isPlayer && isOperator) {
            sender.sendMessage(ChatColor.RED + "Sorry, this command is only for console. Use the /revive command instead.");
            return true;
        } else if (!isOperator) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        //If the args is not equal to 1
        if (args.length < 1) {
            Bukkit.getLogger().info("Please specify a player you want to revive!");
            return true;
        } else if (args.length > 1) {
            Bukkit.getLogger().info("Usage: /reviveplayer <playername>");
        }

        //Checking if target is valid
        Player target = Bukkit.getPlayer(args[0]);
        String targetName = target.getName();
        if (!target.isOnline()) {
            sender.sendMessage(ChatColor.RED + "Can't find that player!");
            return true;
        }

        //Checking if it's possible to revive the player!
        AttributeInstance maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth != null && target.getGameMode().equals(GameMode.SPECTATOR) && maxHealth.getBaseValue() <= 2) {
            target.setGameMode(GameMode.SURVIVAL);
            target.teleport(target.getWorld().getSpawnLocation());
            maxHealth.setBaseValue(20);
            target.setHealth(20);
            target.setFoodLevel(20);
            target.setSaturation(20);
            target.spawnParticle(Particle.TOTEM, target.getLocation(), 30);
            target.playSound(target.getLocation(), Sound.ITEM_TOTEM_USE, 1f, 1f);
            target.sendTitle(ChatColor.GREEN + "REVIVED!", targetName, 10, 60, 10);
            Bukkit.getLogger().info("You Successfully revived " + targetName);
        } else {
            Bukkit.getLogger().info("You can't revive this player!");
            return true;
        }

        return true;
    }
}