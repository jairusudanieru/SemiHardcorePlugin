package plugin.semihardcoreplugin.Commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RevivePlayer implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("reviveplayer")) {
            // Check if the sender is a player and has permission to execute the command
            if (!(sender instanceof Player) || !sender.isOp()) { sender.sendMessage("You do not have permission to use this command."); return true; }
            if (args.length != 1) { sender.sendMessage("Usage: /revive <player>"); return true; }

            // Get the target player
            Player player = (Player) sender;
            Player target = sender.getServer().getPlayer(args[0]);
            String playerName = player.getName();
            AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (target == null) {
                sender.sendMessage("Player not found.");
                return true;
            }

            // Revive the target player
            if (target.getGameMode().equals(GameMode.SPECTATOR)) {
                target.setGameMode(GameMode.SURVIVAL);
                target.teleport(target.getWorld().getSpawnLocation());
                maxHealth.setBaseValue(20);
                target.setHealth(20);
                target.setFoodLevel(20);
                target.setSaturation(20);
                target.spawnParticle(Particle.TOTEM, player.getLocation(), 30);
                target.playSound(target.getLocation(), Sound.ITEM_TOTEM_USE, 1f, 1f);
                target.sendMessage(ChatColor.GREEN + "You have been revived by + " + playerName + "!");
                sender.sendMessage(ChatColor.GREEN + "You Successfully revived " + target.getName() + "!");
                return true;
            }
        }

        return true;
    }
}
