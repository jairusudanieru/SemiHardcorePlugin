package plugin.semihardcoreplugin.Commands;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Revive implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        //Checking if it's possible to use the command
        boolean isPlayer = sender instanceof Player;
        boolean isOperator = sender.isOp();
        if (!command.getName().equalsIgnoreCase("revive")) return false;
        if (!isPlayer) {
            Bukkit.getLogger().info("This command is for players only. Use the /reviveplayer command instead.");
            return true;
        } else if (!isOperator) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        //If the args is not equal to 1
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Please specify a player you want to revive!");
            return true;
        } else if (args.length > 1) {
            sender.sendMessage("Usage: /revive <playername>");
            return true;
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
            sender.sendMessage(ChatColor.GREEN + "You Successfully revived " + ChatColor.RESET + targetName);
        } else {
            sender.sendMessage(ChatColor.RED + "You can't revive this player!");
            return true;
        }

        return true;
    }
}
