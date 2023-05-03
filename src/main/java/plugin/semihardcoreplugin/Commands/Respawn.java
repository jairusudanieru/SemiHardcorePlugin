package plugin.semihardcoreplugin.Commands;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Respawn implements CommandExecutor, TabCompleter {

    //Getting the plugin instance
    private final JavaPlugin plugin;
    public Respawn(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        //Checking if the args is 1, then show the array for tab completion
        boolean isConsole = sender instanceof ConsoleCommandSender;
        boolean isOperator = sender.isOp();
        if (isConsole) return Collections.emptyList();
        if (args.length == 1 && isOperator) {
            List<String> players = new ArrayList<>();
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                players.add(player.getName());
            }
            return players;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        //Checking if it's possible to use the command
        boolean isPlayer = sender instanceof Player;
        boolean isConsole = sender instanceof ConsoleCommandSender;
        boolean isOperator = sender.isOp();
        if (!command.getName().equalsIgnoreCase("respawn")) return false;
        if (isConsole && args.length == 0) {
            sender.sendMessage("Please specify a player you want to revive!");
            return true;
        } else if (isPlayer && !isOperator && args.length > 0) {
            sender.sendMessage("Usage: /respawn");
            return true;
        } else if (args.length > 1){
            sender.sendMessage("Usage: /respawn <playername>");
            return true;
        }

        //Getting the player and its name
        Player target = null;
        String targetName = null;
        if (args.length == 1) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Can't find that player!");
                return true;
            } else if (!target.isOnline()) {
                sender.sendMessage(ChatColor.RED + "Can't find that player!");
                return true;
            }
            targetName = target.getName();
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
            sender.sendMessage(ChatColor.GREEN + "You Successfully revived " + ChatColor.RESET + targetName);
        } else {
            sender.sendMessage(ChatColor.RED + "You can't revive this player!");
            return true;
        }

        return true;
    }
}
