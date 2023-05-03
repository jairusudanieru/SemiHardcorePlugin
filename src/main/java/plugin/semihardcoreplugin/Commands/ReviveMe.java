package plugin.semihardcoreplugin.Commands;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.UUID;

public class ReviveMe implements CommandExecutor {

    //Getting the plugin instance
    private final JavaPlugin plugin;
    public ReviveMe(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        //Checking if it's possible to use the command
        boolean isPlayer = sender instanceof Player;
        if (!command.getName().equalsIgnoreCase("reviveme")) return false;
        if (!isPlayer) {
            Bukkit.getLogger().info("This command is for players only. Use the /reviveplayer command instead.");
            return true;
        }

        //If the args is more than 0
        if (args.length > 0) { sender.sendMessage("Usage: /reviveme "); return true; }

        //Player variables
        Player target = (Player) sender;
        String targetName = target.getName();
        World world = target.getWorld();
        UUID uuid = target.getUniqueId();

        //File locations
        File advancementFile = new File(world.getWorldFolder(), "advancements/" + uuid + ".json");
        File playerDataFile = new File(world.getWorldFolder(), "playerdata/" + uuid + ".dat");
        File statsFile = new File(world.getWorldFolder(), "stats/" + uuid + ".json");

        //Checking if it's possible to revive the player!
        AttributeInstance maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealth != null && target.getGameMode().equals(GameMode.SPECTATOR) && maxHealth.getBaseValue() <= 2) {
            target.kickPlayer(ChatColor.GREEN + "You have been successfully revived!" );
            // Delete the player's data file in the world's folder
            if (advancementFile.exists()) advancementFile.delete();
            if (playerDataFile.exists()) playerDataFile.delete();
            if (statsFile.exists()) statsFile.delete();
            Bukkit.getLogger().info("Successfully revived " + targetName);
        } else {
            sender.sendMessage(ChatColor.RED + "You can't revive because you're not eliminated!");
            return true;
        }

        return true;
    }
}
